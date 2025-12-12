package recommendation.mahout;

import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.recommender.Recommender;
import org.apache.mahout.cf.taste.recommender.UserBasedRecommender;
import org.apache.mahout.cf.taste.impl.neighborhood.NearestNUserNeighborhood;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;

import recommendation.RecommendationSettings;

import org.apache.mahout.cf.taste.impl.similarity.EuclideanDistanceSimilarity;
import org.apache.mahout.cf.taste.impl.similarity.PearsonCorrelationSimilarity;

import java.util.List;
import java.util.stream.Collectors;

public class UserBasedMahoutRecommender extends BaseMahoutRecommender {

    private UserSimilarity mahoutSimilarity;
    private UserNeighborhood mahoutNeighborhood;
    private int mahoutNeighborhoodSize = -1;
    private UserBasedRecommender mahoutUserRecommender;
    
    public UserBasedMahoutRecommender(RecommendationSettings settings, DataModel model) {
        super(settings, model);
    }
    
    /**
     * Get the current similarity metric
     * @return UserSimilarity instance
     */
    public UserSimilarity getMahoutSimilarity() {
        return this.mahoutSimilarity;
    }

    /**
     * Get the current neighborhood
     * @return UserNeighborhood instance
     */
    public UserNeighborhood getMahoutNeighborhood() {
        return this.mahoutNeighborhood;
    }

    /**
     * Get the current neighborhood size
     * @return neighborhood size
     */
    public int getMahoutNeighborhoodSize() {
        return mahoutNeighborhoodSize;
    }

    /**
     * Update Mahout components based on current settings
     */
    @Override
    protected void updateMahoutComponents() {
        this.mahoutNeighborhood = null;
        this.mahoutSimilarity = null;
        this.mahoutUserRecommender = null;
        this.mahoutNeighborhoodSize = -1;

        this.mahoutNeighborhoodSize = this.getSettings().getNeighborhoodSize();

        if (getModel() == null) {
            throw new IllegalStateException("DataModel is not set.");
        }
        if(getSettings() == null) {
            throw new IllegalStateException("RecommendationSettings is not set.");
        }

        switch(this.getSettings().getCorrelationSimilarity()) {
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
                throw new IllegalArgumentException("Unsupported similarity metric: " + this.getSettings().getCorrelationSimilarity());
        }

        switch(this.getSettings().getUserSimilarity()) {
            case nearestUserNeighborhood:
                try {
                    this.mahoutNeighborhood = new NearestNUserNeighborhood(
                            this.mahoutNeighborhoodSize,
                            this.mahoutSimilarity,
                            this.getModel()
                    );
                } catch (Exception e) {
                    throw new RuntimeException("Failed to create NearestNUserNeighborhood", e);
                }
                break;
            default:
                throw new IllegalArgumentException("Unsupported user similarity: " + this.getSettings().getUserSimilarity());
        }

        this.mahoutUserRecommender = new GenericUserBasedRecommender(
                this.getModel(),
                this.mahoutNeighborhood,
                this.mahoutSimilarity
        );
    }

    /**
     * Ensures both similarity and neighborhood are initialized with defaults if null
     */
    @Override
    protected void ensureMahoutInitialized() {
        if(this.mahoutNeighborhoodSize == -1) {
            this.mahoutNeighborhoodSize = this.getSettings().getNeighborhoodSize();
        }
        if(this.mahoutSimilarity == null) {
            throw new IllegalStateException("Similarity is not set.");
        }
        if (this.mahoutNeighborhood == null) {
            throw new IllegalStateException("Neighborhood is not set.");
        }
        if(this.mahoutUserRecommender == null) {
            throw new IllegalStateException("UserBasedRecommender is not set.");
        }
    }

    /**
     * @param customerId Costumer ID
     * @param count Count of recommendations to be returned
     * @return List of recommended Books
     */
    @Override
    public List<Integer> recommend(int customerId, int count) {
        // Ensure similarity and neighborhood are initialized with defaults if not set
        ensureMahoutInitialized();

        List<RecommendedItem> recommendations = null;
        try {
            recommendations = mahoutUserRecommender.recommend(customerId, count);
        } catch (org.apache.mahout.cf.taste.common.TasteException e) {
            throw new RuntimeException("Error while recommending books", e);
        }

       return recommendations.stream()
               .map(recommendation -> (int) recommendation.getItemID())
               .collect(Collectors.toList());
    }
}
