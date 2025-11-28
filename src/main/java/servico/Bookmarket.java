package servico;

import dominio.ShipTypes;
import dominio.Address;
import dominio.Book;
import dominio.Cart;
import dominio.CreditCards;
import dominio.Customer;
import dominio.Order;
import dominio.OrderLine;
import dominio.SUBJECTS;
import dominio.Stock;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.BinaryOperator;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import util.TPCW_Util;

/**
 * Provides a public facade for the online bookstore system, simplifying interaction with the core business logic.
 * <p>
 * This class acts as the primary entry point for clients. It abstracts the underlying complexity of the bookstore's
 * architecture, including the management of one or more {@link Bookstore} instances.
 * </p>
 * <h2>Architecture</h2>
 * <p>
 * The {@code Bookmarket} uses a {@link StateMachine} pattern to manage and delegate operations to a collection of
 * {@link Bookstore} instances. This design allows the system to operate on a single bookstore or a distributed
 * network of them.
 * </p>
 * <p>
 * Client requests are encapsulated as {@link Action} objects and executed by the state machine. This ensures that
 * operations are performed consistently across all managed {@code Bookstore} instances. Most methods in this class
 * are static and delegate their calls to the underlying {@code Bookstore}(s) via the state machine, providing a
 * single, unified interface for all bookstore operations.
 * </p>
 *
 * <img src="./doc-files/Bookstore.png" alt="Bookmarket">
 * <br><a href="./doc-files/Bookmarket.html"> code </a>
 *
 */
public class Bookmarket {

    private interface Action<STATE> {

        Object executeOn(STATE sm);
    }

    static class StateMachine {

        private final List<Bookstore> state;

        public StateMachine(final List object) {
            this.state = object;
        }

        Object execute(Action action) {
            return action.executeOn(getStateStream());
        }

        void checkpoint() {

        }
        
        List<Bookstore> getState() {
            return state;
        }

        Stream getStateStream() {
            return state.stream();
        }

        static StateMachine create(Bookstore... state) {
            List list = new ArrayList();
            list.addAll(Arrays.asList(state));
            return new StateMachine(list);
        }

    }

    private class UmbrellaException extends RuntimeException {

    }
    private static Random random;
    private static StateMachine stateMachine;

    static StateMachine getStateMachine() {
        return stateMachine;
    }
    
    

    /**
     * Initializes the Bookmarket system with a random seed and one or more {@link Bookstore} instances.
     *
     * @param seed  The seed for the random number generator.
     * @param state A variable number of {@link Bookstore} instances to be managed by the state machine.
     */
    public static void init(int seed, Bookstore... state) {
        random = new Random(seed);
        try {
            stateMachine = StateMachine.create(state);
        } catch (UmbrellaException e) {
            throw new RuntimeException(e);
        }
    }

    private static Stream<Bookstore> getBookstoreStream() {
        return (Stream) stateMachine.getStateStream();
    }

    /**
     * Retrieves a customer by their username.
     *
     * @param UNAME The username of the customer.
     * @return The corresponding {@link Customer} object.
     */
    public static Customer getCustomer(String UNAME) {
        return Bookstore.getCustomer(UNAME).get();
    }

    /**
     * Retrieves a customer's full name and username by their ID.
     *
     * @param c_id The customer's unique ID.
     * @return A string array containing the first name, last name, and username.
     */
    public static String[] getName(int c_id) {

        Customer customer = Bookstore.getCustomer(c_id);

        String name[] = new String[3];
        name[0] = customer.getFname();
        name[1] = customer.getLname();
        name[2] = customer.getUname();
        return name;
    }

    /**
     * Retrieves a customer's username by their ID.
     *
     * @param C_ID The customer's unique ID.
     * @return The customer's username.
     */
    public static String getUserName(int C_ID) {
        return Bookstore.getCustomer(C_ID).getUname();
    }

    /**
     * Retrieves a customer's password by their username.
     *
     * @param C_UNAME The customer's username.
     * @return The customer's password.
     */
    public static String getPassword(String C_UNAME) {
        return Bookstore.getCustomer(C_UNAME).get().getPasswd();

    }

    /**
     * Retrieves the most recent order for a given customer.
     *
     * @param c_uname The customer's username.
     * @return The most recent {@link Order} placed by the customer.
     */
    public static Order getMostRecentOrder(String c_uname) {
        return Bookstore.getCustomer(c_uname).get().getMostRecentOrder();
    }

    /**
     * Creates a new customer account.
     * This method acts as a facade, delegating the creation to the underlying {@link Bookstore}.
     *
     * @param fname       The customer's first name.
     * @param lname       The customer's last name.
     * @param street1     The first line of the address.
     * @param street2     The second line of the address.
     * @param city        The city.
     * @param state       The state.
     * @param zip         The postal code.
     * @param countryName The name of the country.
     * @param phone       The customer's phone number.
     * @param email       The customer's email address.
     * @param birthdate   The customer's date of birth.
     * @param data        Additional profile data.
     * @return The newly created {@link Customer} object.
     */
    public static Customer createNewCustomer(String fname, String lname,
            String street1, String street2, String city, String state,
            String zip, String countryName, String phone, String email,
            Date birthdate, String data) {
        double discount = (int) (Math.random() * 51);
        long now = System.currentTimeMillis();
        try {
            return (Customer) stateMachine.execute(new CreateCustomerAction(
                    fname, lname, street1, street2, city, state, zip,
                    countryName, phone, email, discount, birthdate, data, now));

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Refreshes a customer's session, extending their login time.
     *
     * @param cId The ID of the customer whose session is to be refreshed.
     */
    public static void refreshSession(int cId) {
        try {
            stateMachine.execute(new RefreshCustomerSessionAction(cId,
                    System.currentTimeMillis()));

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Retrieves a book by its unique ID.
     *
     * @param i_id The ID of the book to retrieve.
     * @return The corresponding {@link Book} object.
     */
    public static Book getBook(int i_id) {

        return Bookstore.getBook(i_id).get();

    }

    /**
     * Retrieves a randomly selected book from the catalog.
     *
     * @return A random {@link Book}.
     */
    public static Book getABookAnyBook() {
        return Bookstore.getABookAnyBook(random);

    }

    /**
     * Performs a search for books by subject.
     *
     * @param search_key The {@link SUBJECTS} to search for.
     * @return A list of books matching the subject.
     */
    public static List<Book> doSubjectSearch(SUBJECTS search_key) {
        return Bookstore.getBooksBySubject(search_key);
    }

    /**
     * Performs a search for books by title.
     *
     * @param search_key The title to search for.
     * @return A list of books with matching titles.
     */
    public static List<Book> doTitleSearch(String search_key) {
        return Bookstore.getBooksByTitle(search_key);
    }

    /**
     * Performs a search for books by author's last name.
     *
     * @param search_key The author's last name to search for.
     * @return A list of books by the specified author.
     */
    public static List<Book> doAuthorSearch(String search_key) {
        return Bookstore.getBooksByAuthor(search_key);
    }

    /**
     * Retrieves a list of the newest products for a given subject.
     *
     * @param subject The {@link SUBJECTS} to search for.
     * @return A list of the newest books in that subject.
     */
    public static List<Book> getNewProducts(SUBJECTS subject) {
        return Bookstore.getNewBooks(subject);
    }

    /**
     * Retrieves the costs of a specific book from all bookstore instances.
     *
     * @param book The book to query.
     * @return A list of costs for the book from each store.
     */
    public static List<Double> getCosts(Book book) {
        return getBookstoreStream().
                map(store -> store.getStock(book.getId())).
                map(stock -> stock.getCost()).
                collect(Collectors.toList());
    }

    /**
     * Retrieves the best-selling books for a given subject, along with their stock information.
     * The stock is ordered by cost.
     * <p><b>Note:</b> This feature is not yet implemented.</p>
     *
     * @param subject The subject to query.
     * @return Currently returns {@code null}.
     */
    public static Map<Book, Set<Stock>> getBestSellers(SUBJECTS subject) {
        // to do
        return null;
    }

    /**
     * Generates book recommendations for a customer based on their item purchase history.
     * <p><b>Note:</b> This feature is not yet implemented.</p>
     *
     * @param c_id The customer's ID.
     * @return Currently returns {@code null}.
     */
    public static List<Book> getRecommendationByItens(int c_id) {
        return Bookstore.getRecommendationByItens(c_id);
    }

    /**
     * Generates book recommendations for a customer based on the behavior of similar users.
     * <p><b>Note:</b> This feature is not yet implemented.</p>
     *
     * @param c_id The customer's ID.
     * @return Currently returns {@code null}.
     */
    public static List<Book> getRecommendationByUsers(int c_id) {
        return Bookstore.getRecommendationByUsers(c_id);
    }

    /**
     * Retrieves stock information for recommended books.
     * <p><b>Note:</b> This feature is not yet implemented.</p>
     *
     * @param c_id The customer's ID.
     * @return Currently returns {@code null}.
     */
    public static Map<Book, Set<Stock>> getStocksRecommendationByUsers(int c_id) {
        // to do
        return null;
    }

    /**
     * Retrieves price information for recommended books, adjusted for subscribers.
     * <p><b>Note:</b> This feature is not yet implemented.</p>
     *
     * @param c_id The customer's ID.
     * @return Currently returns {@code null}.
     */
    public static Map<Book, Double> getPriceBookRecommendationByUsers(int c_id) {
        // to do
        return null;
    }

    /**
     * Retrieves the stock information for a given book from all bookstore instances.
     *
     * @param idBook The ID of the book to check stock for.
     * @return A list of {@link Stock} objects from each store that has the book.
     */
    public static List<Stock> getStocks(final int idBook) {
        return getBookstoreStream()
                .filter(store -> store.getStock(idBook) != null)
                // transforma o stream de bookstore em stream de stock
                .map(store -> store.getStock(idBook))
                .collect(Collectors.toList());
    }

    /**
     * Retrieves the stock information for a book from a specific bookstore.
     *
     * @param idBookstore The ID of the bookstore to query.
     * @param idBook      The ID of the book.
     * @return The {@link Stock} object for the book in the specified store.
     */
    public static Stock getStock(final int idBookstore, final int idBook) {
        return getBookstoreStream()
                .filter(store -> store.getId() != idBookstore)
                // transforma o stream de bookstore em stream de stock
                .map(store -> store.getStock(idBook))
                .findFirst()
                .get();
    }

    /**
     * Retrieves a list of books related to a given book.
     *
     * @param i_id The ID of the book for which to find related items.
     * @return A list of up to 5 related {@link Book} objects.
     */
    public static List<Book> getRelated(int i_id) {
        Book book = Bookstore.getBook(i_id).get();
        ArrayList<Book> related = new ArrayList<>(5);
        related.add(book.getRelated1());
        related.add(book.getRelated2());
        related.add(book.getRelated3());
        related.add(book.getRelated4());
        related.add(book.getRelated5());
        return related;
    }

    /**
     * Performs an administrative update on a book's information.
     *
     * @param iId       The ID of the book to update.
     * @param cost      The new cost of the book.
     * @param image     The new image URL.
     * @param thumbnail The new thumbnail URL.
     */
    public static void adminUpdate(int iId, double cost, String image,
            String thumbnail) {
        try {
            stateMachine.execute(new UpdateBookAction(iId, cost, image,
                    thumbnail, System.currentTimeMillis()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Creates a new, empty shopping cart in a specific bookstore.
     *
     * @param storeId The ID of the bookstore where the cart will be created.
     * @return The unique ID of the newly created cart.
     */
    public static int createEmptyCart(int storeId) {
        try {
            return ((Cart) stateMachine.execute(new CreateCartAction(storeId,
                    System.currentTimeMillis()))).getId();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Updates a shopping cart by adding an item or modifying quantities.
     *
     * @param storeId     The ID of the bookstore where the cart exists.
     * @param SHOPPING_ID The ID of the shopping cart to update.
     * @param I_ID        The ID of the book to add (can be null).
     * @param ids         A list of book IDs to update quantities for.
     * @param quantities  A list of new quantities for the corresponding book IDs.
     * @return The updated {@link Cart} object.
     */
    public static Cart doCart(int storeId, int SHOPPING_ID, Integer I_ID, List<Integer> ids,
            List<Integer> quantities) {
        try {
            Cart cart = (Cart) stateMachine.execute(new CartUpdateAction(storeId,
                    SHOPPING_ID, I_ID, ids, quantities,
                    System.currentTimeMillis()));
            if (cart.getLines().isEmpty()) {
                Book book = Bookstore.getABookAnyBook(random);
                cart = (Cart) stateMachine.execute(new CartUpdateAction(storeId,
                        SHOPPING_ID, book.getId(), new ArrayList<>(),
                        new ArrayList<>(), System.currentTimeMillis()));
            }
            return cart;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Retrieves a shopping cart from a specific bookstore.
     *
     * @param storeId     The ID of the bookstore.
     * @param SHOPPING_ID The ID of the cart to retrieve.
     * @return The {@link Cart} object.
     */
    public static Cart getCart(int storeId, int SHOPPING_ID) {
        Bookstore bookstore = getBookstoreStream()
                .filter(store -> store.getId() == storeId)
                .findFirst()
                .get();
        synchronized (bookstore) {
            return bookstore.getCart(SHOPPING_ID);
        }
    }

    /**
     * Confirms a purchase using the customer's default address.
     *
     * @param storeId     The ID of the bookstore where the purchase is made.
     * @param shopping_id The ID of the shopping cart.
     * @param customer_id The ID of the customer.
     * @param cc_type     The credit card type.
     * @param cc_number   The credit card number.
     * @param cc_name     The name on the credit card.
     * @param cc_expiry   The credit card expiration date.
     * @param shipping    The shipping method.
     * @return The newly created {@link Order} object.
     */
    public static Order doBuyConfirm(int storeId, int shopping_id, int customer_id,
            CreditCards cc_type, long cc_number, String cc_name, Date cc_expiry,
            ShipTypes shipping) {
        long now = System.currentTimeMillis();
        try {
            return (Order) stateMachine.execute(new ConfirmBuyAction(storeId,
                    customer_id, shopping_id, randomComment(),
                    cc_type, cc_number, cc_name, cc_expiry, shipping,
                    randomShippingDate(now), -1, now));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Confirms a purchase using a specified shipping address.
     *
     * @param storeId     The ID of the bookstore where the purchase is made.
     * @param shopping_id The ID of the shopping cart.
     * @param customer_id The ID of the customer.
     * @param cc_type     The credit card type.
     * @param cc_number   The credit card number.
     * @param cc_name     The name on the credit card.
     * @param cc_expiry   The credit card expiration date.
     * @param shipping    The shipping method.
     * @param street_1    The first line of the shipping address.
     * @param street_2    The second line of the shipping address.
     * @param city        The city of the shipping address.
     * @param state       The state of the shipping address.
     * @param zip         The postal code of the shipping address.
     * @param country     The country of the shipping address.
     * @return The newly created {@link Order} object.
     */
    public static Order doBuyConfirm(int storeId, int shopping_id, int customer_id,
            CreditCards cc_type, long cc_number, String cc_name, Date cc_expiry,
            ShipTypes shipping, String street_1, String street_2, String city,
            String state, String zip, String country) {
        Address address = Bookstore.alwaysGetAddress(street_1, street_2,
                city, state, zip, country);
        long now = System.currentTimeMillis();
        try {
            return (Order) stateMachine.execute(new ConfirmBuyAction(storeId,
                    customer_id, shopping_id, randomComment(),
                    cc_type, cc_number, cc_name, cc_expiry, shipping,
                    randomShippingDate(now), address.getId(), now));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static String randomComment() {
        return TPCW_Util.getRandomString(random, 20, 100);
    }

    private static Date randomShippingDate(long now) {
        return new Date(now + 86400000 /* a day */ * (random.nextInt(7) + 1));
    }

    /**
     * Populates the database with a large set of randomly generated data.
     * This is part of the TPC-W benchmark setup.
     *
     * @param items     The number of books to create.
     * @param customers The number of customers to create.
     * @param addresses The number of addresses to create.
     * @param authors   The number of authors to create.
     * @param orders    The number of orders to create in each bookstore instance.
     * @return {@code true} if population was successful, {@code false} otherwise.
     */
    public static boolean populate(int items, int customers, int addresses,
            int authors, int orders) {
        try {
            return (Boolean) stateMachine.execute(new PopulateAction(random.nextLong(),
                    System.currentTimeMillis(), items, customers, addresses,
                    authors, orders));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     *
     */
    public static void checkpoint() {
        try {
            stateMachine.checkpoint();
        } catch (UmbrellaException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Defines a generic action that can be executed on a state machine.
     * This is part of the command pattern used to apply operations to the {@link Bookstore} state.
     *
     * @param <STATE> The type of the state object the action operates on.
     */
    private interface Action<STATE> {

        /**
         * Executes the action.
         *
         * @param sm The state to execute the action on.
         * @return The result of the execution.
         */
        Object executeOn(STATE sm);
    }

    /**
     * Manages the state of the application, which consists of a list of {@link Bookstore} instances.
     * It is responsible for executing actions on this state.
     */
    static class StateMachine {

        private final List<Bookstore> state;

        /**
         * Constructs a new StateMachine.
         *
         * @param object The list of {@link Bookstore} instances.
         */
        public StateMachine(final List object) {
            this.state = object;
        }

        /**
         * Executes a given action on the state.
         *
         * @param action The action to execute.
         * @return The result of the action.
         */
        Object execute(Action action) {
            return action.executeOn(getStateStream());
        }

        void checkpoint() {

        }
        
        List<Bookstore> getState() {
            return state;
        }

        Stream getStateStream() {
            return state.stream();
        }

        /**
         * Factory method to create a new StateMachine.
         *
         * @param state The {@link Bookstore} instances to manage.
         * @return A new {@link StateMachine} instance.
         */
        static StateMachine create(Bookstore... state) {
            List list = new ArrayList();
            list.addAll(Arrays.asList(state));
            return new StateMachine(list);
        }

    }

    private class UmbrellaException extends RuntimeException {

    }
    private static Random random;
    private static StateMachine stateMachine;

    static StateMachine getStateMachine() {
        return stateMachine;
    }
    
    

    /**
     * Initializes the Bookmarket system with a random seed and one or more {@link Bookstore} instances.
     *
     * @param seed  The seed for the random number generator.
     * @param state A variable number of {@link Bookstore} instances to be managed by the state machine.
     */
    public static void init(int seed, Bookstore... state) {
        random = new Random(seed);
        try {
            stateMachine = StateMachine.create(state);
        } catch (UmbrellaException e) {
            throw new RuntimeException(e);
        }
    }

    private static Stream<Bookstore> getBookstoreStream() {
        return (Stream) stateMachine.getStateStream();
    }

    /**
     * Retrieves a customer by their username.
     *
     * @param UNAME The username of the customer.
     * @return The corresponding {@link Customer} object.
     */
    public static Customer getCustomer(String UNAME) {
        return Bookstore.getCustomer(UNAME).get();
    }

    /**
     * Retrieves a customer's full name and username by their ID.
     *
     * @param c_id The customer's unique ID.
     * @return A string array containing the first name, last name, and username.
     */
    public static String[] getName(int c_id) {

        Customer customer = Bookstore.getCustomer(c_id);

        String name[] = new String[3];
        name[0] = customer.getFname();
        name[1] = customer.getLname();
        name[2] = customer.getUname();
        return name;
    }

    /**
     * Retrieves a customer's username by their ID.
     *
     * @param C_ID The customer's unique ID.
     * @return The customer's username.
     */
    public static String getUserName(int C_ID) {
        return Bookstore.getCustomer(C_ID).getUname();
    }

    /**
     * Retrieves a customer's password by their username.
     *
     * @param C_UNAME The customer's username.
     * @return The customer's password.
     */
    public static String getPassword(String C_UNAME) {
        return Bookstore.getCustomer(C_UNAME).get().getPasswd();

    }

    /**
     * Retrieves the most recent order for a given customer.
     *
     * @param c_uname The customer's username.
     * @return The most recent {@link Order} placed by the customer.
     */
    public static Order getMostRecentOrder(String c_uname) {
        return Bookstore.getCustomer(c_uname).get().getMostRecentOrder();
    }

    /**
     * Creates a new customer account.
     * This method acts as a facade, delegating the creation to the underlying {@link Bookstore}.
     *
     * @param fname       The customer's first name.
     * @param lname       The customer's last name.
     * @param street1     The first line of the address.
     * @param street2     The second line of the address.
     * @param city        The city.
     * @param state       The state.
     * @param zip         The postal code.
     * @param countryName The name of the country.
     * @param phone       The customer's phone number.
     * @param email       The customer's email address.
     * @param birthdate   The customer's date of birth.
     * @param data        Additional profile data.
     * @return The newly created {@link Customer} object.
     */
    public static Customer createNewCustomer(String fname, String lname,
            String street1, String street2, String city, String state,
            String zip, String countryName, String phone, String email,
            Date birthdate, String data) {
        double discount = (int) (Math.random() * 51);
        long now = System.currentTimeMillis();
        try {
            return (Customer) stateMachine.execute(new CreateCustomerAction(
                    fname, lname, street1, street2, city, state, zip,
                    countryName, phone, email, discount, birthdate, data, now));

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Refreshes a customer's session, extending their login time.
     *
     * @param cId The ID of the customer whose session is to be refreshed.
     */
    public static void refreshSession(int cId) {
        try {
            stateMachine.execute(new RefreshCustomerSessionAction(cId,
                    System.currentTimeMillis()));

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Retrieves a book by its unique ID.
     *
     * @param i_id The ID of the book to retrieve.
     * @return The corresponding {@link Book} object.
     */
    public static Book getBook(int i_id) {

        return Bookstore.getBook(i_id).get();

    }

    /**
     * Retrieves a randomly selected book from the catalog.
     *
     * @return A random {@link Book}.
     */
    public static Book getABookAnyBook() {
        return Bookstore.getABookAnyBook(random);

    }

    /**
     * Performs a search for books by subject.
     *
     * @param search_key The {@link SUBJECTS} to search for.
     * @return A list of books matching the subject.
     */
    public static List<Book> doSubjectSearch(SUBJECTS search_key) {
        return Bookstore.getBooksBySubject(search_key);
    }

    /**
     * Performs a search for books by title.
     *
     * @param search_key The title to search for.
     * @return A list of books with matching titles.
     */
    public static List<Book> doTitleSearch(String search_key) {
        return Bookstore.getBooksByTitle(search_key);
    }

    /**
     * Performs a search for books by author's last name.
     *
     * @param search_key The author's last name to search for.
     * @return A list of books by the specified author.
     */
    public static List<Book> doAuthorSearch(String search_key) {
        return Bookstore.getBooksByAuthor(search_key);
    }

    /**
     * Retrieves a list of the newest products for a given subject.
     *
     * @param subject The {@link SUBJECTS} to search for.
     * @return A list of the newest books in that subject.
     */
    public static List<Book> getNewProducts(SUBJECTS subject) {
        return Bookstore.getNewBooks(subject);
    }

    /**
     * Retrieves the costs of a specific book from all bookstore instances.
     *
     * @param book The book to query.
     * @return A list of costs for the book from each store.
     */
    public static List<Double> getCosts(Book book) {
        return getBookstoreStream().
                map(store -> store.getStock(book.getId())).
                map(stock -> stock.getCost()).
                collect(Collectors.toList());
    }

    /**
     * Retrieves the best-selling books for a given subject, along with their stock information.
     * The stock is ordered by cost.
     * <p><b>Note:</b> This feature is not yet implemented.</p>
     *
     * @param subject The subject to query.
     * @return Currently returns {@code null}.
     */
    public static Map<Book, Set<Stock>> getBestSellers(SUBJECTS subject) {
        // to do
        return null;
    }

    /**
     * Generates book recommendations for a customer based on their item purchase history.
     * <p><b>Note:</b> This feature is not yet implemented.</p>
     *
     * @param c_id The customer's ID.
     * @return Currently returns {@code null}.
     */
    public static List<Book> getRecommendationByItens(int c_id) {
        return Bookstore.getRecommendationByItens(c_id);
    }

    /**
     * Generates book recommendations for a customer based on the behavior of similar users.
     * <p><b>Note:</b> This feature is not yet implemented.</p>
     *
     * @param c_id The customer's ID.
     * @return Currently returns {@code null}.
     */
    public static List<Book> getRecommendationByUsers(int c_id) {
        return Bookstore.getRecommendationByUsers(c_id);
    }

    /**
     * Retrieves stock information for recommended books.
     * <p><b>Note:</b> This feature is not yet implemented.</p>
     *
     * @param c_id The customer's ID.
     * @return Currently returns {@code null}.
     */
    public static Map<Book, Set<Stock>> getStocksRecommendationByUsers(int c_id) {
        // to do
        return null;
    }

    /**
     * Retrieves price information for recommended books, adjusted for subscribers.
     * <p><b>Note:</b> This feature is not yet implemented.</p>
     *
     * @param c_id The customer's ID.
     * @return Currently returns {@code null}.
     */
    public static Map<Book, Double> getPriceBookRecommendationByUsers(int c_id) {
        // to do
        return null;
    }

    /**
     * Retrieves the stock information for a given book from all bookstore instances.
     *
     * @param idBook The ID of the book to check stock for.
     * @return A list of {@link Stock} objects from each store that has the book.
     */
    public static List<Stock> getStocks(final int idBook) {
        return getBookstoreStream()
                .filter(store -> store.getStock(idBook) != null)
                // transforma o stream de bookstore em stream de stock
                .map(store -> store.getStock(idBook))
                .collect(Collectors.toList());
    }

    /**
     * Retrieves the stock information for a book from a specific bookstore.
     *
     * @param idBookstore The ID of the bookstore to query.
     * @param idBook      The ID of the book.
     * @return The {@link Stock} object for the book in the specified store.
     */
    public static Stock getStock(final int idBookstore, final int idBook) {
        return getBookstoreStream()
                .filter(store -> store.getId() != idBookstore)
                // transforma o stream de bookstore em stream de stock
                .map(store -> store.getStock(idBook))
                .findFirst()
                .get();
    }

    /**
     * Retrieves a list of books related to a given book.
     *
     * @param i_id The ID of the book for which to find related items.
     * @return A list of up to 5 related {@link Book} objects.
     */
    public static List<Book> getRelated(int i_id) {
        Book book = Bookstore.getBook(i_id).get();
        ArrayList<Book> related = new ArrayList<>(5);
        related.add(book.getRelated1());
        related.add(book.getRelated2());
        related.add(book.getRelated3());
        related.add(book.getRelated4());
        related.add(book.getRelated5());
        return related;
    }

    /**
     * Performs an administrative update on a book's information.
     *
     * @param iId       The ID of the book to update.
     * @param cost      The new cost of the book.
     * @param image     The new image URL.
     * @param thumbnail The new thumbnail URL.
     */
    public static void adminUpdate(int iId, double cost, String image,
            String thumbnail) {
        try {
            stateMachine.execute(new UpdateBookAction(iId, cost, image,
                    thumbnail, System.currentTimeMillis()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Creates a new, empty shopping cart in a specific bookstore.
     *
     * @param storeId The ID of the bookstore where the cart will be created.
     * @return The unique ID of the newly created cart.
     */
    public static int createEmptyCart(int storeId) {
        try {
            return ((Cart) stateMachine.execute(new CreateCartAction(storeId,
                    System.currentTimeMillis()))).getId();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Updates a shopping cart by adding an item or modifying quantities.
     *
     * @param storeId     The ID of the bookstore where the cart exists.
     * @param SHOPPING_ID The ID of the shopping cart to update.
     * @param I_ID        The ID of the book to add (can be null).
     * @param ids         A list of book IDs to update quantities for.
     * @param quantities  A list of new quantities for the corresponding book IDs.
     * @return The updated {@link Cart} object.
     */
    public static Cart doCart(int storeId, int SHOPPING_ID, Integer I_ID, List<Integer> ids,
            List<Integer> quantities) {
        try {
            Cart cart = (Cart) stateMachine.execute(new CartUpdateAction(storeId,
                    SHOPPING_ID, I_ID, ids, quantities,
                    System.currentTimeMillis()));
            if (cart.getLines().isEmpty()) {
                Book book = Bookstore.getABookAnyBook(random);
                cart = (Cart) stateMachine.execute(new CartUpdateAction(storeId,
                        SHOPPING_ID, book.getId(), new ArrayList<>(),
                        new ArrayList<>(), System.currentTimeMillis()));
            }
            return cart;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Retrieves a shopping cart from a specific bookstore.
     *
     * @param storeId     The ID of the bookstore.
     * @param SHOPPING_ID The ID of the cart to retrieve.
     * @return The {@link Cart} object.
     */
    public static Cart getCart(int storeId, int SHOPPING_ID) {
        Bookstore bookstore = getBookstoreStream()
                .filter(store -> store.getId() == storeId)
                .findFirst()
                .get();
        synchronized (bookstore) {
            return bookstore.getCart(SHOPPING_ID);
        }
    }

    /**
     * Confirms a purchase using the customer's default address.
     *
     * @param storeId     The ID of the bookstore where the purchase is made.
     * @param shopping_id The ID of the shopping cart.
     * @param customer_id The ID of the customer.
     * @param cc_type     The credit card type.
     * @param cc_number   The credit card number.
     * @param cc_name     The name on the credit card.
     * @param cc_expiry   The credit card expiration date.
     * @param shipping    The shipping method.
     * @return The newly created {@link Order} object.
     */
    public static Order doBuyConfirm(int storeId, int shopping_id, int customer_id,
            CreditCards cc_type, long cc_number, String cc_name, Date cc_expiry,
            ShipTypes shipping) {
        long now = System.currentTimeMillis();
        try {
            return (Order) stateMachine.execute(new ConfirmBuyAction(storeId,
                    customer_id, shopping_id, randomComment(),
                    cc_type, cc_number, cc_name, cc_expiry, shipping,
                    randomShippingDate(now), -1, now));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Confirms a purchase using a specified shipping address.
     *
     * @param storeId     The ID of the bookstore where the purchase is made.
     * @param shopping_id The ID of the shopping cart.
     * @param customer_id The ID of the customer.
     * @param cc_type     The credit card type.
     * @param cc_number   The credit card number.
     * @param cc_name     The name on the credit card.
     * @param cc_expiry   The credit card expiration date.
     * @param shipping    The shipping method.
     * @param street_1    The first line of the shipping address.
     * @param street_2    The second line of the shipping address.
     * @param city        The city of the shipping address.
     * @param state       The state of the shipping address.
     * @param zip         The postal code of the shipping address.
     * @param country     The country of the shipping-address.
     * @return The newly created {@link Order} object.
     */
    public static Order doBuyConfirm(int storeId, int shopping_id, int customer_id,
            CreditCards cc_type, long cc_number, String cc_name, Date cc_expiry,
            ShipTypes shipping, String street_1, String street_2, String city,
            String state, String zip, String country) {
        Address address = Bookstore.alwaysGetAddress(street_1, street_2,
                city, state, zip, country);
        long now = System.currentTimeMillis();
        try {
            return (Order) stateMachine.execute(new ConfirmBuyAction(storeId,
                    customer_id, shopping_id, randomComment(),
                    cc_type, cc_number, cc_name, cc_expiry, shipping,
                    randomShippingDate(now), address.getId(), now));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static String randomComment() {
        return TPCW_Util.getRandomString(random, 20, 100);
    }

    private static Date randomShippingDate(long now) {
        return new Date(now + 86400000 /* a day */ * (random.nextInt(7) + 1));
    }

    /**
     * Populates the database with a large set of randomly generated data.
     * This is part of the TPC-W benchmark setup.
     *
     * @param items     The number of books to create.
     * @param customers The number of customers to create.
     * @param addresses The number of addresses to create.
     * @param authors   The number of authors to create.
     * @param orders    The number of orders to create in each bookstore instance.
     * @return {@code true} if population was successful, {@code false} otherwise.
     */
    public static boolean populate(int items, int customers, int addresses,
            int authors, int orders) {
        try {
            return (Boolean) stateMachine.execute(new PopulateAction(random.nextLong(),
                    System.currentTimeMillis(), items, customers, addresses,
                    authors, orders));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Checkpoints the current state of the state machine.
     * <p><b>Note:</b> This feature is not yet implemented.</p>
     */
    public static void checkpoint() {
        try {
            stateMachine.checkpoint();
        } catch (UmbrellaException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Abstract base class for all actions that operate on {@link Bookstore} instances.
     */
    protected static abstract class BookstoreAction implements Action<Stream<Bookstore>>,
            Serializable {

        /**
         * Executes the action on a stream of {@link Bookstore} instances.
         *
         * @param sm The stream of bookstores.
         * @return The result of the execution.
         */
        @Override
        public Object executeOn(Stream<Bookstore> sm) {
            return executeOnBookstore(sm);
        }

        /**
         * The core logic of the action to be implemented by subclasses.
         *
         * @param bookstore The stream of {@link Bookstore} instances.
         * @return The result of the action.
         */
        public abstract Object executeOnBookstore(Stream<Bookstore> bookstore);
    }

    /**
     * An action to create a new customer.
     */
    protected static class CreateCustomerAction extends BookstoreAction {

        private static final long serialVersionUID = 6039962163348790677L;

        String fname;
        String lname;
        String street1;
        String street2;
        String city;
        String state;
        String zip;
        String countryName;
        String phone;
        String email;
        double discount;
        Date birthdate;
        String data;
        long now;

        /**
         * Constructs a new CreateCustomerAction.
         *
         * @param fname       First name.
         * @param lname       Last name.
         * @param street1     Address line 1.
         * @param street2     Address line 2.
         * @param city        City.
         * @param state       State.
         * @param zip         Postal code.
         * @param countryName Country.
         * @param phone       Phone number.
         * @param email       Email address.
         * @param discount    Discount rate.
         * @param birthdate   Date of birth.
         * @param data        Additional data.
         * @param now         Timestamp of creation.
         */
        public CreateCustomerAction(String fname, String lname, String street1,
                String street2, String city, String state, String zip,
                String countryName, String phone, String email,
                double discount, Date birthdate, String data, long now) {
            this.fname = fname;
            this.lname = lname;
            this.street1 = street1;
            this.street2 = street2;
            this.city = city;
            this.state = state;
            this.zip = zip;
            this.countryName = countryName;
            this.phone = phone;
            this.email = email;
            this.discount = discount;
            this.birthdate = birthdate;
            this.data = data;
            this.now = now;
        }

        /**
         * Executes the customer creation logic.
         *
         * @param bookstore The stream of bookstores.
         * @return The new {@link Customer}.
         */
        @Override
        public Object executeOnBookstore(Stream<Bookstore> bookstore) {
            return Bookstore.createCustomer(fname, lname, street1, street2,
                    city, state, zip, countryName, phone, email, discount,
                    birthdate, data, now);
        }
    }

    /**
     * An action to refresh a customer's session.
     */
    protected static class RefreshCustomerSessionAction extends BookstoreAction {

        private static final long serialVersionUID = -5391031909452321478L;

        int cId;
        long now;

        /**
         * Constructs a new RefreshCustomerSessionAction.
         *
         * @param id  The customer ID.
         * @param now The current timestamp.
         */
        public RefreshCustomerSessionAction(int id, long now) {
            cId = id;
            this.now = now;
        }

        /**
         * Executes the session refresh logic.
         *
         * @param bookstore The stream of bookstores.
         * @return Always returns {@code null}.
         */
        @Override
        public Object executeOnBookstore(Stream<Bookstore> bookstore) {
            Bookstore.refreshCustomerSession(cId, now);
            return null;
        }
    }

    /**
     * An action to update a book's information.
     */
    protected static class UpdateBookAction extends BookstoreAction {

        private static final long serialVersionUID = -745697943594643776L;

        int bId;
        double cost;
        String image;
        String thumbnail;
        long now;

        /**
         * Constructs a new UpdateBookAction.
         *
         * @param id        The book ID.
         * @param cost      The new cost.
         * @param image     The new image URL.
         * @param thumbnail The new thumbnail URL.
         * @param now       The current timestamp.
         */
        public UpdateBookAction(int id, double cost, String image,
                String thumbnail, long now) {
            bId = id;
            this.cost = cost;
            this.image = image;
            this.thumbnail = thumbnail;
            this.now = now;
        }

        /**
         * Executes the book update logic.
         *
         * @param bookstore The stream of bookstores.
         * @return Always returns {@code null}.
         */
        @Override
        public Object executeOnBookstore(Stream<Bookstore> bookstore) {
            Bookstore.updateBook(bId, image, thumbnail, now);
            return null;
        }
    }

    /**
     * An action to create an empty shopping cart.
     */
    protected static class CreateCartAction extends BookstoreAction {

        private static final long serialVersionUID = 8255648428785854052L;

        long now, storeId;

        /**
         * Constructs a new CreateCartAction.
         *
         * @param idBookstore The ID of the store where the cart is created.
         * @param now         The current timestamp.
         */
        public CreateCartAction(int idBookstore, long now) {
            this.now = now;
            this.storeId = idBookstore;
        }

        /**
         * Executes the cart creation logic.
         *
         * @param bookstore The stream of bookstores.
         * @return The new {@link Cart}.
         */
        @Override
        public Object executeOnBookstore(Stream<Bookstore> bookstore) {
            return bookstore.filter(bs -> bs.getId() == this.storeId).findFirst().get().createCart(now);
        }
    }

    /**
     * An action to update a shopping cart.
     */
    protected static class CartUpdateAction extends BookstoreAction {

        private static final long serialVersionUID = -6062032194650262105L;

        final int cId, storeId;
        final Integer bId;
        final List<Integer> bIds;
        final List<Integer> quantities;
        final long now;

        /**
         * Constructs a new CartUpdateAction.
         *
         * @param storeId    The ID of the store.
         * @param id         The cart ID.
         * @param id2        The ID of a book to add.
         * @param ids        A list of book IDs to update.
         * @param quantities A list of new quantities.
         * @param now        The current timestamp.
         */
        public CartUpdateAction(int storeId, int id, Integer id2, List<Integer> ids,
                List<Integer> quantities, long now) {
            this.storeId = storeId;
            cId = id;
            bId = id2;
            bIds = ids;
            this.quantities = quantities;
            this.now = now;
        }

        /**
         * Executes the cart update logic.
         *
         * @param bookstore The stream of bookstores.
         * @return The updated {@link Cart}.
         */
        @Override
        public Object executeOnBookstore(Stream<Bookstore> bookstore) {
            return bookstore.filter(bs -> bs.getId() == this.storeId).findFirst().get().cartUpdate(cId, bId, bIds, quantities, now);
        }
    }

    /**
     * An action to confirm a purchase and create an order.
     */
    protected static class ConfirmBuyAction extends BookstoreAction {

        private static final long serialVersionUID = -6180290851118139002L;

        final int customerId, storeId, cartId;
        String comment;
        CreditCards ccType;
        long ccNumber;
        String ccName;
        Date ccExpiry;
        ShipTypes shipping;
        Date shippingDate;
        int addressId;
        long now;

        /**
         * Constructs a new ConfirmBuyAction.
         *
         * @param storeId      The store ID.
         * @param customerId   The customer ID.
         * @param cartId       The cart ID.
         * @param comment      An order comment.
         * @param ccType       Credit card type.
         * @param ccNumber     Credit card number.
         * @param ccName       Name on credit card.
         * @param ccExpiry     Credit card expiration.
         * @param shipping     Shipping method.
         * @param shippingDate Estimated shipping date.
         * @param addressId    Shipping address ID.
         * @param now          Current timestamp.
         */
        public ConfirmBuyAction(int storeId, int customerId, int cartId,
                String comment, CreditCards ccType, long ccNumber,
                String ccName, Date ccExpiry, ShipTypes shipping,
                Date shippingDate, int addressId, long now) {
            this.storeId = storeId;
            this.customerId = customerId;
            this.cartId = cartId;
            this.comment = comment;
            this.ccType = ccType;
            this.ccNumber = ccNumber;
            this.ccName = ccName;
            this.ccExpiry = ccExpiry;
            this.shipping = shipping;
            this.shippingDate = shippingDate;
            this.addressId = addressId;
            this.now = now;
        }

        /**
         * Executes the purchase confirmation logic.
         *
         * @param bookstore The stream of bookstores.
         * @return The new {@link Order}.
         */
        @Override
        public Object executeOnBookstore(Stream<Bookstore> bookstore) {
            return bookstore.filter(bs -> bs.getId() == this.storeId).findFirst().get().confirmBuy(customerId, cartId, comment, ccType,
                    ccNumber, ccName, ccExpiry, shipping, shippingDate,
                    addressId, now);
        }
    }

    /**
     * An action to populate the database with benchmark data.
     */
    protected static class PopulateAction extends BookstoreAction {

        private static final long serialVersionUID = -5240430799502573886L;

        long seed;
        long now;
        int items;
        int customers;
        int addresses;
        int authors;
        int orders;

        /**
         * Constructs a new PopulateAction.
         *
         * @param seed      The random seed.
         * @param now       The current timestamp.
         * @param items     Number of books.
         * @param customers Number of customers.
         * @param addresses Number of addresses.
         * @param authors   Number of authors.
         * @param orders    Number of orders per store.
         */
        public PopulateAction(long seed, long now, int items, int customers,
                int addresses, int authors, int orders) {
            this.seed = seed;
            this.now = now;
            this.items = items;
            this.customers = customers;
            this.addresses = addresses;
            this.authors = authors;
            this.orders = orders;
        }

        /**
         * Executes the database population logic.
         *
         * @param bookstore The stream of bookstores.
         * @return Always returns {@code true}.
         */
        @Override
        public Object executeOnBookstore(Stream<Bookstore> bookstore) {
            Bookstore.populate(seed, now, items, customers, addresses, authors);
            Random rand = new Random(seed);
            bookstore.forEach(instance -> instance.populateInstanceBookstore(orders, rand, now));
            return true;
        }
    }

}
