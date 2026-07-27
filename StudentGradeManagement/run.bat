@echo off
title Student Grade Management System
echo ===================================================
echo     Student Grade Management System Desktop App
echo ===================================================
echo.
echo Launching application...
java -cp bin com.grade.main.Main
if %ERRORLEVEL% NEQ 0 (
    echo.
    echo Launching from JAR file...
    java -jar StudentGradeManagement.jar
)
pause
