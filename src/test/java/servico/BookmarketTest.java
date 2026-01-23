package servico;

import java.util.*;

import dominio.*;
import org.junit.*;

import recommendation.RecommendationSettings;
import util.TPCW_Util;

import static org.junit.Assert.*;

/**
 *
 * @author INF329
 */
public class BookmarketTest {

    static long seed = 0;
    static Bookstore[] bookstores;

    private static Book topSellerBook;
    private static Book secondSellerBook;

    /**
     * É necessário popular a Bookstore para realização dos testes.
     *
     * Criamos 5 Bookstores dentro do Bookmarket para validar o bestseller,
     * populando cada instância do Bookstore com dados diferentes, garantindo
     * assim o funcionamento correto dos métodos do bestseller.
     */
    @BeforeClass
    public static void setUp() {
        long now = System.currentTimeMillis();

        int items = 500;
        int customers = 1000;
        int addresses = 1000;
        int authors = 1000;
        int orders = 1000;
        int stocks = 1000;
        int evaluations = 1000;

//        Bookstore.populate(seed, now, items, customers, addresses, authors);

        bookstores = new Bookstore[5];

        for (int i = 0; i < 5; i++) {
            Bookstore bookstore = new Bookstore(i);
//            bookstore.createCart(0);
//
//            /**
//             * Adicionamos este loop para adicionar todos os livros ao estoque
//             * com preço 1 desta forma prevenimos exceptions em métodos que
//             * adiciona livros aleatoriamente no carrinho
//             */
//            for (int j = 0; j < items; j++) {
//                bookstore.updateStock(j, 1);
//            }

            bookstores[i] = bookstore;
        }

        RecommendationSettings recommendationSettings = new RecommendationSettings();

        Bookmarket.init(0, recommendationSettings, bookstores);
        Bookmarket.populate(items, customers, addresses, authors, orders, stocks, evaluations);

        // Create a predictable sales scenario for bestseller testing
        List<Book> artBooks = Bookmarket.doSubjectSearch(SUBJECTS.ARTS);
        topSellerBook = artBooks.get(0);
        secondSellerBook = artBooks.get(1);

        Customer customer = Bookstore.getCustomer(1).orElseThrow(() -> new RuntimeException("Customer ID not found"));
        int cartId = Bookmarket.createEmptyCart(bookstores[0].getId());
        Cart cart = Bookmarket.getCart(bookstores[0].getId(), cartId);

        Stock topSellerStock = bookstores[0].getStock(topSellerBook.getId());
        topSellerStock.setQty(1000000);

        Stock secondSellerStock = bookstores[0].getStock(secondSellerBook.getId());
        secondSellerStock.setQty(90000);

        int currentStockTop = bookstores[0].getStock(topSellerBook.getId()).getQty();
        cart.increaseLine(bookstores[0].getStock(topSellerBook.getId()), currentStockTop);

        int currentStockSec = bookstores[0].getStock(secondSellerBook.getId()).getQty();
        cart.increaseLine(bookstores[0].getStock(secondSellerBook.getId()), currentStockSec);

        Bookmarket.doBuyConfirm(
                bookstores[0].getId(), cartId, customer.getId(), CreditCards.AMEX,
                new long[]{1111, 2222, 3333, 4444}, "Test User",
                new Date(), ShipTypes.AIR, StatusTypes.SHIPPED);
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
        List<Book> bestsellerList = new ArrayList<>(result.keySet());
        Book firstBook = bestsellerList.get(0);
        assertEquals(topSellerBook, firstBook);

        Book secondBook = bestsellerList.get(1);
        assertEquals(secondSellerBook, secondBook);

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
        Random rand = new Random(seed);

        int storeId = bookstores[0].getId();
        Customer customer = Bookstore.getCustomer(1)
                .orElseThrow(() -> new RuntimeException("Customer ID not found"));
        Book book = Bookstore.getABookAnyBook(rand);
        double rating = TPCW_Util.getRandomDouble(rand, 0, 4);

        Evaluation eval = Bookmarket.createEvaluation(storeId, customer.getId(), book.getId(), rating);

        assertNotNull(eval);
        assertEquals( customer, eval.getCustomer() );
        assertEquals( book, eval.getBook() );
        assertEquals( rating, eval.getRating(), 0.01 );
    }

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
        assertEquals("Customer name string array should have length of 2", 2, name.length);
        assertFalse("Customer first name should not be empty", name[0].isEmpty());
        assertEquals("Customer first name should match", "First", name[0]);
        assertFalse("Customer middle name should not be empty", name[1].isEmpty());
        assertEquals("Customer middle name should match", "Last", name[1]);

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
    }

    @Test
    public void testOrderSequence() {
        Calendar.getInstance().set(2000, Calendar.NOVEMBER, 16, 0, 0, 0);
        Date birthdate = Calendar.getInstance().getTime();

        Customer customer = Bookmarket.createNewCustomer(
                "First", "Last", "Street1", "Street2", "City",
                "State", "123456-999", "Country", "11-1234-5678", "e@e.com",
                birthdate, "Data");

        assertNotNull("Customer should be not null", customer);

        Date now = new Date();
        int cartId = Bookmarket.createEmptyCart(0);
        assertTrue("Cart ID should be greater than 0", cartId > 0);

        Cart cart = Bookmarket.getCart(0, cartId);
        assertNotNull("Cart should not be null", cart);
        assertEquals("Carts should be equal", cart.getId(), cartId);

        List<CartLine> cartLines = new ArrayList<>(cart.getLines());
        assertTrue("Cart should be empty at creation", cartLines.isEmpty());

        Book randomBook = Bookmarket.getABookAnyBook();
        HashMap<Integer, Integer> bookQtyMap = new HashMap<>();
        bookQtyMap.put(randomBook.getId(), 1);

        Bookmarket.doCart(0, cartId, bookQtyMap);

        cartLines = new ArrayList<>(cart.getLines());
        assertFalse("Cart should be empty at creation", cartLines.isEmpty());
        assertTrue("Cart should have the random book in it",
                cartLines.stream().anyMatch(pred -> pred.getBook().equals(randomBook)));

        Order order = Bookmarket.doBuyConfirm(0, cartId, customer.getId(), CreditCards.VISA,
                new long[]{1111, 2222, 3333, 4444},
                "FIRST LAST", now, ShipTypes.AIR);

        String username = Bookmarket.getUserName(customer.getId());
        Order recentOrder = Bookmarket.getMostRecentOrder(username);
        assertNotNull("Most recent order should not be null", recentOrder);
        assertEquals("", order, recentOrder);
    }

    @Test
    public void testGetBook() {
        Book randomBook = Bookmarket.getABookAnyBook();
        assertNotNull("Book should not be null", randomBook);

        Bookmarket.adminUpdate(randomBook.getId(), 1234.0, "image.png", "thumbnail.png");

        Book anotherBook = Bookmarket.getBook(randomBook.getId());
        assertNotNull("Book should not be null", anotherBook);
        assertEquals("Books should be equal", anotherBook, randomBook);
        assertEquals("Book cost should have been updated", 1234.0, randomBook.getSrp(), 0.001);
        assertEquals("Book image should have been updated", "image.png", randomBook.getImage());
        assertEquals("Book thumbnail should have been updated", "thumbnail.png", randomBook.getThumbnail());
        assertThrows("getBook should throw with invalid id",
                Exception.class, () -> Bookmarket.getBook(-1));
    }

    @Test
    public void testSubjectSearch() {
        Book randomBook = Bookmarket.getABookAnyBook();
        assertNotNull("Book should not be null", randomBook);

        List<Book> subjectSearch = Bookmarket.doSubjectSearch(randomBook.getSubject());
        assertFalse("Subject search should not be empty", subjectSearch.isEmpty());
        assertTrue("Subject search should return a book with equal Subject",
                subjectSearch.stream().anyMatch( book -> book.getSubject().equals(randomBook.getSubject())));
    }

    @Test
    public void testTitleSearch() {
        Book randomBook = Bookmarket.getABookAnyBook();
        assertNotNull("Book should not be null", randomBook);

        List<Book> titleSearch = Bookmarket.doTitleSearch(randomBook.getTitle().substring(0, 8));
        assertFalse("Title search should not be empty", titleSearch.isEmpty());
        assertTrue("Title search should return a book with equal Title",
                titleSearch.stream().anyMatch(book -> book.getTitle().equals(randomBook.getTitle())));
    }

    @Test
    public void testAuthorSearch() {
        Book randomBook = Bookmarket.getABookAnyBook();
        assertNotNull("Book should not be null", randomBook);

        List<Book> authorSearch = Bookmarket.doAuthorSearch(randomBook.getAuthor().getMname());
        assertFalse("Author search should not be empty", authorSearch.isEmpty());
        assertTrue("Author search should return a book with equal Author",
                authorSearch.stream().anyMatch(book -> book.getAuthor().equals(randomBook.getAuthor())));
    }

    @Test
    public void testGetRelated() {
        Book randomBook = Bookmarket.getABookAnyBook();
        assertNotNull("Book should not be null", randomBook);

        List<Book> related = Bookmarket.getRelated(randomBook.getId());
        assertFalse("Related list should not be empty", related.isEmpty());
        assertEquals("Related list should have length of 5", 5, related.size());
        assertEquals(related.get(0), randomBook.getRelated1());
        assertEquals(related.get(1), randomBook.getRelated2());
        assertEquals(related.get(2), randomBook.getRelated3());
        assertEquals(related.get(3), randomBook.getRelated4());
        assertEquals(related.get(4), randomBook.getRelated5());

        List<Book> invalid = Bookmarket.getRelated(-1);
        assertTrue("Invalid ID should return empty", invalid.isEmpty());
    }

    @Test
    public void testGetNewProducts() {
        Book randomBook = Bookmarket.getABookAnyBook();
        assertNotNull("Book should not be null", randomBook);

        List<Book> newProducts = Bookmarket.getNewProducts(randomBook.getSubject());
        assertFalse("New products list should not be empty", newProducts.isEmpty());
    }

    @Test
    public void testGetBookCosts() {
        Book randomBook = Bookmarket.getABookAnyBook();
        assertNotNull("Book should not be null", randomBook);

        List<Stock> stocks = Bookmarket.getStocks(randomBook.getId());
        assertFalse("Stocks list should not be empty", stocks.isEmpty());

        List<Double> bookCostInStock = Bookmarket.getCosts(randomBook);
        assertFalse("Book costs list should not be empty", bookCostInStock.isEmpty());

        assertEquals("Both lists should have same length", stocks.size(), bookCostInStock.size());

        for (int i = 0; i < stocks.size(); i++) {
            assertEquals("Costs should be equal", stocks.get(i).getCost(), bookCostInStock.get(i), 0.001);
        }
    }

    @Test
    public void testGetSellers() {
        Book randomBook = Bookmarket.getABookAnyBook();
        assertNotNull("Book should not be null", randomBook);

        Map<Book, Set<Stock>> bestsellers = Bookmarket.getBestSellers(randomBook.getSubject());
        assertFalse("Bestsellers list should not be empty", bestsellers.isEmpty());
    }

    @Test
    public void testGetStock() {
        Book randomBook = Bookmarket.getABookAnyBook();
        assertNotNull("Book should not be null", randomBook);

        List<Stock> stocks = Bookmarket.getStocks(randomBook.getId());
        assertNotNull("Stock list should not be null", stocks);
        assertFalse("Stock list should not be empty", stocks.isEmpty());

        int storeId = stocks.get(0).getIdBookstore();
        Stock storeStock = Bookmarket.getStock(storeId, randomBook.getId());
        assertNotNull("Stock should not be null", storeStock);
        assertEquals("Stocks should be equal", stocks.get(0), storeStock);
    }

    @Test
    public void testRecommendationByItens() {
        List<Book> recommendations = Bookmarket.getRecommendationByItens(79, 10);
        assertNotNull("Recommendation list should not be null", recommendations);
        assertFalse("Recommendation list should not be empty", recommendations.isEmpty());
        assertTrue("Recommendation list should have at least 1 Book for Customer(id=79)",
                recommendations.size() >= 1);
    }

    @Test
    public void testRecommendationByUsers() {
        List<Book> recommendations = Bookmarket.getRecommendationByUsers(79, 10);
        assertNotNull("Recommendation list should not be null", recommendations);
        assertFalse("Recommendation list should not be empty", recommendations.isEmpty());
        assertEquals("Recommendation list should have the expected amount of Books for Customer(id=79)",
                7, recommendations.size());
    }

    @Test
    public void testStocksRecommendationByUsers() {
        // TODO
        System.out.println("TODO: testStocksRecommendationByUsers");
    }

    @Test
    public void testGetRecommendationByUsersReturnsEmptyListWhenNoRecommendations() {
        // Create a new customer with no order history
        Calendar calendar = Calendar.getInstance();
        calendar.set(2000, Calendar.JANUARY, 1, 0, 0, 0);
        Date birthdate = calendar.getTime();

        Customer newCustomer = Bookmarket.createNewCustomer(
                "Test", "NoOrders", "Street", "Street2", "City",
                "State", "12345", "Country", "123456789", "test@test.com",
                birthdate, "Data");

        List<Book> recommendations = Bookmarket.getRecommendationByUsers(newCustomer.getId(), 10);

        assertNotNull("Recommendation list should not be null", recommendations);
        assertTrue("Recommendation list should be empty for customer with no order history",
               recommendations.isEmpty());
    }

    @Test
    public void testPriceBookRecommendationByUsers() {
        // TODO
        System.out.println("TODO: testPriceBookRecommendationByUsers");
    }

    @Test
    public void testGetRecommendationByUsersWithNonExistentCustomer() {
        // Test with a customer ID that doesn't exist
        List<Book> recommendations = Bookmarket.getRecommendationByUsers(999999, 10);

        assertNotNull("Recommendation list should never be null", recommendations);
        assertTrue("Recommendation list should be empty for non-existent customer",
               recommendations.isEmpty());
    }
}
