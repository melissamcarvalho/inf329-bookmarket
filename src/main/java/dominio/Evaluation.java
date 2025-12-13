package dominio;

/**
 * <img src="./doc-files/Evaluation.png" alt="Bookmarket">
 * <br><a href="./doc-files/Evaluation.html"> code </a>
 *
 */
public class Evaluation {

    private final int id;
    private final Customer customer;
    private final Book book;
    private double rating;

    /**
     * Evaluation class constructor
     * @param id Evaluation id number
     * @param c Costumer class reference
     * @param b Book class reference
     * @param rating Book rating
     */
    public Evaluation(final int id, Customer c, Book b, double rating) {
        this.id = id;
        this.customer = c;
        this.book = b;
        this.rating = rating;
    }

    /**
     * Get Costumer assigned to the evaluation
     * @return Costumer reference
     */
    public Customer getCustomer() {
        return customer;
    }

    /**
     * Get Book assigned to the evaluation
     * @return Book reference
     */
    public Book getBook() {
        return book;
    }

    /**
     * Get rating assigned to the evaluation
     * @return Rating
     */
    public double getRating() {
        return rating;
    }

    /**
     * Set rating to the evaluation
     * @param rating
     */
    public void setRating(double rating) {
        this.rating = rating;
    }
}
