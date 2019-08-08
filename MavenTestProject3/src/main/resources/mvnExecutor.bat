@echo off
set var=%cd%

echo "Setting Path for maven..."
set PATH=%PATH%;%var%\apache-maven-3.5.4\bin

For /f "tokens=1-4 delims=/ " %%a in ("%date% /t") do (set mydate=%%c-%%a-%%b)
For /f "tokens=1-2 delims=/: " %%a in ("%TIME%") do (if %%a LSS 10 (set mytime=0%%a%%b) else (set mytime=%%a%%b))

echo "Initiating the Automation Suite..."
echo Logs will be written to %var%\logs\%mydate%_%mytime%_Log_Automation.txt
call mvn -f %var%\pom.xml -X test --log-file %var%\logs\%mydate%_%mytime%_Log_Automation.txt

echo "Execution Complete"

echo "Opening Extent Results"
start chrome --new-window %var%\test-output-extent\ExtentReport.html

pause