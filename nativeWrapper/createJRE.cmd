@echo off
color 09

echo Author Marco Scherzer
echo.
echo BEST USABILITY/SELFREMINDER: ADD THIS SCRIPT TO THE CENTRAL SCRIPT 
echo DIRECTORY REFERENCED IN THE SYSTEM'S PATH ENVIRONMENT-VARIABLE 
echo.
echo Builds a custom JRE.
echo.
echo PLEASE ENTER JDK-FX-VERSION ( FOR EXAMPLE 21_J21_FX21 ):
set /p version=
set jhome=Z:\jdks\%version%\jdk-fx\bin
echo.
echo PLEASE ENTER PROJECT-NAME ( FOR EXAMPLE MSendBackupMail ): 
set /p name=
set prodir=Z:\MarcoScherzer-Projects\%name%\nativeWrapper
echo.
echo To list available modules
pause
echo.

for %%f in (Z:\jdks\%version%\jdk-fx\jmods\*.jmod) do (
    echo %%~nxf - %%~zf bytes
)

echo.
echo Adjustable example buildline for building a custom JRE
echo.
echo.
echo %jhome%\jlink --module-path %jhome%\jmods --add-modules java.base,java.logging,java.net.http,java.security.sasl,jdk.crypto.ec,jdk.httpserver,jdk.security.auth,jdk.unsupported,java.xml --output %prodir%\jre

echo.
echo.
cmd /k



