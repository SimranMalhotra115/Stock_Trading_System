import java.io.*;
import java.util.*;

class Stock {
    String name;
    double price;

    Stock(String name, double price) {
        this.name = name;
        this.price = price;
    }
}

class Transaction {
    String stockName;
    int quantity;
    double price;
    String type;

    Transaction(String stockName, int quantity, double price, String type) {
        this.stockName = stockName;
        this.quantity = quantity;
        this.price = price;
        this.type = type;
    }
}

class User {
    double balance = 10000;
    HashMap<String, Integer> portfolio = new HashMap<>();
}

public class CodeAlpha_StockTradingPlatform {

    static ArrayList<Stock> market = new ArrayList<>();
    static User user = new User();
    static final String FILE_NAME = "portfolio.txt";

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        // Market setup
        market.add(new Stock("TCS", 3500));
        market.add(new Stock("INFY", 1500));
        market.add(new Stock("RELIANCE", 2500));

        while (true) {
            System.out.println("\n--- STOCK TRADING PLATFORM ---");
            System.out.println("1. View Market");
            System.out.println("2. Buy Stock");
            System.out.println("3. Sell Stock");
            System.out.println("4. View Portfolio");
            System.out.println("5. Exit");

            System.out.print("Enter choice: ");

            int choice;
            try {
                choice = Integer.parseInt(sc.nextLine());
            } catch (Exception e) {
                System.out.println("Invalid input!");
                continue;
            }

            switch (choice) {
                case 1:
                    viewMarket();
                    break;
                case 2:
                    buyStock(sc);
                    break;
                case 3:
                    sellStock(sc);
                    break;
                case 4:
                    viewPortfolio();
                    break;
                case 5:
                    System.out.println("Exiting...");
                    return;
                default:
                    System.out.println("Invalid choice!");
            }
        }
    }

    // 🔹 View Market
    static void viewMarket() {
        System.out.println("\n--- MARKET DATA ---");
        for (Stock s : market) {
            System.out.println(s.name + " - ₹" + s.price);
        }
    }

    // 🔹 Buy Stock
    static void buyStock(Scanner sc) {
        System.out.println("Your Current Balance: ₹" + user.balance);
        viewMarket();

        System.out.print("Enter stock name: ");
        String name = sc.nextLine().toUpperCase();

        Stock selected = null;

        for (Stock s : market) {
            if (s.name.equalsIgnoreCase(name)) {
                selected = s;
            }
        }

        if (selected == null) {
            System.out.println("Stock not found!");
            return;
        }

        int maxQty = (int)(user.balance / selected.price);
        System.out.println("You can buy up to " + maxQty + " shares of " + selected.name);
        System.out.println("Enter quantity (based on your balance): ");

        
        int qty = Integer.parseInt(sc.nextLine());

        double total = qty * selected.price;

        if (user.balance >= total) {
            user.balance -= total;

            user.portfolio.put(name,
                user.portfolio.getOrDefault(name, 0) + qty);

            saveToFile(name, qty, selected.price, "BUY");

            System.out.println("Stock bought successfully!");
        } else {
            System.out.println("Insufficient balance!");
        }
    }

    // 🔹 Sell Stock
   static void sellStock(Scanner sc) {

    // Step 1: Check if portfolio empty
    if (user.portfolio.isEmpty()) {
        System.out.println("⚠ No stocks available to sell!");
        return;
    }

    // Step 2: Show user holdings
    System.out.println("\n--- YOUR HOLDINGS ---");
    for (String stock : user.portfolio.keySet()) {
        int qty = user.portfolio.get(stock);
        System.out.println(stock + " → " + qty + " shares");
    }
    System.out.println("---------------------");

    // Step 3: Take input
    System.out.print("Enter stock name to sell: ");
    String name = sc.nextLine().toUpperCase();

    // Step 4: Validate stock exists
    if (!user.portfolio.containsKey(name)) {
        System.out.println("❌ You don't own this stock!");
        return;
    }

    System.out.print("Enter quantity: ");
    int qty = sc.nextInt();
    sc.nextLine();

    int ownedQty = user.portfolio.get(name);

    if (qty > ownedQty) {
        System.out.println("❌ Not enough quantity!");
        return;
    }

    // Step 5: Find price
    double price = 0;
    boolean found = false;

    for (Stock s : market) {
        if (s.name.equalsIgnoreCase(name)) {
            price = s.price;
            found = true;
            break;
        }
    }

    if (!found) {
        System.out.println("❌ Stock not found in market!");
        return;
    }

    // Step 6: Update balance
    user.balance += qty * price;

    // Step 7: Update portfolio
    if (qty == ownedQty) {
        user.portfolio.remove(name);
    } else {
        user.portfolio.put(name, ownedQty - qty);
    }

    System.out.println("✅ Stock sold successfully!");
}

    // 🔹 View Portfolio
 static void viewPortfolio() {
    System.out.println("\n--- YOUR PORTFOLIO ---");

    // 🔥 Always show balance first
    System.out.println("Current Balance: ₹" + user.balance);

    if (user.portfolio.isEmpty()) {
        System.out.println("No stocks owned.");
        return;
    }

    double totalInvestment = 0;

    for (String stock : user.portfolio.keySet()) {
        int qty = user.portfolio.get(stock);

        // stock price find kar
        double price = 0;
        for (Stock s : market) {
            if (s.name.equalsIgnoreCase(stock)) {
                price = s.price;
            }
        }

        double value = qty * price;
        totalInvestment += value;

        System.out.println(stock + " | Qty: " + qty + " | Value: ₹" + value);
    }

    // 🔥 total summary
    System.out.println("--------------------------");
    System.out.println("Total Investment: ₹" + totalInvestment);
}
    // 🔹 Save transactions
    static void saveToFile(String name, int qty, double price, String type) {
        try (FileWriter fw = new FileWriter(FILE_NAME, true)) {
            fw.write(type + "," + name + "," + qty + "," + price + "\n");
        } catch (IOException e) {
            System.out.println("Error saving data!");
        }
    }
}