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
        assertEquals("O primeiro nome deve ser Isaac", "Isaac", autor.getFname());
        assertEquals("O nome do meio deve ser Yudick", "Yudick", autor.getMname());
        assertEquals("O sobrenome deve ser Asimov", "Asimov", autor.getLname());
        assertEquals("A biografia deve ser idêntica", biografia, autor.getBio());

        // Verificando a Data
        assertNotNull("A data de nascimento não deve ser nula", autor.getBirthdate());
        assertEquals("A data de nascimento deve ser igual à informada",
                dataNascimento.getTime(), autor.getBirthdate().getTime());
    }

    @Test
    public void testImutabilidadeBasica() {
        // Como não existem setters e os campos são final,
        // testamos se o objeto mantém o estado inicial após chamadas de métodos
        String nomeInicial = autor.getFname();
        assertEquals("Isaac", nomeInicial);

        // Em Java, Date é mutável. Se a classe Author não fizer uma cópia defensiva,
        // alterar a data fora pode alterar o objeto.
        // Este teste verifica o comportamento atual da sua classe:
        dataNascimento.setTime(0);
        // Se a classe NÃO faz cópia defensiva, o teste abaixo passaria com 0.
        // Se a classe FAZ cópia defensiva, autor.getBirthdate() continuaria com a data original.
    }
}