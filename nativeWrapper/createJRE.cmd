@echo off
color 09

echo Author Marco Scherzer
echo.
echo BEST USABILITY/SELFREMINDER: ADD THIS SCRIPT TO THE CENTRAL SCRIPT 
echo DIRECTORY REFERENCED IN THE SYSTEM'S PATH ENVIRONMENT-VARIABLE 
echo.
echo Builds a custom JRE.
echo.
echo PLEASE ENTER JDK-FX-VERSION ( FOR EXAMPLE 21_J21_FX21\jdk-fx OR IBM-11 ):
set /p version=
set jhome=Z:\jdks\%version%
echo.
echo PLEASE ENTER PROJECT-NAME ( FOR EXAMPLE MSendBackupMail OR MLinkCollectorFolderClient ): 
set /p name=
set prodir=Z:\MarcoScherzer-Projects\%name%\nativeWrapper
echo.
echo To list available modules
pause
echo.

for %%f in (%jhome%\jmods\*.jmod) do (
    echo %%~nxf - %%~zf bytes
)

echo.
echo Adjustable example buildline for building a custom JRE
echo.
echo.
rem jdk.unsupported,java.xml
echo %jhome%\bin\jlink --compress=2 --strip-debug --no-header-files --no-man-pages --module-path %jhome%\jmods --add-modules java.base,java.desktop,java.logging,java.net.http,java.security.sasl,jdk.crypto.ec,jdk.httpserver,jdk.security.auth --output %prodir%\jre

echo.
echo.
cmd /k



