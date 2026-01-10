package dominio;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class CountryTest {

    private Country brasil;

    @Before
    public void setUp() {
        brasil = new Country(1, "Brasil", "BRL", 1.0);
    }

    @Test
    public void testGetters() {
        assertEquals(1, brasil.getId());
        assertEquals("Brasil", brasil.getName());
        assertEquals("BRL", brasil.getCurrency());
        assertEquals(1.0, brasil.getExchange(), 0.0001);
    }

    @Test
    public void testEqualsSameName() {
        Country brasilDiferente = new Country(2, "Brasil", "BRL", 5.50);
        assertEquals("Países com o mesmo nome devem ser considerados iguais", brasil, brasilDiferente);
    }

    @Test
    public void testEqualsDifferentName() {
        Country argentina = new Country(1, "Argentina", "Peso", 0.5);
        assertNotEquals("Países com nomes diferentes devem ser considerados diferentes", brasil, argentina);
    }

    @Test
    public void testHashCode() {
        Country brasilDiferente = new Country(99, "Brasil", "XYZ", 0.0);

        assertEquals("Se o equals baseia-se no nome, o hashCode também deve ser igual para o mesmo nome",
                brasil.hashCode(), brasilDiferente.hashCode());
    }

    @Test(expected = Exception.class)
    public void testCountryExchangeRateZeroOrNegative() {
        // Taxa de câmbio zero ou negativa causaria divisões por zero ou valores financeiros irreais
        new Country(1, "País Fantasma", "MOEDA", -1.5);
    }

    @Test(expected = Exception.class)
    public void testConstructorShouldFailWithNegativeId() {
        new Country(-1, "País Fantasma", "MOEDA", 1.5);
    }

    @Test(expected = Exception.class)
    public void testConstructorShouldFailWithNullCurrency() {
        new Country(1, "País", null, 0);
    }

    @Test(expected = Exception.class)
    public void testConstructorShouldFailWithNullName() {
        new Country(1, null, "MOEDA", 0);
    }
}