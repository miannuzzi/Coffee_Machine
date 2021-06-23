package machine;

import java.util.Scanner;

public class CoffeeMachine {
    private static final int COFFEE_AMOUNT_BY_CUP = 15;
    private static final int MILK_AMOUNT_BY_CUP = 50;
    private static final int WATER_AMOUNT_BY_CUP = 200;

    private static int cups = 9;
    private static int milk = 540;
    private static int coffee = 120;
    private static int water = 400;
    private static int money = 550;


    public enum Transition {
        BUY {
            @Override
            public void handle() {
                coffeeMenu();
            }
        },
        FILL {
            @Override
            public void handle() {
                fill();
            }
        },
        TAKE {
            @Override
            public void handle() {
                take();
            }
        },
        REMAINING {
            @Override
            public void handle() {
                printCurrentState();
            }
        },

        ESPRESSO {
            @Override
            public void handle() {
                prepareEspresso();
            }
        },
        LATTE {
            @Override
            public void handle() {
                prepareLatte();
            }
        },
        CAPUCCINO {
            @Override
            public void handle() {
                prepareCapuccino();
            }
        },
        BACK {
            @Override
            public void handle() {
                //empty
            }
        },
        EXIT {
            @Override
            public void handle() {
                //empty
            }
        };

        public abstract void handle();
    }

    public enum State  {

        MENU{
            @Override
            public State nextState(Transition transition) {
                State next = State.MENU;
                transition.handle();

                switch (transition) {
                    case BUY:
                        next = State.BUY;
                        break;
                    case EXIT:
                        next = State.EXIT;
                        break;
                }
                return next;
            }
        },
        BUY {
            @Override
            public State nextState(Transition transition) {
                transition.handle();
                return State.MENU;
            }
        },
        EXIT {
            @Override
            public State nextState(Transition transition) {
                return this;
            }
        };

        public abstract State nextState(Transition transition);
    }

    private static State state = State.MENU;


    public static void main(String[] args) {

       // CoffeeMachine.printCurrentState();
        mainMenu();

        //CoffeeMachine.printCurrentState();

       // CoffeeMachine.inputValues();
        //CoffeeMachine.inputCups();
        //CoffeeMachine.printAvailableCups();

    }

    public static String getInput() {
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine().toUpperCase();

        switch (input) {
            case "1":
                input = "ESPRESSO";
                break;
            case "2":
                input = "LATTE";
                break;
            case "3":
                input = "CAPUCCINO";
                break;
        }

        return input;
    }

    private static void mainMenu() {

        System.out.println("Write action (buy, fill, take, remaining, exit):");

        while (state != State.EXIT) {
            Transition transition = Transition.valueOf(getInput());

            state = state.nextState(transition);
/*
            if ("buy".equals(menuOption)) {
                prepareCoffee();
            } else if ("fill".equals(menuOption)) {
                fill();
            } else if ("take".equals(menuOption)) {
                take();
            } else if ("remaining".equals((menuOption))) {
                printCurrentState();
            }*/
        }

    }

    private static void take() {
        System.out.printf("I gave you %d%n:", CoffeeMachine.getMoney());
        CoffeeMachine.setMoney(0);
    }

    private static void fill() {
        addWater();
        addMilk();
        addCoffee();
        addCups();
    }
/*
    private static void prepareCoffee() {
      //  String option = CoffeeMachine.coffeeMenu();

        switch (option) {
            case "1":
                prepareEspresso();
                break;
            case "2":
                prepareLatte();
                break;
            case "3":
                prepareCapuccino();
                break;
        }
    }*/

    private static void prepareEspresso() {
        if (hasEnoughResources(0,250,16)) {
            System.out.println("I have enough resources, making you a coffee!");
            brew(0,250,16,4);
        } else {
            System.out.println("it can't make a cup of coffee.");
        }
    }

    private static void prepareLatte() {
        if (hasEnoughResources(75,350,20)) {
            System.out.println("I have enough resources, making you a coffee!");
            brew(75,350,20,7);
        } else {
            System.out.println("it can't make a cup of coffee.");
        }

    }

    private static void prepareCapuccino() {
        if (hasEnoughResources(100,200,12)) {
            System.out.println("I have enough resources, making you a coffee!");
            brew(100,200,12,6);
        } else {
            System.out.println("it can't make a cup of coffee.");
        }

    }

    private static boolean hasEnoughResources(int pMilk, int pWater, int pCoffee) {

        return (CoffeeMachine.getMilk() - pMilk) >= 0 && (CoffeeMachine.getWater() - pWater) >= 0
                    && (CoffeeMachine.getCoffee() - pCoffee) >= 0 && CoffeeMachine.getCups() > 0;
    }

    private static void brew(int pMilk, int pWater, int pCoffee, int pMoney) {
        CoffeeMachine.setMilk(CoffeeMachine.getMilk() - pMilk);
        CoffeeMachine.setWater(CoffeeMachine.getWater() - pWater);
        CoffeeMachine.setCoffee(CoffeeMachine.getCoffee() - pCoffee);
        CoffeeMachine.setMoney(CoffeeMachine.getMoney() + pMoney);
        CoffeeMachine.setCups(CoffeeMachine.getCups() - 1);
    }

    private static void coffeeMenu() {
        System.out.println("What do you want to buy? 1 - espresso, 2 - latte, 3 - cappuccino, back - to main menu:");

    }

    private static int getAvailableCups() {
        int n = 0;

        int currentCoffee   = CoffeeMachine.getCoffee() - CoffeeMachine.COFFEE_AMOUNT_BY_CUP;
        int currentMilk     = CoffeeMachine.getMilk() - CoffeeMachine.MILK_AMOUNT_BY_CUP;
        int currentWater    = CoffeeMachine.getWater() - CoffeeMachine.WATER_AMOUNT_BY_CUP;

        while (currentCoffee >= 0 && currentWater >= 0 && currentMilk >= 0) {
            n++;

            currentCoffee   -= CoffeeMachine.COFFEE_AMOUNT_BY_CUP;
            currentMilk     -= CoffeeMachine.MILK_AMOUNT_BY_CUP;
            currentWater    -= CoffeeMachine.WATER_AMOUNT_BY_CUP;
        }

        return n;
    }

    private static void printAvailableCups() {

        int n = CoffeeMachine.getAvailableCups();

        if (CoffeeMachine.getCups() == n) {
            System.out.println("Yes, I can make that amount of coffee");
        } else if (CoffeeMachine.getCups() < n) {
            System.out.printf("Yes, I can make that amount of coffee (and even %d more than that)%n", n - CoffeeMachine.getCups());
        } else {
            System.out.printf("No, I can make only %d cup(s) of coffee%n", n);
        }

    }

    private static void inputValues() {
        CoffeeMachine.inputWater();
        CoffeeMachine.inputMilk();
        CoffeeMachine.inputCoffee();
    }

    private static void inputCups() {
        System.out.println("Write how many cups of coffee you will need:");
        Scanner scanner = new Scanner(System.in);

        int input = scanner.nextInt();
        CoffeeMachine.setCups(input);
    }

    private static void inputMilk() {
        System.out.println("Write how many ml of milk the coffee machine has:");
        Scanner scanner = new Scanner(System.in);

        int input = scanner.nextInt();
        CoffeeMachine.setMilk(input);
    }

    private static void inputWater() {
        System.out.println("Write how many ml of water the coffee machine has:");
        Scanner scanner = new Scanner(System.in);

        int input = scanner.nextInt();
        CoffeeMachine.setWater(input);
    }

    private static void inputCoffee() {
        System.out.println("Write how many grams of coffee beans the coffee machine has:");
        Scanner scanner = new Scanner(System.in);

        int input = scanner.nextInt();
        CoffeeMachine.setCoffee(input);
    }

    private static void addCups() {
        System.out.println("Write how many cups of coffee you will need:");
        Scanner scanner = new Scanner(System.in);

        int input = scanner.nextInt();
        CoffeeMachine.setCups(CoffeeMachine.getCups() + input);
    }

    private static void addMilk() {
        System.out.println("Write how many ml of milk the coffee machine has:");
        Scanner scanner = new Scanner(System.in);

        int input = scanner.nextInt();
        CoffeeMachine.setMilk(CoffeeMachine.getMilk() + input);
    }

    private static void addWater() {
        System.out.println("Write how many ml of water the coffee machine has:");
        Scanner scanner = new Scanner(System.in);

        int input = scanner.nextInt();
        CoffeeMachine.setWater(CoffeeMachine.getWater() + input);
    }

    private static void addCoffee() {
        System.out.println("Write how many grams of coffee beans the coffee machine has:");
        Scanner scanner = new Scanner(System.in);

        int input = scanner.nextInt();
        CoffeeMachine.setCoffee(CoffeeMachine.getCoffee() + input);
    }

    private static void printCurrentState() {
        System.out.println("The coffee machine has:");
        System.out.printf("%d ml of water%n", CoffeeMachine.getWater());
        System.out.printf("%d ml of milk%n", CoffeeMachine.getMilk());
        System.out.printf("%d g of coffee beans%n", CoffeeMachine.getCoffee());
        System.out.printf("%d disposable cups%n", CoffeeMachine.getCups());
        System.out.printf("%d of money%n", CoffeeMachine.getMoney());
    }

    private static long getCoffeeAmount() {
        return CoffeeMachine.COFFEE_AMOUNT_BY_CUP * CoffeeMachine.getCups();
    }

    private static long getMilkAmount() {
        return CoffeeMachine.MILK_AMOUNT_BY_CUP * CoffeeMachine.getCups();
    }

    private static long getWaterAmount() {
        return CoffeeMachine.WATER_AMOUNT_BY_CUP * CoffeeMachine.getCups();
    }

    private static void setCups(int pCups) {
        CoffeeMachine.cups = pCups;
    }

    private static int getCups() {
        return CoffeeMachine.cups;
    }

    private static void setCoffee(int pCoffee) {
        CoffeeMachine.coffee = pCoffee;
    }

    private static int getCoffee() {
        return CoffeeMachine.coffee;
    }
    private static void setWater(int pWater) {
        CoffeeMachine.water = pWater;
    }

    private static int getMilk() {
        return CoffeeMachine.milk;
    }

    private static void setMilk(int pMilk) {
        CoffeeMachine.milk = pMilk;
    }

    private static int getWater() {
        return CoffeeMachine.water;
    }

    private static void setMoney(int pMoney) {
        CoffeeMachine.money = pMoney;
    }

    private static int getMoney() {
        return CoffeeMachine.money;
    }

}
