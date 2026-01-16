package dominio;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.Date;

public class BookTest {

    private Book book;
    private Author author;
    private Date now;
    private Date pubDate;
    private Date availDate;
    private int[] dims = {10, 15, 2};

    @Before
    public void setUp() {
        now = new Date();
        pubDate = new Date();
        availDate = new Date(pubDate.getTime() + 86400000); // Amanhã
        author = new Author("John", "D.", "Doe", now, "Bio text");

        // Criando a instância principal
        book = new Book(
                1, "Java Programming", pubDate, "Tech Press",
                SUBJECTS.ARTS, "A great book", "thumb.jpg", "full.jpg",
                49.90, availDate, "123456789", 500, BACKINGS.HARDBACK,
                dims, 1.2, author
        );
    }

    @Test
    public void testGetters() {
        assertEquals(1, book.getId());
        assertEquals("Java Programming", book.getTitle());
        assertEquals("Tech Press", book.getPublisher());
        assertEquals(SUBJECTS.ARTS, book.getSubject());
        assertEquals(49.90, book.getSrp(), 0.001); // Delta para double
        assertEquals(author, book.getAuthor());
        assertArrayEquals(dims, book.getDimensions());
        assertEquals(500, book.getPage());
    }

    @Test
    public void testSettersAndState() {
        // Testando setters de imagem
        book.setImage("new_image.jpg");
        assertEquals("new_image.jpg", book.getImage());

        book.setThumbnail("new_thumb.jpg");
        assertEquals("new_thumb.jpg", book.getThumbnail());

        // Testando setter de data
        Date newDate = new Date(0); // 1970
        book.setPubDate(newDate);
        assertEquals(newDate, book.getPubDate());
    }

    @Test
    public void testRelatedBooks() {
        // Mock de um livro relacionado
        Book related = new Book(2, "Related Book", now, "Publisher",
                SUBJECTS.BIOGRAPHIES, "Desc", "t.jpg", "i.jpg",
                20.0, now, "987", 100, BACKINGS.PAPERBACK,
                new int[]{1,1,1}, 0.5, author);

        assertNull("Relacionado 1 deve começar nulo", book.getRelated1());

        book.setRelated1(related);
        assertNotNull(book.getRelated1());
        assertEquals(2, book.getRelated1().getId());
    }

    @Test
    public void testEqualsAndHashCode() {
        // Para a classe Book, a igualdade é baseada APENAS no ID
        Book bookSameId = new Book(1, "Outro Titulo", now, "Outra Editora",
                SUBJECTS.COOKING, "Outra Desc", "t.jpg", "i.jpg",
                10.0, now, "000", 50, BACKINGS.PAPERBACK,
                new int[]{1,1,1}, 0.1, author
        );

        Book bookDifferentId = new Book(2, "Java Programming", now, "Tech Press",
                SUBJECTS.ARTS, "Desc", "t.jpg", "i.jpg",
                49.90, now, "123", 500, BACKINGS.HARDBACK,
                dims, 1.2, author
        );

        // Teste de Equals
        assertEquals("Livros com mesmo ID devem ser iguais", book, bookSameId);
        assertNotEquals("Livros com IDs diferentes devem ser diferentes", book, bookDifferentId);

        // Teste de HashCode
        assertEquals("HashCodes devem ser iguais para IDs iguais", book.hashCode(), bookSameId.hashCode());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorShouldFailWithNegativeWeight() {
        new Book(
                1, "Java Programming", now, "Tech Press",
                SUBJECTS.ARTS, "A great book", "thumb.jpg", "full.jpg",
                49.90, now, "123456789", 500, BACKINGS.HARDBACK,
                dims, -1.0, author
        );
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorShouldFailWithNegativePrice() {
        new Book(
                1, "Java Programming", now, "Tech Press",
                SUBJECTS.ARTS, "A great book", "thumb.jpg", "full.jpg",
                -49.90, now, "123456789", 500, BACKINGS.HARDBACK,
                dims, 1.0, author
        );
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorShouldFailWithNegativePageCount() {
        new Book(
                1, "Java Programming", now, "Tech Press",
                SUBJECTS.ARTS, "A great book", "thumb.jpg", "full.jpg",
                49.90, now, "123456789", -1, BACKINGS.HARDBACK,
                dims, 1.0, author
        );
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorShouldFailWithNullAuthor() {
        new Book(
                1, "Java Programming", now, "Tech Press",
                SUBJECTS.ARTS, "A great book", "thumb.jpg", "full.jpg",
                49.90, now, "123456789", 500, BACKINGS.HARDBACK,
                dims, 1.2, null
        );
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorShouldFailWithNullDimensions() {
        new Book(
                1, "Java Programming", now, "Tech Press",
                SUBJECTS.ARTS, "A great book", "thumb.jpg", "full.jpg",
                49.90, now, "123456789", 500, BACKINGS.HARDBACK,
                null, 1.0, author
        );
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorShouldFailWithNullBackings() {
        new Book(
                1, "Java Programming", now, "Tech Press",
                SUBJECTS.ARTS, "A great book", "thumb.jpg", "full.jpg",
                49.90, now, "123456789", 500, null,
                dims, 1.0, author
        );
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorShouldFailWithNullISBN() {
        new Book(
                1, "Java Programming", now, "Tech Press",
                SUBJECTS.ARTS, "A great book", "thumb.jpg", "full.jpg",
                49.90, now, null, 500, BACKINGS.HARDBACK,
                dims, 1.0, author
        );
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorShouldFailWithNullAvailability() {
        new Book(
                1, "Java Programming", now, "Tech Press",
                SUBJECTS.ARTS, "A great book", "thumb.jpg", "full.jpg",
                49.90, null, "123456789", 500, BACKINGS.HARDBACK,
                dims, 1.0, author
        );
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorShouldFailWithNullImage() {
        new Book(
                1, "Java Programming", now, "Tech Press",
                SUBJECTS.ARTS, "A great book", "thumb.jpg", null,
                49.90, now, "123456789", 500, BACKINGS.HARDBACK,
                dims, 1.0, author
        );
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorShouldFailWithNullThumbnail() {
        new Book(
                1, "Java Programming", now, "Tech Press",
                SUBJECTS.ARTS, "A great book", null, "full.jpg",
                49.90, now, "123456789", 500, BACKINGS.HARDBACK,
                dims, 1.0, author
        );
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorShouldFailWithNullDescription() {
        new Book(
                1, "Java Programming", now, "Tech Press",
                SUBJECTS.ARTS, null, "thumb.jpg", "full.jpg",
                49.90, now, "123456789", 500, BACKINGS.HARDBACK,
                dims, 1.0, author
        );
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorShouldFailWithNullSubject() {
        new Book(
                1, "Java Programming", now, "Tech Press",
                null, "A great book", "thumb.jpg", "full.jpg",
                49.90, now, "123456789", 500, BACKINGS.HARDBACK,
                dims, 1.0, author
        );
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorShouldFailWithNullPublisher() {
        new Book(
                1, "Java Programming", now, null,
                SUBJECTS.ARTS, "A great book", "thumb.jpg", "full.jpg",
                49.90, now, "123456789", 500, BACKINGS.HARDBACK,
                dims, 1.0, author
        );
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorShouldFailWithNullPublishDate() {
        new Book(
                1, "Java Programming", null, "Tech Press",
                SUBJECTS.ARTS, "A great book", "thumb.jpg", "full.jpg",
                49.90, now, "123456789", 500, BACKINGS.HARDBACK,
                dims, 1.0, author
        );
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorShouldFailWithNullTitle() {
        new Book(
                1, null, now, "Tech Press",
                SUBJECTS.ARTS, "A great book", "thumb.jpg", "full.jpg",
                49.90, now, "123456789", 500, BACKINGS.HARDBACK,
                dims, 1.0, author
        );
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorShouldFailWithNegativeId() {
        new Book(
                -1, "Java Programming", now, "Tech Press",
                SUBJECTS.ARTS, "A great book", "thumb.jpg", "full.jpg",
                49.90, now, "123456789", 500, BACKINGS.HARDBACK,
                dims, 1.0, author
        );
    }

    @Test
    public void testConstructorShouldNotBeAffectedByExternalDateMutation() {
        long originalTime = pubDate.getTime();

        // Tentativa de sabotagem externa: alterando o objeto Date original
        pubDate.setTime(0);

        assertEquals("VULNERABILIDADE: O estado interno do Book foi alterado através da referência externa no construtor!",
                originalTime, book.getPubDate().getTime());
    }

    @Test
    public void testGetterShouldNotReturnMutableReference() {
        Date returnedDate = book.getPubDate();
        long originalTime = returnedDate.getTime();

        // Tentativa de sabotagem via Getter: alterando o objeto retornado
        returnedDate.setTime(0);

        assertEquals("VULNERABILIDADE: O estado interno do Book foi alterado através da referência retornada pelo Getter!",
                originalTime, book.getPubDate().getTime());
    }
}