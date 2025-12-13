package recommendation;

public class RecommendationSettings {
    private final RecommendationCorrelationSimilarity correlationSimilarity;
    private final RecommendationUserSimilarity userSimilarity;
    private final int neighborhoodSize ;

    public RecommendationSettings() {
        this(RecommendationCorrelationSimilarity.pearson, RecommendationUserSimilarity.nearestUserNeighborhood, 2);
    }

    public RecommendationSettings(RecommendationCorrelationSimilarity correlationSimilarity,
                                 RecommendationUserSimilarity userSimilarity, int neighborhoodSize) {
        this.correlationSimilarity = correlationSimilarity;
        this.userSimilarity = userSimilarity;
        this.neighborhoodSize = neighborhoodSize;
    }

    public RecommendationCorrelationSimilarity getCorrelationSimilarity() {
        return correlationSimilarity;
    }

    public RecommendationUserSimilarity getUserSimilarity() {
        return userSimilarity;
    }

    public int getNeighborhoodSize() {
        return neighborhoodSize;
    }
}
