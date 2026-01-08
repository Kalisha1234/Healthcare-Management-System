@echo off

REM Set database password
set DB_PASSWORD=bece2018

REM Navigate to project directory

REM Clean and compile
call mvn clean compile

REM Run JavaFX application
call mvn javafx:run

pause
