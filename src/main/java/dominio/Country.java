package dominio;

/* 
 * Address.java - Stores a country.
 *
 ************************************************************************
 *
 * This is part of the the Java TPC-W distribution,
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
 * Represents a country and its financial metadata, such as currency and exchange rates.
 * This class is used for internationalization and price conversion within the store.
 * <br><img src="./doc-files/Country.png" alt="Country Diagram">
 */
public class Country implements Serializable {

    private static final long serialVersionUID = 5171617014956861344L;

    private final int id;
    private final String name;
    private final String currency;
    private final double exchange;

    /**
     * Constructs a Country instance with mandatory financial validation.
     * @param id Unique database identifier.
     * @param name Full name of the country. Must not be empty.
     * @param currency Currency code (e.g., USD, BRL). Must not be empty.
     * @param exchange Exchange rate relative to the base currency. Must be positive.
     * @throws NullPointerException if name or currency is null.
     * @throws IllegalArgumentException if exchange is zero or negative.
     */
    public Country(int id, String name, String currency, double exchange) {
        this.id = Validator.notNegative(id, "id");
        this.name = Validator.notEmpty(name, "name");
        this.currency = Validator.notEmpty(currency, "currency");
        this.exchange = Validator.notNegative(exchange, "exchange");
    }

    /** @return Unique identifier. */
    public int getId() {
        return id;
    }

    /** @return The country name. */
    public String getName() {
        return name;
    }

    /** @return The currency code. */
    public String getCurrency() {
        return currency;
    }

    /** @return The current exchange rate. */
    public double getExchange() {
        return exchange;
    }

    /**
     * Compares equality based on the unique name of the country.
     * @param o Object to compare.
     * @return true if names are identical.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Country)) return false;
        Country country = (Country) o;
        return name.equals(country.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

}
