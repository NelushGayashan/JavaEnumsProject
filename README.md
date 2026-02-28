# Java Enums - The Complete Guide
## 10 Lessons + Capstone Project

A complete hands-on course covering Java enums from basics to advanced design patterns.

---

## Project Structure

```
JavaEnumsProject/
├── README.md               <- You are here
├── run_all.sh              <- Run all lessons (Linux/Mac)
├── run_all.bat             <- Run all lessons (Windows)
└── src/
    ├── Lesson1_BasicEnum.java
    ├── Lesson2_BuiltInMethods.java
    ├── Lesson3_FieldsMethodsConstructors.java
    ├── Lesson4_AbstractMethods.java
    ├── Lesson5_Interfaces.java
    ├── Lesson6_EnumSet.java
    ├── Lesson7_EnumMap.java
    ├── Lesson8_Singleton.java
    ├── Lesson9_StrategyPattern.java
    ├── Lesson10_StateMachine.java
    └── CoffeeShopSystem.java   <- Capstone (uses all 10 lessons)
```

---

## Lessons at a Glance

| # | File | Topic | Key Concept |
|---|------|-------|-------------|
| 1 | Lesson1_BasicEnum | Basic Enum | Declaring, comparing, switch |
| 2 | Lesson2_BuiltInMethods | Built-in Methods | name(), ordinal(), valueOf(), values() |
| 3 | Lesson3_FieldsMethodsConstructors | Fields & Constructors | Enums as full classes with data |
| 4 | Lesson4_AbstractMethods | Abstract Methods | Per-constant behavior, no switch chains |
| 5 | Lesson5_Interfaces | Interfaces | Polymorphism with enums |
| 6 | Lesson6_EnumSet | EnumSet | High-performance set for enum flags |
| 7 | Lesson7_EnumMap | EnumMap | High-performance map with enum keys |
| 8 | Lesson8_Singleton | Singleton Pattern | Safest singleton in Java |
| 9 | Lesson9_StrategyPattern | Strategy Pattern | Interchangeable algorithms |
| 10 | Lesson10_StateMachine | State Machine | Lifecycle and workflow modeling |
| star | CoffeeShopSystem | Capstone Project | All 10 lessons in one program |

---

## Prerequisites

- Java 14 or higher
- No build tools or dependencies needed

---

## How to Run

### Single lesson (from the src/ directory):

    cd src
    javac Lesson1_BasicEnum.java && java Lesson1_BasicEnum
    javac Lesson2_BuiltInMethods.java && java Lesson2_BuiltInMethods
    javac Lesson3_FieldsMethodsConstructors.java && java Lesson3_FieldsMethodsConstructors
    javac Lesson4_AbstractMethods.java && java Lesson4_AbstractMethods
    javac Lesson5_Interfaces.java && java Lesson5_Interfaces
    javac Lesson6_EnumSet.java && java Lesson6_EnumSet
    javac Lesson7_EnumMap.java && java Lesson7_EnumMap
    javac Lesson8_Singleton.java && java Lesson8_Singleton
    javac Lesson9_StrategyPattern.java && java Lesson9_StrategyPattern
    javac Lesson10_StateMachine.java && java Lesson10_StateMachine
    javac CoffeeShopSystem.java && java CoffeeShopSystem

### All at once (Linux/Mac):

    chmod +x run_all.sh
    ./run_all.sh

### All at once (Windows):

    run_all.bat

### In an IDE (recommended):
1. Open IntelliJ IDEA or VS Code
2. Open the src/ folder
3. Open any file and click Run

---

## Learning Path

Work through lessons IN ORDER. Each builds on the previous.

After each lesson try a small exercise:
- L1  : Add a Color enum and write a switch for it
- L2  : Use valueOf() to parse user input safely
- L3  : Add a Planet enum with mass/radius and compute surface gravity
- L4  : Add a new Operation (e.g. SQUARE_ROOT) to the calculator
- L5  : Make an enum implement Comparable
- L6  : Model user permissions with EnumSet
- L7  : Build a weekly schedule with EnumMap<Day, String>
- L8  : Create a DatabaseConfig singleton
- L9  : Add a new discount strategy without touching existing constants
- L10 : Add a REFUNDED state to the order lifecycle
- Cap : Add a new Drink or Extra to the coffee shop

---

## Key Rules

    Use == to compare enums (not .equals())
    Constructor is always private
    Fields should be final for immutability
    Use EnumSet instead of HashSet when elements are enums
    Use EnumMap instead of HashMap when keys are enums
    valueOf() throws IllegalArgumentException - always handle it
    Never rely on ordinal() for persistent storage
    Enums cannot extend classes (already extends java.lang.Enum)
    Enums CAN implement interfaces

---

Happy coding!
