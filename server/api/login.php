<?php

declare(strict_types=1);

header('Content-Type: application/json; charset=utf-8');

require_once __DIR__ . '/lib_auth_bootstrap.php';

if ($_SERVER['REQUEST_METHOD'] !== 'POST') {
    http_response_code(405);
    echo json_encode([
        'success' => false,
        'error' => 'Method not allowed. Use POST.'
    ]);
    exit;
}

$rawBody = file_get_contents('php://input');
if ($rawBody === false || trim($rawBody) === '') {
    http_response_code(400);
    echo json_encode([
        'success' => false,
        'error' => 'Request body is empty.'
    ]);
    exit;
}

$payload = json_decode($rawBody, true);
if (!is_array($payload)) {
    http_response_code(400);
    echo json_encode([
        'success' => false,
        'error' => 'Invalid JSON payload.'
    ]);
    exit;
}

$password = isset($payload['password']) ? trim((string)$payload['password']) : '';
if ($password === '') {
    http_response_code(400);
    echo json_encode([
        'success' => false,
        'error' => 'Passwort fehlt.'
    ]);
    exit;
}

if (!auth_is_initialized()) {
    $created = auth_create_config_from_password($password);

    if (!$created['success']) {
        $isValidationError = isset($created['error']) && str_contains((string)$created['error'], 'mindestens 8 Zeichen');
        http_response_code($isValidationError ? 400 : 500);
        echo json_encode($created);
        exit;
    }

    http_response_code(201);
    echo json_encode([
        'success' => true,
        'initialized' => true,
        'message' => 'Erstes Passwort wurde gesetzt.',
        'algorithm' => $created['algorithm']
    ]);
    exit;
}

$verification = auth_verify_password($password);
if (!$verification['success']) {
    http_response_code(500);
    echo json_encode($verification);
    exit;
}

if (!$verification['valid']) {
    http_response_code(401);
    echo json_encode([
        'success' => false,
        'error' => 'Passwort ist nicht korrekt.'
    ]);
    exit;
}

http_response_code(200);
echo json_encode([
    'success' => true,
    'initialized' => false,
    'message' => 'Login erfolgreich.'
]);
