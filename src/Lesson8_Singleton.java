public class Lesson8_Singleton {

    enum AppConfig {
        INSTANCE;

        private String  appName     = "MyApp";
        private String  version     = "2.1.0";
        private String  environment = "PRODUCTION";
        private int     maxRetries  = 3;
        private int     timeoutMs   = 5000;
        private boolean debugMode   = false;
        private String  dbUrl       = "jdbc:postgresql://localhost:5432/mydb";

        public String  getAppName()     { return appName;     }
        public String  getVersion()     { return version;     }
        public String  getEnvironment() { return environment; }
        public int     getMaxRetries()  { return maxRetries;  }
        public int     getTimeoutMs()   { return timeoutMs;   }
        public boolean isDebugMode()    { return debugMode;   }
        public String  getDbUrl()       { return dbUrl;       }

        public void setMaxRetries(int r)       { this.maxRetries  = r; }
        public void setTimeoutMs(int t)        { this.timeoutMs   = t; }
        public void setDebugMode(boolean d)    { this.debugMode   = d; }
        public void setDbUrl(String url)       { this.dbUrl       = url; }
        public void setEnvironment(String env) { this.environment = env; }

        public boolean isProduction() {
            return "PRODUCTION".equals(environment);
        }

        @Override
        public String toString() {
            return String.format(
                "AppConfig{app='%s', v='%s', env='%s', retries=%d, timeout=%d, debug=%b}",
                appName, version, environment, maxRetries, timeoutMs, debugMode
            );
        }
    }

    enum Logger {
        INSTANCE;

        public enum Level { DEBUG, INFO, WARN, ERROR }

        private Level  minLevel  = Level.INFO;
        private boolean showTime = false;
        private int     logCount = 0;

        public void setMinLevel(Level level)   { this.minLevel  = level;   }
        public void setShowTime(boolean show)  { this.showTime  = show;    }
        public int  getLogCount()              { return logCount;           }

        public void log(Level level, String message) {
            if (level.ordinal() >= minLevel.ordinal()) {
                logCount++;
                String prefix = showTime
                    ? "[" + java.time.LocalTime.now() + "] "
                    : "";
                System.out.printf("%s[%-5s] %s%n", prefix, level, message);
            }
        }

        public void debug(String msg) { log(Level.DEBUG, msg); }
        public void info (String msg) { log(Level.INFO,  msg); }
        public void warn (String msg) { log(Level.WARN,  msg); }
        public void error(String msg) { log(Level.ERROR, msg); }

        @Override
        public String toString() {
            return "Logger{minLevel=" + minLevel + ", totalLogs=" + logCount + "}";
        }
    }

    enum IdGenerator {
        INSTANCE;

        private int  orderIdCounter  = 1000;
        private int  userIdCounter   = 1;
        private int  productIdCounter = 100;

        public String nextOrderId()   { return "ORD-" + (orderIdCounter++);   }
        public String nextUserId()    { return "USR-" + String.format("%04d", userIdCounter++); }
        public String nextProductId() { return "PRD-" + (productIdCounter++); }

        public void reset() {
            orderIdCounter   = 1000;
            userIdCounter    = 1;
            productIdCounter = 100;
        }
    }

    static class DatabaseService {
        private final String name = "DatabaseService";

        void connect() {
            String url     = AppConfig.INSTANCE.getDbUrl();
            int    timeout = AppConfig.INSTANCE.getTimeoutMs();
            Logger.INSTANCE.info(name + " connecting to " + url + " (timeout=" + timeout + "ms)");
        }

        void query(String sql) {
            Logger.INSTANCE.debug(name + " executing: " + sql);
        }
    }

    static class AuthService {
        private final String name = "AuthService";

        boolean authenticate(String username) {
            int maxRetries = AppConfig.INSTANCE.getMaxRetries();
            Logger.INSTANCE.info(name + " authenticating '" + username
                + "' (max retries: " + maxRetries + ")");
            return username.length() > 3;
        }
    }

    static class OrderService {
        private final String name = "OrderService";

        String createOrder(String customer) {
            String orderId = IdGenerator.INSTANCE.nextOrderId();
            Logger.INSTANCE.info(name + " created order " + orderId + " for " + customer);
            return orderId;
        }
    }

    static class UserService {
        private final String name = "UserService";

        String registerUser(String username) {
            String userId = IdGenerator.INSTANCE.nextUserId();
            Logger.INSTANCE.info(name + " registered user " + userId + " (" + username + ")");
            return userId;
        }
    }


    public static void main(String[] args) {

        System.out.println("=== AppConfig Singleton ===");
        System.out.println(AppConfig.INSTANCE);

        AppConfig.INSTANCE.setDebugMode(true);
        AppConfig.INSTANCE.setMaxRetries(5);
        AppConfig.INSTANCE.setEnvironment("STAGING");
        System.out.println("After reconfigure: " + AppConfig.INSTANCE);

        AppConfig ref1 = AppConfig.INSTANCE;
        AppConfig ref2 = AppConfig.INSTANCE;
        AppConfig ref3 = AppConfig.INSTANCE;
        System.out.println("\nref1 == ref2 == ref3 ? "
            + (ref1 == ref2 && ref2 == ref3)); // always true

        System.out.println("isProduction? " + AppConfig.INSTANCE.isProduction());

        System.out.println("\n=== Logger Singleton ===");

        Logger log = Logger.INSTANCE;
        log.info("Application started");
        log.debug("This won't show — min level is INFO");
        log.warn("Low memory warning");
        log.error("Something went wrong!");

        System.out.println("\n--- Changing log level to DEBUG ---");
        log.setMinLevel(Logger.Level.DEBUG);
        log.debug("Now debug messages show!");
        log.info("Info still works too");

        System.out.println("\nTotal messages logged: " + log.getLogCount());


        System.out.println("\n=== ID Generator Singleton ===");

        IdGenerator ids = IdGenerator.INSTANCE;
        System.out.println("Order IDs  : " + ids.nextOrderId()
            + ", " + ids.nextOrderId()
            + ", " + ids.nextOrderId());
        System.out.println("User IDs   : " + ids.nextUserId()
            + ", " + ids.nextUserId());
        System.out.println("Product IDs: " + ids.nextProductId()
            + ", " + ids.nextProductId()
            + ", " + ids.nextProductId());


        System.out.println("\n=== Services Sharing Singletons ===");

        AppConfig.INSTANCE.setDebugMode(true);
        Logger.INSTANCE.setMinLevel(Logger.Level.DEBUG);

        DatabaseService db    = new DatabaseService();
        AuthService     auth  = new AuthService();
        OrderService    orders = new OrderService();
        UserService     users  = new UserService();

        Logger.INSTANCE.info("--- Application startup ---");
        db.connect();

        String uid1 = users.registerUser("alice");
        String uid2 = users.registerUser("bob");

        auth.authenticate("alice");
        auth.authenticate("bob");

        db.query("SELECT * FROM products WHERE active=true");

        String o1 = orders.createOrder("alice");
        String o2 = orders.createOrder("bob");
        String o3 = orders.createOrder("alice");

        Logger.INSTANCE.info("--- Session complete ---");
        System.out.println("\nFinal logger state: " + Logger.INSTANCE);


        System.out.println("\n=== Singleton Verification ===");
        System.out.println("AppConfig.INSTANCE identity: "
            + System.identityHashCode(AppConfig.INSTANCE));
        System.out.println("Logger.INSTANCE identity: "
            + System.identityHashCode(Logger.INSTANCE));
        System.out.println("IdGenerator.INSTANCE identity: "
            + System.identityHashCode(IdGenerator.INSTANCE));
        System.out.println("(These never change across the entire app lifetime)");

        System.out.println("\nLesson 8 Complete!");
    }
}