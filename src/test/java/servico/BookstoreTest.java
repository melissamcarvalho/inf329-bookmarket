package servico;

import java.util.*;
import java.util.regex.Pattern;

import dominio.*;
import static org.junit.Assert.assertEquals;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 *
 * @author INF329
 */
public class BookstoreTest {

    static Bookstore instance;

    @BeforeClass
    public static void setUpClass() {
        
        long seed = 0;
        long now = System.currentTimeMillis();
        int items = 1000;
        int customers = 100;
        int addresses = 1000;
        int authors = 100;
        int orders = 10000;
        int stocks = 10000;
        int evaluations = 10000;
        Random rand = new Random(seed);
        Bookstore.populate(seed, now, items, customers, addresses, authors);
        instance = new Bookstore(0);
        instance.populateInstanceBookstore(orders, stocks, evaluations, rand, now);
    }

    /**
     * Test of getBestSellers method, of class Bookstore.
     */
    @Test
    public void testGetBestSellers() {
        System.out.println("getBestSellers");
        SUBJECTS subject = SUBJECTS.ARTS;

        // 1. Get baseline sales
        Map<Book, Integer> baselineSales = instance.getBestSellers(subject);

        // 2. Create a new sale
        List<Book> artBooks = Bookstore.getBooksBySubject(subject);
        Book book1 = artBooks.get(0);
        Book book2 = artBooks.get(1);

        int baselineCount1 = baselineSales.getOrDefault(book1, 0);
        int baselineCount2 = baselineSales.getOrDefault(book2, 0);

        int qty1 = 100;
        int qty2 = 50;

        Customer customer = Bookstore.getCustomer(1);
        Cart cart = instance.createCart(System.currentTimeMillis());
        cart.increaseLine(instance.getStock(book1.getId()), qty1);
        cart.increaseLine(instance.getStock(book2.getId()), qty2);

        instance.confirmBuy(customer.getId(), cart.getId(), "Test comment",
                CreditCards.VISA, 1234567890123456L, "Test Customer", new Date(),
                ShipTypes.AIR, new Date(), customer.getAddress().getId(),
                System.currentTimeMillis(), StatusTypes.SHIPPED);

        // 3. Get new sales and assert
        Map<Book, Integer> newSales = instance.getBestSellers(subject);

        int newCount1 = newSales.getOrDefault(book1, 0);
        int newCount2 = newSales.getOrDefault(book2, 0);

        assertEquals(baselineCount1 + qty1, newCount1);
        assertEquals(baselineCount2 + qty2, newCount2);
    }

    /**
     * Test of isPopulated method, of class Bookstore.
     */
    @Test
    public void testIsPopulated() {
        System.out.println("isPopulated");
        boolean expResult = true;
        boolean result = instance.isPopulated();
        assertEquals(expResult, result);
    }

    /**
     * Test of alwaysGetAddress method, of class Bookstore.
     */
    @Test
    public void testAlwaysGetAddress() {
        System.out.println("alwaysGetAddress");
        String street1 = "";
        String street2 = "";
        String city = "";
        String state = "";
        String zip = "";
        String countryName = "";
        Address result = Bookstore.alwaysGetAddress(street1, street2, city, state, zip, countryName);
        Address expResult = new Address(0, street1, street2, city, state, zip, result.getCountry());
        assertEquals(expResult, result);

    }

    /**
     * Test of getCustomer method, of class Bookstore.
     */
    @Test
    public void testGetCustomer_int() {
        System.out.println("getCustomer");
        int cId = 0;
        Customer result = Bookstore.getCustomer(cId);
        assertEquals(cId, result.getId());
    }

    /**
     * Test of getCustomer method, of class Bookstore.
     */
    @Test
    public void testGetCustomer_String() {
        System.out.println("getCustomer");
        String username = Bookstore.getCustomer(10).getUname();
        Customer result = Bookstore.getCustomer(username).get();
        assertNotNull(result);
        assertEquals(username, result.getUname());
    }

    /**
     * Test of createCustomer method, of class Bookstore.
     */
    @Test
    public void testCreateCustomer() {
        System.out.println("createCustomer");
        String fname = "";
        String lname = "";
        String street1 = "";
        String street2 = "";
        String city = "";
        String state = "";
        String zip = "";
        String countryName = "";
        String phone = "";
        String email = "";
        double discount = 0.0;
        Date birthdate = null;
        String data = "";
        long now = 0L;

        Customer result = Bookstore.createCustomer(fname, lname, street1, street2, city, state, zip, countryName, phone, email, discount, birthdate, data, now);
        int id = result.getId();
        String uname = result.getUname();
        Date since = result.getSince();
        Date lastVisit = result.getLastVisit();
        Date login = result.getLogin();
        Date expiration = result.getExpiration();
        Address address = result.getAddress();
        Customer expResult = new Customer(id, uname, uname.toLowerCase(), fname,
                lname, phone, email, since, lastVisit, login, expiration,
                discount, 0, 0, birthdate, data, address);
        assertEquals(expResult, result);

    }

    /**
     * Test of refreshCustomerSession method, of class Bookstore.
     */
    @Test
    public void testRefreshCustomerSession() {
        System.out.println("refreshCustomerSession");
        int cId = 0;
        long now = 0L;
        Bookstore.refreshCustomerSession(cId, now);
    }

    /**
     * Test of getBook method, of class Bookstore.
     */
    @Test
    public void testGetBook() {
        System.out.println("getBook");
        int bId = 0;
        Book result = Bookstore.getBook(bId).get();
        assertNotNull(result);
        assertEquals(bId, result.getId());
    }

    /**
     * Test of getBooksBySubject method, of class Bookstore.
     */
    @Test
    public void testGetBooksBySubject() {
        System.out.println("getBooksBySubject");
        SUBJECTS subject = SUBJECTS.ARTS;
        List<Book> result = Bookstore.getBooksBySubject(subject);
        assertEquals(result.size(), result.stream().filter(book -> book.getSubject().equals(subject)).count());

    }

    /**
     * Test of getBooksByTitle method, of class Bookstore.
     */
    @Test
    public void testGetBooksByTitle() {
        System.out.println("getBooksByTitle");
        Book book = Bookstore.getBook(0).get();
        assertNotNull(book);

        String search = book.getTitle().substring(0, 4);
        Pattern regex = Pattern.compile(search, Pattern.CASE_INSENSITIVE);

        List<Book> result = Bookstore.getBooksByTitle(search);
        assertTrue("All returned titles must match the search regex",
                result.stream().allMatch(pred -> regex.matcher(pred.getTitle()).find()));
    }

    /**
     * Test of getBooksByAuthor method, of class Bookstore.
     */
    @Test
    public void testGetBooksByAuthor() {
        System.out.println("getBooksByAuthor");
        Book book = Bookstore.getBook(0).get();
        assertNotNull(book);

        Author author = book.getAuthor();
        List<Book> result = Bookstore.getBooksByAuthor(author.getLname());
        Pattern regex = Pattern.compile(author.getLname(), Pattern.CASE_INSENSITIVE);

        assertTrue("All returned titles must match the search regex",
                result.stream().allMatch(pred -> regex.matcher(pred.getAuthor().getLname()).find()));
    }

    /**
     * Test of getNewBooks method, of class Bookstore.
     */
    @Test
    public void testGetNewBooks() {
        System.out.println("getNewBooks");
        SUBJECTS subject = Bookstore.getBook(0).get().getSubject();
        List<Book> result = Bookstore.getNewBooks(subject);
        assertEquals(result.size(),
                result.stream().filter(book -> book.getSubject().equals(subject)).count());

    }

    /**
     * Test of updateBook method, of class Bookstore.
     */
    @Test
    public void testUpdateBook() {
        System.out.println("updateBook");
        int bId = 0;
        double cost = 0.0;
        String image = "";
        String thumbnail = "";
        long now = 0L;
        Book book = Bookstore.getBook(bId).get();
        Bookstore.updateBook(bId, image, thumbnail, now);
        assertEquals(bId, book.getId());
        //assertEquals(cost, book.getCost(), 0.0);
        assertEquals(image, book.getImage());
        assertEquals(thumbnail, book.getThumbnail());
    }

    @Test
    public void customerRecommendation() {
        List<Book> recommendations = Bookstore.getRecommendationByUsers(79);
        assertEquals(10, recommendations.size());
    }

    @Test
    public void debugRecommendation() {
        Map<Book, List<Customer>> booksBuyers = new HashMap<>();
        instance.getOrdersById().forEach((order) -> {
            order.getLines().forEach((line) -> {
                Book book = line.getBook();
                if(!booksBuyers.containsKey(book)) {
                    booksBuyers.put(book, new ArrayList<>());
                }

                List<Customer> buyers = booksBuyers.get(book);
                Customer customer = order.getCustomer();
                if(!buyers.contains(customer)) {
                    buyers.add(customer);
                }
            });
        });

        Book randomBook = booksBuyers.keySet().stream().findFirst().get();
        List<Customer> buyers = booksBuyers.get(randomBook);

        Map<Customer, List<Book>> recommendedBooks = new HashMap<>();
        buyers.forEach(buyer -> {
            List<Book> recommendations = Bookstore.getRecommendationByUsers(buyer.getId());
            recommendedBooks.put(buyer, recommendations);
        });

        recommendedBooks.clear();
    }




}
