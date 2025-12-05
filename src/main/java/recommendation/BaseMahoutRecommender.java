package recommendation;

import org.apache.mahout.cf.taste.model.DataModel;

import java.util.List;

public interface BaseMahoutRecommender {
    /**
     * Refresh Mahout DataModel
     * @param model Mahout DataModel
     */
    public void refresh(DataModel model);

    /**
     * Run recommendation algorithm
     * @param customer_id Costumer ID
     * @param count Count of recommendations to be returned
     * @return List of recommended Book IDs
     */
    public List<Integer> recommend(int customer_id, int count);
}
