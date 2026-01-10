package recommendation;

import org.apache.mahout.cf.taste.impl.model.GenericDataModel;
import org.apache.mahout.cf.taste.impl.model.GenericUserPreferenceArray;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.model.PreferenceArray;
import org.junit.Before;
import org.junit.Test;

import recommendation.mahout.ItemBasedMahoutRecommender;

import static org.junit.Assert.*;

import java.util.List;

/**
 * Test class for ItemBasedMahoutRecommender
 *
 */
public class ItemBasedMahoutRecommenderTest {

    private DataModel dataModel;
    private ItemBasedMahoutRecommender recommender;
    private RecommendationSettings settings = new RecommendationSettings();

    @Before
    public void setUp() throws Exception {
        // Create a simple data model with user preferences
        // All these users, have similar preferences, and the ONLY option is to
        // recommend what is MISSING
        // User 1: items 1, 2, 3 ; rating 5, 5, 5; recommend 4
        // User 2: items 1, 2, 4 ; rating 5, 5, 5; recommend 3
        // User 3: items 2, 3, 4 ; rating 5, 5, 5; recommend 1
        // User 4: items 1, 3, 4 ; rating 5, 5, 5; recommend 2

        PreferenceArray user1Prefs = new GenericUserPreferenceArray(3);
        user1Prefs.setUserID(0, 1);

        user1Prefs.setItemID(0, 1);
        user1Prefs.setValue(0, 5.0f);

        user1Prefs.setItemID(1, 2);
        user1Prefs.setValue(1, 5.0f);

        user1Prefs.setItemID(2, 3);
        user1Prefs.setValue(2, 5.0f);

        PreferenceArray user2Prefs = new GenericUserPreferenceArray(3);
        user2Prefs.setUserID(0, 2);

        user2Prefs.setItemID(0, 1);
        user2Prefs.setValue(0, 5.0f);

        user2Prefs.setItemID(1, 2);
        user2Prefs.setValue(1, 5.0f);

        user2Prefs.setItemID(2, 4);
        user2Prefs.setValue(2, 5.0f);

        PreferenceArray user3Prefs = new GenericUserPreferenceArray(3);
        user3Prefs.setUserID(0, 3);

        user3Prefs.setItemID(0, 2);
        user3Prefs.setValue(0, 5.0f);

        user3Prefs.setItemID(1, 3);
        user3Prefs.setValue(1, 5.0f);

        user3Prefs.setItemID(2, 4);
        user3Prefs.setValue(2, 5.0f);

        PreferenceArray user4Prefs = new GenericUserPreferenceArray(3);
        user4Prefs.setUserID(0, 4);

        user4Prefs.setItemID(0, 1);
        user4Prefs.setValue(0, 5.0f);

        user4Prefs.setItemID(1, 3);
        user4Prefs.setValue(1, 5.0f);

        user4Prefs.setItemID(2, 4);
        user4Prefs.setValue(2, 5.0f);

        // Create FastByIDMap and populate it
        org.apache.mahout.cf.taste.impl.common.FastByIDMap<PreferenceArray> userData = new org.apache.mahout.cf.taste.impl.common.FastByIDMap<PreferenceArray>();
        userData.put(1L, user1Prefs);
        userData.put(2L, user2Prefs);
        userData.put(3L, user3Prefs);
        userData.put(4L, user4Prefs);

        dataModel = new GenericDataModel(userData);

        // Initialize recommender
        recommender = new ItemBasedMahoutRecommender(settings, dataModel);
    }

    /**
     * Test constructor with data model
     */
    @Test
    public void testConstructor() {
        assertNotNull("Recommender should be created", recommender);
    }

    /**
     * Test default similarity initialization
     */
    @Test
    public void testDefaultSimilarity() {
        // After calling recommend, similarity should be auto-initialized with default
        // Pearson Correlation Similarity
        recommender.recommend(1, 1);
        assertNotNull("Similarity should be auto-initialized after recommend", recommender.getMahoutSimilarity());
        // Check that the similarity is of type PearsonCorrelationSimilarity
        assertTrue("Similarity should be PearsonCorrelationSimilarity",
                recommender
                        .getMahoutSimilarity() instanceof org.apache.mahout.cf.taste.impl.similarity.PearsonCorrelationSimilarity);
    }

    /**
     * Test custom similarity setting
     */
    @Test
    public void testSetCustomSimilarity() throws Exception {
        RecommendationSettings settings = new RecommendationSettings(
                RecommendationCorrelationSimilarity.euclideanDistance,
                RecommendationUserSimilarity.nearestUserNeighborhood, 2);
        recommender.setSettings(settings);

        assertTrue("Similarity should be EuclideanDistanceSimilarity",
                recommender
                        .getMahoutSimilarity() instanceof org.apache.mahout.cf.taste.impl.similarity.EuclideanDistanceSimilarity);
    }

    /**
     * Test recommendation with default settings
     * Uses Euclidean Distance because all ratings are uniform (5.0)
     */
    @Test
    public void testRecommendWithDefaults() throws Exception {
        // Use Euclidean Distance Similarity which works with uniform ratings
        RecommendationSettings settings = new RecommendationSettings(
                RecommendationCorrelationSimilarity.euclideanDistance,
                RecommendationUserSimilarity.nearestUserNeighborhood, 2);
        recommender.setSettings(settings);

        // Request recommendations for user 1 (who likes items 1, 2, 3)
        // Should recommend item 4 based on similar items
        List<Integer> recommendations = recommender.recommend(1, 1);

        assertNotNull("Recommendations should not be null", recommendations);
        assertTrue("Should return one recommendation", recommendations.size() == 1);

        // Check that recommended item is 4
        assertEquals("Recommended item should be 4", Integer.valueOf(4), recommendations.get(0));
    }

    /**
     * Test recommendation count limit
     */
    @Test
    public void testRecommendationCountLimit() {
        int requestedCount = 1;
        List<Integer> recommendations = recommender.recommend(1, requestedCount);

        assertNotNull("Recommendations should not be null", recommendations);
        assertTrue("Should not exceed requested count", recommendations.size() <= requestedCount);
    }

    /**
     * Test recommendation with non-existent user
     * Item-based recommender throws NoSuchUserException for non-existent users
     */
    @Test(expected = RuntimeException.class)
    public void testRecommendWithNonExistentUser() {
        // User ID 999 doesn't exist in our data model
        // Item-based recommender throws exception for non-existent users
        recommender.recommend(999, 1);
    }

    /**
     * Test recommendation for customer 1
     * User 1 has items 1, 2, 3 ; so the expected recommendation is item 4
     * Uses Euclidean Distance because all ratings are uniform (5.0)
     */
    @Test
    public void testRecommendUser1() throws Exception {
        // Use Euclidean Distance Similarity which works with uniform ratings
        RecommendationSettings settings = new RecommendationSettings(
                RecommendationCorrelationSimilarity.euclideanDistance,
                RecommendationUserSimilarity.nearestUserNeighborhood, 2);
        recommender.setSettings(settings);

        List<Integer> recommendations = recommender.recommend(1, 1);

        assertNotNull("Recommendations should not be null", recommendations);
        assertTrue("Should return one recommendation", recommendations.size() == 1);

        // Check that recommended item is 4
        assertEquals("Recommended item should be 4", Integer.valueOf(4), recommendations.get(0));
    }

    /**
     * Test recommendation for customer 2
     * User 2 has items 1, 2, 4 ; so the expected recommendation is item 3
     * Uses Euclidean Distance because all ratings are uniform (5.0)
     */
    @Test
    public void testRecommendUser2() throws Exception {
        // Use Euclidean Distance Similarity which works with uniform ratings
        RecommendationSettings settings = new RecommendationSettings(
                RecommendationCorrelationSimilarity.euclideanDistance,
                RecommendationUserSimilarity.nearestUserNeighborhood, 2);
        recommender.setSettings(settings);

        List<Integer> recommendations = recommender.recommend(2, 1);

        assertNotNull("Recommendations should not be null", recommendations);
        assertTrue("Should return one recommendation", recommendations.size() == 1);

        // Check that recommended item is 3
        assertEquals("Recommended item should be 3", Integer.valueOf(3), recommendations.get(0));
    }

    /**
     * Test recommendation for customer 3
     * User 3 has items 2, 3, 4 ; so the expected recommendation is item 1
     * Uses Euclidean Distance because all ratings are uniform (5.0)
     */
    @Test
    public void testRecommendUser3() throws Exception {
        // Use Euclidean Distance Similarity which works with uniform ratings
        RecommendationSettings settings = new RecommendationSettings(
                RecommendationCorrelationSimilarity.euclideanDistance,
                RecommendationUserSimilarity.nearestUserNeighborhood, 2);
        recommender.setSettings(settings);

        List<Integer> recommendations = recommender.recommend(3, 1);

        assertNotNull("Recommendations should not be null", recommendations);
        assertTrue("Should return one recommendation", recommendations.size() == 1);

        // Check that recommended item is 1
        assertEquals("Recommended item should be 1", Integer.valueOf(1), recommendations.get(0));
    }

    /**
     * Test recommendation for customer 4
     * User 4 has items 1, 3, 4 ; so the expected recommendation is item 2
     * Uses Euclidean Distance because all ratings are uniform (5.0)
     */
    @Test
    public void testRecommendUser4() throws Exception {
        // Use Euclidean Distance Similarity which works with uniform ratings
        RecommendationSettings settings = new RecommendationSettings(
                RecommendationCorrelationSimilarity.euclideanDistance,
                RecommendationUserSimilarity.nearestUserNeighborhood, 2);
        recommender.setSettings(settings);

        List<Integer> recommendations = recommender.recommend(4, 1);

        assertNotNull("Recommendations should not be null", recommendations);
        assertTrue("Should return one recommendation", recommendations.size() == 1);

        // Check that recommended item is 2
        assertEquals("Recommended item should be 2", Integer.valueOf(2), recommendations.get(0));
    }

    /**
     * Test refresh functionality
     * Verifies that the recommender can be refreshed with a new data model
     */
    @Test
    public void testRefresh() throws Exception {
        // Create a new minimal data model
        PreferenceArray user5Prefs = new GenericUserPreferenceArray(2);
        user5Prefs.setUserID(0, 5);
        user5Prefs.setItemID(0, 1);
        user5Prefs.setValue(0, 4.0f);
        user5Prefs.setItemID(1, 2);
        user5Prefs.setValue(1, 3.0f);

        org.apache.mahout.cf.taste.impl.common.FastByIDMap<PreferenceArray> newUserData = new org.apache.mahout.cf.taste.impl.common.FastByIDMap<PreferenceArray>();
        newUserData.put(5L, user5Prefs);

        DataModel newDataModel = new GenericDataModel(newUserData);

        // Refresh the recommender
        recommender.refresh(newDataModel);

        // Verify the model was updated
        assertNotNull("Recommender should still be valid after refresh", recommender);
        assertEquals("Data model should be updated", newDataModel, recommender.getModel());
    }

    /**
     * Test that settings can be updated
     */
    @Test
    public void testSettingsUpdate() {
        RecommendationSettings newSettings = new RecommendationSettings(
                RecommendationCorrelationSimilarity.euclideanDistance,
                RecommendationUserSimilarity.nearestUserNeighborhood,
                3);

        recommender.setSettings(newSettings);

        assertEquals("Settings should be updated", newSettings, recommender.getSettings());
        assertTrue("Similarity should be updated to EuclideanDistanceSimilarity",
                recommender
                        .getMahoutSimilarity() instanceof org.apache.mahout.cf.taste.impl.similarity.EuclideanDistanceSimilarity);
    }
}
