package servico;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import dominio.Book;
import dominio.Cart;
import dominio.CreditCards;
import dominio.Customer;
import dominio.SUBJECTS;
import dominio.ShipTypes;
import dominio.StatusTypes;
import dominio.Stock;
import recommendation.RecommendationSettings;
import dominio.Order;
import java.util.HashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import static org.junit.Assert.*;

/**
 *
 * @author INF329
 */
public class BookmarketTest {

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
        long seed = 0;
        long now = System.currentTimeMillis();

        int items = 500;
        int customers = 1000;
        int addresses = 1000;
        int authors = 1000;
        int orders = 1000;
        int stocks = 1000;
        int evaluations = 100;

        Bookstore.populate(seed, now, items, customers, addresses, authors);

        Bookstore[] bookstores = new Bookstore[5];

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
                //bookstore.updateStock(j, 1);
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

    @After
    public void tearDown() {
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

}
