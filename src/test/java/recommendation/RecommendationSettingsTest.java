package recommendation;

import org.junit.Test;
import static org.junit.Assert.*;

public class RecommendationSettingsTest {

    @Test
    public void testValidSettings() {
        // Valid settings should work
        RecommendationSettings settings = new RecommendationSettings(
                RecommendationCorrelationSimilarity.pearson,
                RecommendationUserSimilarity.nearestUserNeighborhood,
                5);

        assertNotNull(settings);
        assertEquals(RecommendationCorrelationSimilarity.pearson, settings.getCorrelationSimilarity());
        assertEquals(RecommendationUserSimilarity.nearestUserNeighborhood, settings.getUserSimilarity());
        assertEquals(5, settings.getNeighborhoodSize());
    }

    @Test
    public void testDefaultConstructor() {
        RecommendationSettings settings = new RecommendationSettings();

        assertNotNull(settings);
        assertEquals(RecommendationCorrelationSimilarity.pearson, settings.getCorrelationSimilarity());
        assertEquals(RecommendationUserSimilarity.nearestUserNeighborhood, settings.getUserSimilarity());
        assertEquals(10, settings.getNeighborhoodSize());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullCorrelationSimilarity() {
        new RecommendationSettings(null, RecommendationUserSimilarity.nearestUserNeighborhood, 5);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullUserSimilarity() {
        new RecommendationSettings(RecommendationCorrelationSimilarity.pearson, null, 5);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testZeroNeighborhoodSize() {
        new RecommendationSettings(
                RecommendationCorrelationSimilarity.pearson,
                RecommendationUserSimilarity.nearestUserNeighborhood,
                0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeNeighborhoodSize() {
        new RecommendationSettings(
                RecommendationCorrelationSimilarity.pearson,
                RecommendationUserSimilarity.nearestUserNeighborhood,
                -1);
    }

    @Test
    public void testMinimumValidNeighborhoodSize() {
        RecommendationSettings settings = new RecommendationSettings(
                RecommendationCorrelationSimilarity.pearson,
                RecommendationUserSimilarity.nearestUserNeighborhood,
                1);

        assertEquals(1, settings.getNeighborhoodSize());
    }

    @Test
    public void testLargeNeighborhoodSize() {
        RecommendationSettings settings = new RecommendationSettings(
                RecommendationCorrelationSimilarity.pearson,
                RecommendationUserSimilarity.nearestUserNeighborhood,
                1000);

        assertEquals(1000, settings.getNeighborhoodSize());
    }
}
