import java.util.EnumMap;
import java.util.EnumSet;
import java.util.ArrayList;
import java.util.List;

public class CoffeeShopSystem {

    enum Size {
        SMALL("S", 0.00), MEDIUM("M", 0.50), LARGE("L", 1.00);
        final String label;
        final double extra;
        Size(String label, double extra) { this.label = label; this.extra = extra; }
    }

    enum Drink {
        ESPRESSO  ("Espresso",    2.50),
        LATTE     ("Latte",       3.75),
        CAPPUCCINO("Cappuccino",  3.50),
        COLD_BREW ("Cold Brew",   4.00),
        MATCHA    ("Matcha Latte",4.25);

        final String name;
        final double base;

        Drink(String name, double base) { this.name = name; this.base = base; }

        public double price(Size s) { return base + s.extra; }
    }

    enum Extra {
        SHOT(0.80) {
            @Override public String describe() { return "Extra espresso shot"; }
        },
        OAT_MILK(0.60) {
            @Override public String describe() { return "Oat milk";            }
        },
        SYRUP(0.40) {
            @Override public String describe() { return "Vanilla syrup";       }
        },
        WHIP(0.50) {
            @Override public String describe() { return "Whipped cream";       }
        };

        final double cost;
        Extra(double cost) { this.cost = cost; }
        public abstract String describe();
    }

    interface Discountable {
        double apply(double price);
        String label();
        default double savings(double price) { return price - apply(price); }
    }

    enum Discount implements Discountable {
        NONE {
            @Override public double apply(double p) { return p;        }
            @Override public String label()         { return "None";   }
        },
        LOYALTY {
            @Override public double apply(double p) { return p * 0.90; }
            @Override public String label()         { return "Loyalty (10% off)"; }
        },
        STAFF {
            @Override public double apply(double p) { return p * 0.70; }
            @Override public String label()         { return "Staff (30% off)";   }
        };
    }

    enum OrderStatus {
        PENDING {
            @Override public OrderStatus next()  { return PREPARING;            }
            @Override public String      label() { return "Waiting in queue";   }
        },
        PREPARING {
            @Override public OrderStatus next()  { return READY;                }
            @Override public String      label() { return "Barista is making it";}
        },
        READY {
            @Override public OrderStatus next()  { return COLLECTED;            }
            @Override public String      label() { return "Ready for pickup!";  }
        },
        COLLECTED {
            @Override public OrderStatus next()  { return COLLECTED;            }
            @Override public String      label() { return "Completed";          }
        };

        public abstract OrderStatus next();
        public abstract String      label();
    }

    enum CafeConfig {
        INSTANCE;

        private final double taxRate  = 0.08;
        private final String cafeName = "Claude's Coffee";
        private int ordersToday = 0;

        public double getTaxRate()     { return taxRate;     }
        public String getCafeName()    { return cafeName;    }
        public int    getOrdersToday() { return ordersToday; }
        public void   recordOrder()    { ordersToday++;      }
    }

    enum DayOfWeek { MON, TUE, WED, THU, FRI, SAT, SUN }

    static class Order {
        final String         id;
        final Drink          drink;
        final Size           size;
        final EnumSet<Extra> extras;
        final Discount       discount;
        OrderStatus          status = OrderStatus.PENDING;

        Order(String id, Drink drink, Size size,
              EnumSet<Extra> extras, Discount discount) {
            this.id       = id;
            this.drink    = drink;
            this.size     = size;
            this.extras   = extras;
            this.discount = discount;
            CafeConfig.INSTANCE.recordOrder();
        }

        double subtotal() {
            double total = drink.price(size);
            for (Extra e : extras) total += e.cost;
            return total;
        }

        double tax()   { return subtotal() * CafeConfig.INSTANCE.getTaxRate(); }
        double total() { return discount.apply(subtotal() + tax()); }

        void advance() { status = status.next(); }

        void printReceipt() {
            System.out.println("\n  === Order #" + id + " ===");
            System.out.printf("  %-24s $%.2f%n", drink.name + " (" + size.label + ")", drink.price(size));
            for (Extra e : extras) {
                System.out.printf("  + %-22s $%.2f%n", e.describe(), e.cost);
            }
            System.out.println("  " + "-".repeat(32));
            System.out.printf("  %-24s $%.2f%n", "Subtotal",  subtotal());
            System.out.printf("  %-24s $%.2f%n", "Tax (8%)",  tax());
            if (discount != Discount.NONE) {
                System.out.printf("  %-24s-$%.2f%n",
                    "Discount (" + discount.label() + ")",
                    discount.savings(subtotal() + tax()));
            }
            System.out.printf("  %-24s $%.2f%n", "TOTAL", total());
            System.out.println("  Status: " + status + " - " + status.label());
        }
    }

    public static void main(String[] args) {

        System.out.println("*".repeat(45));
        System.out.println("  Welcome to " + CafeConfig.INSTANCE.getCafeName());
        System.out.println("*".repeat(45));

        // Build orders
        Order o1 = new Order("001",
            Drink.LATTE, Size.LARGE,
            EnumSet.of(Extra.OAT_MILK, Extra.SYRUP),
            Discount.LOYALTY);

        Order o2 = new Order("002",
            Drink.ESPRESSO, Size.SMALL,
            EnumSet.noneOf(Extra.class),
            Discount.NONE);

        Order o3 = new Order("003",
            Drink.COLD_BREW, Size.MEDIUM,
            EnumSet.of(Extra.SHOT, Extra.WHIP),
            Discount.STAFF);

        Order o4 = new Order("004",
            Drink.MATCHA, Size.LARGE,
            EnumSet.of(Extra.OAT_MILK),
            Discount.NONE);

        List<Order> orders = List.of(o1, o2, o3, o4);
        for (Order order : orders) order.printReceipt();

        System.out.println("\n=== Order #001 Progress (State Machine) ===");
        Order tracked = o1;
        for (int i = 0; i < 4; i++) {
            System.out.println("  " + tracked.status + " - " + tracked.status.label());
            tracked.advance();
        }

        EnumMap<DayOfWeek, Double> weeklySales = new EnumMap<>(DayOfWeek.class);
        weeklySales.put(DayOfWeek.MON, 342.50);
        weeklySales.put(DayOfWeek.TUE, 287.00);
        weeklySales.put(DayOfWeek.WED, 319.75);
        weeklySales.put(DayOfWeek.THU, 298.50);
        weeklySales.put(DayOfWeek.FRI, 415.25);
        weeklySales.put(DayOfWeek.SAT, 512.75);
        weeklySales.put(DayOfWeek.SUN, 489.00);

        System.out.println("\n=== Weekly Sales Report (EnumMap) ===");
        double weekTotal = 0;
        for (var entry : weeklySales.entrySet()) {
            System.out.printf("  %-4s: $%6.2f%n", entry.getKey(), entry.getValue());
            weekTotal += entry.getValue();
        }
        System.out.printf("  %-4s: $%6.2f%n", "TOTAL", weekTotal);

        EnumSet<Extra> popularToday   = EnumSet.of(Extra.OAT_MILK, Extra.SYRUP, Extra.SHOT);
        EnumSet<Extra> unpopularToday = EnumSet.complementOf(popularToday);
        System.out.println("\n=== Today's Extras (EnumSet) ===");
        System.out.println("  Popular   : " + popularToday);
        System.out.println("  Unpopular : " + unpopularToday);

        System.out.println("\n=== Daily Summary (Singleton) ===");
        System.out.println("  Cafe    : " + CafeConfig.INSTANCE.getCafeName());
        System.out.println("  Orders  : " + CafeConfig.INSTANCE.getOrdersToday());
        System.out.printf("  Revenue : $%.2f%n",
            orders.stream().mapToDouble(Order::total).sum());

        System.out.println("\n  Thank you and have a great day!");
    }
}
