@ECHO OFF
SETLOCAL ENABLEDELAYEDEXPANSION

SET APP_HOME=%~dp0
SET WRAPPER_PROPERTIES=%APP_HOME%gradle\wrapper\gradle-wrapper.properties

IF NOT EXIST "%WRAPPER_PROPERTIES%" (
  ECHO ERROR: Missing %WRAPPER_PROPERTIES%
  EXIT /B 1
)

FOR /F "tokens=1,* delims==" %%A IN (%WRAPPER_PROPERTIES%) DO (
  IF "%%A"=="distributionUrl" SET DISTRIBUTION_URL=%%B
)

IF "%DISTRIBUTION_URL%"=="" (
  ECHO ERROR: distributionUrl is not configured in gradle-wrapper.properties
  EXIT /B 1
)

SET DISTRIBUTION_URL=%DISTRIBUTION_URL:\:=:%
FOR %%I IN (%DISTRIBUTION_URL%) DO SET DIST_FILE=%%~nxI
FOR /F "tokens=2 delims=-" %%V IN ("%DIST_FILE%") DO SET EXPECTED_VERSION=%%V

SET CACHE_DIR=%APP_HOME%.gradle-wrapper
SET ZIP_PATH=%CACHE_DIR%\%DIST_FILE%
SET EXTRACT_DIR=%CACHE_DIR%\gradle-%EXPECTED_VERSION%
SET GRADLE_BIN=%EXTRACT_DIR%\bin\gradle.bat

IF NOT EXIST "%CACHE_DIR%" MKDIR "%CACHE_DIR%"

IF NOT EXIST "%GRADLE_BIN%" (
  IF NOT EXIST "%ZIP_PATH%" (
    ECHO Downloading %DISTRIBUTION_URL%
    powershell -NoProfile -ExecutionPolicy Bypass -Command "Invoke-WebRequest -Uri '%DISTRIBUTION_URL%' -OutFile '%ZIP_PATH%'"
    IF ERRORLEVEL 1 EXIT /B 1
  )

  IF EXIST "%EXTRACT_DIR%" RMDIR /S /Q "%EXTRACT_DIR%"
  powershell -NoProfile -ExecutionPolicy Bypass -Command "Expand-Archive -Path '%ZIP_PATH%' -DestinationPath '%CACHE_DIR%' -Force"
  IF ERRORLEVEL 1 EXIT /B 1
)

IF NOT EXIST "%GRADLE_BIN%" (
  ECHO ERROR: Gradle binary not found at %GRADLE_BIN%
  EXIT /B 1
)

CALL "%GRADLE_BIN%" %*
