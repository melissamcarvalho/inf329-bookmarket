package dominio;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class CountryTest {

    private Country brasil;

    @Before
    public void setUp() {
        // Inicializa uma instância padrão para os testes
        brasil = new Country(1, "Brasil", "Real", 1.0);
    }

    @Test
    public void testGetters() {
        assertEquals(1, brasil.getId());
        assertEquals("Brasil", brasil.getName());
        assertEquals("Real", brasil.getCurrency());
        assertEquals(1.0, brasil.getExchange(), 0.0001);
    }

    @Test
    public void testEqualsMesmoNome() {
        // De acordo com sua implementação, se o nome for igual, o país é o mesmo
        // mesmo que o ID, moeda ou câmbio sejam diferentes.
        Country brasilDiferente = new Country(2, "Brasil", "BRL", 5.50);

        assertEquals("Países com o mesmo nome devem ser considerados iguais", brasil, brasilDiferente);
    }

    @Test
    public void testEqualsNomesDiferentes() {
        Country argentina = new Country(1, "Argentina", "Peso", 0.5);

        assertNotEquals("Países com nomes diferentes devem ser considerados diferentes", brasil, argentina);
    }

    @Test
    public void testHashCode() {
        Country brasilDiferente = new Country(99, "Brasil", "XYZ", 0.0);

        assertEquals("Se o equals baseia-se no nome, o hashCode também deve ser igual para o mesmo nome",
                brasil.hashCode(), brasilDiferente.hashCode());
    }
}