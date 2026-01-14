package dominio;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.Date;

public class StockTest {

    private Address address;
    private Book book;
    private Stock stock;


    @Before
    public void setUp() {
        Date now = new Date();
        Country brasil = new Country(1, "Brasil", "BRL", 1.0);

        Author author = new Author(
                "Isaac",
                "Yudick",
                "Asimov",
                now,
                "Escritor de ficção científica com mais de 20 obras publicadas."
        );

        address = new Address(1, "Main St", "", "City", "ST", "123", brasil);

        book = new Book(1, "Title", now, "Pub", SUBJECTS.ARTS, "Desc", "t",
                "i", 50.0, now, "ISBN", 100, BACKINGS.HARDBACK, new int[]{1,1}, 0.5, author);

        // Criando instância com ID 10, Custo 30.0 e Qtd 100
        stock = new Stock(10, address, book, 30.0, 100);
    }

    @Test
    public void testSuccessfulStockCreation() {
        assertEquals(10, stock.getIdBookstore());
        assertEquals(address, stock.getAddress());
        assertEquals(book, stock.getBook());
        assertEquals(30.0, stock.getCost(), 0.001);
        assertEquals(100, stock.getQty());
    }

    @Test
    public void testAddQtyPositiveAmount() {
        stock.addQty(50);
        assertEquals(150, stock.getQty());
    }

    @Test(expected = Exception.class)
    public void testAddQtyNegativeAmountShouldFail() {
        stock.setQty(5);
        stock.addQty(-5);
    }

    @Test(expected = Exception.class)
    public void testSetNegativeCostShouldFail() {
        stock.setCost(-1.0);
    }

    @Test(expected = Exception.class)
    public void testSetNegativeQtyShouldFail() {
        stock.setQty(-5);
    }

    @Test(expected = Exception.class)
    public void testConstructorShouldFailWithNegativeQty() {
        new Stock(1, address, book, 10.0, -1);
    }

    @Test(expected = Exception.class)
    public void testConstructorShouldFailWithNegativeCost() {
        new Stock(1, address, book, -1.0, 5);
    }

    @Test(expected = Exception.class)
    public void testConstructorShouldFailWithNullBook() {
        new Stock(1, address, null, 10.0, 5);
    }

    @Test(expected = Exception.class)
    public void testConstructorShouldFailWithNullAddress() {
        new Stock(1, null, book, 10.0, 5);
    }

    @Test(expected = Exception.class)
    public void testConstructorShouldFailWithNegativeId() {
        new Stock(-1, address, book, 10.0, 5);
    }
}