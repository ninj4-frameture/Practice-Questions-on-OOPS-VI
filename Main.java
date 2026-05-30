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
