package servico;

import java.util.*;

import dominio.*;
import org.junit.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import recommendation.RecommendationSettings;
import util.TPCW_Util;

import java.util.stream.Collectors;
import java.util.stream.Stream;
import static org.junit.Assert.*;

/**
 *
 * @author INF329
 */
public class BookmarketTest {

    long seed = 0;
    Bookstore[] bookstores;

    private Book topSellerBook;
    private Book secondSellerBook;

    public BookmarketTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    /**
     * É necessário popular a Bookstore para realização dos testes.
     *
     * Criamos 5 Bookstores dentro do Bookmarket para validar o bestseller,
     * populando cada instância do Bookstore com dados diferentes, garantindo
     * assim o funcionamento correto dos métodos do bestseller.
     */
    public void setUp() {
        long now = System.currentTimeMillis();

        int items = 500;
        int customers = 1000;
        int addresses = 1000;
        int authors = 1000;
        int orders = 1000;
        int stocks = 1000;
        int evaluations = 100;

        Bookstore.populate(seed, now, items, customers, addresses, authors);

        bookstores = new Bookstore[5];

        for (int i = 0; i < 5; i++) {
            Bookstore bookstore = new Bookstore(i);
            bookstore.createCart(0);

            /**
             * Adicionamos este loop para adicionar todos os livros ao estoque
             * com preço 1 desta forma prevenimos exceptions em métodos que
             * adiciona livros aleatoriamente no carrinho
             */
            for (int j = 0; j < items; j++) {
                bookstore.updateStock(j, 1);
            }

            bookstores[i] = bookstore;
        }

        RecommendationSettings recommendationSettings = new RecommendationSettings();

        Bookmarket.init(0, recommendationSettings, bookstores);
        Bookmarket.populate(items, customers, addresses, authors, orders, stocks, evaluations);

        // Create a predictable sales scenario for best-seller testing
        List<Book> artBooks = Bookmarket.doSubjectSearch(SUBJECTS.ARTS);
        topSellerBook = artBooks.get(0);
        secondSellerBook = artBooks.get(1);

        Customer customer = Bookstore.getCustomer(1);
        int cartId = Bookmarket.createEmptyCart(bookstores[0].getId());
        Cart cart = Bookmarket.getCart(bookstores[0].getId(), cartId);
        cart.increaseLine(bookstores[0].getStock(topSellerBook.getId()), 1000000);
        cart.increaseLine(bookstores[0].getStock(secondSellerBook.getId()), 50);

        Bookmarket.doBuyConfirm(bookstores[0].getId(), cartId, customer.getId(), CreditCards.AMEX, 123456789012345L, "Test User", new Date(), ShipTypes.AIR, StatusTypes.SHIPPED);
    }

    /**
     * Test of getBestSellers method, of class Bookmarket.
     */
    @Test
    public void testGetBestSellers() {
        SUBJECTS subject = SUBJECTS.ARTS;
        int limit = 10;
        Map<Book, Set<Stock>> result = Bookmarket.getBestSellers(subject, limit);

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertTrue(result.size() <= limit);

        // Check the first book is the one we sold the most
        Book firstBook = result.keySet().iterator().next();
        assertEquals(topSellerBook, firstBook);

        // Check the stocks for the first book
        Set<Stock> stocks = result.get(firstBook);
        assertNotNull(stocks);
        assertFalse(stocks.isEmpty());

        // Verify that stocks are sorted by cost
        double lastCost = -1;
        for (Stock stock : stocks) {
            assertTrue(stock.getCost() >= lastCost);
            lastCost = stock.getCost();
        }
    }

    @Test
    public void testCreateEvaluation() {
        System.out.println("testCreateEvaluation");

        Random rand = new Random(seed);

        int storeId = bookstores[0].getId();
        Customer customer = Bookstore.getCustomer(1);
        Book book = Bookstore.getABookAnyBook(rand);
        double rating = TPCW_Util.getRandomDouble(rand, 0, 4);

        Evaluation eval = Bookmarket.createEvaluation(storeId, customer.getId(), book.getId(), rating);

        assertNotNull(eval);
        assertEquals( customer, eval.getCustomer() );
        assertEquals( book, eval.getBook() );
        assertEquals( rating, eval.getRating(), 0.01 );
    }

    @Test
    public void testUpdateEvaluation() {
        System.out.println("testUpdateEvaluation");

        Random rand = new Random(seed);

        Optional<Evaluation> eval = bookstores[0].getEvaluation(0);
        assertNotNull(eval);

        int id = eval.get().getId();
        double rating = TPCW_Util.getRandomDouble(rand, 0, 4);

        Evaluation eval2 = Bookmarket.updateEvaluation(bookstores[0].getId(), id, rating);
        assertNotNull(eval2);
        assertEquals( eval.get(), eval2 );
        assertEquals( eval.get().getCustomer(), eval2.getCustomer() );
        assertEquals( eval.get().getBook(), eval2.getBook() );
        assertEquals( rating, eval2.getRating(), 0.01 );

    }

    /**
     * Test of getPriceBookRecommendationByUsers method, of class Bookmarket.
     */
    @Test
    public void testGetPriceBookRecommendationByUsers() {
        System.out.println("getPriceBookRecommendationByUsers");

    }

    /**
     * Test of getPriceBookRecommendationByUsers method, of class Bookmarket.
     */
    @Test
    public void testGetStocksRecommendationByUsers() {
        System.out.println("getStocksRecommendationByUsers");
    }

    // createNewCustomer
    // getName
    // getUserName
    // getPassword
    // getCustomer
    // getMostRecentOrder
    @Test
    public void testCustomerOperations() {
        Calendar.getInstance().set(2000, Calendar.NOVEMBER, 16, 0, 0, 0);
        Date birthdate = Calendar.getInstance().getTime();

        Customer customer = Bookmarket.createNewCustomer(
                "First", "Last", "Street1", "Street2", "City",
                "State", "123456-999", "Country", "11-1234-5678", "e@e.com",
                birthdate, "Data");

        assertNotNull("Customer should be not null", customer);

        String[] name = Bookmarket.getName(customer.getId());
        assertNotNull("Customer name should be not null", name);
        assertEquals("Customer name string should have length of 3", 3, name.length);
        assertFalse("Customer first name should not be empty", name[0].isEmpty());
        assertEquals("Customer first name should match", "First", name[0]);
        assertFalse("Customer middle name should not be empty", name[1].isEmpty());
        assertEquals("Customer middle name should match", "Last", name[1]);
        assertFalse("Customer last name should not be empty", name[2].isEmpty());
        assertEquals("Customer last name should match", "OGBABABA", name[2]);

        String username = Bookmarket.getUserName(customer.getId());
        assertNotNull("Username should not be null", username);
        assertFalse("Username should not be empty", username.isEmpty());

        String password = Bookmarket.getPassword(username);
        assertNotNull("Password should not be null", password);
        assertFalse("Password should not be empty", password.isEmpty());

        Customer other = Bookmarket.getCustomer(username);
        assertNotNull("Other customer should not be null", other);
        assertEquals("Customers should be equal", customer, other);

        Order nullorder = Bookmarket.getMostRecentOrder(username);
        assertNull("Most recent order should be null", nullorder);

        Date now = new Date();
        Cart cart = bookstores[0].createCart(new Date().getTime());
        Address address = customer.getAddress();

        Order order = bookstores[0].confirmBuy(
                customer.getId(), cart.getId(), "Comment", CreditCards.VISA, 4444L,
                "FIRST LAST", now, ShipTypes.AIR, now, address.getId(), now.getTime());

        Order recentOrder = Bookmarket.getMostRecentOrder(username);
        assertNotNull("Most recent order should not be null", recentOrder);
        assertEquals("", order, recentOrder);
    }

    // getBook
    // getABookAnyBook
    // doSubjectSearch
    // doTitleSearch
    // doAuthorSearch
    // getNewProducts
    // getCosts
    // getBestSellers
    // getRecommendationByItems
    // getRecommendationByUsers
    // getStockRecommendationByUsers
    // getStocks
    // getStock
    // getRelated
    // adminUpdate
    // createEmptyCart
    // doCart
    // getCart
    // doBuyConfirm
    // createEvaluation
    // updateEvaluation

}
