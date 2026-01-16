package dominio;

/* 
 * Customer.java - stores the important information for a single customer. 
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
import java.util.Date;
import java.util.Objects;

/**
 * Represents a registered customer within the Bookmarket system.
 * This class maintains profile information, authentication metadata,
 * and purchase history.
 * <br><img src="./doc-files/Customer.png" alt="Customer Diagram">
 */
public class Customer implements Serializable {

    private static final long serialVersionUID = -7297414189618511748L;

    private final int id;
    private final String uname;
    private final String passwd;
    private final String fname;
    private final String lname;
    private final String phone;
    private final String email;
    private final Date since;
    private final Date lastVisit;
    private Date login;
    private Date expiration;
    private final double discount;
    private final double balance;
    private final double ytdPmt;
    private final Date birthdate;
    private final String data;
    private final Address address;
    private Order mostRecentOrder;

    /**
     * Constructs a validated Customer instance with full profile data.
     * @param id Unique identifier.
     * @param uname Username for authentication. Must not be empty.
     * @param passwd Password. Must not be empty.
     * @param fname First name. Must not be empty.
     * @param lname Last name. Must not be empty.
     * @param phone Contact phone number.
     * @param email Contact email. Must not be empty.
     * @param since Date the account was created.
     * @param lastVisit Date of the previous session.
     * @param login Current session login timestamp.
     * @param expiration Session expiration timestamp.
     * @param discount Discount percentage (0.0 to 100.0).
     * @param balance Current account balance.
     * @param ytdPmt Year-to-date payments made.
     * @param birthdate Customer's birthdate.
     * @param data Additional profile metadata.
     * @param address Physical billing/shipping address. Must not be null.
     */
    public Customer(int id, String uname, String passwd, String fname,
            String lname, String phone, String email, Date since,
            Date lastVisit, Date login, Date expiration, double discount,
            double balance, double ytdPmt, Date birthdate, String data,
            Address address) {

        this.id = Validator.notNegative(id, "id");
        this.uname = Validator.notEmpty(uname, "uname");
        this.passwd = Validator.notEmpty(passwd, "passwd");
        this.fname = Validator.notEmpty(fname, "fname");
        this.lname = Validator.notEmpty(lname, "lname");
        this.phone = Validator.notEmpty(phone, "phone");
        this.email = Validator.notEmpty(email, "email");
        this.address = Validator.notNull(address, "address");

        this.discount = Validator.notOverrangeInclusive(discount, 0, 100, "discount");

        this.balance = Validator.notNegative(balance, "balance");
        this.ytdPmt = Validator.notNegative(ytdPmt, "ytdPmt");
        this.data = Validator.notNull(data, "data");

        // Defensive copying for all Date objects
        this.since = new Date(Validator.notNull(since,"since").getTime());
        this.lastVisit = new Date(Validator.notNull(lastVisit,"lastVisit").getTime());
        this.login = new Date(Validator.notNull(login,"login").getTime());
        this.expiration = new Date(Validator.notNull(expiration,"expiration").getTime());
        this.birthdate = new Date(Validator.notNull(birthdate,"birthdate").getTime());

        this.mostRecentOrder = null;
    }

    /** @return The customer's first name. */
    public final String getFname() { return fname; }

    /** @return The customer's last name. */
    public final String getLname() { return lname; }

    /** @return The unique ID. */
    public final int getId() { return id; }

    /** @return The account discount percentage. */
    public final double getDiscount() { return discount; }

    /** @return The primary address. */
    public final Address getAddress() { return address; }

    /** @return The contact email. */
    public final String getEmail() { return email; }

    /** @return The username. */
    public final String getUname() { return uname; }

    /**
     * Records the most recent order placed by this customer.
     * @param order The order instance.
     */
    public final void logOrder(Order order) {
        Validator.notNull(order, "order");
        this.mostRecentOrder = order;
    }

    /** @return The most recent {@link Order}. */
    public final Order getMostRecentOrder() { return mostRecentOrder; }

    /** @param login Sets a copy of the current login timestamp. */
    public final void setLogin(Date login) {
        this.login = new Date(Validator.notNull(login, "login").getTime());
    }

    /** @param expiration Sets a copy of the session expiration. */
    public final void setExpiration(Date expiration) {
        this.expiration = new Date(Validator.notNull(expiration, "expiration").getTime());
    }

    /**
     * Returns the encrypted or hashed password for authentication purposes.
     * @return The customer's password string.
     */
    public final String getPasswd() {
        return passwd;
    }

    /**
     * @return The contact phone number associated with this profile.
     */
    public final String getPhone() {
        return phone;
    }

    /**
     * Returns a copy of the date when the customer account was first created.
     * @return A {@link Date} object representing account creation time.
     */
    public final Date getSince() {
        return new Date(since.getTime());
    }

    /**
     * Returns a copy of the timestamp from the customer's previous successful session.
     * @return A {@link Date} object of the last visit.
     */
    public final Date getLastVisit() {
        return new Date(lastVisit.getTime());
    }

    /**
     * Returns a copy of the current session's login timestamp.
     * @return A {@link Date} object of the current login.
     */
    public final Date getLogin() {
        return new Date(login.getTime());
    }

    /**
     * Returns a copy of the timestamp when the current session is set to expire.
     * @return A {@link Date} object representing session expiration.
     */
    public final Date getExpiration() {
        return new Date(expiration.getTime());
    }

    /**
     * @return The current outstanding monetary balance on the customer's account.
     */
    public final double getBalance() {
        return balance;
    }

    /**
     * Gets the Year-To-Date (YTD) total of payments made by this customer.
     * @return The total sum of payments for the current calendar year.
     */
    public final double getYtdPmt() {
        return ytdPmt;
    }

    /**
     * Returns a copy of the customer's date of birth.
     * @return A {@link Date} object of the birthdate, or null if not provided.
     */
    public final Date getBirthdate() {
        return new Date(birthdate.getTime());
    }

    /**
     * Returns supplementary profile metadata or serialized preference data.
     * @return A string containing additional customer data.
     */
    public final String getData() {
        return data;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, uname, email, address);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Customer other = (Customer) obj;
        return id == other.id &&
                Objects.equals(uname, other.uname) &&
                Objects.equals(email, other.email);
    }
}
