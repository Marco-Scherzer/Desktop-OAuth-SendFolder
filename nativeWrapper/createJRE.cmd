@echo off
color 09


echo Author Marco Scherzer
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
dir /b Z:\jdks\%version%\jdk-fx\jmods
echo.
echo.
echo Adjustable example buildline for building a custom JRE
echo.
echo.
echo %jhome%\jlink --module-path %jhome%\jmods --add-modules java.base,java.desktop --output %prodir%\jre
echo.
echo.
cmd /k

