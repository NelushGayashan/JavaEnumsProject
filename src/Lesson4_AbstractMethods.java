public class Lesson4_AbstractMethods {

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

        public abstract double apply(double x, double y);

        @Override
        public String toString() {
            return symbol;
        }
    }

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

        public abstract int    deliveryDays();
        public abstract double cost();
        public abstract String description();

        public String eta() {
            int days = deliveryDays();
            if (days == 0) return "Today!";
            if (days == 1) return "Tomorrow";
            return "In " + days + " days";
        }
    }

    enum ButtonAction {

        SAVE {
            @Override
            public void execute(String data) {
                System.out.println("  [SAVE] Saving data: " + data);
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

        System.out.println("\nSpecific operation:");
        Operation chosen = Operation.MULTIPLY;
        System.out.printf("  %s.apply(6, 7) = %.0f%n", chosen, chosen.apply(6, 7));

        System.out.println("\nAll ops on 100 and 4:");
        for (Operation op : Operation.values()) {
            System.out.printf("  100 %s 4 = %.2f%n", op, op.apply(100, 4));
        }

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

        int maxDays = 3;
        System.out.println("\nBest option arriving within " + maxDays + " days:");
        for (ShippingSpeed speed : ShippingSpeed.values()) {
            if (speed.deliveryDays() <= maxDays) {
                System.out.println("  → " + speed.description()
                    + " ($" + speed.cost() + ")");
                break;
            }
        }

        System.out.println("\n=== Button Actions ===");

        String record = "User#1042 - John Doe";

        ButtonAction.SAVE.execute(record);
        ButtonAction.PRINT.execute(record);
        ButtonAction.DELETE.execute(record);
        ButtonAction.CANCEL.execute(record);

        System.out.println("\nAvailable actions:");
        for (ButtonAction action : ButtonAction.values()) {
            System.out.println("  [" + action.ordinal() + "] " + action.label());
        }

        System.out.println("\n=== Comparators ===");

        int a = 5, b = 8;
        System.out.println("Testing " + a + " vs " + b + ":");
        for (Comparator cmp : Comparator.values()) {
            System.out.printf("  %d %s %d → %b%n", a, cmp, b, cmp.test(a, b));
        }

        System.out.println("\nLesson 4 Complete!");
    }
}
