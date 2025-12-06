package dominio;



/**
 * <img src="./doc-files/Evaluation.png" alt="Bookmarket">
 * <br><a href="./doc-files/Evaluation.html"> code </a>
 *
 */
public class Evaluation {

    public int userId;
    public int itemId;
    public double score;

    public Evaluation(int userId, int itemId, double score) {
        this.userId = userId;
        this.itemId = itemId;
        this.score = score;
    }
    
}
