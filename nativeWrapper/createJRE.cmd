@echo off
color 09
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
echo Example BuildLine for wrapped JRE
echo.
echo.
echo Z:\jdks\21_J21_FX21\jdk-fx\bin\jlink --module-path Z:\jdks\21_J21_FX21\jdk-fx\jmods --add-modules java.base,java.desktop --output jre
echo.
echo.
cmd /k

