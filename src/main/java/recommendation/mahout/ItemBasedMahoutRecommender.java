package recommendation.mahout;

import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.recommender.ItemBasedRecommender;
import org.apache.mahout.cf.taste.impl.recommender.GenericItemBasedRecommender;
import org.apache.mahout.cf.taste.similarity.ItemSimilarity;

import recommendation.RecommendationSettings;

import org.apache.mahout.cf.taste.impl.similarity.EuclideanDistanceSimilarity;
import org.apache.mahout.cf.taste.impl.similarity.PearsonCorrelationSimilarity;

import java.util.List;
import java.util.stream.Collectors;

public class ItemBasedMahoutRecommender extends BaseMahoutRecommender {

    private ItemSimilarity mahoutSimilarity;
    private ItemBasedRecommender mahoutItemRecommender;

    public ItemBasedMahoutRecommender(RecommendationSettings settings, DataModel model) {
        super(settings, model);
    }

    /**
     * Get the current similarity metric
     * 
     * @return ItemSimilarity instance
     */
    public ItemSimilarity getMahoutSimilarity() {
        return this.mahoutSimilarity;
    }

    /**
     * Update Mahout components based on current settings
     * 
     * @throws MahoutRecommenderException if component initialization fails
     */
    @Override
    protected void updateMahoutComponents() {
        try {
            if (getModel() == null) {
                throw new MahoutRecommenderException("DataModel is not set.");
            }
            if (getSettings() == null) {
                throw new MahoutRecommenderException("RecommendationSettings is not set.");
            }

            // Set Mahout similarity based on settings
            switch (this.getSettings().getCorrelationSimilarity()) {
                case pearson:
                    try {
                        this.mahoutSimilarity = new PearsonCorrelationSimilarity(getModel());
                    } catch (Exception e) {
                        throw new MahoutRecommenderException("Failed to create Pearson similarity. " +
                                "This may be due to insufficient data or invalid preferences in the DataModel.", e);
                    }
                    break;
                case euclideanDistance:
                    try {
                        this.mahoutSimilarity = new EuclideanDistanceSimilarity(getModel());
                    } catch (Exception e) {
                        throw new MahoutRecommenderException("Failed to create Euclidean Distance similarity. " +
                                "This may be due to insufficient data or invalid preferences in the DataModel.", e);
                    }
                    break;
                default:
                    throw new MahoutRecommenderException(
                            "Unsupported similarity metric: " + this.getSettings().getCorrelationSimilarity());
            }

            // Create the ItemBasedRecommender
            try {
                this.mahoutItemRecommender = new GenericItemBasedRecommender(
                        this.getModel(),
                        this.mahoutSimilarity);
            } catch (Exception e) {
                throw new MahoutRecommenderException("Failed to create item-based recommender. " +
                        "Verify that the DataModel contains valid item preferences.", e);
            }
        } catch (MahoutRecommenderException e) {
            // Re-throw our custom exceptions
            throw e;
        } catch (Exception e) {
            // Catch any unexpected exceptions
            throw new MahoutRecommenderException("Unexpected error during component initialization", e);
        }
    }

    /**
     * Generate recommendations based on Item
     * 
     * @param itemId Item ID
     * @param count      Count of recommendations to be returned
     * @return List of recommended Books
     * @throws MahoutRecommenderException if recommendation fails or user/item data
     *                                    is inconsistent
     */
    @Override
    public List<Integer> recommend(int itemId, int count) {
        // Validate input parameters
        validateRecommendationParameters(itemId, count);

        if (mahoutItemRecommender == null) {
            throw new MahoutRecommenderException("Item-based recommender is not initialized. " +
                    "Call updateMahoutComponents() first.");
        }

        List<RecommendedItem> recommendations = null;
        try {
            recommendations = mahoutItemRecommender.recommend(itemId, count);
        } catch (org.apache.mahout.cf.taste.common.NoSuchUserException e) {
            throw new MahoutRecommenderException("User with ID " + itemId + " not found in DataModel", e);
        } catch (org.apache.mahout.cf.taste.common.TasteException e) {
            throw new MahoutRecommenderException("Error generating recommendations for user " + itemId + ". " +
                    "This may be due to insufficient data or inconsistencies in the DataModel.", e);
        } catch (Exception e) {
            throw new MahoutRecommenderException("Unexpected error during recommendation generation", e);
        }

        return recommendations.stream()
                .map(recommendation -> (int) recommendation.getItemID())
                .collect(Collectors.toList());
    }

}
