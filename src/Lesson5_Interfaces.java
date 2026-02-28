// ============================================================
//  LESSON 5 — IMPLEMENTING INTERFACES
// ============================================================
//
//  KEY INSIGHT: Enums CAN implement interfaces.
//
//  Since an enum is a class, it can implement one or more
//  interfaces — just like any other class.
//
//  WHY IS THIS POWERFUL?
//  ----------------------
//  1. POLYMORPHISM — You can pass an enum to a method that
//     accepts an interface type. The method doesn't even need
//     to know it's talking to an enum.
//
//  2. CONTRACTS — The interface guarantees every constant
//     provides certain behavior.
//
//  3. MULTIPLE INTERFACES — An enum can implement as many
//     interfaces as needed.
//
//  DIFFERENCE FROM ABSTRACT METHODS (Lesson 4):
//  -----------------------------------------------
//  - Abstract methods: behavior is DEFINED INSIDE the enum
//  - Interface methods: behavior CONTRACT is defined OUTSIDE
//    the enum — the enum then promises to fulfill it
//
//  WHAT ENUMS CANNOT DO:
//  ----------------------
//  ✅ Can: implement interfaces
//  ❌ Cannot: extend classes (already extends java.lang.Enum)
//
// ============================================================

public class Lesson5_Interfaces {

    // --------------------------------------------------------
    // Define our interfaces
    // --------------------------------------------------------

    // Anything that can produce a display string
    interface Displayable {
        String display();
    }

    // Anything that has a price
    interface Priceable {
        double basePrice();

        // Default method — automatically available to all implementors
        default double withTax(double taxRate) {
            return basePrice() * (1 + taxRate);
        }

        default String formattedPrice() {
            return String.format("$%.2f", basePrice());
        }
    }

    // Anything that can be described
    interface Describable {
        String describe();
    }


    // --------------------------------------------------------
    // EXAMPLE 1: Menu Items implementing Displayable + Priceable
    // --------------------------------------------------------
    enum MenuItem implements Displayable, Priceable {

        BURGER {
            @Override public String display()   { return "🍔 Classic Burger"; }
            @Override public double basePrice() { return 8.99;  }
        },
        PIZZA {
            @Override public String display()   { return "🍕 Margherita Pizza"; }
            @Override public double basePrice() { return 11.99; }
        },
        SALAD {
            @Override public String display()   { return "🥗 Garden Salad";    }
            @Override public double basePrice() { return 6.49;  }
        },
        PASTA {
            @Override public String display()   { return "🍝 Spaghetti Bolognese"; }
            @Override public double basePrice() { return 10.49; }
        },
        SODA {
            @Override public String display()   { return "🥤 Soft Drink";      }
            @Override public double basePrice() { return 1.99;  }
        };

        // We can also add enum-specific methods
        public boolean isMainCourse() {
            return this != SODA;
        }
    }


    // --------------------------------------------------------
    // EXAMPLE 2: Language enum implementing Displayable
    // --------------------------------------------------------
    interface Greetable {
        String greet(String name);
        String farewell(String name);
    }

    enum Language implements Greetable, Displayable {

        ENGLISH {
            @Override public String greet(String name)    { return "Hello, "    + name + "!";     }
            @Override public String farewell(String name) { return "Goodbye, "  + name + "!";     }
            @Override public String display()             { return "English (EN)";                }
        },
        SPANISH {
            @Override public String greet(String name)    { return "¡Hola, "    + name + "!";     }
            @Override public String farewell(String name) { return "¡Adiós, "   + name + "!";     }
            @Override public String display()             { return "Spanish (ES)";                }
        },
        FRENCH {
            @Override public String greet(String name)    { return "Bonjour, "  + name + "!";     }
            @Override public String farewell(String name) { return "Au revoir, " + name + "!";    }
            @Override public String display()             { return "French (FR)";                 }
        },
        GERMAN {
            @Override public String greet(String name)    { return "Hallo, "    + name + "!";     }
            @Override public String farewell(String name) { return "Auf Wiedersehen, " + name + "!"; }
            @Override public String display()             { return "German (DE)";                 }
        },
        JAPANESE {
            @Override public String greet(String name)    { return "こんにちは、" + name + "さん!"; }
            @Override public String farewell(String name) { return "さようなら、" + name + "さん!"; }
            @Override public String display()             { return "Japanese (JA)";               }
        };
    }


    // --------------------------------------------------------
    // EXAMPLE 3: Shapes implementing Describable + a custom area()
    // --------------------------------------------------------
    interface Shape {
        double area(double size);
        double perimeter(double size);
    }

    enum ShapeType implements Shape, Describable {

        CIRCLE {
            @Override
            public double area(double radius) {
                return Math.PI * radius * radius;
            }
            @Override
            public double perimeter(double radius) {
                return 2 * Math.PI * radius;
            }
            @Override
            public String describe() {
                return "A circle — perfectly round, infinite symmetry";
            }
        },
        SQUARE {
            @Override
            public double area(double side) {
                return side * side;
            }
            @Override
            public double perimeter(double side) {
                return 4 * side;
            }
            @Override
            public String describe() {
                return "A square — all sides equal, all angles 90°";
            }
        },
        EQUILATERAL_TRIANGLE {
            @Override
            public double area(double side) {
                return (Math.sqrt(3) / 4) * side * side;
            }
            @Override
            public double perimeter(double side) {
                return 3 * side;
            }
            @Override
            public String describe() {
                return "An equilateral triangle — 3 equal sides, 60° angles";
            }
        };
    }


    // --------------------------------------------------------
    // Helper methods that accept INTERFACE types (not enum types!)
    // This is POLYMORPHISM in action.
    // --------------------------------------------------------

    // Accepts ANY Priceable object — could be an enum, a class, anything
    static void printReceipt(Priceable item) {
        double tax = 0.08;
        System.out.printf("  Base: %-10s | Tax(8%%): $%.2f | Total: $%.2f%n",
            item.formattedPrice(), item.basePrice() * tax, item.withTax(tax));
    }

    // Accepts ANY Displayable object
    static void printLabel(Displayable item) {
        System.out.println("  → " + item.display());
    }

    // Accepts ANY Greetable
    static void greetUser(Greetable lang, String name) {
        System.out.println("  " + lang.greet(name));
    }


    public static void main(String[] args) {

        // --------------------------------------------------------
        // Using MenuItem
        // --------------------------------------------------------
        System.out.println("=== Menu ===");
        for (MenuItem item : MenuItem.values()) {
            System.out.printf("  %-30s  %s%n", item.display(), item.formattedPrice());
        }

        System.out.println("\n=== Receipt with Tax ===");
        for (MenuItem item : MenuItem.values()) {
            System.out.print(item.name() + ": ");
            printReceipt(item);
        }

        // Polymorphism — enum used as interface type
        System.out.println("\n=== Polymorphic Usage ===");
        Priceable    priceItem   = MenuItem.PIZZA;       // enum as Priceable
        Displayable  displayItem = MenuItem.BURGER;      // enum as Displayable
        System.out.println("Pizza price: " + priceItem.formattedPrice());
        System.out.println("Burger label: " + displayItem.display());

        // Passing enum to methods that expect interfaces
        System.out.println("\nPassing to interface-typed methods:");
        for (MenuItem item : MenuItem.values()) {
            printLabel(item);   // enum passed as Displayable
        }

        System.out.println("\nMain courses only:");
        for (MenuItem item : MenuItem.values()) {
            if (item.isMainCourse()) {
                System.out.println("  " + item.display());
            }
        }


        // --------------------------------------------------------
        // Using Language
        // --------------------------------------------------------
        System.out.println("\n=== Languages ===");
        String personName = "Alice";

        for (Language lang : Language.values()) {
            System.out.println(lang.display() + ":");
            System.out.println("  Greet  : " + lang.greet(personName));
            System.out.println("  Bye    : " + lang.farewell(personName));
        }

        // Polymorphic: passing enum to method expecting Greetable
        System.out.println("\nGreeting via interface method:");
        for (Language lang : Language.values()) {
            greetUser(lang, "Bob"); // enum passed as Greetable
        }


        // --------------------------------------------------------
        // Using ShapeType
        // --------------------------------------------------------
        System.out.println("\n=== Shapes (size = 5.0) ===");
        double size = 5.0;

        for (ShapeType shape : ShapeType.values()) {
            System.out.println("\n" + shape.name() + ":");
            System.out.println("  Description : " + shape.describe());
            System.out.printf("  Area        : %.4f%n", shape.area(size));
            System.out.printf("  Perimeter   : %.4f%n", shape.perimeter(size));
        }

        // Using as interface reference
        Shape myShape = ShapeType.CIRCLE;
        System.out.printf("\nCircle with radius 10: area = %.2f%n", myShape.area(10));

        System.out.println("\nLesson 5 Complete!");
    }
}

// ============================================================
//  SUMMARY
// ============================================================
//  - Enums CAN implement interfaces (but cannot extend classes)
//  - Each enum constant must implement all abstract interface methods
//  - Default interface methods are inherited for free
//  - Enum instances can be assigned to interface-typed variables
//  - This enables powerful polymorphism — code that works with
//    any implementor of an interface, including enums
//
//  SYNTAX:
//    enum MyEnum implements Interface1, Interface2 {
//        CONSTANT_A {
//            @Override public void method() { ... }
//        };
//        // OR provide default in enum body if all constants share it
//    }
//
//  COMPILE & RUN:
//    javac Lesson5_Interfaces.java
//    java  Lesson5_Interfaces
// ============================================================
