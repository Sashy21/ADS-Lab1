@echo off
echo Compiling Fruit Service Engine with Java 8 compatibility...

REM Create target directory if it doesn't exist
if not exist "target\classes" mkdir "target\classes"

REM Compile interfaces first with Java 8 compatibility
echo Compiling interfaces...
javac -source 8 -target 8 -cp src\main\java -d target\classes src\main\java\server\interfaces\*.java

REM Compile model classes
echo Compiling model classes...
javac -source 8 -target 8 -cp src\main\java -d target\classes src\main\java\model\*.java

REM Compile server tasks
echo Compiling server tasks...
javac -source 8 -target 8 -cp "src\main\java;target\classes" -d target\classes src\main\java\server\tasks\*.java

REM Compile server classes
echo Compiling server classes...
javac -source 8 -target 8 -cp "src\main\java;target\classes" -d target\classes src\main\java\server\*.java

REM Compile client classes
echo Compiling client classes...
javac -source 8 -target 8 -cp "src\main\java;target\classes" -d target\classes src\main\java\client\*.java

echo Compilation complete!
echo Note: Using Java 8 compatibility mode for Java 8 runtime
pause
