package dominio;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Calendar;
import java.util.Date;

public class EvaluationTest {

    private static Customer customer;
    private static Book book;
    private static Evaluation evaluation;

    @BeforeClass
    public static void setUp() {
        // Setup de dependências mínimas para o teste
        Date now = new Date();
        Country country = new Country(1, "Brasil", "BRL", 1.0);
        Address addr = new Address(1, "Main St", "101", "NY", "NY", "10001", country);
        customer = new Customer(1, "jdoe", "pass", "John", "Doe", "123", "j@d.com", now, now, now, now, 0, 0, 0, now, "data", addr);

        Author author = new Author(
                "Isaac",
                "Yudick",
                "Asimov",
                new Date(),
                "Escritor de ficção científica com mais de 20 obras publicadas."
        );

        book = new Book(1, "Quality Code", now, "Tech", SUBJECTS.ARTS, "Desc", "t", "i", 50.0, now, "ISBN", 100, BACKINGS.HARDBACK, new int[]{1,1,1}, 0.5, author);

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