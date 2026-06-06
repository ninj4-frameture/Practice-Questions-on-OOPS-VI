import java.time.LocalDate;
import java.time.temporal.ChronoUnit;//date calculations
import java.util.*;//user input
//base class

abstract class Item {
    private String id;
    private String title;
    private LocalDate publicationDate;
    private int maxCheckoutDays;
    private boolean available = true;

    // constructor
    public Item(String id, String title, LocalDate publicationDate, int maxCheckoutDays) {
        this.id = id;
        this.title = title;
        this.publicationDate = publicationDate;
        this.maxCheckoutDays = maxCheckoutDays;
    }

    public boolean isAvailable() {
        return available;
    }

    // method-check if the book is borrowed
    public void markBorrowed() {
        available = false;
    }

    // method-check if a book is returned
    public void markReturned() {
        available = true;
    }

    // returns allowed within duration
    public int getMaxCheckoutDays() {
        return maxCheckoutDays;
    }

    public String getTitle() {
        return title;
    }

    public String getId() {
        return id;
    }
}

// book class-show book borrowing
class Book extends Item {
    private String author; // Book author

    public Book(String id, String title, LocalDate date, String author) {
        super(id, title, date, 21); // Books can be borrowed
        this.author = author;
    }

}

class DVD extends Item {
    public DVD(String id, String title, LocalDate date) {
        super(id, title, date, 7); // DVDs: 7-day borrowing period
    }
}

class CD extends Item {
    public CD(String id, String title, LocalDate date) {
        super(id, title, date, 14); // CDs: 14-day borrowing period
    }
}

// loan class-borrow the book
class Loan {
    // create objects
    Item item;
    LocalDate due;
    LocalDate returned;

    // method-when the item was borrowed
    Loan(Item item) {
        this.item = item;
        this.due = LocalDate.now().plusDays(item.getMaxCheckoutDays());
    }

    // method-run when an item is returned
    void close() {
        returned = LocalDate.now();
    }

    // method-to fine an overdue user
    long fine() {
        if (returned == null || !returned.isAfter(due)) {
            return 0;
        }
        // calculate the number of days late
        long late_days = ChronoUnit.DAYS.between(due, returned);
        return late_days * 50;// fine per late day
    }
}

// class library user
class User {
    // list of items borrowed
    List<Loan> loans = new ArrayList<>();

    // borrow an item
    void borrow(Item item) {
        // item limit
        if (loans.size() >= 10) {
            System.out.println("Limit reached. You cannot borrow more than 10 items");
            return;
        }
        // Check if Item is available
        if (!item.isAvailable()) {
            System.out.println("Item is not available");
            return;
        }
        // create a new borrowed item
        Loan l = new Loan(item);
        loans.add(l);
        item.markBorrowed();// mark the item as borrowed
        // display info
        System.out.println("Borrowed:" + item.getTitle());
        System.out.println("Due:" + l.due);
    }
}

// main class
public class Main {
    // main method
    public static void main(String[] args) {
        // input object
        Scanner input = new Scanner(System.in);
        // library array-store items with the ID
        Map<String, Item> lib = new HashMap<>();
        // add sample items
        lib.put("B1", new Book("B1", "Blood Invasion", LocalDate.of(2020, 1, 1), "Unknown"));
        lib.put("D1", new DVD("D1", "Inception", LocalDate.of(2010, 7, 18)));
        lib.put("C1", new CD("C1", "Thriller", LocalDate.of(1983, 11, 26)));
        // new user object
        User user = new User();
        int choice = 0;
        do {
            System.out.println("1. View items available");
            System.out.println("2. Borrow an Item");
            System.out.println("3. Return Item");
            System.out.println("4. View Borrowed items");
            System.out.println("5. Exit");
            System.out.println("Enter a choice:");
            try {
                choice = input.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number.");
                input.nextLine(); // Clear the invalid input from buffer
                continue;
            }
            if (choice == 1) {
                System.out.println("\n Items Available:");
                for (Item i : lib.values()) {
                    System.out.println(i.getId() + "-" + i.getTitle() + "|Available:" + i.isAvailable());
                }
            } else if (choice == 2) {
                System.out.println("Enter the item ID:");
                String bid = input.next();
                if (lib.containsKey((bid))) {
                    user.borrow(lib.get(bid));
                } else {
                    System.out.println("Invalid ID");
                }
            } else if (choice == 3) {
                System.out.println("Enter ID:");
                String returnId = input.next();
                if (lib.containsKey(returnId)) {
                    Item returnItem = lib.get(returnId);
                    returnItem.markReturned();
                    System.out.println("Item returned");
                } else {
                    System.out.println("Invalid ID");
                }
            } else if (choice == 4) {
                System.out.println("\nBorrowed Items:");
                for (Loan loan : user.loans) {
                    System.out.println(loan.item.getTitle() + " - Due: " + loan.due);
                }
            } else {
                System.out.println("Exiting......");
            }
        } while (choice != 5);
    }
}