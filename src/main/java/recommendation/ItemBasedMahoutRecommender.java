package recommendation;

import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.similarity.ItemSimilarity;

import java.util.List;

public class ItemBasedMahoutRecommender implements BaseMahoutRecommender {
    private DataModel model;
    private ItemSimilarity similarity;

    /**
     * @param model Mahout DataModel
     */
    public ItemBasedMahoutRecommender(DataModel model) {
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
    public List<Integer> recommend(int customer_id, int count) {
        // TODO
        return null;
    }
}
