package dominio;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.Date;
import java.util.Calendar;

public class AuthorTest {

    private Author autor;
    private Date dataNascimento;
    private String biografia;

    @Before
    public void setUp() {
        // Criando uma data fixa para o teste: 15 de Maio de 1980
        Calendar cal = Calendar.getInstance();
        cal.set(1980, Calendar.MAY, 15, 0, 0, 0);
        cal.set(Calendar.MILLISECOND, 0);
        dataNascimento = cal.getTime();

        biografia = "Escritor de ficção científica com mais de 20 obras publicadas.";

        autor = new Author(
                "Isaac",
                "Yudick",
                "Asimov",
                dataNascimento,
                biografia
        );
    }

    @Test
    public void testGetters() {
        // Verificando Strings
        assertEquals("Isaac", autor.getFname());
        assertEquals("Yudick", autor.getMname());
        assertEquals("Asimov", autor.getLname());
        assertEquals(biografia, autor.getBio());

        // Verificando a Data
        assertNotNull(autor.getBirthdate());
        assertEquals(dataNascimento.getTime(), autor.getBirthdate().getTime());
    }

    @Test
    public void testDateMutationLeak() {
        // java.util.Date é mutável. Se passarmos uma data e alterarmos ela fora...
        Date dataOriginal = new Date();
        Author author = new Author("Nome", "", "Sobrenome", dataOriginal, "Bio");

        long tempoOriginal = dataOriginal.getTime();
        dataOriginal.setTime(0L); // Alterando a data externamente

        assertEquals("VULNERABILIDADE: O objeto Author teve sua data alterada externamente (Falta de cópia defensiva)",
                tempoOriginal, author.getBirthdate().getTime());
    }

    @Test(expected = Exception.class)
    public void testConstructorShouldFailWithNullBio() {
        new Author("Isaac","Yudick","Asimov", dataNascimento, null);
    }

    @Test(expected = Exception.class)
    public void testConstructorShouldFailWithNullBirthdate() {
        new Author("Isaac","Yudick","Asimov", null, biografia);
    }

    @Test(expected = Exception.class)
    public void testConstructorShouldFailWithNullLastName() {
        new Author("Isaac","Yudick",null, dataNascimento, biografia);
    }

    @Test(expected = Exception.class)
    public void testConstructorShouldFailWithNullMiddleName() {
        new Author("Isaac",null,"Asimov", dataNascimento, biografia);
    }

    @Test(expected = Exception.class)
    public void testConstructorShouldFailWithNullFirstName() {
        new Author(null,"Yudick","Asimov", dataNascimento, biografia);
    }
}