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

import org.apache.mahout.cf.taste.impl.similarity.PearsonCorrelationSimilarity;

import java.util.List;

public class ItemBasedMahoutRecommender extends BaseMahoutRecommender {

    public ItemBasedMahoutRecommender(RecommendationSettings settings, DataModel model) {
        super(settings, model);
    }

    @Override
    protected void ensureMahoutInitialized() {
        throw new UnsupportedOperationException("Unimplemented method 'ensureMahoutInitialized'");
    }

    @Override
    protected void updateMahoutComponents() {
        throw new UnsupportedOperationException("Unimplemented method 'updateMahoutComponents'");
    }

    @Override
    public List<Integer> recommend(int id, int count) {
        throw new UnsupportedOperationException("Unimplemented method 'recommend'");
    }

}
