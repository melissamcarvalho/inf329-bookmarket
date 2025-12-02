package recommendation;

import org.apache.mahout.cf.taste.model.DataModel;

import java.util.List;

import dominio.Book;

public interface BaseRecommender {
    /**
     * Refresh Mahout DataModel
     * @param model Mahout DataModel
     */
    public void refresh(DataModel model);

    /**
     * Run recommendation algorithm
     * @param customer_id Costumer ID
     * @param count Count of recommendations to be returned
     * @return List of recommended Books
     */
    public List<Book> recommend(int customer_id, int count);
}
