package recommendation.mahout;

import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.common.TasteException;

import recommendation.RecommendationSettings;

import java.util.List;

public abstract class BaseMahoutRecommender {
    private RecommendationSettings settings;

    private DataModel model;

    public BaseMahoutRecommender(RecommendationSettings settings, DataModel model) {
        validateSettings(settings);
        validateDataModelNotNull(model);

        this.settings = settings;
        this.model = model;

        try {
            this.updateMahoutComponents();
        } catch (Exception e) {
            throw new MahoutRecommenderException("Failed to initialize Mahout recommender components", e);
        }
    }

    /**
     * Refresh Mahout DataModel
     * 
     * @param model Mahout DataModel
     * @throws MahoutRecommenderException if the model is invalid or component
     *                                    update fails
     */
    public void refresh(DataModel model) {
        validateDataModelNotNull(model);

        this.model = model;

        try {
            this.updateMahoutComponents();
        } catch (Exception e) {
            throw new MahoutRecommenderException("Failed to refresh Mahout recommender with new DataModel", e);
        }
    }

    /**
     * Get the current Mahout DataModel
     * 
     * @return DataModel instance
     */
    public DataModel getModel() {
        return model;
    }

    /**
     * Get the current recommendation settings
     * 
     * @return RecommendationSettings instance
     */
    public RecommendationSettings getSettings() {
        return settings;
    }

    /**
     * Set new recommendation settings
     * 
     * @param settings new recommendation settings
     * @throws MahoutRecommenderException if settings are invalid or component
     *                                    update fails
     */
    public void setSettings(RecommendationSettings settings) {
        validateSettings(settings);

        this.settings = settings;

        try {
            this.updateMahoutComponents();
        } catch (Exception e) {
            throw new MahoutRecommenderException("Failed to update Mahout recommender with new settings", e);
        }
    }

    /**
     * Update Mahout components based on current settings
     * 
     * @throws MahoutRecommenderException if component initialization fails
     */
    protected abstract void updateMahoutComponents();

    /**
     * Recommend items for a given id
     * 
     * @param id    user or item ID
     * @param count number of recommendations
     * @return list of recommended item IDs
     * @throws MahoutRecommenderException if recommendation fails
     */
    public abstract List<Integer> recommend(int id, int count);

    /**
     * Validate recommendation settings
     * 
     * @param settings settings to validate
     * @throws MahoutRecommenderException if settings are null or invalid
     */
    protected void validateSettings(RecommendationSettings settings) {
        if (settings == null) {
            throw new MahoutRecommenderException("RecommendationSettings cannot be null");
        }

        if (settings.getCorrelationSimilarity() == null) {
            throw new MahoutRecommenderException("Correlation similarity cannot be null");
        }

        if (settings.getUserSimilarity() == null) {
            throw new MahoutRecommenderException("User similarity cannot be null");
        }

        if (settings.getNeighborhoodSize() <= 0) {
            throw new MahoutRecommenderException(
                    "Neighborhood size must be positive, got: " + settings.getNeighborhoodSize());
        }
    }

    /**
     * Validate DataModel is not null (basic check)
     * 
     * @param model DataModel to validate
     * @throws MahoutRecommenderException if model is null
     */
    protected void validateDataModelNotNull(DataModel model) {
        if (model == null) {
            throw new MahoutRecommenderException("DataModel cannot be null");
        }
    }

    /**
     * Validate DataModel has data (used before operations)
     * 
     * @param model DataModel to validate
     * @throws MahoutRecommenderException if model is null or contains invalid data
     */
    protected void validateDataModel(DataModel model) {
        validateDataModelNotNull(model);

        try {
            int numUsers = model.getNumUsers();
            int numItems = model.getNumItems();

            if (numUsers == 0) {
                throw new MahoutRecommenderException("DataModel has no users. " +
                        "Cannot perform recommendation operations on an empty model.");
            }

            if (numItems == 0) {
                throw new MahoutRecommenderException("DataModel has no items. " +
                        "Cannot perform recommendation operations on an empty model.");
            }

        } catch (TasteException e) {
            throw new MahoutRecommenderException("Error accessing DataModel information", e);
        }
    }

    /**
     * Validate recommendation parameters
     * 
     * @param id    user or item ID to validate
     * @param count number of recommendations requested
     * @throws MahoutRecommenderException if parameters are invalid
     */
    protected void validateRecommendationParameters(long id, int count) {
        if (id < 0) {
            throw new MahoutRecommenderException("ID must be non-negative, got: " + id);
        }

        if (count <= 0) {
            throw new MahoutRecommenderException(
                    "Recommendation count must be positive, got: " + count);
        }
    }
}
