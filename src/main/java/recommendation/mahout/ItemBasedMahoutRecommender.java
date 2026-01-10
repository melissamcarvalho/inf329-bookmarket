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
     */
    @Override
    protected void updateMahoutComponents() {

        if (getModel() == null) {
            throw new IllegalStateException("DataModel is not set.");
        }
        if (getSettings() == null) {
            throw new IllegalStateException("RecommendationSettings is not set.");
        }

        switch (this.getSettings().getCorrelationSimilarity()) {
            case pearson:
                try {
                    this.mahoutSimilarity = new PearsonCorrelationSimilarity(getModel());
                } catch (Exception e) {
                    throw new RuntimeException("Failed to create Pearson similarity", e);
                }
                break;
            case euclideanDistance:
                try {
                    this.mahoutSimilarity = new EuclideanDistanceSimilarity(getModel());
                } catch (Exception e) {
                    throw new RuntimeException("Failed to create EuclideanDistanceSimilarity", e);
                }
                break;
            default:
                throw new IllegalArgumentException(
                        "Unsupported similarity metric: " + this.getSettings().getCorrelationSimilarity());
        }

        this.mahoutItemRecommender = new GenericItemBasedRecommender(
                this.getModel(),
                this.mahoutSimilarity);
    }

    /**
     * @param customerId Customer ID
     * @param count      Count of recommendations to be returned
     * @return List of recommended Books
     */
    @Override
    public List<Integer> recommend(int customerId, int count) {

        List<RecommendedItem> recommendations = null;
        try {
            recommendations = mahoutItemRecommender.recommend(customerId, count);
        } catch (org.apache.mahout.cf.taste.common.TasteException e) {
            throw new RuntimeException("Error while recommending books", e);
        }

        return recommendations.stream()
                .map(recommendation -> (int) recommendation.getItemID())
                .collect(Collectors.toList());
    }

}
