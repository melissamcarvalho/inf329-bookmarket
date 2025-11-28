package dominio;

/**
 * Enumeration of the possible statuses for an {@link Order}.
 *
 * @author User
 */
public enum StatusTypes {
    /**
     * The order is being processed.
     */
    PROCESSING,
    /**
     * The order has been shipped.
     */
    SHIPPED,
    /**
     * The order is pending payment or processing.
     */
    PENDING,
    /**
     * The order has been denied or canceled.
     */
    DENIED
}
