package dominio;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.Date;

public class CCTransactionTest {

    private Date futureDate;
    private Date currentDate;
    private Country brazil;
    private CCTransaction transaction;

    @Before
    public void setUp() {
        currentDate = new Date();
        // Data de expiração: hoje + 1 ano
        futureDate = new Date(currentDate.getTime() + (365L * 24 * 60 * 60 * 1000));
        brazil = new Country(1, "Brazil", "BRL", 1.0);

        transaction = new CCTransaction(
                CreditCards.VISA, 1234567812345678L, "JOHN DOE",
                futureDate, "AUTH-999", 150.75, currentDate, brazil
        );
    }

    @Test
    public void testGetters() {
        assertEquals(CreditCards.VISA, transaction.getType());
        assertEquals(1234567812345678L, transaction.getNum());
        assertEquals("JOHN DOE", transaction.getName());
        assertEquals(150.75, transaction.getAmount(), 0.001);
        assertEquals(brazil, transaction.getCountry());
    }

    @Test(expected = Exception.class)
    public void testConstructorShouldFailWithNullAddress() {
        new CCTransaction(
                CreditCards.VISA, 1234567812345678L, "JOHN DOE",
                futureDate, "AUTH-999", 150.75, currentDate, null
        );
    }

    @Test(expected = Exception.class)
    public void testConstructorShouldFailWithNullDate() {
        new CCTransaction(
                CreditCards.VISA, 1234567812345678L, "JOHN DOE",
                futureDate, "AUTH-999", 150.75, null, brazil
        );
    }

    @Test(expected = Exception.class)
    public void testConstructorShouldFailWithNullAuthId() {
        new CCTransaction(
                CreditCards.VISA, 1234567812345678L, "JOHN DOE",
                futureDate, null, 150.75, currentDate, brazil
        );
    }

    @Test(expected = Exception.class)
    public void testConstructorShouldFailWithNullExpirationDate() {
        new CCTransaction(
                CreditCards.VISA, 1234567812345678L, "JOHN DOE",
                null, "AUTH-999", 150.75, currentDate, brazil
        );
    }

    @Test(expected = Exception.class)
    public void testConstructorShouldFailWithNullName() {
        new CCTransaction(
                CreditCards.VISA, 1234567812345678L, null,
                futureDate, "AUTH-999", 150.75, currentDate, brazil
        );
    }

    @Test(expected = Exception.class)
    public void testConstructorShouldFailWithNullType() {
        new CCTransaction(
                null, 1234567812345678L, "JOHN DOE",
                futureDate, "AUTH-999", 150.75, currentDate, brazil
        );
    }

    @Test(expected = Exception.class)
    public void testConstructorShouldFailWithNegativeCardNumber() {
        new CCTransaction(
                CreditCards.VISA, -1, "JOHN DOE",
                futureDate, "AUTH-999", 150.75, currentDate, brazil
        );
    }

    @Test(expected = Exception.class)
    public void testConstructorShouldFailWithNegativeAmount() {
        new CCTransaction(CreditCards.MASTERCARD, 4444L, "Name", futureDate, "ID", -1.0, currentDate, brazil);
    }

    @Test
    public void testExpirationDateImmutability() {
        CCTransaction transaction = new CCTransaction(
                CreditCards.VISA, 4444L, "Name", futureDate, "ID", 10.0, currentDate, brazil
        );

        long originalTime = futureDate.getTime();
        futureDate.setTime(0); // Tentativa de alteração maliciosa externa

        assertEquals("VULNERABILIDADE: A data de expiração da transação foi alterada externamente!",
                originalTime, transaction.getExpire().getTime());
    }
}