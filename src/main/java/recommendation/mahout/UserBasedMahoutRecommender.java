package recommendation.mahout;

import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
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
    private int mahoutNeighborhoodSize = 2;
    private UserBasedRecommender mahoutUserRecommender;

    public UserBasedMahoutRecommender(RecommendationSettings settings, DataModel model) {
        super(settings, model);
    }

    /**
     * Get the current similarity metric
     * 
     * @return UserSimilarity instance
     */
    public UserSimilarity getMahoutSimilarity() {
        return this.mahoutSimilarity;
    }

    /**
     * Get the current neighborhood
     * 
     * @return UserNeighborhood instance
     */
    public UserNeighborhood getMahoutNeighborhood() {
        return this.mahoutNeighborhood;
    }

    /**
     * Get the current neighborhood size
     * 
     * @return neighborhood size
     */
    public int getMahoutNeighborhoodSize() {
        return mahoutNeighborhoodSize;
    }

    /**
     * Update Mahout components based on current settings
     * 
     * @throws MahoutRecommenderException if component initialization fails
     */
    @Override
    protected void updateMahoutComponents() {
        try {
            // Set Mahout neighborhood size from settings
            this.mahoutNeighborhoodSize = this.getSettings().getNeighborhoodSize();

            if (getModel() == null) {
                throw new MahoutRecommenderException("DataModel is not set.");
            }
            if (getSettings() == null) {
                throw new MahoutRecommenderException("RecommendationSettings is not set.");
            }

            if (this.mahoutNeighborhoodSize <= 0) {
                throw new MahoutRecommenderException("Neighborhood size must be positive, got: " +
                        this.mahoutNeighborhoodSize);
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

            // Set Mahout neighborhood based on settings
            switch (this.getSettings().getUserSimilarity()) {
                case nearestUserNeighborhood:
                    try {
                        this.mahoutNeighborhood = new NearestNUserNeighborhood(
                                this.mahoutNeighborhoodSize,
                                this.mahoutSimilarity,
                                this.getModel());
                    } catch (Exception e) {
                        throw new MahoutRecommenderException("Failed to create NearestNUserNeighborhood. " +
                                "Verify that the DataModel has enough users for the specified neighborhood size (" +
                                this.mahoutNeighborhoodSize + ").", e);
                    }
                    break;
                default:
                    throw new MahoutRecommenderException(
                            "Unsupported user similarity: " + this.getSettings().getUserSimilarity());
            }

            // Create the UserBasedRecommender
            try {
                this.mahoutUserRecommender = new GenericUserBasedRecommender(
                        this.getModel(),
                        this.mahoutNeighborhood,
                        this.mahoutSimilarity);
            } catch (Exception e) {
                throw new MahoutRecommenderException("Failed to create user-based recommender. " +
                        "Verify that the DataModel contains valid user preferences.", e);
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
     * Generate recommendations for a customer
     * 
     * @param customerId Customer ID
     * @param count      Count of recommendations to be returned
     * @return List of recommended Books
     * @throws MahoutRecommenderException if recommendation fails or user data is
     *                                    inconsistent
     */
    @Override
    public List<Integer> recommend(int customerId, int count) {
        // Validate input parameters
        validateRecommendationParameters(customerId, count);

        if (mahoutUserRecommender == null) {
            throw new MahoutRecommenderException("User-based recommender is not initialized. " +
                    "Call updateMahoutComponents() first.");
        }

        List<RecommendedItem> recommendations = null;
        try {
            recommendations = mahoutUserRecommender.recommend(customerId, count);
        } catch (org.apache.mahout.cf.taste.common.NoSuchUserException e) {
            throw new MahoutRecommenderException("User with ID " + customerId + " not found in DataModel", e);
        } catch (org.apache.mahout.cf.taste.common.TasteException e) {
            throw new MahoutRecommenderException("Error generating recommendations for user " + customerId + ". " +
                    "This may be due to insufficient data, insufficient neighbors, or inconsistencies in the DataModel.",
                    e);
        } catch (Exception e) {
            throw new MahoutRecommenderException("Unexpected error during recommendation generation", e);
        }

        return recommendations.stream()
                .map(recommendation -> (int) recommendation.getItemID())
                .collect(Collectors.toList());
    }
}
