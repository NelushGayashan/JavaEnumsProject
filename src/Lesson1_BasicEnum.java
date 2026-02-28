// ============================================================
//  LESSON 1 — BASIC ENUM
// ============================================================

public class Lesson1_BasicEnum {

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

        System.out.println("Direction : " + dir);
        System.out.println("Season    : " + season);
        System.out.println("Day       : " + day);


        // --------------------------------------------------------
        // PART 2: Comparing enum values
        // --------------------------------------------------------
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
        System.out.println("\n=== PART 5: switch expression (modern) ===");

        String dayType = switch (today) {
            case SATURDAY, SUNDAY                          -> "Weekend";
            case MONDAY, FRIDAY                            -> "Near the weekend";
            case TUESDAY, WEDNESDAY, THURSDAY              -> "Midweek";
            default                                        -> "Unknown";
        };
        System.out.println(today + " is: " + dayType);

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
        String badDirection = "NROTH";
        if (badDirection.equals("NORTH")) {
            System.out.println("Going north (this won't print due to typo)");
        }

        int badDay = 9;
        System.out.println("Bad day number: " + badDay);

        Direction goodDir = Direction.NORTH;
        System.out.println("Good direction: " + goodDir);

        System.out.println("\nLesson 1 Complete!");
    }
}