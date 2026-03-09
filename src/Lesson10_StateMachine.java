// ============================================================
//  LESSON 10 — STATE MACHINE PATTERN
// ============================================================
//
//  WHAT IS A STATE MACHINE?
//  -------------------------
//  A State Machine (or Finite State Machine / FSM) models an
//  object that:
//    1. Can be in exactly ONE state at a time
//    2. Transitions from one state to another in response to events
//    3. May perform actions when entering/exiting/transitioning states
//
//  Real-world examples:
//    - Traffic light: RED → GREEN → YELLOW → RED
//    - Order lifecycle: PLACED → CONFIRMED → SHIPPED → DELIVERED
//    - TCP connection: CLOSED → LISTEN → SYN_RECEIVED → ESTABLISHED
//    - Vending machine: IDLE → HAS_COIN → ITEM_SELECTED → DISPENSING
//
//  WHY ENUM FOR STATE MACHINES?
//  ----------------------------
//  ✅ The set of states is FIXED and KNOWN — perfect for enum
//  ✅ Each state can define its OWN transitions and behavior
//  ✅ Invalid states are impossible (type safety)
//  ✅ Switch expressions on current state are exhaustive
//  ✅ Self-contained: state knows what to do and where to go next
//
//  THE KEY INSIGHT:
//  ----------------
//  Instead of a state being just a label (like a String "RED"),
//  the state KNOWS:
//    - What actions to take while in this state
//    - Where to go when an event occurs
//    - What transitions are valid
//
// ============================================================

public class Lesson10_StateMachine {

    // --------------------------------------------------------
    // EXAMPLE 1: Traffic Light — Simple Linear State Machine
    // Each state defines where to go next and what to do.
    // --------------------------------------------------------
    enum TrafficLight {

        RED {
            @Override public TrafficLight next() { return GREEN;  }
            @Override public String action()     { return "STOP — Do not cross the road."; }
            @Override public int    duration()   { return 30; } // seconds
            @Override public String emoji()      { return "🔴"; }
        },
        GREEN {
            @Override public TrafficLight next() { return YELLOW; }
            @Override public String action()     { return "GO — Safe to proceed."; }
            @Override public int    duration()   { return 25; }
            @Override public String emoji()      { return "🟢"; }
        },
        YELLOW {
            @Override public TrafficLight next() { return RED; }
            @Override public String action()     { return "CAUTION — Prepare to stop."; }
            @Override public int    duration()   { return 5; }
            @Override public String emoji()      { return "🟡"; }
        };

        public abstract TrafficLight next();
        public abstract String       action();
        public abstract int          duration();
        public abstract String       emoji();
    }


    // --------------------------------------------------------
    // EXAMPLE 2: Order Lifecycle — State with Valid Transitions
    // Not all states can transition to all others.
    // --------------------------------------------------------
    enum OrderState {

        PLACED {
            @Override
            public OrderState confirm()  { return CONFIRMED; }
            @Override
            public OrderState cancel()   { return CANCELLED; }
            @Override
            public OrderState ship()     { invalid("ship",     this); return this; }
            @Override
            public OrderState deliver()  { invalid("deliver",  this); return this; }
            @Override
            public boolean    canCancel(){ return true;  }
            @Override
            public String     describe() { return "Order placed — awaiting confirmation."; }
        },
        CONFIRMED {
            @Override
            public OrderState confirm()  { invalid("confirm",  this); return this; }
            @Override
            public OrderState cancel()   { return CANCELLED;  }
            @Override
            public OrderState ship()     { return SHIPPED;    }
            @Override
            public OrderState deliver()  { invalid("deliver",  this); return this; }
            @Override
            public boolean    canCancel(){ return true;  }
            @Override
            public String     describe() { return "Order confirmed — being prepared for shipment."; }
        },
        SHIPPED {
            @Override
            public OrderState confirm()  { invalid("confirm",  this); return this; }
            @Override
            public OrderState cancel()   { invalid("cancel",   this); return this; }
            @Override
            public OrderState ship()     { invalid("ship",     this); return this; }
            @Override
            public OrderState deliver()  { return DELIVERED;  }
            @Override
            public boolean    canCancel(){ return false; }
            @Override
            public String     describe() { return "Order shipped — on its way to you."; }
        },
        DELIVERED {
            @Override
            public OrderState confirm()  { invalid("confirm",  this); return this; }
            @Override
            public OrderState cancel()   { invalid("cancel",   this); return this; }
            @Override
            public OrderState ship()     { invalid("ship",     this); return this; }
            @Override
            public OrderState deliver()  { return this; } // already delivered
            @Override
            public boolean    canCancel(){ return false; }
            @Override
            public String     describe() { return "Order delivered — enjoy your purchase!"; }
        },
        CANCELLED {
            @Override
            public OrderState confirm()  { invalid("confirm",  this); return this; }
            @Override
            public OrderState cancel()   { return this; } // already cancelled
            @Override
            public OrderState ship()     { invalid("ship",     this); return this; }
            @Override
            public OrderState deliver()  { invalid("deliver",  this); return this; }
            @Override
            public boolean    canCancel(){ return false; }
            @Override
            public String     describe() { return "Order cancelled."; }
        };

        // Each state defines all possible events
        public abstract OrderState confirm();
        public abstract OrderState cancel();
        public abstract OrderState ship();
        public abstract OrderState deliver();
        public abstract boolean    canCancel();
        public abstract String     describe();

        // Helper: log invalid transition attempt
        protected void invalid(String action, OrderState state) {
            System.out.println("  ⚠️  Cannot '" + action + "' when order is " + state);
        }
    }


    // --------------------------------------------------------
    // EXAMPLE 3: Vending Machine — Event-Driven State Machine
    // The machine reacts to different user events.
    // --------------------------------------------------------
    enum VendingState {

        IDLE {
            @Override
            public VendingState insertCoin() {
                System.out.println("  💰 Coin inserted. Please select an item.");
                return HAS_COIN;
            }
            @Override
            public VendingState selectItem() {
                System.out.println("  ❌ Please insert a coin first.");
                return IDLE;
            }
            @Override
            public VendingState pressDispense() {
                System.out.println("  ❌ No coin inserted. Nothing to dispense.");
                return IDLE;
            }
            @Override
            public VendingState refund() {
                System.out.println("  ❌ No coin to refund.");
                return IDLE;
            }
        },
        HAS_COIN {
            @Override
            public VendingState insertCoin() {
                System.out.println("  ⚠️  Coin already inserted.");
                return HAS_COIN;
            }
            @Override
            public VendingState selectItem() {
                System.out.println("  ✅ Item selected. Press dispense to get it.");
                return ITEM_SELECTED;
            }
            @Override
            public VendingState pressDispense() {
                System.out.println("  ❌ Please select an item first.");
                return HAS_COIN;
            }
            @Override
            public VendingState refund() {
                System.out.println("  💵 Coin refunded.");
                return IDLE;
            }
        },
        ITEM_SELECTED {
            @Override
            public VendingState insertCoin() {
                System.out.println("  ⚠️  Item already selected.");
                return ITEM_SELECTED;
            }
            @Override
            public VendingState selectItem() {
                System.out.println("  ⚠️  Item already selected.");
                return ITEM_SELECTED;
            }
            @Override
            public VendingState pressDispense() {
                System.out.println("  🎉 Dispensing item... Enjoy!");
                return IDLE;
            }
            @Override
            public VendingState refund() {
                System.out.println("  💵 Item deselected. Coin refunded.");
                return IDLE;
            }
        };

        public abstract VendingState insertCoin();
        public abstract VendingState selectItem();
        public abstract VendingState pressDispense();
        public abstract VendingState refund();
    }


    // --------------------------------------------------------
    // EXAMPLE 4: Document Workflow
    // A document goes through an editorial review pipeline.
    // --------------------------------------------------------
    enum DocumentState {

        DRAFT {
            @Override
            public DocumentState submit()  { return REVIEW;     }
            @Override
            public DocumentState approve() { cantDo("approve"); return this; }
            @Override
            public DocumentState reject()  { cantDo("reject");  return this; }
            @Override
            public DocumentState publish() { cantDo("publish"); return this; }
            @Override
            public String status()         { return "✏️  Being written"; }
        },
        REVIEW {
            @Override
            public DocumentState submit()  { cantDo("submit");  return this; }
            @Override
            public DocumentState approve() { return APPROVED;   }
            @Override
            public DocumentState reject()  { return DRAFT;      } // send back
            @Override
            public DocumentState publish() { cantDo("publish"); return this; }
            @Override
            public String status()         { return "🔍 Under review"; }
        },
        APPROVED {
            @Override
            public DocumentState submit()  { cantDo("submit");  return this; }
            @Override
            public DocumentState approve() { return this;       } // already approved
            @Override
            public DocumentState reject()  { return DRAFT;      } // reject after approval
            @Override
            public DocumentState publish() { return PUBLISHED;  }
            @Override
            public String status()         { return "✅ Approved, ready to publish"; }
        },
        PUBLISHED {
            @Override
            public DocumentState submit()  { cantDo("submit");  return this; }
            @Override
            public DocumentState approve() { cantDo("approve"); return this; }
            @Override
            public DocumentState reject()  { cantDo("reject");  return this; }
            @Override
            public DocumentState publish() { return this;       } // already published
            @Override
            public String status()         { return "🌍 Live and published"; }
        };

        public abstract DocumentState submit();
        public abstract DocumentState approve();
        public abstract DocumentState reject();
        public abstract DocumentState publish();
        public abstract String        status();

        protected void cantDo(String action) {
            System.out.println("  ⚠️  Cannot '" + action + "' a " + name() + " document.");
        }
    }


    // --------------------------------------------------------
    // Helper to print current state nicely
    // --------------------------------------------------------
    static void printState(String machine, Object state) {
        System.out.println("  [" + machine + "] State: " + state);
    }


    public static void main(String[] args) {

        // --------------------------------------------------------
        // Traffic Light
        // --------------------------------------------------------
        System.out.println("=== Traffic Light State Machine ===");
        TrafficLight light = TrafficLight.RED;

        for (int i = 0; i < 9; i++) {
            System.out.printf("  %s %-8s | %-40s | %d sec%n",
                light.emoji(), light, light.action(), light.duration());
            light = light.next();
        }


        // --------------------------------------------------------
        // Order Lifecycle
        // --------------------------------------------------------
        System.out.println("\n=== Order Lifecycle State Machine ===");

        OrderState order = OrderState.PLACED;
        System.out.println("\nNew order created:");
        printState("Order", order);
        System.out.println("  → " + order.describe());

        System.out.println("\nEvent: confirm()");
        order = order.confirm();
        printState("Order", order);
        System.out.println("  → " + order.describe());

        System.out.println("\nEvent: ship()");
        order = order.ship();
        printState("Order", order);
        System.out.println("  → " + order.describe());

        System.out.println("\nEvent: cancel() [should fail — already shipped]");
        order = order.cancel(); // triggers invalid message
        printState("Order", order); // still SHIPPED

        System.out.println("\nEvent: deliver()");
        order = order.deliver();
        printState("Order", order);
        System.out.println("  → " + order.describe());

        // Show a cancel flow
        System.out.println("\n--- Cancel Flow ---");
        OrderState cancelOrder = OrderState.PLACED;
        System.out.println("Start: " + cancelOrder);
        cancelOrder = cancelOrder.confirm();
        System.out.println("After confirm: " + cancelOrder + " (canCancel=" + cancelOrder.canCancel() + ")");
        cancelOrder = cancelOrder.cancel();
        System.out.println("After cancel: " + cancelOrder);


        // --------------------------------------------------------
        // Vending Machine
        // --------------------------------------------------------
        System.out.println("\n=== Vending Machine State Machine ===");

        VendingState vm = VendingState.IDLE;
        System.out.println("Initial state: " + vm);

        System.out.println("\n--- Scenario 1: Happy path ---");
        vm = vm.insertCoin();    printState("VM", vm);
        vm = vm.selectItem();    printState("VM", vm);
        vm = vm.pressDispense(); printState("VM", vm);

        System.out.println("\n--- Scenario 2: Refund path ---");
        vm = vm.insertCoin();    printState("VM", vm);
        vm = vm.selectItem();    printState("VM", vm);
        vm = vm.refund();        printState("VM", vm);

        System.out.println("\n--- Scenario 3: Invalid actions ---");
        vm = VendingState.IDLE;
        vm = vm.selectItem();    // can't select without coin
        vm = vm.pressDispense(); // can't dispense without coin
        vm = vm.insertCoin();    printState("VM", vm);
        vm = vm.insertCoin();    // double insert


        // --------------------------------------------------------
        // Document Workflow
        // --------------------------------------------------------
        System.out.println("\n=== Document Workflow State Machine ===");

        DocumentState doc = DocumentState.DRAFT;
        System.out.println("Document created: " + doc.status());

        System.out.println("\nTrying invalid action (approve while DRAFT):");
        doc.approve(); // shows warning, doesn't change state

        System.out.println("\nSubmit for review:");
        doc = doc.submit();
        System.out.println("Status: " + doc.status());

        System.out.println("\nReviewer rejects — sent back to DRAFT:");
        doc = doc.reject();
        System.out.println("Status: " + doc.status());

        System.out.println("\nRevised and resubmitted:");
        doc = doc.submit();
        System.out.println("Status: " + doc.status());

        System.out.println("\nReviewer approves:");
        doc = doc.approve();
        System.out.println("Status: " + doc.status());

        System.out.println("\nPublished!");
        doc = doc.publish();
        System.out.println("Status: " + doc.status());

        System.out.println("\nTrying to modify published document:");
        doc.reject(); // shows warning

        System.out.println("\nLesson 10 Complete!");
        System.out.println("\n🎉 All 10 lessons done! You now know Java Enums from basics to design patterns!");
    }
}

// ============================================================
//  SUMMARY
// ============================================================
//  State Machine Pattern:
//    - Each enum constant = one state
//    - Abstract methods = events/transitions
//    - Each constant implements transitions to other states
//    - Invalid transitions: warn and return self (no state change)
//
//  Key benefits:
//    - Impossible to be in an invalid state
//    - Each state controls its own valid transitions
//    - Adding a new state: add constant + implement all methods
//    - Compiler enforces you handle all events in all states
//
//  Combines best of Lessons 4 (abstract methods) + 1 (constants)
//
//  Best for: workflows, protocols, UI state, game logic,
//            order/document lifecycle, device control
//
//  COMPILE & RUN:
//    javac Lesson10_StateMachine.java
//    java  Lesson10_StateMachine
// ============================================================
