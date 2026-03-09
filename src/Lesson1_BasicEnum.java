// ============================================================
//  LESSON 1 — BASIC ENUM
// ============================================================
//
//  WHAT IS AN ENUM?
//  ----------------
//  An enum (short for "enumeration") is a special Java type
//  that represents a fixed set of named constants.
//
//  Think of real-world things that only have a limited number
//  of possible values:
//    - Days of the week   → MONDAY, TUESDAY ... SUNDAY
//    - Compass directions → NORTH, SOUTH, EAST, WEST
//    - Seasons            → SPRING, SUMMER, AUTUMN, WINTER
//    - Traffic lights     → RED, YELLOW, GREEN
//
//  WHY USE ENUMS INSTEAD OF STRINGS OR INTS?
//  ------------------------------------------
//  Without enums, you might write:
//    String direction = "NROTH";   // typo → compiles fine, fails at runtime
//    int    day       = 8;         // invalid day → no error until something breaks
//
//  With enums:
//    Direction dir = Direction.NROTH;  // typo → COMPILE ERROR immediately
//    Day d = Day.FUNDAY;               // invalid → COMPILE ERROR immediately
//
//  Enums give you:
//    ✅ Type safety  (only valid values allowed)
//    ✅ Readability  (NORTH is clearer than 0)
//    ✅ IDE support  (auto-complete, refactoring)
//    ✅ Switch support
//
// ============================================================

public class Lesson1_BasicEnum {

    // --- Declaring enums ---
    // Convention: enum name is PascalCase, constants are ALL_CAPS

    enum Direction {
        NORTH, SOUTH, EAST, WEST
    }

    enum Season {
        SPRING, SUMMER, AUTUMN, WINTER
    }

    enum Day {
        MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY
    }

    enum TrafficLight {
        RED, YELLOW, GREEN
    }

    public static void main(String[] args) {

        // --------------------------------------------------------
        // PART 1: Assigning and printing enum values
        // --------------------------------------------------------
        System.out.println("=== PART 1: Assigning Enum Values ===");

        Direction dir    = Direction.NORTH;
        Season    season = Season.SUMMER;
        Day       day    = Day.FRIDAY;

        System.out.println("Direction : " + dir);    // NORTH
        System.out.println("Season    : " + season); // SUMMER
        System.out.println("Day       : " + day);    // FRIDAY


        // --------------------------------------------------------
        // PART 2: Comparing enum values
        // --------------------------------------------------------
        // Always use == to compare enums (NOT .equals())
        // == works because each enum constant is a UNIQUE SINGLETON object
        System.out.println("\n=== PART 2: Comparing Enum Values ===");

        if (dir == Direction.NORTH) {
            System.out.println("We are heading North!");
        }

        if (season != Season.WINTER) {
            System.out.println("It's not winter.");
        }


        // --------------------------------------------------------
        // PART 3: if / else with enums
        // --------------------------------------------------------
        System.out.println("\n=== PART 3: if/else ===");

        Season current = Season.WINTER;

        if (current == Season.SUMMER) {
            System.out.println("It's hot — wear light clothes.");
        } else if (current == Season.WINTER) {
            System.out.println("It's cold — wear a coat.");
        } else if (current == Season.SPRING) {
            System.out.println("Flowers are blooming!");
        } else {
            System.out.println("Leaves are falling.");
        }


        // --------------------------------------------------------
        // PART 4: switch statement (classic style)
        // --------------------------------------------------------
        System.out.println("\n=== PART 4: switch (classic) ===");

        Day today = Day.SATURDAY;

        switch (today) {
            case MONDAY:
                System.out.println("Start of the work week. Stay strong!");
                break;
            case TUESDAY:
            case WEDNESDAY:
            case THURSDAY:
                System.out.println("Midweek grind.");
                break;
            case FRIDAY:
                System.out.println("TGIF! Almost there.");
                break;
            case SATURDAY:
            case SUNDAY:
                System.out.println("Weekend! Time to relax.");
                break;
            default:
                System.out.println("Unknown day.");
        }


        // --------------------------------------------------------
        // PART 5: switch expression (Java 14+ modern style)
        // --------------------------------------------------------
        // More concise, no break needed, can return a value
        System.out.println("\n=== PART 5: switch expression (modern) ===");

        String dayType = switch (today) {
            case SATURDAY, SUNDAY                          -> "Weekend";
            case MONDAY, FRIDAY                            -> "Near the weekend";
            case TUESDAY, WEDNESDAY, THURSDAY              -> "Midweek";
            default                                        -> "Unknown";
        };
        System.out.println(today + " is: " + dayType);

        // You can also run code inside the arrow:
        switch (today) {
            case SATURDAY, SUNDAY -> System.out.println("No alarm needed today!");
            default               -> System.out.println("Set your alarm.");
        }


        // --------------------------------------------------------
        // PART 6: Traffic light example (real-world use)
        // --------------------------------------------------------
        System.out.println("\n=== PART 6: Traffic Light ===");

        TrafficLight light = TrafficLight.RED;

        String instruction = switch (light) {
            case RED    -> "STOP — do not cross.";
            case YELLOW -> "CAUTION — prepare to stop.";
            case GREEN  -> "GO — it's safe to cross.";
        };
        System.out.println("Light is " + light + " → " + instruction);


        // --------------------------------------------------------
        // PART 7: Why NOT strings or ints — the danger
        // --------------------------------------------------------
        System.out.println("\n=== PART 7: Enum vs String vs Int ===");

        // BAD — using a String
        String badDirection = "NROTH"; // Typo — Java won't warn you
        if (badDirection.equals("NORTH")) { // This check will FAIL silently
            System.out.println("Going north (this won't print due to typo)");
        }

        // BAD — using an int
        int badDay = 9; // 9 is not a valid day — no error!
        System.out.println("Bad day number: " + badDay); // prints 9, meaninglessly

        // GOOD — using enums
        Direction goodDir = Direction.NORTH; // typo = compile error
        System.out.println("Good direction: " + goodDir);

        System.out.println("\nLesson 1 Complete!");
    }
}

// ============================================================
//  SUMMARY
// ============================================================
//  - enum declares a type with a fixed set of named constants
//  - Use == for comparison (not .equals())
//  - Enums work in if/else and switch statements
//  - Enums catch typos and invalid values at compile time
//  - Convention: enum names PascalCase, constants ALL_CAPS
//
//  COMPILE & RUN:
//    javac Lesson1_BasicEnum.java
//    java  Lesson1_BasicEnum
// ============================================================
