package recommendation;

import dominio.Customer;
import dominio.Evaluation;
import org.apache.mahout.cf.taste.impl.model.GenericUserPreferenceArray;
import recommendation.mahout.BaseMahoutRecommender;
import recommendation.mahout.UserBasedMahoutRecommender;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.mahout.cf.taste.impl.common.FastByIDMap;
import org.apache.mahout.cf.taste.impl.model.GenericDataModel;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.model.PreferenceArray;

public class RecommendationEngine {
    private final UserBasedMahoutRecommender userBasedMahoutRecommender;
    // private final BaseMahoutRecommender itemBasedMahoutRecommender;
    private List<Evaluation> evaluations;
    
    /**
     * Acts as an Adapter for Evaluation to the DataModel Mahout expects as input.
     * @param evaluations List of evaluation to be loaded as Model
     */
    public RecommendationEngine(List<Evaluation> evaluations, RecommendationSettings settings) {
        this.evaluations = evaluations;

        DataModel mahoutUserDataModel = buildMahoutUserDataModel();
        userBasedMahoutRecommender = new UserBasedMahoutRecommender(settings, mahoutUserDataModel);

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
        this.evaluations = evaluations;

        DataModel mahoutUserDataModel = buildMahoutUserDataModel();
        this.userBasedMahoutRecommender.refresh(mahoutUserDataModel);

        // this.itemBasedMahoutRecommender.refresh(mahoutDataModel);
    }

    private DataModel buildMahoutUserDataModel() {
        Map<Customer, Set<Evaluation>> evaluationsByCustomer = new HashMap<>();
        this.evaluations.forEach(evaluation -> {
            Customer customer = evaluation.getCustomer();
            if(!evaluationsByCustomer.containsKey(customer)) {
                evaluationsByCustomer.put(customer, new HashSet<>());
            }

            evaluationsByCustomer.get(customer).add(evaluation);
        });

        FastByIDMap<PreferenceArray> userData =  new FastByIDMap<>();
        evaluationsByCustomer.forEach((customer, customerEvaluations) -> {
            PreferenceArray customerPreferences = new GenericUserPreferenceArray(customerEvaluations.size());
            customerPreferences.setUserID(0, customer.getId());

            AtomicInteger preferenceIndex = new AtomicInteger();
            customerEvaluations.forEach(evaluation -> {
                int index = preferenceIndex.get();
                customerPreferences.setItemID(index, evaluation.getBook().getId());
                customerPreferences.setValue(index, 5.0f);

                preferenceIndex.getAndIncrement();
            });

            userData.put(customer.getId(), customerPreferences);
        });
        
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
    public List<Integer> recommendByUsers(int customer_id, int count) {
        List<Integer> recommendations = this.userBasedMahoutRecommender.recommend(customer_id, count);
        return recommendations;
    }
}
