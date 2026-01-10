package dominio;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.Date;

public class CartLineTest {

    private CartLine cartLine;
    private Stock stock;

    @Before
    public void setUp() {
        Date now = new Date();
        Author author = new Author("James", "G.", "Gosling", now, "Bio");
        Country country = new Country(1, "Brasil", "BRL", 0);

        // Criando o livro
        Book book = new Book(
                1, "Java Essentials", now, "TechPub", SUBJECTS.ARTS,
                "Description", "t.jpg", "i.jpg", 50.0, now, "ISBN123",
                300, BACKINGS.PAPERBACK, new int[]{1, 2, 3}, 0.8, author
        );

        // Criando o estoque (Assumindo que Stock recebe Book e quantidade disponível)
        Address addr = new Address(1, "Rua", "", "Cidade", "ST", "zip", country);
        stock = new Stock(1, addr, book, 45.0, 100);

        // Criando a linha do carrinho com quantidade inicial 2
        cartLine = new CartLine(2, stock);
    }

    @Test
    public void testGetters() {
        assertEquals("A quantidade inicial deve ser 2", 2, cartLine.getQty());
        assertEquals("O estoque associado deve ser o mesmo", stock, cartLine.getStock());
        assertNotNull("O livro não deve ser nulo", cartLine.getBook());
        assertEquals("O ID do livro deve ser 1", 1, cartLine.getBook().getId());
        assertEquals("O título deve ser Java Essentials", "Java Essentials", cartLine.getBook().getTitle());
    }

    @Test
    public void testSetQty() {
        cartLine.setQty(10);
        assertEquals("A quantidade deve ter sido atualizada para 10", 10, cartLine.getQty());
    }

    @Test
    public void testUpdateQtyToZero() {
        cartLine.setQty(0);
        assertEquals(0, cartLine.getQty());
    }

    @Test(expected = Exception.class)
    public void testUpdateQtyToNegative() {
        cartLine.setQty(-1);
    }

    @Test(expected = Exception.class)
    public void testConstructorShouldFailWithNullStock() {
        new CartLine(1, null);
    }

    @Test(expected = Exception.class)
    public void testConstructorShouldFailWithNegativeId() {
        new CartLine(-1, stock);
    }
}