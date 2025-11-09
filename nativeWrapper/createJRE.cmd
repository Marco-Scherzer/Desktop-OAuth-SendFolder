@echo off
color 09
set jhome=Z:\jdks\21_J21_FX21\jdk-fx\bin

echo Author Marco Scherzer
echo.
echo Builds a custom JRE.
echo.
echo To list available modules
pause
echo.
dir /b Z:\jdks\21_J21_FX21\jdk-fx\jmods
echo.
echo.
echo Adjustable example buildline for building a custom JRE
echo.
echo.
echo %jhome%\jlink --module-path %jhome%\jmods --add-modules java.base,java.desktop --output jre
echo.
echo.
cmd /k

