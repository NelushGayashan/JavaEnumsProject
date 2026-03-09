// ============================================================
//  LESSON 4 — ABSTRACT METHODS (Per-Constant Behavior)
// ============================================================
//
//  THE PROBLEM WITH SWITCH STATEMENTS:
//  ------------------------------------
//  In Lesson 3, all constants shared the SAME methods.
//  But what if each constant needs DIFFERENT behavior?
//
//  A beginner might write a switch everywhere:
//
//    switch (operation) {
//        case ADD:      return x + y;
//        case SUBTRACT: return x - y;
//        case MULTIPLY: return x * y;
//    }
//
//  Problems with this approach:
//    ❌ You repeat this switch in EVERY place you need it
//    ❌ If you add a new constant, you must update ALL switches
//    ❌ Easy to forget a case (the compiler won't always warn you)
//    ❌ Code gets long and messy
//
//  THE SOLUTION — ABSTRACT METHODS:
//  ---------------------------------
//  Declare an abstract method in the enum.
//  Each constant provides its OWN implementation.
//  The behavior travels WITH the constant — no switch needed!
//
//    enum Operation {
//        ADD { public double apply(double x, double y) { return x + y; } },
//        ...;
//        public abstract double apply(double x, double y);
//    }
//
//    // Anywhere in your code — just call it:
//    result = operation.apply(x, y);  // no switch needed!
//
// ============================================================

public class Lesson4_AbstractMethods {

    // --------------------------------------------------------
    // EXAMPLE 1: Math Operations
    // Each constant implements its own apply() method.
    // --------------------------------------------------------
    enum Operation {
        ADD("+") {
            @Override
            public double apply(double x, double y) {
                return x + y;
            }
        },
        SUBTRACT("-") {
            @Override
            public double apply(double x, double y) {
                return x - y;
            }
        },
        MULTIPLY("*") {
            @Override
            public double apply(double x, double y) {
                return x * y;
            }
        },
        DIVIDE("/") {
            @Override
            public double apply(double x, double y) {
                if (y == 0) throw new ArithmeticException("Cannot divide by zero!");
                return x / y;
            }
        },
        POWER("^") {
            @Override
            public double apply(double x, double y) {
                return Math.pow(x, y);
            }
        },
        MODULO("%") {
            @Override
            public double apply(double x, double y) {
                return x % y;
            }
        };

        private final String symbol;

        Operation(String symbol) {
            this.symbol = symbol;
        }

        // This forces EVERY constant to provide an implementation.
        // If a constant doesn't implement this → COMPILE ERROR.
        public abstract double apply(double x, double y);

        @Override
        public String toString() {
            return symbol;
        }
    }


    // --------------------------------------------------------
    // EXAMPLE 2: Shipping Speed
    // Each shipping option has different costs and delivery times.
    // --------------------------------------------------------
    enum ShippingSpeed {

        STANDARD {
            @Override public int    deliveryDays() { return 7;     }
            @Override public double cost()         { return 0.00;  }
            @Override public String description()  { return "Standard Shipping (Free)"; }
        },
        EXPRESS {
            @Override public int    deliveryDays() { return 3;     }
            @Override public double cost()         { return 9.99;  }
            @Override public String description()  { return "Express Shipping"; }
        },
        OVERNIGHT {
            @Override public int    deliveryDays() { return 1;     }
            @Override public double cost()         { return 24.99; }
            @Override public String description()  { return "Overnight Shipping"; }
        },
        SAME_DAY {
            @Override public int    deliveryDays() { return 0;     }
            @Override public double cost()         { return 39.99; }
            @Override public String description()  { return "Same-Day Delivery"; }
        };

        // Three abstract methods — each constant must implement all of them
        public abstract int    deliveryDays();
        public abstract double cost();
        public abstract String description();

        // Non-abstract method shared by all constants
        public String eta() {
            int days = deliveryDays();
            if (days == 0) return "Today!";
            if (days == 1) return "Tomorrow";
            return "In " + days + " days";
        }
    }


    // --------------------------------------------------------
    // EXAMPLE 3: Button Actions (UI event handling)
    // Each action knows how to execute itself.
    // --------------------------------------------------------
    enum ButtonAction {

        SAVE {
            @Override
            public void execute(String data) {
                System.out.println("  [SAVE] Saving data: " + data);
                // Imagine file/database write here
            }
            @Override
            public String label() { return "Save"; }
        },
        CANCEL {
            @Override
            public void execute(String data) {
                System.out.println("  [CANCEL] Operation cancelled. Changes discarded.");
            }
            @Override
            public String label() { return "Cancel"; }
        },
        DELETE {
            @Override
            public void execute(String data) {
                System.out.println("  [DELETE] Deleting record: " + data);
                // Imagine database delete here
            }
            @Override
            public String label() { return "Delete"; }
        },
        PRINT {
            @Override
            public void execute(String data) {
                System.out.println("  [PRINT] Sending to printer: " + data);
            }
            @Override
            public String label() { return "Print"; }
        };

        public abstract void   execute(String data);
        public abstract String label();
    }


    // --------------------------------------------------------
    // EXAMPLE 4: Comparison Operators
    // Notice how we avoid a giant switch statement.
    // --------------------------------------------------------
    enum Comparator {

        LESS_THAN("<") {
            @Override
            public boolean test(int a, int b) { return a < b; }
        },
        LESS_OR_EQUAL("<=") {
            @Override
            public boolean test(int a, int b) { return a <= b; }
        },
        EQUAL("==") {
            @Override
            public boolean test(int a, int b) { return a == b; }
        },
        NOT_EQUAL("!=") {
            @Override
            public boolean test(int a, int b) { return a != b; }
        },
        GREATER_OR_EQUAL(">=") {
            @Override
            public boolean test(int a, int b) { return a >= b; }
        },
        GREATER_THAN(">") {
            @Override
            public boolean test(int a, int b) { return a > b; }
        };

        private final String symbol;

        Comparator(String symbol) { this.symbol = symbol; }

        public abstract boolean test(int a, int b);

        @Override
        public String toString() { return symbol; }
    }


    public static void main(String[] args) {

        // --------------------------------------------------------
        // Using Operation
        // --------------------------------------------------------
        System.out.println("=== Math Operations ===");

        double x = 10, y = 3;
        System.out.println("Values: x=" + x + ", y=" + y);
        System.out.println();

        for (Operation op : Operation.values()) {
            try {
                System.out.printf("  %4.1f %s %4.1f = %.4f%n",
                    x, op, y, op.apply(x, y));
            } catch (ArithmeticException e) {
                System.out.println("  Error: " + e.getMessage());
            }
        }

        // Use a specific operation
        System.out.println("\nSpecific operation:");
        Operation chosen = Operation.MULTIPLY;
        System.out.printf("  %s.apply(6, 7) = %.0f%n", chosen, chosen.apply(6, 7));

        // Demonstrate no switch needed — just call apply()
        System.out.println("\nAll ops on 100 and 4:");
        for (Operation op : Operation.values()) {
            System.out.printf("  100 %s 4 = %.2f%n", op, op.apply(100, 4));
        }


        // --------------------------------------------------------
        // Using ShippingSpeed
        // --------------------------------------------------------
        System.out.println("\n=== Shipping Options ===");
        System.out.printf("  %-30s  %-8s  %-10s  %s%n", "Option", "Days", "Cost", "ETA");
        System.out.println("  " + "-".repeat(62));
        for (ShippingSpeed speed : ShippingSpeed.values()) {
            System.out.printf("  %-30s  %-8d  $%-9.2f  %s%n",
                speed.description(),
                speed.deliveryDays(),
                speed.cost(),
                speed.eta());
        }

        // Business logic: find cheapest option within N days
        int maxDays = 3;
        System.out.println("\nBest option arriving within " + maxDays + " days:");
        for (ShippingSpeed speed : ShippingSpeed.values()) {
            if (speed.deliveryDays() <= maxDays) {
                System.out.println("  → " + speed.description()
                    + " ($" + speed.cost() + ")");
                break;
            }
        }


        // --------------------------------------------------------
        // Using ButtonAction
        // --------------------------------------------------------
        System.out.println("\n=== Button Actions ===");

        String record = "User#1042 - John Doe";

        // Simulate different button clicks:
        ButtonAction.SAVE.execute(record);
        ButtonAction.PRINT.execute(record);
        ButtonAction.DELETE.execute(record);
        ButtonAction.CANCEL.execute(record);

        // Build a menu from enum constants (no hardcoding!)
        System.out.println("\nAvailable actions:");
        for (ButtonAction action : ButtonAction.values()) {
            System.out.println("  [" + action.ordinal() + "] " + action.label());
        }


        // --------------------------------------------------------
        // Using Comparator
        // --------------------------------------------------------
        System.out.println("\n=== Comparators ===");

        int a = 5, b = 8;
        System.out.println("Testing " + a + " vs " + b + ":");
        for (Comparator cmp : Comparator.values()) {
            System.out.printf("  %d %s %d → %b%n", a, cmp, b, cmp.test(a, b));
        }

        System.out.println("\nLesson 4 Complete!");
    }
}

// ============================================================
//  SUMMARY
// ============================================================
//  - Declare abstract method in the enum body
//  - Each constant provides its own { } implementation block
//  - The compiler forces every constant to implement all abstract methods
//  - No switch/if-else chains needed — just call the method
//  - Combine with fields (Lesson 3) for maximum power
//
//  PATTERN TO REMEMBER:
//    CONSTANT_NAME {
//        @Override
//        public ReturnType methodName(params) { ... }
//    };
//    public abstract ReturnType methodName(params);  // declared below constants
//
//  COMPILE & RUN:
//    javac Lesson4_AbstractMethods.java
//    java  Lesson4_AbstractMethods
// ============================================================
