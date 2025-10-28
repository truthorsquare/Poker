@echo off
echo Compiling Texas Hold'em Poker...
javac -d bin src/*.java src/model/*.java src/ai/*.java src/logic/*.java src/ui/*.java

if %errorlevel% equ 0 (
    echo.
    echo Compilation successful!
    echo.
    echo Creating JAR file...
    echo Main-Class: Poker > manifest.txt
    cd bin
    jar cvfm ../Poker.jar ../manifest.txt *
    cd ..
    del manifest.txt
    echo.
    echo Build complete! Run with: java -jar Poker.jar
) else (
    echo.
    echo Compilation failed!
    pause
)

pause

