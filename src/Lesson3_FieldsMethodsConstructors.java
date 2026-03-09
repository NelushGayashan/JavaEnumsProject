// ============================================================
//  LESSON 3 — FIELDS, METHODS & CONSTRUCTORS
// ============================================================
//
//  KEY INSIGHT: An enum is a FULL CLASS in Java.
//
//  Each enum constant is actually an INSTANCE of that class.
//  That means every constant can carry its own:
//    - Fields (data / state)
//    - Methods (behavior)
//    - A constructor (to initialize the fields)
//
//  This is a huge step up from Lesson 1 where constants
//  were just names. Now they carry MEANINGFUL DATA.
//
//  RULES:
//  ------
//  1. The constructor is ALWAYS private (implicitly or explicitly).
//     You cannot create enum instances yourself — Java does it.
//  2. Fields should usually be final (constants should be immutable).
//  3. You can have as many fields, methods, and constructors as needed.
//  4. You can override toString() for nicer output.
//
// ============================================================

public class Lesson3_FieldsMethodsConstructors {

    // --------------------------------------------------------
    // EXAMPLE 1: HTTP Status Codes
    // Each status has a numeric code AND a text reason.
    // --------------------------------------------------------
    enum HttpStatus {
        // Syntax: CONSTANT_NAME(arg1, arg2, ...)
        // These arguments go to the constructor below.
        OK(200, "OK"),
        CREATED(201, "Created"),
        NO_CONTENT(204, "No Content"),
        BAD_REQUEST(400, "Bad Request"),
        UNAUTHORIZED(401, "Unauthorized"),
        FORBIDDEN(403, "Forbidden"),
        NOT_FOUND(404, "Not Found"),
        INTERNAL_SERVER_ERROR(500, "Internal Server Error"),
        SERVICE_UNAVAILABLE(503, "Service Unavailable");

        // Fields — final because HTTP codes never change
        private final int    code;
        private final String reason;

        // Constructor — called once per constant when class loads
        // Always private: you can write "private" or leave it out
        HttpStatus(int code, String reason) {
            this.code   = code;
            this.reason = reason;
        }

        // Getters — standard access to fields
        public int    getCode()   { return code;   }
        public String getReason() { return reason; }

        // Method: is this an error status?
        public boolean isError() {
            return code >= 400;
        }

        // Method: is this a success status?
        public boolean isSuccess() {
            return code >= 200 && code < 300;
        }

        // Static method: look up by code number
        // Useful when you receive a raw integer (e.g., from an API)
        public static HttpStatus fromCode(int code) {
            for (HttpStatus s : values()) {
                if (s.code == code) return s;
            }
            throw new IllegalArgumentException("Unknown HTTP status code: " + code);
        }

        // Override toString for cleaner output
        @Override
        public String toString() {
            return code + " " + reason;
        }
    }


    // --------------------------------------------------------
    // EXAMPLE 2: Coins
    // Each coin has a fixed value in cents.
    // --------------------------------------------------------
    enum Coin {
        PENNY(1),
        NICKEL(5),
        DIME(10),
        QUARTER(25),
        HALF_DOLLAR(50),
        DOLLAR(100);

        private final int cents;

        Coin(int cents) {
            this.cents = cents;
        }

        public int getCents() { return cents; }

        // How much is N of these coins worth (in dollars)?
        public double valueOf(int count) {
            return (cents * count) / 100.0;
        }

        // Dollar value of one coin
        public double toDollars() {
            return cents / 100.0;
        }
    }


    // --------------------------------------------------------
    // EXAMPLE 3: Planet (with multiple fields and methods)
    // Each planet has mass and radius — we can compute gravity!
    // --------------------------------------------------------
    enum Planet {
        MERCURY(3.303e+23, 2.4397e6),
        VENUS  (4.869e+24, 6.0518e6),
        EARTH  (5.976e+24, 6.37814e6),
        MARS   (6.421e+23, 3.3972e6),
        JUPITER(1.9e+27,   7.1492e7),
        SATURN (5.688e+26, 6.0268e7);

        private static final double G = 6.67300E-11; // gravitational constant

        private final double mass;    // in kilograms
        private final double radius;  // in meters

        Planet(double mass, double radius) {
            this.mass   = mass;
            this.radius = radius;
        }

        public double getMass()   { return mass;   }
        public double getRadius() { return radius; }

        // Surface gravity of this planet
        public double surfaceGravity() {
            return G * mass / (radius * radius);
        }

        // Weight of an object on this planet
        public double surfaceWeight(double otherMass) {
            return otherMass * surfaceGravity();
        }
    }


    // --------------------------------------------------------
    // EXAMPLE 4: Card Suit (simple single-field enum)
    // --------------------------------------------------------
    enum Suit {
        HEARTS("♥", "Red"),
        DIAMONDS("♦", "Red"),
        CLUBS("♣", "Black"),
        SPADES("♠", "Black");

        private final String symbol;
        private final String color;

        Suit(String symbol, String color) {
            this.symbol = symbol;
            this.color  = color;
        }

        public String getSymbol() { return symbol; }
        public String getColor()  { return color;  }
        public boolean isRed()    { return color.equals("Red"); }

        @Override
        public String toString() {
            return symbol + " " + name();
        }
    }


    public static void main(String[] args) {

        // --------------------------------------------------------
        // Using HttpStatus
        // --------------------------------------------------------
        System.out.println("=== HTTP Status Codes ===");

        HttpStatus status = HttpStatus.NOT_FOUND;
        System.out.println("Status  : " + status);             // uses toString()
        System.out.println("Code    : " + status.getCode());
        System.out.println("Reason  : " + status.getReason());
        System.out.println("Error?  : " + status.isError());
        System.out.println("Success?: " + status.isSuccess());

        System.out.println("\nAll statuses:");
        for (HttpStatus s : HttpStatus.values()) {
            System.out.printf("  %-35s  success=%-5b  error=%b%n",
                s, s.isSuccess(), s.isError());
        }

        // Static lookup
        System.out.println("\nLookup by code:");
        int[] codesToCheck = {200, 201, 404, 500};
        for (int code : codesToCheck) {
            HttpStatus found = HttpStatus.fromCode(code);
            System.out.println("  " + code + " → " + found);
        }

        // Safe lookup with try/catch
        try {
            HttpStatus.fromCode(999);
        } catch (IllegalArgumentException e) {
            System.out.println("  Caught: " + e.getMessage());
        }


        // --------------------------------------------------------
        // Using Coin
        // --------------------------------------------------------
        System.out.println("\n=== Coins ===");

        for (Coin coin : Coin.values()) {
            System.out.printf("  %-12s = %3d cents = $%.2f  |  7 of them = $%.2f%n",
                coin, coin.getCents(), coin.toDollars(), coin.valueOf(7));
        }

        // Make change for 87 cents
        System.out.println("\nMaking change for 87 cents:");
        int remaining = 87;
        Coin[] coinOrder = {Coin.HALF_DOLLAR, Coin.QUARTER, Coin.DIME, Coin.NICKEL, Coin.PENNY};
        for (Coin coin : coinOrder) {
            int count = remaining / coin.getCents();
            remaining %= coin.getCents();
            if (count > 0) {
                System.out.println("  " + coin + " x " + count);
            }
        }


        // --------------------------------------------------------
        // Using Planet
        // --------------------------------------------------------
        System.out.println("\n=== Planets (Weight on each planet) ===");

        double earthWeight = 75.0; // kg
        double earthMass   = earthWeight / Planet.EARTH.surfaceGravity();

        System.out.println("A " + earthWeight + "kg person weighs:");
        for (Planet planet : Planet.values()) {
            System.out.printf("  %-8s: %6.2f kg%n",
                planet, planet.surfaceWeight(earthMass));
        }


        // --------------------------------------------------------
        // Using Suit
        // --------------------------------------------------------
        System.out.println("\n=== Card Suits ===");

        for (Suit suit : Suit.values()) {
            System.out.printf("  %s  color=%-6s  red=%b%n",
                suit, suit.getColor(), suit.isRed());
        }

        // Filter red suits only
        System.out.println("\nRed suits only:");
        for (Suit suit : Suit.values()) {
            if (suit.isRed()) {
                System.out.println("  " + suit);
            }
        }

        System.out.println("\nLesson 3 Complete!");
    }
}

// ============================================================
//  SUMMARY
// ============================================================
//  - Enums can have fields, methods, and constructors
//  - Constructor is always private (Java enforces this)
//  - Fields should be final for immutability
//  - toString() can be overridden for nicer output
//  - Static methods (like fromCode()) allow reverse lookup
//  - Each constant is a fully initialized object at class load time
//
//  COMPILE & RUN:
//    javac Lesson3_FieldsMethodsConstructors.java
//    java  Lesson3_FieldsMethodsConstructors
// ============================================================
