package dominio;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.Date;

public class CustomerTest {

    private Customer customer;
    private Address address;
    private Date now;
    private Date birth;
    private Country country;

    @Before
    public void setUp() {
        now = new Date();
        birth = new Date(946684800000L); // Ano 2000
        country = new Country(1, "Brasil", "BRL", 0);
        address = new Address(1, "Rua Central", "Apto 1", "Sampa", "SP", "123", country);

        customer = new Customer(
                1, "user_teste", "senha123", "João", "Silva",
                "11-9999-9999", "joao@email.com", now, now, now, now,
                15.0, 500.0, 1000.0, birth, "Dados extra", address
        );
    }

    @Test
    public void testGetters() {
        assertEquals(1, customer.getId());
        assertEquals("user_teste", customer.getUname());
        assertEquals("João", customer.getFname());
        assertEquals("Silva", customer.getLname());
        assertEquals(15.0, customer.getDiscount(), 0.001);
        assertEquals(address, customer.getAddress());
        assertEquals(birth, customer.getBirthdate());
    }

    @Test
    public void testLoginExpirationUpdates() {
        Date newLogin = new Date(now.getTime() + 3600000); // +1 hora
        customer.setLogin(newLogin);
        assertEquals(newLogin, customer.getLogin());

        Date newExp = new Date(now.getTime() + 7200000); // +2 horas
        customer.setExpiration(newExp);
        assertEquals(newExp, customer.getExpiration());
    }

    @Test
    public void testLogOrder() {
        // Assume-se que Order existe e pode ser nulo ou mockado
        assertNull("Pedido recente deve iniciar nulo", customer.getMostRecentOrder());
    }

    @Test
    public void testEqualsAndHashCode() {
        Customer sameCustomer = new Customer(
                1, "user_teste", "senha123", "João", "Silva",
                "11-9999-9999", "joao@email.com", now, now, now, now,
                15.0, 500.0, 1000.0, birth, "Dados extra", address
        );

        Customer differentCustomer = new Customer(
                2, "outro_user", "senha123", "Maria", "Silva",
                "11-8888-8888", "maria@email.com", now, now, now, now,
                0.0, 0.0, 0.0, birth, "Dados", address
        );

        // Teste de Igualdade
        assertEquals("Clientes com mesmos dados devem ser iguais", customer, sameCustomer);
        assertNotEquals("Clientes com IDs diferentes devem ser diferentes", customer, differentCustomer);

        // Teste de HashCode
        assertEquals("HashCodes devem ser iguais para objetos iguais",
                customer.hashCode(), sameCustomer.hashCode());
    }

    @Test(expected = Exception.class)
    public void testConstructorShouldFailWithNullAddress() {
        new Customer(
                1, "user_teste", "senha123", "João", "Silva",
                "11-9999-9999", "joao@email.com", now, now, now, now,
                15.0, 500.0, 1000.0, birth, "Dados extra", null
        );
    }

    @Test(expected = Exception.class)
    public void testConstructorShouldFailWithNullData() {
        new Customer(
                1, "user_teste", "senha123", "João", "Silva",
                "11-9999-9999", "joao@email.com", now, now, now, now,
                15.0, 500.0, 1000.0, birth, null, address
        );
    }

    @Test(expected = Exception.class)
    public void testConstructorShouldFailWithNullBirthdate() {
        new Customer(
                1, "user_teste", "senha123", "João", "Silva",
                "11-9999-9999", "joao@email.com", now, now, now, now,
                15.0, 500.0, 1000.0, null, "Dados extra", address
        );
    }

    @Test(expected = Exception.class)
    public void testConstructorShouldFailWithNegativeYTDPayment() {
        new Customer(
                1, "user_teste", "senha123", "João", "Silva",
                "11-9999-9999", "joao@email.com", now, now, now, now,
                15.0, 500.0, -1.0, birth, "Dados extra", address
        );
    }

    @Test(expected = Exception.class)
    public void testConstructorShouldFailWithNegativeBalance() {
        new Customer(
                1, "user_teste", "senha123", "João", "Silva",
                "11-9999-9999", "joao@email.com", now, now, now, now,
                15.0, -1.0, 1000.0, birth, "Dados extra", address
        );
    }

    @Test(expected = Exception.class)
    public void testConstructorShouldFailWithNegativeDiscount() {
        new Customer(
                1, "user_teste", "senha123", "João", "Silva",
                "11-9999-9999", "joao@email.com", now, now, now, now,
                -1.0, 500.0, 1000.0, birth, "Dados extra", address
        );
    }

    @Test(expected = Exception.class)
    public void testConstructorShouldFailWithNullExpirationDate() {
        new Customer(
                1, "user_teste", "senha123", "João", "Silva",
                "11-9999-9999", "joao@email.com", now, now, now, null,
                15.0, 500.0, 1000.0, birth, "Dados extra", address
        );
    }

    @Test(expected = Exception.class)
    public void testConstructorShouldFailWithNullLoginDate() {
        new Customer(
                1, "user_teste", "senha123", "João", "Silva",
                "11-9999-9999", "joao@email.com", now, now, null, now,
                15.0, 500.0, 1000.0, birth, "Dados extra", address
        );
    }

    @Test(expected = Exception.class)
    public void testConstructorShouldFailWithNullLastVisitDate() {
        new Customer(
                1, "user_teste", "senha123", "João", "Silva",
                "11-9999-9999", "joao@email.com", now, null, now, now,
                15.0, 500.0, 1000.0, birth, "Dados extra", address
        );
    }

    @Test(expected = Exception.class)
    public void testConstructorShouldFailWithNullSinceDate() {
        new Customer(
                1, "user_teste", "senha123", "João", "Silva",
                "11-9999-9999", "joao@email.com", null, now, now, now,
                15.0, 500.0, 1000.0, birth, "Dados extra", address
        );
    }

    @Test(expected = Exception.class)
    public void testConstructorShouldFailWithNullEmail() {
        new Customer(
                1, "user_teste", "senha123", "João", "Silva",
                "11-9999-9999", null, now, now, now, now,
                15.0, 500.0, 1000.0, birth, "Dados extra", address
        );
    }

    @Test(expected = Exception.class)
    public void testConstructorShouldFailWithNullPhone() {
        new Customer(
                1, "user_teste", "senha123", "João", "Silva",
                null, "joao@email.com", now, now, now, now,
                15.0, 500.0, 1000.0, birth, "Dados extra", address
        );
    }

    @Test(expected = Exception.class)
    public void testConstructorShouldFailWithNullLastName() {
        new Customer(
                1, "user_teste", "senha123", "João", null,
                "11-9999-9999", "joao@email.com", now, now, now, now,
                15.0, 500.0, 1000.0, birth, "Dados extra", address
        );
    }

    @Test(expected = Exception.class)
    public void testConstructorShouldFailWithNullFirstName() {
        new Customer(
                1, "user_teste", "senha123", null, "Silva",
                "11-9999-9999", "joao@email.com", now, now, now, now,
                15.0, 500.0, 1000.0, birth, "Dados extra", address
        );
    }

    @Test(expected = Exception.class)
    public void testConstructorShouldFailWithNullPassword() {
        new Customer(
                1, "user_teste", null, "João", "Silva",
                "11-9999-9999", "joao@email.com", now, now, now, now,
                15.0, 500.0, 1000.0, birth, "Dados extra", address
        );
    }

    @Test(expected = Exception.class)
    public void testConstructorShouldFailWithNullUsername() {
        new Customer(
                1, null, "senha123", "João", "Silva",
                "11-9999-9999", "joao@email.com", now, now, now, now,
                15.0, 500.0, 1000.0, birth, "Dados extra", address
        );
    }

    @Test(expected = Exception.class)
    public void testConstructorShouldFailWithNegativeId() {
        new Customer(
                -1, "user_teste", "senha123", "João", "Silva",
                "11-9999-9999", "joao@email.com", now, now, now, now,
                15.0, 500.0, 1000.0, birth, "Dados extra", address
        );
    }
}