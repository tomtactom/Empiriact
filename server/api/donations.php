<?php

declare(strict_types=1);

header('Content-Type: application/json; charset=utf-8');

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

$records = is_array($payload) && array_is_list($payload) ? $payload : [$payload];
if (count($records) === 0) {
    http_response_code(400);
    echo json_encode([
        'success' => false,
        'error' => 'No donation records provided.'
    ]);
    exit;
}

$storageDir = __DIR__ . '/data';
if (!is_dir($storageDir) && !mkdir($storageDir, 0750, true) && !is_dir($storageDir)) {
    http_response_code(500);
    echo json_encode([
        'success' => false,
        'error' => 'Could not create storage directory.'
    ]);
    exit;
}

$csvPath = $storageDir . '/donations.csv';
$fileExists = file_exists($csvPath);
$handle = fopen($csvPath, 'ab');

if ($handle === false) {
    http_response_code(500);
    echo json_encode([
        'success' => false,
        'error' => 'Could not open CSV file.'
    ]);
    exit;
}

if (!flock($handle, LOCK_EX)) {
    fclose($handle);
    http_response_code(500);
    echo json_encode([
        'success' => false,
        'error' => 'Could not lock CSV file.'
    ]);
    exit;
}

$writtenRows = 0;

try {
    if (!$fileExists || filesize($csvPath) === 0) {
        fputcsv($handle, [
            'received_at_utc',
            'weekday',
            'hour',
            'valence',
            'textFingerprint',
            'updatedAtDayBucket'
        ]);
    }

    foreach ($records as $record) {
        if (!is_array($record)) {
            continue;
        }

        $weekday = isset($record['weekday']) ? (int)$record['weekday'] : null;
        $hour = isset($record['hour']) ? (int)$record['hour'] : null;
        $valence = isset($record['valence']) ? (int)$record['valence'] : null;
        $fingerprint = isset($record['textFingerprint']) ? trim((string)$record['textFingerprint']) : '';
        $dayBucket = isset($record['updatedAtDayBucket']) ? (int)$record['updatedAtDayBucket'] : null;

        if ($weekday === null || $hour === null || $valence === null || $fingerprint === '' || $dayBucket === null) {
            continue;
        }

        fputcsv($handle, [
            gmdate('c'),
            $weekday,
            $hour,
            $valence,
            $fingerprint,
            $dayBucket
        ]);

        $writtenRows++;
    }
} finally {
    flock($handle, LOCK_UN);
    fclose($handle);
}

if ($writtenRows === 0) {
    http_response_code(400);
    echo json_encode([
        'success' => false,
        'error' => 'No valid records were written.'
    ]);
    exit;
}

http_response_code(201);
echo json_encode([
    'success' => true,
    'writtenRows' => $writtenRows
]);
