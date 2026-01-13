package servico;

import java.util.Date;
import java.util.Map;

import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import org.junit.BeforeClass;
import org.junit.Test;

import dominio.Book;
import dominio.Cart;
import dominio.CreditCards;
import dominio.Customer;
import dominio.Order;
import dominio.OrderLine;
import dominio.SUBJECTS;
import dominio.ShipTypes;
import dominio.StatusTypes;

/**
 * Unit tests for Bookstore.getBestSellers behavior introduced in recent
 * refactor.
 */
public class BookstoreGetBestSellersTest {

    static Bookstore instance;

    @BeforeClass
    public static void setUpClass() {
        long seed = 0;
        long now = System.currentTimeMillis();
        // Use a sufficiently large population so this test doesn't interfere
        // with other test classes that expect larger datasets.
        int items = 1000;
        int customers = 100;
        int addresses = 1000;
        int authors = 100;

        // Only populate global static data if it hasn't been populated by other tests.
        if (!(new Bookstore(0)).isPopulated()) {
            Bookstore.populate(seed, now, items, customers, addresses, authors);
        }
        instance = new Bookstore(0);
        // populate a small bookstore instance so ordersByCreation is usable
        instance.populateInstanceBookstore(100, 100, 100, new java.util.Random(seed), now);
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Test
    public void testGetBestSellers_nullSubject_throws() {
        try {
            instance.getBestSellers(null);
            fail("Expected IllegalArgumentException when subject is null");
        } catch (IllegalArgumentException ex) {
            // expected
        }
    }

    @Test
    public void testGetBestSellers_ignoresOrderLinesWithNullBook() {
        SUBJECTS subject = SUBJECTS.ARTS;

        // snapshot before
        Map<Book, Integer> before = instance.getBestSellers(subject);

        // create an empty cart and confirm a shipped order (no real lines)
        Cart cart = instance.createCart(System.currentTimeMillis());
        Customer customer = Bookstore.getCustomer(1);
        Order order = instance.confirmBuy(customer.getId(), cart.getId(), "test",
                CreditCards.VISA, 1234567890123456L, "tester", new Date(), ShipTypes.AIR,
                new Date(), customer.getAddress().getId(), System.currentTimeMillis(), StatusTypes.SHIPPED);

        // Mutate the order to add an OrderLine with null book (simulates malformed data)
        order.getLines().add(new OrderLine(null, 999, 0.0, "null-book-line"));

        // After adding the null-book line, getBestSellers should not throw and should
        // not count the null-book line toward any Book sales.
        Map<Book, Integer> after = instance.getBestSellers(subject);

        // Sizes may differ because other background population could change counts,
        // but for all existing keys in 'before' the counts should remain the same
        // since we only added a null-book line.
        for (Map.Entry<Book, Integer> e : before.entrySet()) {
            int prev = e.getValue();
            int now = after.getOrDefault(e.getKey(), 0);
            assertEquals("Sales count changed for book " + e.getKey(), prev, now);
        }
    }

}
