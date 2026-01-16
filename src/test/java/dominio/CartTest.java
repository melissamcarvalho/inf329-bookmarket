package dominio;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.Date;
import java.util.Collection;

public class CartTest {

    private Cart cart;
    private Stock stockJava;
    private Stock stockSql;
    private Customer customer;

    @Before
    public void setUp() {
        // Setup de datas e endereços
        Date now = new Date();
        Country country = new Country(1, "Brasil", "BRL", 0);
        Address addr = new Address(1, "Rua A", "", "Cidade", "SP", "123", country);

        // Setup de Autores e Livros
        Author author = new Author("John", "D", "Doe", now, "Bio");
        Book bookJava = new Book(1, "Java", now, "Pub", SUBJECTS.ARTS, "Desc", "t", "i", 100.0, now, "1", 100, BACKINGS.PAPERBACK, new int[]{1,1,1}, 1.0, author);
        Book bookSql = new Book(2, "SQL", now, "Pub", SUBJECTS.ARTS, "Desc", "t", "i", 50.0, now, "2", 100, BACKINGS.PAPERBACK, new int[]{1,1,1}, 1.0, author);

        // Setup de Stock (Assumindo que Stock recebe book, qty, cost e address)
        stockJava = new Stock(1, addr, bookJava, 100.0, 10);
        stockSql = new Stock(1, addr, bookSql, 50.0, 5);

        // Setup de Customer (Assumindo que tem um desconto de 10%)
        customer = new Customer(
                1, "user", "pass", "", "", "", "",
                now, now, now, now, 10.0, 0.0, 0.0, now, "", addr);

        cart = new Cart(500, now);
    }

    @Test
    public void testIncreaseLine() {
        cart.increaseLine(stockJava, 2);

        Collection<CartLine> lines = cart.getLines();
        assertEquals("Deve ter 1 linha no carrinho", 1, lines.size());

        CartLine line = lines.iterator().next();
        assertEquals("A quantidade deve ser 2", 2, line.getQty());
        assertEquals(stockJava, line.getStock());
    }

    @Test
    public void testIncreaseLineExistingBook() {
        cart.increaseLine(stockJava, 1);
        cart.increaseLine(stockJava, 2); // Adiciona mais 2 ao mesmo livro

        assertEquals(1, cart.getLines().size());
        CartLine line = cart.getLines().iterator().next();
        assertEquals("A quantidade total deve ser 3", 3, line.getQty());
    }

    @Test
    public void testChangeLine() {
        cart.increaseLine(stockJava, 1);
        cart.changeLine(stockJava, 5); // Altera de 1 para 5

        CartLine line = cart.getLines().iterator().next();
        assertEquals("A quantidade deve ter sido alterada para 5", 5, line.getQty());
    }

    @Test
    public void testRemoveLineWhenQtyZero() {
        cart.increaseLine(stockJava, 2);
        cart.changeLine(stockJava, 0); // Alterar para zero deve remover a linha

        assertTrue("O carrinho deve estar vazio", cart.getLines().isEmpty());
    }

    @Test
    public void testSubTotalComDesconto() {
        cart.increaseLine(stockJava, 1); // 100.0
        cart.increaseLine(stockSql, 2);  // 50.0 * 2 = 100.0

        // Total bruto = 200.0. Com 10% de desconto = 180.0
        double esperado = 180.0;
        assertEquals(esperado, cart.subTotal(10.0), 0.001);
    }

    @Test
    public void testTax() {
        cart.increaseLine(stockJava, 1); // Cost 100.0
        // Desconto 0% -> Subtotal 100. Taxa 8.25% de 100 = 8.25
        assertEquals(8.25, cart.tax(0.0), 0.001);
    }

    @Test
    public void testClear() {
        cart.increaseLine(stockJava, 1);
        assertFalse(cart.getLines().isEmpty());

        cart.clear();
        assertTrue("Após clear, o carrinho deve estar vazio", cart.getLines().isEmpty());
    }

    @Test
    public void testTotal() {
        cart.increaseLine(stockJava, 1); // Cost 100.0
        // Customer tem 10% desconto.
        // Subtotal = 90.0
        // Tax (8.25% de 90) = 7.425
        // ShipCost (Falso serviço) = 3.0 + (1.0 * 1 unidade) = 4.0
        // Total esperado = 90.0 + 7.425 + 4.0 = 101.425

        double totalEsperado = 101.425;
        assertEquals(totalEsperado, cart.total(customer), 0.001);
    }

    @Test(expected = Exception.class)
    public void testIncreaseLineWithNegativeQuantity() {
        cart.increaseLine(stockJava, -5);
    }

    @Test(expected = Exception.class)
    public void testChangeLineWithNegativeQuantity() {
        cart.changeLine(stockJava, -10);
    }

    @Test(expected = Exception.class)
    public void testSubTotalWithDiscountOverOneHundred() {
        cart.changeLine(stockJava, 1);
        cart.subTotal(110.0);
    }

    @Test(expected = Exception.class)
    public void testSubTotalWithNegativeDiscount() {
        cart.changeLine(stockJava, 1);
        cart.subTotal(-1.0);
    }

    @Test(expected = Exception.class)
    public void testConstructorShouldFailWithNullTime() {
        new Cart(1, null);
    }

    @Test(expected = Exception.class)
    public void testConstructorShouldFailWithNegativeId() {
        new Cart(-1, new Date());
    }
}