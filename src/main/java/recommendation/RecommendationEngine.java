package recommendation;

import dominio.Customer;
import dominio.Evaluation;
import org.apache.mahout.cf.taste.impl.model.GenericUserPreferenceArray;
import recommendation.mahout.ItemBasedMahoutRecommender;
import recommendation.mahout.MahoutEvaluation;
import recommendation.mahout.UserBasedMahoutRecommender;

import java.util.*;
import java.util.stream.Collectors;

import org.apache.mahout.cf.taste.impl.common.FastByIDMap;
import org.apache.mahout.cf.taste.impl.model.GenericDataModel;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.model.PreferenceArray;

public class RecommendationEngine {
    private List<MahoutEvaluation> mahoutEvaluations;

    private final UserBasedMahoutRecommender userBasedMahoutRecommender;
    private final ItemBasedMahoutRecommender itemBasedMahoutRecommender;
    
    /**
     * Acts as an Adapter for Evaluation to the DataModel Mahout expects as input.
     * @param evaluations List of evaluation to be loaded as Model
     * @param settings Recommendation engine settings
     */
    public RecommendationEngine(List<Evaluation> evaluations, RecommendationSettings settings) {
        this.mahoutEvaluations = buildMahoutEvaluations(evaluations);

        DataModel mahoutDataModel = buildMahoutDataModel();

        userBasedMahoutRecommender = new UserBasedMahoutRecommender(settings, mahoutDataModel);
        itemBasedMahoutRecommender = new ItemBasedMahoutRecommender(settings, mahoutDataModel);
    }

    /**
     * Set Recommendation Engine Settings
     * 
     * @param settings Recommendation engine settings
     */
    public void setSettings(final RecommendationSettings settings) {
        userBasedMahoutRecommender.setSettings(settings);
        itemBasedMahoutRecommender.setSettings(settings);
    }

    /**
     * Refresh Mahout DataModel with new Evaluations
     * 
     * @param evaluations List of evaluation to be refreshed as Mahout Model
     */
    public void refreshModel(List<Evaluation> evaluations) {
        this.mahoutEvaluations = buildMahoutEvaluations(evaluations);

        DataModel mahoutDataModel = buildMahoutDataModel();

        this.userBasedMahoutRecommender.refresh(mahoutDataModel);
        this.itemBasedMahoutRecommender.refresh(mahoutDataModel);
    }

    private List<MahoutEvaluation> buildMahoutEvaluations(List<Evaluation> evaluations) {
        return evaluations
                .stream()
                .map(MahoutEvaluation::new)
                .collect(Collectors.toList());
    }

    private DataModel buildMahoutDataModel() {
        Map<Customer, List<MahoutEvaluation>> evaluationsByCustomer = this.mahoutEvaluations
            .stream()
            .collect(Collectors.groupingBy(Evaluation::getCustomer, Collectors.toList()));

        FastByIDMap<PreferenceArray> customerPreferences =  new FastByIDMap<>();
        evaluationsByCustomer.forEach((customer, customerEvaluations) -> {
            customerPreferences.put(customer.getId(), new GenericUserPreferenceArray(customerEvaluations));
        });
        
        return new GenericDataModel(customerPreferences);
    }

    /**
     * Recommend by Items
     * @param item_id Item ID
     * @param count Number of recommendations to return
     * @return List of Book IDs (empty list if no recommendations available)
     */
    public List<Integer> recommendByItems(int item_id, int count) {
        return this.itemBasedMahoutRecommender.recommend(item_id, count);
    }

    /**
     * Recommend by Users
     * @param customer_id Customer ID
     * @param count Number of recommendations to return
     * @return List of Book IDs (empty list if no recommendations available)
     */
    public List<Integer> recommendByUsers(int customer_id, int count) {
        return this.userBasedMahoutRecommender.recommend(customer_id, count);
    }
}
