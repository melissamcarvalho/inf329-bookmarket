package recommendation.mahout;

import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.recommender.Recommender;
import org.apache.mahout.cf.taste.impl.neighborhood.NearestNUserNeighborhood;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;

import recommendation.RecommendationSettings;
import recommendation.mahout.BaseMahoutRecommender;

import org.apache.mahout.cf.taste.impl.similarity.PearsonCorrelationSimilarity;

import java.util.List;

public abstract class BaseMahoutRecommender {
    private RecommendationSettings settings;

    private DataModel model;

    public BaseMahoutRecommender(RecommendationSettings settings, DataModel model) {
        this.settings = settings;
        this.model = model; 
        
        this.updateMahoutComponents();
    }

    /**
     * Refresh Mahout DataModel
     * @param model Mahout DataModel
     */
    public void refresh(DataModel model) {
        this.model = model;

        this.updateMahoutComponents();
    }

    /**
     * Get the current Mahout DataModel
     * @return DataModel instance
     */
    public DataModel getModel() {
        return model;
    }

    /**
     * Get the current recommendation settings
     * @return RecommendationSettings instance
     */
    public RecommendationSettings getSettings() {
        return settings;
    }

    /**
     * Set new recommendation settings
     * @param settings
     */
    public void setSettings(RecommendationSettings settings) {
        this.settings = settings;

        this.updateMahoutComponents();
    }

    /**
     * Ensure the recommender is initialized
     */
    protected abstract void ensureMahoutInitialized();
    
    /**
     * Update Mahout components based on current settings
     */
    protected abstract void updateMahoutComponents();

    /**
     * Recommend items for a given id
     */
    public abstract List<Integer> recommend(int id, int count);

}
