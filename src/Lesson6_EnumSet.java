// ============================================================
//  LESSON 6 — EnumSet
// ============================================================
//
//  WHAT IS EnumSet?
//  ----------------
//  EnumSet is a specialized Set implementation designed
//  specifically for use with enum types.
//
//  WHY NOT JUST USE HashSet?
//  -------------------------
//  You COULD use HashSet<Permission> — it works.
//  But EnumSet is:
//    ✅ Much FASTER       — uses a bit vector internally (just bit operations!)
//    ✅ Less MEMORY       — one bit per enum constant
//    ✅ Always ORDERED    — iteration is always in enum declaration order
//    ✅ More READABLE     — makes intent clear ("these are flags/options")
//    ✅ Null-safe         — throws NullPointerException on null, not silently
//
//  HOW IT WORKS INTERNALLY:
//  ------------------------
//  Imagine Permission has 5 constants (READ, WRITE, EXECUTE, DELETE, ADMIN).
//  Internally, EnumSet stores them as a single long (64-bit integer):
//    READ    = bit 0 = 00001
//    WRITE   = bit 1 = 00010
//    EXECUTE = bit 2 = 00100
//    DELETE  = bit 3 = 01000
//    ADMIN   = bit 4 = 10000
//
//  EnumSet{READ, WRITE} = 00011 (just a number!)
//  Adding EXECUTE       = 00111 (just a bit flip!)
//  This is WHY it's so fast — operations are single CPU instructions.
//
//  WHEN TO USE EnumSet:
//  --------------------
//  Whenever you need a SET and the elements are enum constants.
//  Classic use case: permission flags, feature toggles, day selection.
//
// ============================================================

import java.util.EnumSet;

public class Lesson6_EnumSet {

    enum Permission {
        READ, WRITE, EXECUTE, DELETE, ADMIN
    }

    enum Day {
        MON, TUE, WED, THU, FRI, SAT, SUN
    }

    enum Feature {
        DARK_MODE, NOTIFICATIONS, AUTO_SAVE, SPELL_CHECK, WORD_COUNT, CLOUD_SYNC
    }


    public static void main(String[] args) {

        // --------------------------------------------------------
        // PART 1: Creating EnumSets — the different factory methods
        // --------------------------------------------------------
        System.out.println("=== PART 1: Creating EnumSets ===");

        // allOf — every constant in the enum
        EnumSet<Permission> allPerms = EnumSet.allOf(Permission.class);
        System.out.println("allOf    : " + allPerms);
        // [READ, WRITE, EXECUTE, DELETE, ADMIN]

        // noneOf — empty set, but typed for this enum
        EnumSet<Permission> noPerms = EnumSet.noneOf(Permission.class);
        System.out.println("noneOf   : " + noPerms);
        // []

        // of — specific constants
        EnumSet<Permission> readOnly = EnumSet.of(Permission.READ);
        EnumSet<Permission> basic    = EnumSet.of(Permission.READ, Permission.WRITE);
        EnumSet<Permission> standard = EnumSet.of(Permission.READ, Permission.WRITE, Permission.EXECUTE);
        System.out.println("readOnly : " + readOnly);
        System.out.println("basic    : " + basic);
        System.out.println("standard : " + standard);

        // range — all constants between two (inclusive), by declaration order
        EnumSet<Permission> rangePerms = EnumSet.range(Permission.READ, Permission.EXECUTE);
        System.out.println("range    : " + rangePerms);
        // READ, WRITE, EXECUTE (the first three in declaration order)

        // copyOf — make a copy of an existing EnumSet
        EnumSet<Permission> copy = EnumSet.copyOf(standard);
        System.out.println("copyOf   : " + copy);


        // --------------------------------------------------------
        // PART 2: Adding and Removing Elements
        // --------------------------------------------------------
        System.out.println("\n=== PART 2: Adding and Removing ===");

        EnumSet<Permission> userPerms = EnumSet.of(Permission.READ);
        System.out.println("Start  : " + userPerms);

        userPerms.add(Permission.WRITE);
        System.out.println("+ WRITE: " + userPerms);

        userPerms.add(Permission.EXECUTE);
        System.out.println("+ EXEC : " + userPerms);

        userPerms.remove(Permission.EXECUTE);
        System.out.println("- EXEC : " + userPerms);

        // addAll / removeAll
        EnumSet<Permission> extras = EnumSet.of(Permission.DELETE, Permission.ADMIN);
        userPerms.addAll(extras);
        System.out.println("addAll : " + userPerms);

        userPerms.removeAll(extras);
        System.out.println("remAll : " + userPerms);


        // --------------------------------------------------------
        // PART 3: Checking Contents
        // --------------------------------------------------------
        System.out.println("\n=== PART 3: Checking Contents ===");

        EnumSet<Permission> perms = EnumSet.of(Permission.READ, Permission.WRITE);

        System.out.println("Perms          : " + perms);
        System.out.println("contains READ  : " + perms.contains(Permission.READ));    // true
        System.out.println("contains DELETE: " + perms.contains(Permission.DELETE));  // false
        System.out.println("isEmpty        : " + perms.isEmpty());                    // false
        System.out.println("size           : " + perms.size());                       // 2

        // containsAll
        EnumSet<Permission> required = EnumSet.of(Permission.READ, Permission.WRITE);
        System.out.println("containsAll    : " + perms.containsAll(required)); // true


        // --------------------------------------------------------
        // PART 4: complementOf — the opposite set
        // --------------------------------------------------------
        System.out.println("\n=== PART 4: complementOf ===");

        EnumSet<Permission> granted = EnumSet.of(Permission.READ, Permission.WRITE);
        EnumSet<Permission> denied  = EnumSet.complementOf(granted);
        System.out.println("Granted : " + granted);
        System.out.println("Denied  : " + denied);
        // Denied = EXECUTE, DELETE, ADMIN (everything NOT in granted)


        // --------------------------------------------------------
        // PART 5: Set Operations — union, intersection, difference
        // --------------------------------------------------------
        System.out.println("\n=== PART 5: Set Operations ===");

        EnumSet<Permission> setA = EnumSet.of(Permission.READ, Permission.WRITE, Permission.EXECUTE);
        EnumSet<Permission> setB = EnumSet.of(Permission.WRITE, Permission.EXECUTE, Permission.DELETE);
        System.out.println("Set A        : " + setA);
        System.out.println("Set B        : " + setB);

        // UNION (A OR B) — everything in either set
        EnumSet<Permission> union = EnumSet.copyOf(setA);
        union.addAll(setB);
        System.out.println("Union (A|B)  : " + union);

        // INTERSECTION (A AND B) — only what's in BOTH
        EnumSet<Permission> intersection = EnumSet.copyOf(setA);
        intersection.retainAll(setB);
        System.out.println("Intersect(A&B): " + intersection);

        // DIFFERENCE (A NOT B) — in A but NOT in B
        EnumSet<Permission> difference = EnumSet.copyOf(setA);
        difference.removeAll(setB);
        System.out.println("Diff (A-B)   : " + difference);


        // --------------------------------------------------------
        // PART 6: Iterating
        // --------------------------------------------------------
        System.out.println("\n=== PART 6: Iterating ===");

        // Always iterates in enum DECLARATION ORDER (not insertion order)
        EnumSet<Permission> mixed = EnumSet.of(
            Permission.ADMIN, Permission.READ, Permission.DELETE); // added out of order

        System.out.println("Added: ADMIN, READ, DELETE");
        System.out.print("Iteration order: ");
        for (Permission p : mixed) {
            System.out.print(p + " ");  // READ DELETE ADMIN (declaration order!)
        }
        System.out.println();


        // --------------------------------------------------------
        // PART 7: Days example — weekend / weekday
        // --------------------------------------------------------
        System.out.println("\n=== PART 7: Days ===");

        EnumSet<Day> weekend = EnumSet.of(Day.SAT, Day.SUN);
        EnumSet<Day> weekday = EnumSet.complementOf(weekend);
        EnumSet<Day> allDays = EnumSet.allOf(Day.class);

        System.out.println("All days : " + allDays);
        System.out.println("Weekend  : " + weekend);
        System.out.println("Weekday  : " + weekday);

        Day today = Day.WED;
        System.out.println("\nToday is " + today);
        System.out.println("Is weekend? " + weekend.contains(today));
        System.out.println("Is weekday? " + weekday.contains(today));


        // --------------------------------------------------------
        // PART 8: Feature Flags — real-world use case
        // --------------------------------------------------------
        System.out.println("\n=== PART 8: Feature Flags ===");

        // Different user types get different features
        EnumSet<Feature> freeFeatures = EnumSet.of(
            Feature.SPELL_CHECK, Feature.WORD_COUNT);

        EnumSet<Feature> premiumFeatures = EnumSet.of(
            Feature.SPELL_CHECK, Feature.WORD_COUNT,
            Feature.DARK_MODE, Feature.NOTIFICATIONS, Feature.AUTO_SAVE);

        EnumSet<Feature> enterpriseFeatures = EnumSet.allOf(Feature.class);

        System.out.println("Free      : " + freeFeatures);
        System.out.println("Premium   : " + premiumFeatures);
        System.out.println("Enterprise: " + enterpriseFeatures);

        // Check if a user can use a feature
        Feature requested = Feature.CLOUD_SYNC;
        String userTier   = "PREMIUM";

        EnumSet<Feature> userFeatures = switch (userTier) {
            case "FREE"       -> freeFeatures;
            case "PREMIUM"    -> premiumFeatures;
            case "ENTERPRISE" -> enterpriseFeatures;
            default           -> EnumSet.noneOf(Feature.class);
        };

        System.out.println("\nUser tier: " + userTier);
        System.out.println("Requested: " + requested);
        if (userFeatures.contains(requested)) {
            System.out.println("✅ Feature available");
        } else {
            System.out.println("❌ Upgrade required");
        }

        // What features does a premium user NOT have?
        EnumSet<Feature> locked = EnumSet.complementOf(premiumFeatures);
        System.out.println("\nLocked features for Premium: " + locked);


        // --------------------------------------------------------
        // PART 9: Role-Based Access Control (RBAC) — classic use
        // --------------------------------------------------------
        System.out.println("\n=== PART 9: Role-Based Access Control ===");

        EnumSet<Permission> adminRole    = EnumSet.allOf(Permission.class);
        EnumSet<Permission> editorRole   = EnumSet.of(Permission.READ, Permission.WRITE);
        EnumSet<Permission> viewerRole   = EnumSet.of(Permission.READ);

        System.out.printf("%-10s : %s%n", "Admin",  adminRole);
        System.out.printf("%-10s : %s%n", "Editor", editorRole);
        System.out.printf("%-10s : %s%n", "Viewer", viewerRole);

        System.out.println();
        Permission[] actions = Permission.values();
        String[] roles       = {"Admin", "Editor", "Viewer"};
        EnumSet[] roleSets   = {adminRole, editorRole, viewerRole};

        for (int r = 0; r < roles.length; r++) {
            for (Permission action : actions) {
                String result = ((EnumSet<Permission>) roleSets[r]).contains(action)
                    ? "✅" : "❌";
                System.out.printf("  %-8s %-8s %s%n", roles[r], action, result);
            }
            System.out.println();
        }

        System.out.println("Lesson 6 Complete!");
    }
}

// ============================================================
//  SUMMARY
// ============================================================
//  EnumSet.allOf(E.class)     → all constants
//  EnumSet.noneOf(E.class)    → empty set
//  EnumSet.of(A, B, C)        → specific constants
//  EnumSet.range(A, C)        → A through C inclusive
//  EnumSet.copyOf(set)        → copy of another set
//  EnumSet.complementOf(set)  → everything NOT in the set
//
//  set.add(E)                 → add one constant
//  set.remove(E)              → remove one constant
//  set.addAll(other)          → union in-place
//  set.retainAll(other)       → intersection in-place
//  set.removeAll(other)       → difference in-place
//  set.contains(E)            → membership check
//
//  ALWAYS prefer EnumSet over HashSet when elements are enums.
//
//  COMPILE & RUN:
//    javac Lesson6_EnumSet.java
//    java  Lesson6_EnumSet
// ============================================================
