// ============================================================
//  LESSON 7 — EnumMap
// ============================================================
//
//  WHAT IS EnumMap?
//  ----------------
//  EnumMap is a specialized Map implementation where the KEYS
//  must be enum constants. Values can be any type.
//
//  WHY NOT JUST USE HashMap?
//  -------------------------
//  You COULD use HashMap<Day, String> — it works fine.
//  But EnumMap is:
//    ✅ Much FASTER       — uses a simple array internally (O(1) by ordinal)
//    ✅ Less MEMORY       — no hashing, no buckets, just a plain array
//    ✅ Always ORDERED    — iteration always follows enum declaration order
//    ✅ More READABLE     — makes intent clear ("keys are always enum constants")
//    ✅ No null keys      — prevents accidental null key bugs
//
//  HOW IT WORKS INTERNALLY:
//  ------------------------
//  EnumMap is basically just an Object[] array.
//    Day.MON.ordinal() = 0  →  array[0] = value for MON
//    Day.TUE.ordinal() = 1  →  array[1] = value for TUE
//  get(Day.MON) just does: return array[Day.MON.ordinal()]
//  That's why it's so fast — no hashing at all!
//
//  WHEN TO USE EnumMap:
//  --------------------
//  Whenever your Map keys are enum constants. Classic uses:
//    - Map each day to a task/event
//    - Map each category to a count
//    - Map each status to a handler
//    - Map each priority to a list of items
//
// ============================================================

import java.util.EnumMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Lesson7_EnumMap {

    enum Day      { MON, TUE, WED, THU, FRI, SAT, SUN }
    enum Priority { LOW, MEDIUM, HIGH, CRITICAL }
    enum Month    { JAN, FEB, MAR, APR, MAY, JUN, JUL, AUG, SEP, OCT, NOV, DEC }
    enum Status   { PENDING, IN_PROGRESS, REVIEW, DONE, CANCELLED }


    public static void main(String[] args) {

        // --------------------------------------------------------
        // PART 1: Creating an EnumMap and basic operations
        // --------------------------------------------------------
        System.out.println("=== PART 1: Basic Operations ===");

        // Create — must pass the enum class
        EnumMap<Day, String> schedule = new EnumMap<>(Day.class);

        // put(key, value)
        schedule.put(Day.MON, "Team standup at 9am");
        schedule.put(Day.WED, "Sprint review at 2pm");
        schedule.put(Day.FRI, "Retrospective at 4pm");
        schedule.put(Day.SAT, "Rest day");
        schedule.put(Day.SUN, "Rest day");

        // get(key)
        System.out.println("Monday   : " + schedule.get(Day.MON));
        System.out.println("Wednesday: " + schedule.get(Day.WED));
        System.out.println("Tuesday  : " + schedule.get(Day.TUE)); // null — not in map

        // size, isEmpty
        System.out.println("Size     : " + schedule.size());
        System.out.println("Empty?   : " + schedule.isEmpty());

        // containsKey, containsValue
        System.out.println("Has MON? : " + schedule.containsKey(Day.MON));
        System.out.println("Has TUE? : " + schedule.containsKey(Day.TUE));


        // --------------------------------------------------------
        // PART 2: Safe Getting — getOrDefault
        // --------------------------------------------------------
        System.out.println("\n=== PART 2: getOrDefault ===");

        // getOrDefault returns a fallback if the key isn't present
        // Much cleaner than checking for null
        String tuesday  = schedule.getOrDefault(Day.TUE, "No meeting scheduled");
        String thursday = schedule.getOrDefault(Day.THU, "No meeting scheduled");
        String monday   = schedule.getOrDefault(Day.MON, "No meeting scheduled");

        System.out.println("TUE: " + tuesday);   // No meeting scheduled
        System.out.println("THU: " + thursday);  // No meeting scheduled
        System.out.println("MON: " + monday);    // Team standup at 9am


        // --------------------------------------------------------
        // PART 3: putIfAbsent and compute
        // --------------------------------------------------------
        System.out.println("\n=== PART 3: putIfAbsent ===");

        // putIfAbsent — only inserts if key not already present
        schedule.putIfAbsent(Day.TUE, "Focus time");   // TUE not present → adds it
        schedule.putIfAbsent(Day.MON, "CHANGED");       // MON already present → ignores

        System.out.println("After putIfAbsent:");
        System.out.println("  MON: " + schedule.get(Day.MON)); // unchanged
        System.out.println("  TUE: " + schedule.get(Day.TUE)); // now set


        // --------------------------------------------------------
        // PART 4: Iterating — always in declaration order!
        // --------------------------------------------------------
        System.out.println("\n=== PART 4: Iterating ===");

        System.out.println("Full schedule (declaration order):");
        for (Map.Entry<Day, String> entry : schedule.entrySet()) {
            System.out.printf("  %-4s : %s%n", entry.getKey(), entry.getValue());
        }

        System.out.println("\nJust keys:");
        for (Day day : schedule.keySet()) {
            System.out.print(day + " ");
        }
        System.out.println();

        System.out.println("\nJust values:");
        for (String value : schedule.values()) {
            System.out.println("  " + value);
        }


        // --------------------------------------------------------
        // PART 5: Removing entries
        // --------------------------------------------------------
        System.out.println("\n=== PART 5: Removing ===");

        System.out.println("Before: " + schedule.keySet());
        schedule.remove(Day.SAT);
        schedule.remove(Day.SUN);
        System.out.println("After removing SAT, SUN: " + schedule.keySet());


        // --------------------------------------------------------
        // PART 6: Counting with EnumMap
        // --------------------------------------------------------
        System.out.println("\n=== PART 6: Counting per Category ===");

        // Initialize all priorities to 0
        EnumMap<Priority, Integer> taskCount = new EnumMap<>(Priority.class);
        for (Priority p : Priority.values()) {
            taskCount.put(p, 0);
        }

        // Simulate incoming tasks
        Priority[] tasks = {
            Priority.HIGH, Priority.LOW, Priority.HIGH,
            Priority.CRITICAL, Priority.MEDIUM, Priority.HIGH,
            Priority.LOW, Priority.CRITICAL, Priority.MEDIUM
        };

        for (Priority p : tasks) {
            taskCount.put(p, taskCount.get(p) + 1);
        }

        System.out.println("Task distribution:");
        for (Map.Entry<Priority, Integer> entry : taskCount.entrySet()) {
            String bar = "█".repeat(entry.getValue());
            System.out.printf("  %-10s : %s (%d)%n",
                entry.getKey(), bar, entry.getValue());
        }


        // --------------------------------------------------------
        // PART 7: EnumMap with List values
        // --------------------------------------------------------
        System.out.println("\n=== PART 7: EnumMap<Priority, List<String>> ===");

        EnumMap<Priority, List<String>> tasksByPriority = new EnumMap<>(Priority.class);

        // Initialize all lists
        for (Priority p : Priority.values()) {
            tasksByPriority.put(p, new ArrayList<>());
        }

        // Add tasks
        tasksByPriority.get(Priority.CRITICAL).add("Fix login bug");
        tasksByPriority.get(Priority.CRITICAL).add("Security patch");
        tasksByPriority.get(Priority.HIGH).add("Update user profile page");
        tasksByPriority.get(Priority.HIGH).add("Fix checkout flow");
        tasksByPriority.get(Priority.MEDIUM).add("Add dark mode");
        tasksByPriority.get(Priority.MEDIUM).add("Improve search");
        tasksByPriority.get(Priority.LOW).add("Update README");

        // Print by priority
        for (Map.Entry<Priority, List<String>> entry : tasksByPriority.entrySet()) {
            if (!entry.getValue().isEmpty()) {
                System.out.println("[" + entry.getKey() + "]");
                for (String task : entry.getValue()) {
                    System.out.println("  - " + task);
                }
            }
        }


        // --------------------------------------------------------
        // PART 8: Monthly Sales Report
        // --------------------------------------------------------
        System.out.println("\n=== PART 8: Monthly Sales ===");

        EnumMap<Month, Double> sales = new EnumMap<>(Month.class);
        sales.put(Month.JAN, 12400.00);
        sales.put(Month.FEB, 15200.00);
        sales.put(Month.MAR, 17800.00);
        sales.put(Month.APR, 13900.00);
        sales.put(Month.MAY, 19200.00);
        sales.put(Month.JUN, 22100.00);
        sales.put(Month.JUL, 18500.00);
        sales.put(Month.AUG, 16700.00);
        sales.put(Month.SEP, 20300.00);
        sales.put(Month.OCT, 24100.00);
        sales.put(Month.NOV, 28000.00);
        sales.put(Month.DEC, 31500.00);

        double total  = 0;
        double best   = 0;
        Month  bestMonth = null;

        System.out.printf("  %-5s  %10s  %s%n", "Month", "Sales", "Bar");
        System.out.println("  " + "-".repeat(50));

        for (Map.Entry<Month, Double> entry : sales.entrySet()) {
            double amount = entry.getValue();
            total += amount;
            if (amount > best) {
                best      = amount;
                bestMonth = entry.getKey();
            }
            int bars = (int)(amount / 1000);
            System.out.printf("  %-5s  $%9.2f  %s%n",
                entry.getKey(), amount, "▮".repeat(bars));
        }

        System.out.printf("%n  Total  : $%.2f%n", total);
        System.out.printf("  Average: $%.2f%n", total / 12);
        System.out.printf("  Best   : %s ($%.2f)%n", bestMonth, best);


        // --------------------------------------------------------
        // PART 9: Status → Handler mapping
        // --------------------------------------------------------
        System.out.println("\n=== PART 9: Status Handler Mapping ===");

        EnumMap<Status, String> handlers = new EnumMap<>(Status.class);
        handlers.put(Status.PENDING,     "Assign to available team member");
        handlers.put(Status.IN_PROGRESS, "Monitor progress, unblock if needed");
        handlers.put(Status.REVIEW,      "Send to code reviewer");
        handlers.put(Status.DONE,        "Archive and notify stakeholders");
        handlers.put(Status.CANCELLED,   "Log reason and close ticket");

        Status current = Status.REVIEW;
        System.out.println("Current status: " + current);
        System.out.println("Action        : " + handlers.get(current));

        System.out.println("\nAll status handlers:");
        for (Map.Entry<Status, String> entry : handlers.entrySet()) {
            System.out.printf("  %-12s → %s%n", entry.getKey(), entry.getValue());
        }

        System.out.println("\nLesson 7 Complete!");
    }
}

// ============================================================
//  SUMMARY
// ============================================================
//  new EnumMap<>(MyEnum.class)       → create empty map
//  map.put(key, value)               → insert/update
//  map.get(key)                      → retrieve (null if absent)
//  map.getOrDefault(key, fallback)   → safe retrieve
//  map.putIfAbsent(key, value)       → only insert if absent
//  map.containsKey(key)              → membership check
//  map.remove(key)                   → delete entry
//  map.entrySet()                    → iterate key-value pairs
//  map.keySet()                      → iterate keys
//  map.values()                      → iterate values
//
//  ALWAYS prefer EnumMap over HashMap when keys are enums.
//  Iteration ALWAYS follows enum declaration order.
//
//  COMPILE & RUN:
//    javac Lesson7_EnumMap.java
//    java  Lesson7_EnumMap
// ============================================================
