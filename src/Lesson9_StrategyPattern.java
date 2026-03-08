import java.util.Arrays;

public class Lesson9_StrategyPattern {

    enum DiscountStrategy {

        NONE {
            @Override
            public double apply(double price) { return price; }
            @Override
            public String description()       { return "No discount applied"; }
            @Override
            public int    percentOff()        { return 0; }
        },
        STUDENT {
            @Override
            public double apply(double price) { return price * 0.80; }
            @Override
            public String description()       { return "Student discount (20% off)"; }
            @Override
            public int    percentOff()        { return 20; }
        },
        SENIOR {
            @Override
            public double apply(double price) { return price * 0.85; }
            @Override
            public String description()       { return "Senior discount (15% off)"; }
            @Override
            public int    percentOff()        { return 15; }
        },
        MEMBER {
            @Override
            public double apply(double price) { return price * 0.90; }
            @Override
            public String description()       { return "Member discount (10% off)"; }
            @Override
            public int    percentOff()        { return 10; }
        },
        VIP {
            @Override
            public double apply(double price) { return price * 0.70; }
            @Override
            public String description()       { return "VIP discount (30% off)"; }
            @Override
            public int    percentOff()        { return 30; }
        },
        FLASH_SALE {
            @Override
            public double apply(double price) { return price * 0.50; }
            @Override
            public String description()       { return "Flash sale (50% off!)"; }
            @Override
            public int    percentOff()        { return 50; }
        };

        public abstract double apply(double price);
        public abstract String description();
        public abstract int    percentOff();

        public double savings(double price) {
            return price - apply(price);
        }
    }

    enum SortStrategy {

        BUBBLE {
            @Override
            public void sort(int[] arr) {
                int n = arr.length;
                for (int i = 0; i < n - 1; i++) {
                    for (int j = 0; j < n - i - 1; j++) {
                        if (arr[j] > arr[j + 1]) {
                            int tmp = arr[j]; arr[j] = arr[j + 1]; arr[j + 1] = tmp;
                        }
                    }
                }
            }
            @Override public String description() { return "Bubble Sort  — O(n²)"; }
        },
        SELECTION {
            @Override
            public void sort(int[] arr) {
                int n = arr.length;
                for (int i = 0; i < n - 1; i++) {
                    int minIdx = i;
                    for (int j = i + 1; j < n; j++) {
                        if (arr[j] < arr[minIdx]) minIdx = j;
                    }
                    int tmp = arr[minIdx]; arr[minIdx] = arr[i]; arr[i] = tmp;
                }
            }
            @Override public String description() { return "Selection Sort — O(n²)"; }
        },
        INSERTION {
            @Override
            public void sort(int[] arr) {
                int n = arr.length;
                for (int i = 1; i < n; i++) {
                    int key = arr[i];
                    int j   = i - 1;
                    while (j >= 0 && arr[j] > key) {
                        arr[j + 1] = arr[j];
                        j--;
                    }
                    arr[j + 1] = key;
                }
            }
            @Override public String description() { return "Insertion Sort — O(n²), fast for small/nearly sorted"; }
        },
        BUILT_IN {
            @Override
            public void sort(int[] arr) {
                Arrays.sort(arr); // Java's dual-pivot quicksort
            }
            @Override public String description() { return "Built-in Sort  — O(n log n)"; }
        };

        public abstract void sort(int[] arr);
        public abstract String description();
    }


    enum PasswordPolicy {

        BASIC {
            @Override
            public boolean validate(String password) {
                return password != null && password.length() >= 6;
            }
            @Override
            public String requirements() {
                return "At least 6 characters";
            }
        },
        STANDARD {
            @Override
            public boolean validate(String password) {
                if (password == null || password.length() < 8) return false;
                boolean hasUpper = password.chars().anyMatch(Character::isUpperCase);
                boolean hasDigit = password.chars().anyMatch(Character::isDigit);
                return hasUpper && hasDigit;
            }
            @Override
            public String requirements() {
                return "8+ chars, 1 uppercase, 1 digit";
            }
        },
        STRONG {
            @Override
            public boolean validate(String password) {
                if (password == null || password.length() < 12) return false;
                boolean hasUpper   = password.chars().anyMatch(Character::isUpperCase);
                boolean hasLower   = password.chars().anyMatch(Character::isLowerCase);
                boolean hasDigit   = password.chars().anyMatch(Character::isDigit);
                boolean hasSpecial = password.matches(".*[!@#$%^&*()_+].*");
                return hasUpper && hasLower && hasDigit && hasSpecial;
            }
            @Override
            public String requirements() {
                return "12+ chars, upper, lower, digit, special char";
            }
        };

        public abstract boolean validate(String password);
        public abstract String  requirements();
    }


    enum ShippingStrategy {

        STANDARD {
            @Override
            public double calculate(double weight, double distance) {
                return 2.00 + (weight * 0.50) + (distance * 0.01);
            }
            @Override public String label() { return "Standard Shipping"; }
        },
        EXPRESS {
            @Override
            public double calculate(double weight, double distance) {
                return 10.00 + (weight * 1.00) + (distance * 0.02);
            }
            @Override public String label() { return "Express Shipping"; }
        },
        OVERNIGHT {
            @Override
            public double calculate(double weight, double distance) {
                return 25.00 + (weight * 2.00) + (distance * 0.05);
            }
            @Override public String label() { return "Overnight Shipping"; }
        },
        FREE {
            @Override
            public double calculate(double weight, double distance) {
                return 0.00;
            }
            @Override public String label() { return "Free Shipping"; }
        };

        public abstract double calculate(double weight, double distance);
        public abstract String label();
    }


    public static void main(String[] args) {

        System.out.println("=== Discount Strategies ===");

        double originalPrice = 150.00;
        System.out.printf("Original price: $%.2f%n%n", originalPrice);
        System.out.printf("  %-35s  %-10s  %-10s  %s%n", "Strategy", "You Pay", "You Save", "% Off");
        System.out.println("  " + "-".repeat(70));

        for (DiscountStrategy strategy : DiscountStrategy.values()) {
            System.out.printf("  %-35s  $%-9.2f  $%-9.2f  %d%%%n",
                strategy.description(),
                strategy.apply(originalPrice),
                strategy.savings(originalPrice),
                strategy.percentOff());
        }

        System.out.println("\nRuntime strategy selection:");
        String userType = "VIP";
        DiscountStrategy chosen = DiscountStrategy.valueOf(userType);
        System.out.printf("User type '%s' → %s → final price: $%.2f%n",
            userType, chosen.description(), chosen.apply(originalPrice));


        System.out.println("\n=== Sorting Strategies ===");

        int[] original = {64, 34, 25, 12, 22, 11, 90, 47, 3, 78};
        System.out.println("Original: " + Arrays.toString(original));
        System.out.println();

        for (SortStrategy strategy : SortStrategy.values()) {
            int[] arr = original.clone();
            strategy.sort(arr);
            System.out.printf("  %-45s %s%n", strategy.description(), Arrays.toString(arr));
        }

        System.out.println("\nAuto-select strategy by data size:");
        int[] smallData = {5, 2, 8, 1, 9};
        int[] largeData = new int[100];
        for (int i = 0; i < largeData.length; i++) largeData[i] = 100 - i;

        SortStrategy smallStrategy = SortStrategy.INSERTION;
        SortStrategy largeStrategy = SortStrategy.BUILT_IN;

        smallStrategy.sort(smallData);
        largeStrategy.sort(largeData);
        System.out.println("Small data sorted: " + Arrays.toString(smallData));
        System.out.println("Large data sorted (first 10): "
            + Arrays.toString(Arrays.copyOf(largeData, 10)) + "...");


        System.out.println("\n=== Password Policies ===");

        String[] passwords = {"abc", "password", "Password1", "SecurePass1!", "MyStr0ng!Pass"};

        System.out.printf("  %-20s", "Password");
        for (PasswordPolicy policy : PasswordPolicy.values()) {
            System.out.printf("  %-10s", policy.name());
        }
        System.out.println();
        System.out.println("  " + "-".repeat(55));

        for (String pwd : passwords) {
            System.out.printf("  %-20s", pwd.length() > 10 ? pwd.substring(0, 10) + "..." : pwd);
            for (PasswordPolicy policy : PasswordPolicy.values()) {
                System.out.printf("  %-10s", policy.validate(pwd) ? "✅ PASS" : "❌ FAIL");
            }
            System.out.println();
        }

        System.out.println("\nPolicy requirements:");
        for (PasswordPolicy policy : PasswordPolicy.values()) {
            System.out.println("  " + policy + ": " + policy.requirements());
        }


        System.out.println("\n=== Shipping Cost Calculator ===");

        double weight   = 2.5;
        double distance = 300;
        System.out.printf("Package: %.1f kg, Distance: %.0f km%n%n", weight, distance);

        System.out.printf("  %-20s  %s%n", "Method", "Cost");
        System.out.println("  " + "-".repeat(35));
        for (ShippingStrategy strategy : ShippingStrategy.values()) {
            double cost = strategy.calculate(weight, distance);
            System.out.printf("  %-20s  $%.2f%n", strategy.name(), cost);
        }

        ShippingStrategy cheapest = ShippingStrategy.STANDARD;
        double lowestCost = cheapest.calculate(weight, distance);
        for (ShippingStrategy s : ShippingStrategy.values()) {
            double cost = s.calculate(weight, distance);
            if (cost < lowestCost) {
                lowestCost = cost;
                cheapest = s;
            }
        }
        System.out.printf("\nCheapest option: %s ($%.2f)%n", cheapest.name(), lowestCost);

        System.out.println("\nLesson 9 Complete!");
    }
}

