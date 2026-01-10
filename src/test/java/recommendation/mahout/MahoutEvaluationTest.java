package recommendation.mahout;

import dominio.*;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

import java.util.Date;

public class MahoutEvaluationTest {
    private Customer customer;
    private Book book;
    private Evaluation evaluation;

    @Before
    public void setUp() {
        // Minimal valid Address and Country
        Country country = new Country(1, "Brazil", "BRL", 1.0);
        Address address = new Address(1, "Street 1", "Street 2", "City", "State", "00000-000", country);
        // Minimal valid Author
        Author author = new Author("John", "M", "Doe", new Date(), "Bio");
        // Minimal valid Book
        book = new Book(10, "Test Book", new Date(), "Publisher", SUBJECTS.ARTS, "Description", "thumb.jpg", "img.jpg", 10.0, new Date(), "ISBN", 100, BACKINGS.HARDBACK, new int[]{1,2,3}, 1.0, author);
        // Minimal valid Customer
        customer = new Customer(1, "johndoe", "pass", "John", "Doe", "123456789", "john@example.com", new Date(), new Date(), new Date(), new Date(), 0.0, 0.0, 0.0, new Date(), "data", address);
        // Evaluation
        evaluation = new Evaluation(100, customer, book, 4.5);
    }

    @Test
    public void testConstructorAndGetters() {
        MahoutEvaluation mahoutEvaluation = new MahoutEvaluation(evaluation);
        assertEquals(evaluation.getId(), mahoutEvaluation.getId());
        assertEquals(evaluation.getCustomer(), mahoutEvaluation.getCustomer());
        assertEquals(evaluation.getBook(), mahoutEvaluation.getBook());
        assertEquals(evaluation.getRating(), mahoutEvaluation.getRating(), 0.0001);
    }

    @Test
    public void testGetUserID() {
        MahoutEvaluation mahoutEvaluation = new MahoutEvaluation(evaluation);
        assertEquals(customer.getId(), mahoutEvaluation.getUserID());
    }

    @Test
    public void testGetItemID() {
        MahoutEvaluation mahoutEvaluation = new MahoutEvaluation(evaluation);
        assertEquals(book.getId(), mahoutEvaluation.getItemID());
    }

    @Test
    public void testGetValue() {
        MahoutEvaluation mahoutEvaluation = new MahoutEvaluation(evaluation);
        assertEquals((float) evaluation.getRating(), mahoutEvaluation.getValue(), 0.0001f);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetValueThrows() {
        MahoutEvaluation mahoutEvaluation = new MahoutEvaluation(evaluation);
        mahoutEvaluation.setValue(5.0f);
    }
}
