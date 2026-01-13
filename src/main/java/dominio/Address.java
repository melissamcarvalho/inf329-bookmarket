package dominio;

/* 
 * Address.java - Stores an address.
 *
 ************************************************************************
 *
 * This is part of the Java TPC-W distribution,
 * written by Harold Cain, Tim Heil, Milo Martin, Eric Weglarz, and Todd
 * Bezenek.  University of Wisconsin - Madison, Computer Sciences
 * Dept. and Dept. of Electrical and Computer Engineering, as a part of
 * Prof. Mikko Lipasti's Fall 1999 ECE 902 course.
 *
 * Copyright (C) 1999, 2000 by Harold Cain, Timothy Heil, Milo Martin, 
 *                             Eric Weglarz, Todd Bezenek.
 * Copyright © 2008 Gustavo Maciel Dias Vieira
 *
 * This source code is distributed "as is" in the hope that it will be
 * useful.  It comes with no warranty, and no author or distributor
 * accepts any responsibility for the consequences of its use.
 *
 * Everyone is granted permission to copy, modify and redistribute
 * this code under the following conditions:
 *
 * This code is distributed for non-commercial use only.
 * Please contact the maintainer for restrictions applying to 
 * commercial use of these tools.
 *
 * Permission is granted to anyone to make or distribute copies
 * of this code, either as received or modified, in any
 * medium, provided that all copyright notices, permission and
 * nonwarranty notices are preserved, and that the distributor
 * grants the recipient permission for further redistribution as
 * permitted by this document.
 *
 * Permission is granted to distribute this code in compiled
 * or executable form under the same conditions that apply for
 * source code, provided that either:
 *
 * A. it is accompanied by the corresponding machine-readable
 *    source code,
 * B. it is accompanied by a written offer, with no time limit,
 *    to give anyone a machine-readable copy of the corresponding
 *    source code in return for reimbursement of the cost of
 *    distribution.  This written offer must permit verbatim
 *    duplication by anyone, or
 * C. it is distributed by someone who received only the
 *    executable form, and is accompanied by a copy of the
 *    written offer of source code that they received concurrently.
 *
 * In other words, you are welcome to use, share and improve this codes.
 * You are forbidden to forbid anyone else to use, share and improve what
 * you give them.
 *
 ************************************************************************/
import util.Validator;
import java.io.Serializable;

/**
 * Represents a physical address within the system.
 * This class is used to define locations for Customers, Suppliers,
 * and Bookstore stock locations.
 * * <br><img src="./doc-files/Address.png" alt="Address Diagram">
 */
public class Address implements Serializable {

    private static final long serialVersionUID = 3980790290181121772L;

    private final int id;
    private final String street1;
    private final String street2;
    private final String city;
    private final String state;
    private final String zip;
    private final Country country;

    /**
     * Constructs a new Address with full details.
     * @param id The unique identifier for this address record in the database.
     * @param street1 The primary address line (e.g., House number and Street name).
     * @param street2 The secondary address line (e.g., Apartment, Suite, or Unit number).
     * @param city The name of the city.
     * @param state The state, province, or region.
     * @param zip The ZIP or Postal code.
     * @param country The {@link Country} object associated with this address.
     */
    public Address(
            int id,
            final String street1,
            final String street2,
            final String city,
            final String state,
            final String zip,
            final Country country) {
        this.id = Validator.notNegative(id, "id");
        this.street1 = Validator.notEmpty(street1, "street1");
        this.street2 = Validator.notNull(street2, "street2");
        this.city = Validator.notEmpty(city, "city");
        this.state = Validator.notEmpty(state, "state");
        this.zip = Validator.notEmpty(zip, "zip");
        this.country = Validator.notNull(country, "country");
    }

    /**
     * Gets the unique identifier of the address.
     * @return The database ID.
     */
    public final int getId() {
        return id;
    }

    /**
     * Gets the primary street address.
     * @return A String containing the primary street line.
     */
    public final String getStreet1() {
        return street1;
    }

    /**
     * Gets the secondary address line (complement).
     * @return A String containing the street2 line, or null if not provided.
     */
    public final String getStreet2() {
        return street2;
    }

    /**
     * Gets the city name.
     * @return A String containing the city.
     */
    public final String getCity() {
        return city;
    }

    /**
     * Gets the state or province.
     * @return A String containing the state.
     */
    public final String getState() {
        return state;
    }

    /**
     * Gets the ZIP or postal code.
     * @return A String containing the postal code.
     */
    public final String getZip() {
        return zip;
    }

    /**
     * Gets the country associated with this address.
     * @return The {@link Country} object.
     */
    public final Country getCountry() {
        return country;
    }

    /**
     * Compares this address to the specified object for equality.
     * The comparison is based on all physical address fields (street, city, state, zip, country).
     * The 'id' field is ignored for logical equality.
     * @param o The object to compare this Address against.
     * @return {@code true} if the given object represents an Address equivalent to this one.
     */
    @Override
    public boolean equals(Object o) {
        if (o instanceof Address) {
            Address address = (Address) o;
            return street1.equals(address.street1)
                    && street2.equals(address.street2)
                    && city.equals(address.city)
                    && state.equals(address.state)
                    && zip.equals(address.zip)
                    && country.equals(address.country);
        }
        return false;
    }

    /**
     * Returns a hash code value for the address.
     * The hash is computed using the textual components of the address.
     * * @return A hash code value for this object.
     */
    @Override
    public int hashCode() {
        return street1.hashCode() + street2.hashCode() + city.hashCode()
                + state.hashCode() + zip.hashCode() + country.hashCode();
    }
}
