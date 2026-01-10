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
    public void testConstructorAndGetters() {
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

        // Criando um objeto fake de Order (se necessário) ou passando null apenas para testar a atribuição
        // Se Order tiver um construtor simples, instancie-o aqui.
        // customer.logOrder(new Order(...));
        // assertNotNull(customer.getMostRecentOrder());
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

    @Test(expected = UnsupportedOperationException.class)
    public void testSecondaryConstructorThrowsException() {
        // Testa o construtor que você marcou como "Not supported yet"
        new Customer(1, "user", "lower", "fname", "lname", "phone", "email", now, now);
    }
}