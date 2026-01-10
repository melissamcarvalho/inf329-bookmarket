package dominio;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class AddressTest {

    private Country brasil;
    private Address endereco;

    @Before
    public void setUp() {
        // Criando a dependência Country
        brasil = new Country(1, "Brasil", "BR", 0);

        // Criando a instância principal para os testes
        endereco = new Address(
                100,
                "Rua das Flores, 123",
                "Apto 45",
                "São Paulo",
                "SP",
                "01234-567",
                brasil
        );
    }

    @Test
    public void testGetters() {
        assertEquals(100, endereco.getId());
        assertEquals("Rua das Flores, 123", endereco.getStreet1());
        assertEquals("Apto 45", endereco.getStreet2());
        assertEquals("São Paulo", endereco.getCity());
        assertEquals("SP", endereco.getState());
        assertEquals("01234-567", endereco.getZip());
        assertEquals(brasil, endereco.getCountry());
    }

    @Test
    public void testEquals() {
        // Endereço com os mesmos dados (exceto ID, que não é usado no equals original)
        Address enderecoIgual = new Address(
                200, "Rua das Flores, 123", "Apto 45", "São Paulo", "SP", "01234-567", brasil
        );

        // Endereço com dados diferentes
        Address enderecoDiferente = new Address(
                100, "Outra Rua", "Apto 45", "São Paulo", "SP", "01234-567", brasil
        );

        assertEquals("Deveriam ser iguais", endereco, enderecoIgual);
        assertNotEquals("Deveriam ser diferentes", endereco, enderecoDiferente);
        assertNotEquals("Não deve ser igual a nulo", null, endereco);
        assertNotEquals("Não deve ser igual a um objeto de outra classe", "String", endereco);
    }

    @Test
    public void testHashCode() {
        Address enderecoIgual = new Address(
                200, "Rua das Flores, 123", "Apto 45", "São Paulo", "SP", "01234-567", brasil
        );

        assertEquals("Objetos iguais devem ter o mesmo hashCode",
                endereco.hashCode(), enderecoIgual.hashCode());
    }
}