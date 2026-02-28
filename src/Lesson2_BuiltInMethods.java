// ============================================================
//  LESSON 2 — BUILT-IN ENUM METHODS
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
        // --------------------------------------------------------
        System.out.println("=== METHOD 1: name() ===");

        Planet p = Planet.EARTH;
        String name = p.name();
        System.out.println("p.name()    = " + name);
        System.out.println("Type        : " + name.getClass().getSimpleName());

        System.out.println(Planet.JUPITER.name());
        System.out.println(Planet.NEPTUNE.name());


        // --------------------------------------------------------
        // METHOD 2: ordinal()
        // --------------------------------------------------------
        System.out.println("\n=== METHOD 2: ordinal() ===");

        System.out.println("MERCURY ordinal = " + Planet.MERCURY.ordinal()); // 0
        System.out.println("VENUS   ordinal = " + Planet.VENUS.ordinal());   // 1
        System.out.println("EARTH   ordinal = " + Planet.EARTH.ordinal());   // 2
        System.out.println("NEPTUNE ordinal = " + Planet.NEPTUNE.ordinal()); // 7

        System.out.println("\nAll planets with ordinals:");
        for (Planet planet : Planet.values()) {
            System.out.println("  [" + planet.ordinal() + "] " + planet.name());
        }


        // --------------------------------------------------------
        // METHOD 3: toString()
        // --------------------------------------------------------
        System.out.println("\n=== METHOD 3: toString() ===");

        Planet earth = Planet.EARTH;
        System.out.println("name()     = " + earth.name());
        System.out.println("toString() = " + earth.toString());
        System.out.println("Auto: " + earth);


        // --------------------------------------------------------
        // METHOD 4: valueOf(String)
        // --------------------------------------------------------
        System.out.println("\n=== METHOD 4: valueOf(String) ===");

        Planet fromString = Planet.valueOf("MARS");
        System.out.println("valueOf(\"MARS\") = " + fromString);

        String input = "SATURN";
        Planet userPlanet = Planet.valueOf(input);
        System.out.println("User chose: " + userPlanet);
        System.out.println("\nHandling invalid valueOf:");
        try {
            Planet invalid = Planet.valueOf("PLUTO");
        } catch (IllegalArgumentException e) {
            System.out.println("Caught: " + e.getMessage());
        }

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
        // --------------------------------------------------------
        System.out.println("\n=== METHOD 5: values() ===");

        Planet[] allPlanets = Planet.values();
        System.out.println("Total planets: " + allPlanets.length);

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
        // --------------------------------------------------------
        System.out.println("\n=== METHOD 6: compareTo() ===");

        Level beginner = Level.BEGINNER;
        Level advanced = Level.ADVANCED;
        Level expert   = Level.EXPERT;

        int result1 = beginner.compareTo(advanced);
        int result2 = advanced.compareTo(beginner);
        int result3 = advanced.compareTo(advanced);

        System.out.println("BEGINNER.compareTo(ADVANCED) = " + result1);
        System.out.println("ADVANCED.compareTo(BEGINNER) = " + result2);
        System.out.println("ADVANCED.compareTo(ADVANCED) = " + result3);

        // Practical: check if user has minimum required level
        Level required  = Level.INTERMEDIATE;
        Level userLevel = Level.ADVANCED;

        if (userLevel.compareTo(required) >= 0) {
            System.out.println("\n" + userLevel + " meets the minimum requirement of " + required);
        }


        // --------------------------------------------------------
        // BONUS: getDeclaringClass()
        // --------------------------------------------------------
        System.out.println("\n=== BONUS: getDeclaringClass() ===");

        System.out.println(Planet.EARTH.getDeclaringClass().getSimpleName());
        System.out.println(Level.EXPERT.getDeclaringClass().getName());


        // --------------------------------------------------------
        // PUTTING IT ALL TOGETHER
        // --------------------------------------------------------
        System.out.println("\n=== PRACTICAL EXAMPLE ===");
        System.out.println("Color menu:");
        for (Color color : Color.values()) {
            System.out.printf("  [%d] %s%n", color.ordinal() + 1, color.name());
        }

        int userChoice = 3;
        Color selected = Color.values()[userChoice - 1];
        System.out.println("You selected: " + selected);

        Color[] colors = Color.values().clone();
        Arrays.sort(colors, (a, b) -> a.name().compareTo(b.name()));
        System.out.print("Alphabetical: ");
        for (Color c : colors) System.out.print(c + " ");
        System.out.println();

        System.out.println("\nLesson 2 Complete!");
    }
}