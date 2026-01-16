package recommendation.mahout;

/**
 * Custom exception for Mahout recommender errors
 * Handles initialization failures, DataModel inconsistencies, and runtime
 * errors
 */
public class MahoutRecommenderException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * Constructs a new MahoutRecommenderException with the specified detail message
     * 
     * @param message the detail message
     */
    public MahoutRecommenderException(String message) {
        super(message);
    }

    /**
     * Constructs a new MahoutRecommenderException with the specified detail message
     * and cause
     * 
     * @param message the detail message
     * @param cause   the cause of the exception
     */
    public MahoutRecommenderException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructs a new MahoutRecommenderException with the specified cause
     * 
     * @param cause the cause of the exception
     */
    public MahoutRecommenderException(Throwable cause) {
        super(cause);
    }
}
