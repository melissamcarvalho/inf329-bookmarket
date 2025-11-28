package dominio;

/**
 * Enumeration of the available shipping methods for an {@link Order}.
 *
 * @author User
 */
 public enum ShipTypes {
    /**
     * Shipping by air freight.
     */
    AIR,
    /**
     * Shipping via United Parcel Service (UPS).
     */
    UPS,
    /**
     * Shipping via FedEx.
     */
    FEDEX,
    /**
     * Standard ground shipping.
     */
    SHIP,
    /**
     * Local courier delivery.
     */
    COURIER,
    /**
     * Shipping via postal mail.
     */
    MAIL
    
}
