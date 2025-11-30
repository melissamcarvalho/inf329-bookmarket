package recommendation;

import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;

import java.util.Collections;
import java.util.List;

public class UserBasedRecommender implements BaseRecommender {
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
     * @return List of recommended Book IDs
     */
    @Override
    public List<Integer> recommend(int customer_id, int count) {
        // TODO
        return null;
    }
}
