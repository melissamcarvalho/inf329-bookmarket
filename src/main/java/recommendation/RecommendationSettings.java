package recommendation;

public class RecommendationSettings {
    private final RecommendationCorrelationSimilarity correlationSimilarity;
    private final RecommendationUserSimilarity userSimilarity;
    private final int neighborhoodSize;

    public RecommendationSettings() {
        this(RecommendationCorrelationSimilarity.pearson, RecommendationUserSimilarity.nearestUserNeighborhood, 2);
    }

    public RecommendationSettings(RecommendationCorrelationSimilarity correlationSimilarity,
            RecommendationUserSimilarity userSimilarity, int neighborhoodSize) {
        if (correlationSimilarity == null) {
            throw new IllegalArgumentException("Correlation similarity cannot be null");
        }
        if (userSimilarity == null) {
            throw new IllegalArgumentException("User similarity cannot be null");
        }
        if (neighborhoodSize <= 0) {
            throw new IllegalArgumentException("Neighborhood size must be positive, got: " + neighborhoodSize);
        }

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
