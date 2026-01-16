package dominio;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.Date;

public class EvaluationTest {

    private Customer customer;
    private Book book;
    private Evaluation evaluation;

    @Before
    public void setUp() {
        // Setup de dependências mínimas para o teste
        Date now = new Date();
        Address addr = new Address(1, "Main St", "101", "NY", "NY", "10001", null);
        customer = new Customer(1, "jdoe", "pass", "John", "Doe", "123", "j@d.com", now, now, now, now, 0, 0, 0, now, "data", addr);
        book = new Book(1, "Quality Code", now, "Tech", SUBJECTS.ARTS, "Desc", "t", "i", 50.0, now, "ISBN", 100, BACKINGS.HARDBACK, new int[]{1,1}, 0.5, null);

        evaluation = new Evaluation(100, customer, book, 4.5);
    }

    @Test
    public void testGetters() {
        assertEquals(100, evaluation.getId());
        assertEquals(customer, evaluation.getCustomer());
        assertEquals(book, evaluation.getBook());
        assertEquals(4.5, evaluation.getRating(), 0.001);
    }

    @Test
    public void testSetRatingUpdatesValue() {
        evaluation.setRating(5.0);
        assertEquals(5.0, evaluation.getRating(), 0.001);
    }

    @Test(expected = Exception.class)
    public void testConstructorShouldFailWithNullBook() {
        new Evaluation(100, customer, null, 4.5);
    }

    @Test(expected = Exception.class)
    public void testConstructorShouldFailWithNullCustomer() {
        new Evaluation(100, null, book, 4.5);
    }

    @Test(expected = Exception.class)
    public void testConstructorShouldFailWithNegativeId() {
        new Evaluation(-1, customer, book, 4.5);
    }

    @Test(expected = Exception.class)
    public void testConstructorShouldFailWithNegativeRating() {
        // Notas negativas não existem em sistemas de avaliação de 0 a 5 ou 10
        new Evaluation(1, customer, book, -1.0);
    }

    @Test(expected = Exception.class)
    public void testConstructorShouldFailWithRatingAboveLimit() {
        // Assumindo um limite de negócio de 0.0 a 5.0
        new Evaluation(1, customer, book, 6.0);
    }
}