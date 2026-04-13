@echo off

:: ============================================================
::   Script de lancement - Parabox
::   Compatible : Windows
::   Usage : run.bat           -> interface graphique
::           run.bat terminal  -> mode terminal
:: ============================================================

echo.
echo ╔════════════════════════════════════════╗
echo ║          Lancement de Parabox          ║
echo ╚════════════════════════════════════════╝
echo.

set MODE=%1

if "%MODE%"=="terminal" (
    echo Lancement en mode terminal...
    mvn exec:java -q
) else (
    echo Lancement en mode interface graphique...
    mvn javafx:run -q
)

if %errorlevel% neq 0 (
    echo X Erreur lors du lancement !
    echo   Verifiez que vous avez bien lance install.bat d'abord
    pause
    exit /b 1
)
