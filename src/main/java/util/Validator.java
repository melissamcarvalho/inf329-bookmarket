package util;

/**
 * Utility class for validating object states and method arguments.
 * This class facilitates "Fail-Fast" development by throwing exceptions
 * immediately when a constraint is violated.
 */
public final class Validator {
    /**
     * Validates that the specified object is not null.
     * * @param <T> The type of the object.
     * @param obj The object to check.
     * @param paramName The name of the parameter for the error message.
     * @return object if not null
     * @throws NullPointerException if the object is null.
     */
    public static <T> T notNull(T obj, String paramName) {
        if (obj == null) {
            throw new NullPointerException(paramName + " cannot be null.");
        }
        return obj;
    }

    /**
     * Validates that a numeric value falls within a specific range, inclusive of the boundaries.
     * This is typically used for percentage validations, ratings, or coordinate bounds.
     * @param value The double value to be checked.
     * @param min The minimum allowable value (inclusive).
     * @param max The maximum allowable value (inclusive).
     * @param paramName The name of the parameter used in the error message for traceability.
     * @return The validated value if it falls within the specified range.
     * @throws IllegalArgumentException if the value is strictly less than min or strictly greater than max.
     */
    public static double notOverrangeInclusive(double value, double min, double max, String paramName) {
        if (value < min || value > max) {
            throw new IllegalArgumentException(paramName + "is overrange. Value: " + value + ". Min: " + min + ". Max: " + max);
        }
        return value;
    }

    /**
     * Validates that the specified double value is not negative.
     * * @param value The value to check.
     * @param paramName The name of the parameter for the error message.
     * @return value if not negative
     * @throws IllegalArgumentException if the value is less than 0.
     */
    public static long notNegative(long value, String paramName) {
        if (value < 0) {
            throw new IllegalArgumentException(paramName + " cannot be negative. Value provided: " + value);
        }
        return value;
    }

    /**
     * Validates that the specified double value is not negative.
     * * @param value The value to check.
     * @param paramName The name of the parameter for the error message.
     * @return value if not negative
     * @throws IllegalArgumentException if the value is less than 0.
     */
    public static double notNegative(double value, String paramName) {
        if (value < 0) {
            throw new IllegalArgumentException(paramName + " cannot be negative. Value provided: " + value);
        }
        return value;
    }

    /**
     * Validates that the specified integer value is not negative.
     * * @param value The value to check.
     * @param paramName The name of the parameter for the error message.
     * @return value if not negative
     * @throws IllegalArgumentException if the value is less than 0.
     */
    public static int notNegative(int value, String paramName) {
        if (value < 0) {
            throw new IllegalArgumentException(paramName + " cannot be negative. Value provided: " + value);
        }
        return value;
    }

    /**
     * Validates that a string is not null and not empty (after trimming).
     * * @param str The string to check.
     * @param paramName The name of the parameter for the error message.
     * @return string if not empty
     * @throws NullPointerException if string is null.
     * @throws IllegalArgumentException if string is empty or blank.
     */
    public static String notEmpty(String str, String paramName) {
        notNull(str, paramName);
        if (str.trim().isEmpty()) {
            throw new IllegalArgumentException(paramName + " cannot be empty or blank.");
        }
        return str;
    }
}
