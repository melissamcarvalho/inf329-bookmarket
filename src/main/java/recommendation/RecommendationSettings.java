package recommendation;

public class RecommendationSettings {
    private final RecommendationCorrelationSimilarity correlationSimilarity;
    private final RecommendationUserSimilarity userSimilarity;
    private final int neighborhoodSize;
    private final double threshold;

    public RecommendationSettings() {
        this(RecommendationCorrelationSimilarity.pearson, RecommendationUserSimilarity.nearestUserNeighborhood, 2);
    }

    public RecommendationSettings(RecommendationCorrelationSimilarity correlationSimilarity,
            RecommendationUserSimilarity userSimilarity, int neighborhoodSize) {
        this(correlationSimilarity, userSimilarity, neighborhoodSize, 0.7);
    }

    public RecommendationSettings(RecommendationCorrelationSimilarity correlationSimilarity,
            RecommendationUserSimilarity userSimilarity, int neighborhoodSize, double threshold) {
        if (correlationSimilarity == null) {
            throw new IllegalArgumentException("Correlation similarity cannot be null");
        }
        if (userSimilarity == null) {
            throw new IllegalArgumentException("User similarity cannot be null");
        }
        if (neighborhoodSize <= 0) {
            throw new IllegalArgumentException("Neighborhood size must be positive, got: " + neighborhoodSize);
        }
        if (threshold < 0.0 || threshold > 1.0) {
            throw new IllegalArgumentException("Threshold must be between 0.0 and 1.0, got: " + threshold);
        }

        this.correlationSimilarity = correlationSimilarity;
        this.userSimilarity = userSimilarity;
        this.neighborhoodSize = neighborhoodSize;
        this.threshold = threshold;
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

    public double getThreshold() {
        return threshold;
    }
}
