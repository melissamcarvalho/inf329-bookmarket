package dominio;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.Date;

public class OrderTest {

    private Customer customer;
    private Cart cart;
    private Address address;
    private CCTransaction cc;
    private Date now;
    private Order order;

    @Before
    public void setUp() {
        now = new Date();
        Country brasil = new Country(1, "Brasil", "BRL", 1.0);
        address = new Address(1, "Street", "", "City", "ST", "123", brasil);

        customer = new Customer(
                1, "user", "pass", "F", "L", "123", "e@e.com",
                now, now, now, now, 10.0, 0, 0, now, "data", address);

        Author author = new Author(
                "Isaac", "Yudick", "Asimov", now,
                "Escritor de ficção científica com mais de 20 obras publicadas.");

        Book book = new Book(
                1, "Title", now, "Pub", SUBJECTS.ARTS, "D", "t", "i",
                100.0, now, "ISBN", 100, BACKINGS.HARDBACK, new int[]{1,1,1}, 0.5, author);

        // Criando um carrinho com itens para testar a conversão
        Stock stock = new Stock(1, address, book, 100.0, 10);

        cart = new Cart(500, now);
        cart.increaseLine(stock, 1);

        cc = new CCTransaction(
                CreditCards.VISA, new long[]{1234L, 5678L, 1234L, 5678L}, "Owner", now, "AUTH1", 200.0, now, brasil);

        order = new Order(
                1, customer, now, cart, "Comment", ShipTypes.AIR,
                now, StatusTypes.PENDING, address, address, cc);
    }

    @Test
    public void testGetters() {
        assertEquals(1, order.getId());
        assertEquals(customer, order.getCustomer());
        // Verifica se as linhas do carrinho (CartLine) foram convertidas para OrderLine
        assertEquals(1, order.getLines().size());
        assertEquals(1, order.getLines().get(0).getQty());
        assertEquals(10.0, order.getLines().get(0).getDiscount(), 0.001);
        assertEquals(90.0, order.getSubtotal(), 0.001);
        assertEquals(ShipTypes.AIR, order.getShipType());
    }

    @Test
    public void testOrderStatusHelperMethods() {
        Order shipped = new Order(1, customer, now, cart, "C", ShipTypes.AIR, now, StatusTypes.SHIPPED, address, address, cc);
        assertTrue("Order status should be SHIPPED", shipped.isShipped());

        Order pending = new Order(1, customer, now, cart, "C", ShipTypes.AIR, now, StatusTypes.PENDING, address, address, cc);
        assertTrue("Order status should not be PENDING", pending.isPending());

        Order denied = new Order(1, customer, now, cart, "C", ShipTypes.AIR, now, StatusTypes.DENIED, address, address, cc);
        assertTrue("Order status should not be PENDING", denied.isDenined());

        Order processing = new Order(1, customer, now, cart, "C", ShipTypes.AIR, now, StatusTypes.PROCESSING, address, address, cc);
        assertTrue("Order status should not be PENDING", processing.isProcessed());
    }

    @Test
    public void testImmutabilityOfOrderDates() {
        Order immut = new Order(1, customer, now, cart, "C", ShipTypes.AIR, now, StatusTypes.PENDING, address, address, cc);

        long originalTime = now.getTime();
        now.setTime(0); // Tentativa de sabotagem externa

        assertEquals("VULNERABILIDADE: A data do pedido foi alterada externamente!",
                originalTime, immut.getDate().getTime());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorShouldFailWithNegativeId() {
        // QA: Um pedido não pode ser gerado sem um carrinho de origem
        new Order(-1, customer, now, cart, "Comment", ShipTypes.AIR, now, StatusTypes.PENDING, address, address, cc);
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorShouldFailWithOrderDate() {
        // QA: Um pedido não pode ser gerado sem um carrinho de origem
        new Order(1, customer, null, cart, "Comment", ShipTypes.AIR, now, StatusTypes.PENDING, address, address, cc);
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorShouldFailWithNullComment() {
        // QA: Um pedido não pode ser gerado sem um carrinho de origem
        new Order(1, customer, now, cart, null, ShipTypes.AIR, now, StatusTypes.PENDING, address, address, cc);
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorShouldFailWithNullShippingType() {
        // QA: Um pedido não pode ser gerado sem um carrinho de origem
        new Order(1, customer, now, cart, "Comment", null, now, StatusTypes.PENDING, address, address, cc);
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorShouldFailWithNullShipDate() {
        // QA: Um pedido não pode ser gerado sem um carrinho de origem
        new Order(1, customer, now, cart, "Comment", ShipTypes.AIR, null, StatusTypes.PENDING, address, address, cc);
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorShouldFailWithNullStatus() {
        // QA: Um pedido não pode ser gerado sem um carrinho de origem
        new Order(1, customer, now, cart, "Comment", ShipTypes.AIR, now, null, address, address, cc);
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorShouldFailWithNullBillingAddress() {
        // QA: Um pedido não pode ser gerado sem um carrinho de origem
        new Order(1, customer, now, cart, "Comment", ShipTypes.AIR, now, StatusTypes.PENDING, null, address, cc);
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorShouldFailWithNullShippingAddress() {
        // QA: Um pedido não pode ser gerado sem um carrinho de origem
        new Order(1, customer, now, cart, "Comment", ShipTypes.AIR, now, StatusTypes.PENDING, address, null, cc);
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorShouldFailWithNullCC() {
        // QA: Um pedido não pode ser gerado sem um carrinho de origem
        new Order(1, customer, now, cart, "Comment", ShipTypes.AIR, now, StatusTypes.PENDING, address, address, null);
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorShouldFailWithNullCart() {
        // QA: Um pedido não pode ser gerado sem um carrinho de origem
        new Order(1, customer, now, null, "Comment", ShipTypes.AIR, now, StatusTypes.PENDING, address, address, cc);
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorShouldFailWithNullCustomer() {
        new Order(1, null, now, cart, "Comment", ShipTypes.AIR, now, StatusTypes.PENDING, address, address, cc);
    }
}