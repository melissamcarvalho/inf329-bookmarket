package dominio;

/**
 * Enumeration of the different physical formats or bindings for a {@link Book}.
 *
 * @author User
 */
public enum BACKINGS {
    /**
     * A book with a rigid protective cover.
     */
    HARDBACK,
    /**
     * A book with a flexible paper cover.
     */
    PAPERBACK,
    /**
     * A second-hand book.
     */
    USED,
    /**
     * An audiobook format.
     */
    AUDIO,
    /**
     * A special, limited-run version of the book.
     */
    LIMITED_EDITION;
}
