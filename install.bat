@echo off
setlocal enabledelayedexpansion

:: ============================================================
::   Script d'installation - Parabox
::   Compatible : Windows
:: ============================================================

echo.

echo      Installation de Jeu Parabox            

echo.


echo [1/3] Verification de Java...

java -version >nul 2>&1
if %errorlevel% neq 0 (
    echo  Java n'est pas installe !
    echo.
    echo Installez Java 17 :
    echo   https://adoptium.net
    echo   ou via winget : winget install EclipseAdoptium.Temurin.17.JDK
    pause
    exit /b 1
)

for /f "tokens=3" %%g in ('java -version 2^>^&1 ^| findstr /i "version"') do (
    set JAVA_VERSION=%%g
)
set JAVA_VERSION=%JAVA_VERSION:"=%
for /f "delims=." %%a in ("%JAVA_VERSION%") do set JAVA_MAJOR=%%a

if %JAVA_MAJOR% LSS 17 (
    echo  Java %JAVA_MAJOR% detecte. Java 17 minimum requis !
    echo   Telechargez Java 17 : https://adoptium.net
    pause
    exit /b 1
)

echo OK Java %JAVA_MAJOR% detecte


echo [2/3] Verification de Maven...

mvn -version >nul 2>&1
if %errorlevel% neq 0 (
    echo Maven non detecte. Tentative d'installation...

    :: Essayer avec winget
    winget install Apache.Maven >nul 2>&1
    if %errorlevel% neq 0 (
        echo  Installation automatique echouee.
        echo.
        echo Installez Maven manuellement :
        echo   https://maven.apache.org/download.cgi
        echo   Puis ajoutez Maven au PATH et relancez ce script.
        pause
        exit /b 1
    )

    echo Maven installe avec succes !
    echo Veuillez redemarrer ce script pour continuer.
    pause
    exit /b 0
)

for /f "tokens=3" %%v in ('mvn -version 2^>^&1 ^| findstr /i "Apache Maven"') do (
    set MVN_VERSION=%%v
)
echo OK Maven %MVN_VERSION% detecte


echo [3/3] Compilation du projet...

mvn clean compile -q
if %errorlevel% neq 0 (
    echo  Erreur lors de la compilation !
    echo   Relancez avec : mvn clean compile pour voir les details
    pause
    exit /b 1
)

echo OK Compilation reussie


echo.

echo    OK Installation terminee avec succes 
echo  ========================================
echo   Lancer le jeu (terminal) :            
echo     run.bat terminal                    
echo   Lancer le jeu (interface) :           
echo     run.bat                             
echo.
pause
