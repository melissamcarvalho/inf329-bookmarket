package dominio;

/*
 * Author.java - Data about an author. 
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
 * Represents a book author within the bookstore system.
 * This class is immutable and enforces data integrity through validation and defensive copying.
 * * <br><img src="./doc-files/Author.png" alt="Author Diagram">
 */
public class Author implements Serializable {

    private static final long serialVersionUID = 8882043540800200706L;

    private final String fname;
    private final String mname;
    private final String lname;
    private final Date birthdate;
    private final String bio;

    /**
     * Constructs a new Author with mandatory validation and defensive copying for the birthdate.
     * @param fname The author's first name. Cannot be null or empty.
     * @param mname The author's middle name. Cannot be null (use empty string if none).
     * @param lname The author's last name. Cannot be null or empty.
     * @param birthdate The author's date of birth. Cannot be null.
     * @param bio A short biography of the author. Cannot be null.
     * @throws NullPointerException if any parameter is null.
     * @throws IllegalArgumentException if fname or lname are blank.
     */
    public Author(String fname, String mname, String lname, Date birthdate, String bio) {
        this.fname = Validator.notEmpty(fname, "fname");
        this.mname = Validator.notNull(mname, "mname");
        this.lname = Validator.notEmpty(lname, "lname");
        this.bio = Validator.notNull(bio, "bio");

        // Defensive copy to ensure immutability
        Validator.notNull(birthdate, "birthdate");
        this.birthdate = new Date(birthdate.getTime());
    }

    /**
     * Gets the author's first name.
     * @return A non-null String containing the first name.
     */
    public final String getFname() {
        return fname;
    }

    /**
     * Gets the author's last name.
     * @return A non-null String containing the last name.
     */
    public final String getLname() {
        return lname;
    }

    /**
     * Gets the author's middle name.
     * @return A non-null String containing the middle name (may be empty).
     */
    public final String getMname() {
        return mname;
    }

    /**
     * Gets the author's birthdate.
     * Returns a copy of the internal date to maintain immutability.
     * @return A non-null {@link Date} object.
     */
    public final Date getBirthdate() {
        return new Date(birthdate.getTime());
    }

    /**
     * Gets the author's biography.
     * @return A non-null String containing the bio.
     */
    public final String getBio() {
        return bio;
    }

}
