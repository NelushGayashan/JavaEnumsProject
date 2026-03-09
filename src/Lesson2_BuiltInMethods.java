// ============================================================
//  LESSON 2 — BUILT-IN ENUM METHODS
// ============================================================
//
//  Every enum in Java automatically inherits a set of built-in
//  methods from java.lang.Enum. You get these for FREE on
//  every enum you create — no extra code needed.
//
//  THE 6 BUILT-IN METHODS:
//  -----------------------
//  1. name()             → returns the constant's name as a String
//  2. ordinal()          → returns the 0-based position (index)
//  3. toString()         → same as name() unless you override it
//  4. valueOf(String)    → converts a String back to an enum constant
//  5. values()           → returns an array of ALL constants
//  6. compareTo(other)   → compares two constants by ordinal
//
//  PLUS: getDeclaringClass() — returns the enum's Class object
//
// ============================================================

import java.util.Arrays;

public class Lesson2_BuiltInMethods {

    enum Planet {
        MERCURY, VENUS, EARTH, MARS, JUPITER, SATURN, URANUS, NEPTUNE
    }

    enum Level {
        BEGINNER, INTERMEDIATE, ADVANCED, EXPERT
    }

    enum Color {
        RED, GREEN, BLUE, YELLOW, ORANGE, PURPLE
    }

    public static void main(String[] args) {

        // --------------------------------------------------------
        // METHOD 1: name()
        // Returns the EXACT name of the constant as a String.
        // This is the name you wrote in the enum declaration.
        // --------------------------------------------------------
        System.out.println("=== METHOD 1: name() ===");

        Planet p = Planet.EARTH;
        String name = p.name();
        System.out.println("p.name()    = " + name);        // EARTH
        System.out.println("Type        : " + name.getClass().getSimpleName()); // String

        // name() always returns exactly what you declared — no surprises
        System.out.println(Planet.JUPITER.name());  // JUPITER
        System.out.println(Planet.NEPTUNE.name());  // NEPTUNE


        // --------------------------------------------------------
        // METHOD 2: ordinal()
        // Returns the 0-based position of the constant.
        // MERCURY=0, VENUS=1, EARTH=2, MARS=3 ...
        // --------------------------------------------------------
        System.out.println("\n=== METHOD 2: ordinal() ===");

        System.out.println("MERCURY ordinal = " + Planet.MERCURY.ordinal()); // 0
        System.out.println("VENUS   ordinal = " + Planet.VENUS.ordinal());   // 1
        System.out.println("EARTH   ordinal = " + Planet.EARTH.ordinal());   // 2
        System.out.println("NEPTUNE ordinal = " + Planet.NEPTUNE.ordinal()); // 7

        // ⚠️ WARNING: Don't rely on ordinal() in production code!
        // If you add/remove/reorder constants, ordinals change.
        // Use a dedicated field instead (covered in Lesson 3).
        System.out.println("\nAll planets with ordinals:");
        for (Planet planet : Planet.values()) {
            System.out.println("  [" + planet.ordinal() + "] " + planet.name());
        }


        // --------------------------------------------------------
        // METHOD 3: toString()
        // By default, same as name(). But you can override it
        // (covered in Lesson 3) to return something different.
        // --------------------------------------------------------
        System.out.println("\n=== METHOD 3: toString() ===");

        Planet earth = Planet.EARTH;
        System.out.println("name()     = " + earth.name());      // EARTH
        System.out.println("toString() = " + earth.toString());  // EARTH (same by default)

        // toString() is called automatically when you use + with a String
        System.out.println("Auto: " + earth);  // calls toString() → EARTH


        // --------------------------------------------------------
        // METHOD 4: valueOf(String)
        // Converts a String to the matching enum constant.
        // The String must EXACTLY match the constant name (case-sensitive).
        // --------------------------------------------------------
        System.out.println("\n=== METHOD 4: valueOf(String) ===");

        Planet fromString = Planet.valueOf("MARS");
        System.out.println("valueOf(\"MARS\") = " + fromString); // MARS

        // Useful when reading user input or data from a file/database
        String input = "SATURN";
        Planet userPlanet = Planet.valueOf(input);
        System.out.println("User chose: " + userPlanet);

        // ⚠️ CAREFUL: valueOf throws IllegalArgumentException if the String
        // doesn't match any constant. Always handle the exception!
        System.out.println("\nHandling invalid valueOf:");
        try {
            Planet invalid = Planet.valueOf("PLUTO"); // Pluto was reclassified!
        } catch (IllegalArgumentException e) {
            System.out.println("Caught: " + e.getMessage());
        }

        // Safe lookup helper pattern:
        System.out.println("\nSafe lookup:");
        String[] inputs = {"EARTH", "pluto", "MARS", "XYZZY"};
        for (String s : inputs) {
            try {
                Planet result = Planet.valueOf(s.toUpperCase());
                System.out.println("  Found: " + result);
            } catch (IllegalArgumentException e) {
                System.out.println("  Not found: " + s);
            }
        }


        // --------------------------------------------------------
        // METHOD 5: values()
        // Returns an array containing ALL constants in declaration order.
        // Essential for iterating over all enum values.
        // --------------------------------------------------------
        System.out.println("\n=== METHOD 5: values() ===");

        Planet[] allPlanets = Planet.values();
        System.out.println("Total planets: " + allPlanets.length); // 8

        System.out.println("\nIterating with for-each:");
        for (Planet planet : Planet.values()) {
            System.out.println("  " + planet);
        }

        System.out.println("\nIterating with index:");
        Planet[] planets = Planet.values();
        for (int i = 0; i < planets.length; i++) {
            System.out.println("  Planet #" + (i + 1) + ": " + planets[i]);
        }


        // --------------------------------------------------------
        // METHOD 6: compareTo(other)
        // Compares two enum constants by their ordinal values.
        // Returns: negative if this < other
        //          zero     if this == other
        //          positive if this > other
        // --------------------------------------------------------
        System.out.println("\n=== METHOD 6: compareTo() ===");

        Level beginner = Level.BEGINNER; // ordinal 0
        Level advanced = Level.ADVANCED; // ordinal 2
        Level expert   = Level.EXPERT;   // ordinal 3

        int result1 = beginner.compareTo(advanced);
        int result2 = advanced.compareTo(beginner);
        int result3 = advanced.compareTo(advanced);

        System.out.println("BEGINNER.compareTo(ADVANCED) = " + result1); // negative
        System.out.println("ADVANCED.compareTo(BEGINNER) = " + result2); // positive
        System.out.println("ADVANCED.compareTo(ADVANCED) = " + result3); // 0

        // Practical: check if user has minimum required level
        Level required  = Level.INTERMEDIATE;
        Level userLevel = Level.ADVANCED;

        if (userLevel.compareTo(required) >= 0) {
            System.out.println("\n" + userLevel + " meets the minimum requirement of " + required);
        }


        // --------------------------------------------------------
        // BONUS: getDeclaringClass()
        // Returns the Class object of the enum.
        // --------------------------------------------------------
        System.out.println("\n=== BONUS: getDeclaringClass() ===");

        System.out.println(Planet.EARTH.getDeclaringClass().getSimpleName()); // Planet
        System.out.println(Level.EXPERT.getDeclaringClass().getName());       // full class name


        // --------------------------------------------------------
        // PUTTING IT ALL TOGETHER — Practical example
        // --------------------------------------------------------
        System.out.println("\n=== PRACTICAL EXAMPLE ===");
        System.out.println("Color menu:");
        for (Color color : Color.values()) {
            System.out.printf("  [%d] %s%n", color.ordinal() + 1, color.name());
        }

        // Simulate user picking option 3
        int userChoice = 3;
        Color selected = Color.values()[userChoice - 1]; // ordinal is 0-based
        System.out.println("You selected: " + selected);

        // Sort colors alphabetically using compareTo on Strings
        Color[] colors = Color.values().clone();
        Arrays.sort(colors, (a, b) -> a.name().compareTo(b.name()));
        System.out.print("Alphabetical: ");
        for (Color c : colors) System.out.print(c + " ");
        System.out.println();

        System.out.println("\nLesson 2 Complete!");
    }
}

// ============================================================
//  SUMMARY
// ============================================================
//  name()       → "EARTH"           — exact declared name
//  ordinal()    → 2                 — 0-based position
//  toString()   → "EARTH"           — same as name() by default
//  valueOf("X") → Planet.X          — String to enum (throws if not found)
//  values()     → Planet[]          — all constants as array
//  compareTo()  → negative/0/positive by ordinal
//
//  COMPILE & RUN:
//    javac Lesson2_BuiltInMethods.java
//    java  Lesson2_BuiltInMethods
// ============================================================
