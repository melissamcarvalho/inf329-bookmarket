
package dominio;

import util.Validator;

/**
 * Represents the inventory level of a specific book at a specific bookstore location.
 * This class tracks the availability, purchase cost, and physical origin of the stock.
 * <br><img src="./doc-files/Stock.png" alt="Stock Diagram">
 */
public class Stock {
    
    
    private final int idBookstore;
    private final Address address;
    private final Book book;
    private double cost;
    private int qty;

    /**
     * Constructs a Stock entry with mandatory inventory and cost validation.
     * @param idBookstore Unique identifier for the bookstore location.
     * @param address The physical location of the stock. Must not be null.
     * @param book The book associated with this stock entry. Must not be null.
     * @param cost The purchase cost of the book. Must be non-negative.
     * @param qty The current quantity in stock. Must be non-negative.
     * @throws NullPointerException if address or book is null.
     * @throws IllegalArgumentException if cost or qty is negative.
     */
    public Stock(int idBookstore, Address address, Book book, double cost, int qty) {
        this.idBookstore = Validator.notNegative(idBookstore, "idBookstore");
        this.address = Validator.notNull(address, "address");
        this.book = Validator.notNull(book, "book");
        this.cost = Validator.notNegative(cost, "cost");
        this.qty = Validator.notNegative(qty, "qty");
    }

    /** @return The {@link Book} associated with this stock. */
    public Book getBook() {
        return book;
    }

    /**
     * Increments the current stock quantity.
     * @param amount The quantity to add. Can be negative for removals.
     * @throws IllegalArgumentException if the resulting quantity would be negative.
     */
    public void addQty(int amount) {
        int result = this.qty + amount;
        if (result <= 0) {
            throw new IllegalArgumentException("Resulting stock quantity cannot be negative or zero. Current: "
                    + this.qty + ", Adjustment: " + amount);
        }
        this.qty = result;
    }

    /** @return The unit cost of the book for the bookstore. */
    public double getCost() {
        return cost;
    }

    /**
     * @param cost The new unit cost. Must be non-negative.
     * @throws IllegalArgumentException if cost is negative.
     */
    public void setCost(double cost) {
        this.cost = Validator.notNegative(cost, "cost");
    }

    /** @return The current available quantity. */
    public int getQty() {
        return qty;
    }

    /**
     * @param qty The new total quantity. Must be non-negative.
     * @throws IllegalArgumentException if qty is negative.
     */
    public void setQty(int qty) {
        this.qty = Validator.notNegative(qty, "qty");
    }

    /** @return The bookstore ID. */
    public int getIdBookstore() {
        return idBookstore;
    }

    /** @return The physical address where this stock is stored. */
    public Address getAddress() {
        return address;
    }

    @Override
    public String toString() {
        return "Stock{" + "idBookstore=" + idBookstore + ", bookId=" + book.getId() + ", cost=" + cost + ", qty=" + qty + '}';
    }
}
