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
    private final double rating;

    /**
     * Evaluation class constructor
     * @param id Evaluation id number
     * @param customer Costumer class reference
     * @param book Book class reference
     * @param rating Book rating
     */
    public Evaluation(final int id, Customer customer, Book book, double rating) {
        this.id = id;
        this.customer = customer;
        this.book = book;
        this.rating = rating;
    }

    /**
     *
     * @return
     */
    public int getId() { return this.id; }

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

}
