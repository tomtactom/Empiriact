<?php

declare(strict_types=1);

header('Content-Type: application/json; charset=utf-8');

require_once __DIR__ . '/lib_auth_bootstrap.php';

if ($_SERVER['REQUEST_METHOD'] !== 'GET') {
    http_response_code(405);
    echo json_encode([
        'success' => false,
        'error' => 'Method not allowed. Use GET.'
    ]);
    exit;
}

$initialized = auth_is_initialized();
$response = [
    'success' => true,
    'initialized' => $initialized
];

if ($initialized) {
    $loadedConfig = auth_load_config();
    if ($loadedConfig['success'] && !empty($loadedConfig['config']['algorithm'])) {
        $response['algorithm'] = (string)$loadedConfig['config']['algorithm'];
    }
}

http_response_code(200);
echo json_encode($response);
