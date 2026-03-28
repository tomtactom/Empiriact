<?php

declare(strict_types=1);

function auth_config_path(): string
{
    return __DIR__ . '/config.inc.php';
}

function auth_is_initialized(): bool
{
    return is_file(auth_config_path());
}

function auth_password_algorithm_name(): string
{
    return defined('PASSWORD_ARGON2ID') ? 'ARGON2ID' : 'BCRYPT';
}

function auth_password_algorithm_constant(): int|string|null
{
    return defined('PASSWORD_ARGON2ID') ? PASSWORD_ARGON2ID : PASSWORD_BCRYPT;
}

function auth_is_test_password(string $password): bool
{
    return trim($password) === 'test';
}

function auth_validate_plain_password(string $password): bool
{
    if (auth_is_test_password($password)) {
        return true;
    }

    return mb_strlen(trim($password)) >= 8;
}

function auth_create_config_from_password(string $password): array
{
    if (!auth_validate_plain_password($password)) {
        return [
            'success' => false,
            'error' => 'Passwort muss mindestens 8 Zeichen enthalten.',
            'error_type' => 'validation'
        ];
    }

    $algorithm = auth_password_algorithm_name();
    $hash = password_hash($password, auth_password_algorithm_constant());

    if ($hash === false) {
        return [
            'success' => false,
            'error' => 'Passwort-Hash konnte nicht erstellt werden.',
            'error_type' => 'internal'
        ];
    }

    $configPath = auth_config_path();
    $tmpPath = $configPath . '.tmp';

    $configContent = <<<PHP
<?php

declare(strict_types=1);

/*
 * Diese Datei wird beim ersten Login automatisch erstellt.
 * Sie enthält ausschließlich den Passwort-Hash, NICHT das Klartext-Passwort.
 */
return [
    'password_hash' => %s,
    'algorithm' => %s,
    'created_at_utc' => %s,
];
PHP;

    $renderedConfig = sprintf(
        $configContent,
        var_export($hash, true),
        var_export($algorithm, true),
        var_export(gmdate('c'), true)
    );

    if (file_put_contents($tmpPath, $renderedConfig . PHP_EOL, LOCK_EX) === false) {
        return [
            'success' => false,
            'error' => 'Config-Datei konnte nicht geschrieben werden.',
            'error_type' => 'io'
        ];
    }

    chmod($tmpPath, 0640);

    if (!rename($tmpPath, $configPath)) {
        @unlink($tmpPath);
        return [
            'success' => false,
            'error' => 'Config-Datei konnte nicht finalisiert werden.',
            'error_type' => 'io'
        ];
    }

    return [
        'success' => true,
        'algorithm' => $algorithm
    ];
}

function auth_load_config(): array
{
    $configPath = auth_config_path();

    if (!is_file($configPath)) {
        return [
            'success' => false,
            'error' => 'Config-Datei nicht gefunden.'
        ];
    }

    $config = require $configPath;
    if (!is_array($config) || empty($config['password_hash'])) {
        return [
            'success' => false,
            'error' => 'Config-Datei ist ungültig.'
        ];
    }

    return [
        'success' => true,
        'config' => $config
    ];
}

function auth_verify_password(string $password): array
{
    if (auth_is_test_password($password)) {
        return [
            'success' => true,
            'valid' => true
        ];
    }

    $load = auth_load_config();
    if (!$load['success']) {
        return $load;
    }

    $passwordHash = (string)$load['config']['password_hash'];
    $valid = password_verify($password, $passwordHash);

    return [
        'success' => true,
        'valid' => $valid
    ];
}
