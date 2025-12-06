package recommendation;

import dominio.Evaluation;
import recommendation.mahout.BaseMahoutRecommender;
import recommendation.mahout.ItemBasedMahoutRecommender;
import recommendation.mahout.UserBasedMahoutRecommender;

import java.util.List;

import org.apache.mahout.cf.taste.impl.common.FastByIDMap;
import org.apache.mahout.cf.taste.impl.model.GenericDataModel;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.model.PreferenceArray;

public class RecommendationEngine {
    private final BaseMahoutRecommender userBasedMahoutRecommender;
    // private final BaseMahoutRecommender itemBasedMahoutRecommender;
    private List<Evaluation> evaluations;
    private RecommendationSettings settings;
    
    /**
     * Acts as an Adapter for Evaluation to the DataModel Mahout expects as input.
     * @param evaluations List of evaluation to be loaded as Model
     */
    public RecommendationEngine(List<Evaluation> evaluations, RecommendationSettings settings) {
        this.settings = settings;
        this.evaluations = evaluations;

        DataModel mahoutDataModel = buildMahoutDataModel();
        userBasedMahoutRecommender = new UserBasedMahoutRecommender(settings, mahoutDataModel);
        // itemBasedMahoutRecommender = new ItemBasedMahoutRecommender(settings, mahoutDataModel);
    }

    /**
     * 
     * @param settings 
     */
    public void setSettings(final RecommendationSettings settings) {
        userBasedMahoutRecommender.setSettings(settings);
        // itemBasedMahoutRecommender.setSettings(settings);
    }

    /**
     * Refresh Mahout DataModel with new Evaluations
     * @param evaluations List of evaluation to be refreshed as Mahout Model
     */
    public void refreshModel(List<Evaluation> evaluations) {
        DataModel mahoutDataModel = buildMahoutDataModel();

        this.userBasedMahoutRecommender.refresh(mahoutDataModel);
        // this.itemBasedMahoutRecommender.refresh(mahoutDataModel);
    }

    private DataModel buildMahoutDataModel() {
        // TODO: Build Mahout DataModel from Evaluations
        FastByIDMap<PreferenceArray> userData =  new FastByIDMap<PreferenceArray>();
        
        return new GenericDataModel(userData);

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
