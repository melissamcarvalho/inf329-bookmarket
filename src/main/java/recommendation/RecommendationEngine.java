package recommendation;

import dominio.Evaluation;

import java.util.List;

public class RecommendationEngine {
    private final BaseRecommender userBasedRecommender;
    private final BaseRecommender itemBasedRecommender;
    private List<Evaluation> evaluations;

    /**
     * Acts as an Adapter for Evaluation to the DataModel Mahout expects as input.
     * @param evaluations List of evaluation to be loaded as Model
     */
    public RecommendationEngine(List<Evaluation> evaluations) {
        // TODO: Adapt List<Evaluation> to DataModel

        userBasedRecommender = new UserBasedRecommender(null);
        itemBasedRecommender = new ItemBasedRecommender(null);
    }

    /**
     * Refresh Mahout DataModel with new Evaluations
     * @param evaluations List of evaluation to be refreshed as Mahout Model
     */
    public void refreshModel(List<Evaluation> evaluations) {
        // TODO
    }

    /**
     * Recommend by Items
     * @param customer_id Customer ID
     * @return List of Book IDs
     */
    public List<Integer> recommendByItems(int customer_id) {
        // TODO
        return null;
    }

    /**
     * Recommend by Users
     * @param customer_id Customer ID
     * @return List of Book IDs
     */
    public List<Integer> recommendByUsers(int customer_id) {
        // TODO
        return null;
    }
}
