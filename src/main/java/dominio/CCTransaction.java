package dominio;

/* 
 * CCTransaction.java - Class holds data for a single credit card
 *                      transaction.
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
import java.util.Date;

/**
 * Represents a credit card transaction record within the system.
 * This class is a finalized record of a payment attempt, including authorization and
 * origin details. It is designed to be immutable to preserve financial history.
 * <br><img src="./doc-files/CCTransaction.png" alt="CCTransaction Diagram">
 */
public class CCTransaction implements Serializable {

    private static final long serialVersionUID = 5470177450411822726L;

    private final CreditCards type;
    private final long[] num;
    private final String name;
    private final Date expire;
    private final String authId;
    private final double amount;
    private final Date date;
    private final Country country;

    /**
     * Constructs a validated and immutable Credit Card Transaction record.
     * @param type The credit card brand/type. Must not be null.
     * @param num The credit card number.
     * @param name The cardholder's name. Must not be null or empty.
     * @param expire The card's expiration date. Must not be null.
     * @param authId The authorization ID. Must not be null.
     * @param amount The transaction amount. Must be non-negative.
     * @param date The timestamp of the transaction. Must not be null.
     * @param country The country of origin. Must not be null.
     * @throws NullPointerException if any required parameter is null.
     * @throws IllegalArgumentException if amount is negative or name is empty.
     */
    public CCTransaction(
            CreditCards type, long[] num, String name, Date expire,
            String authId, double amount, Date date, Country country) {
        this.type = Validator.notNull(type, "type");
        this.name = Validator.notEmpty(name, "name");
        this.authId = Validator.notEmpty(authId, "authId");
        this.country = Validator.notNull(country, "country");
        this.amount = Validator.notNegative(amount, "amount");
        this.expire = new Date(Validator.notNull(expire, "expire").getTime());
        this.date = new Date(Validator.notNull(date, "date").getTime());

        Validator.notNull(num, "num");
        if (num.length != 4) {
            throw new IllegalArgumentException("CC Card number should be an array with length 4");
        }
        this.num = num;
    }

    /**
     *
     * @return
     */
    public CreditCards getType() {
        return type;
    }

    /**
     *
     * @return
     */
    public long[] getNum() {
        return num;
    }

    /**
     *
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @return
     */
    public Date getExpire() {
        return expire;
    }

    /**
     *
     * @return
     */
    public String getAuthId() {
        return authId;
    }

    /**
     *
     * @return
     */
    public double getAmount() {
        return amount;
    }

    /**
     *
     * @return
     */
    public Date getDate() {
        return date;
    }

    /**
     *
     * @return
     */
    public Country getCountry() {
        return country;
    }

}
