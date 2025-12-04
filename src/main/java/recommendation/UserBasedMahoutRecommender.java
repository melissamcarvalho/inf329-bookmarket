package recommendation;

import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.recommender.Recommender;
import org.apache.mahout.cf.taste.impl.neighborhood.NearestNUserNeighborhood;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;
import org.apache.mahout.cf.taste.impl.similarity.PearsonCorrelationSimilarity;

import java.util.List;

public class UserBasedMahoutRecommender implements BaseMahoutRecommender {
    private DataModel model;
    private UserSimilarity similarity;
    private UserNeighborhood neighborhood;
    private int neighborhoodSize = 2;

    /**
     * Constructor with only required data model.
     * Similarity defaults to Pearson Correlation if not set.
     * Neighborhood defaults to NearestNUserNeighborhood if not set.
     * 
     * @param model Mahout DataModel (required)
     */
    public UserBasedMahoutRecommender(DataModel model) {
        this.model = model;
    }

    /**
     * Get the current similarity metric
     * @return UserSimilarity instance
     */
    public UserSimilarity getSimilarity() {
        return this.similarity;
    }

    /**
     * Set the similarity metric. If null, defaults to Pearson Correlation Similarity
     * @param similarity User similarity metric (null for Pearson default)
     */
    public void setSimilarity(UserSimilarity similarity) {
        if (similarity != null) {
            this.similarity = similarity;
        } else {
            this.similarity = createDefaultSimilarity();
        }
    }

    /**
     * Creates default Pearson Correlation Similarity
     * @return PearsonCorrelationSimilarity instance
     */
    private UserSimilarity createDefaultSimilarity() {
        try {
            return new PearsonCorrelationSimilarity(model);
        } catch (Exception e) {
            throw new RuntimeException("Failed to create default Pearson similarity", e);
        }
    }

    /**
     * Get the current neighborhood
     * @return UserNeighborhood instance
     */
    public UserNeighborhood getNeighborhood() {
        return this.neighborhood;
    }

    /**
     * Set the neighborhood. If null, defaults to NearestNUserNeighborhood
     * @param neighborhood User neighborhood (null for default NearestNUserNeighborhood)
     */
    public void setNeighborhood(UserNeighborhood neighborhood) {
        if (neighborhood != null) {
            this.neighborhood = neighborhood;
        } else {
            this.neighborhood = createDefaultNeighborhood();
        }
    }

    /**
     * Creates default NearestNUserNeighborhood
     * @return NearestNUserNeighborhood instance
     */
    private UserNeighborhood createDefaultNeighborhood() {
        try {
            // Ensure similarity is initialized before creating neighborhood
            if (this.similarity == null) {
                this.similarity = createDefaultSimilarity();
            }
            return new NearestNUserNeighborhood(neighborhoodSize, this.similarity, model);
        } catch (Exception e) {
            throw new RuntimeException("Failed to create default neighborhood", e);
        }
    }

    /**
     * Ensures both similarity and neighborhood are initialized with defaults if null
     */
    private void ensureInitialized() {
        if (this.similarity == null) {
            this.similarity = createDefaultSimilarity();
        }
        if (this.neighborhood == null) {
            this.neighborhood = createDefaultNeighborhood();
        }
    }

    /**
     * Get the neighborhood size used for default neighborhood creation
     * @return neighborhood size
     */
    public int getNeighborhoodSize() {
        return this.neighborhoodSize;
    }

    /**
     * Set the neighborhood size used for default neighborhood creation
     * @param neighborhoodSize size of the neighborhood
     */
    public void setNeighborhoodSize(int neighborhoodSize) {
        this.neighborhoodSize = neighborhoodSize;
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
        // Ensure similarity and neighborhood are initialized with defaults if not set
        ensureInitialized();
        
        Recommender userBasedRecommender = new GenericUserBasedRecommender(model, neighborhood, similarity);
        List<RecommendedItem> recommendations = null;
        try {
            recommendations = userBasedRecommender.recommend(customer_id, count);
        } catch (org.apache.mahout.cf.taste.common.TasteException e) {
            throw new RuntimeException("Error while recommending books", e);
        }

        List<Integer> recommendedBooks = new java.util.ArrayList<>();
        if (recommendations.isEmpty()) {
            System.out.println("  No recommendations found!");
        } else {
            System.out.println("  Found " + recommendations.size() + " recommendations:");
            for (int i = 0; i < recommendations.size(); i++) {
                RecommendedItem recommendation = recommendations.get(i);
                long item_id = recommendation.getItemID();
                recommendedBooks.add((int) item_id);
            }
        }
        return recommendedBooks;
    }
}
