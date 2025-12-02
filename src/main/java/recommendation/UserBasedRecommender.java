package recommendation;

import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;

import java.util.Collections;
import java.util.List;
import dominio.Book;

public class UserBasedRecommender implements BaseMahoutRecommender {
    private DataModel model;
    private UserNeighborhood neighborhood;
    private UserSimilarity similarity;

    /**
     * @param model Mahout DataModel
     */
    public UserBasedRecommender(DataModel model) {
        // TODO
    }

    /**
     * Refresh Mahout DataModel
     * @param model Mahout DataModel
     */
    @Override
    public void refresh(DataModel model) {
        // TODO
    }

    /**
     * @param customer_id Costumer ID
     * @param count Count of recommendations to be returned
     * @return List of recommended Books
     */
    @Override
    public List<Book> recommend(int customer_id, int count) {
        // TODO
        return null;
    }
}
