package dominio;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.Date;

public class OrderLineTest {

    private Book testBook;
    private OrderLine line;

    @Before
    public void setUp() {
        Date now = new Date();
        // Setup de um livro válido para os testes de linha

        Author author = new Author(
                "Isaac", "Yudick", "Asimov", now,
                "Escritor de ficção científica com mais de 20 obras publicadas.");

        testBook = new Book(1, "Test Book", now, "Publisher", SUBJECTS.ARTS,
                "Desc", "t", "i", 100.0, now, "ISBN", 200,
                BACKINGS.PAPERBACK, new int[]{1,1,1}, 0.5, author);

        line = new OrderLine(testBook, 3, 10.0, "Special gift wrapping");
    }

    @Test
    public void testGetters() {
        assertEquals(testBook, line.getBook());
        assertEquals(3, line.getQty());
        assertEquals(10.0, line.getDiscount(), 0.001);
        assertEquals("Special gift wrapping", line.getComments());
    }

    @Test(expected = Exception.class)
    public void testConstructorShouldFailWithNullBook() {
        new OrderLine(null, 1, 0.0, "");
    }

    @Test(expected = Exception.class)
    public void testConstructorShouldFailWithNegativeQuantity() {
        new OrderLine(testBook, -1, 0.0, "");
    }

    @Test(expected = Exception.class)
    public void testConstructorShouldFailWithOverDiscount() {
        new OrderLine(testBook, 1, 101.0, "");
    }

    @Test(expected = Exception.class)
    public void testConstructorShouldFailWithNegativeDiscount() {
        new OrderLine(testBook, 1, -1.0, "");
    }

    @Test(expected = Exception.class)
    public void testConstructorShouldFailWithInvalidDiscount() {
        new OrderLine(testBook, 1, 0.0, null);
    }
}