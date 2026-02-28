@echo off
cd /d "%~dp0src"

echo ======================================
echo   Java Enums - Complete Course Runner
echo ======================================

set files=Lesson1_BasicEnum Lesson2_BuiltInMethods Lesson3_FieldsMethodsConstructors Lesson4_AbstractMethods Lesson5_Interfaces Lesson6_EnumSet Lesson7_EnumMap Lesson8_Singleton Lesson9_StrategyPattern Lesson10_StateMachine CoffeeShopSystem

for %%f in (%files%) do (
    echo.
    echo ======================================
    echo   Running: %%f
    echo ======================================
    javac %%f.java
    if errorlevel 1 (
        echo ERROR: %%f failed to compile
        pause
        exit /b 1
    )
    java %%f
    if errorlevel 1 (
        echo ERROR: %%f failed to run
        pause
        exit /b 1
    )
)

echo.
echo ======================================
echo   All lessons completed successfully!
echo ======================================
pause
