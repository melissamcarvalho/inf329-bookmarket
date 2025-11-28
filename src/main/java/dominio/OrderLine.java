package dominio;

/*
 * OrderLine.java - Class contains the data pertinent to a single
 *                  item in a single order.
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
import java.io.Serializable;

/**
 * Represents a single item in an order within the Bookmarket system.
 * <p>
 * The OrderLine class encapsulates the details of a purchased book, including the book itself,
 * quantity, discount applied, and any comments. It is used as part of the Order entity to model
 * the composition of a customer's purchase.
 * </p>
 * <img src="./doc-files/OrderLine.png" alt="OrderLine">
 * <br><a href="./doc-files/OrderLine.html"> code </a>
 */
public class OrderLine implements Serializable {

    private static final long serialVersionUID = -5063511252485472431L;

    private final Book book;
    private final int qty;
    private final double discount;
    private final String comments;

    /**
     * Constructs an OrderLine with the specified book, quantity, discount, and comments.
     *
     * @param book The book being purchased.
     * @param qty The quantity of the book.
     * @param discount The discount applied to this item.
     * @param comments Any comments related to this order line.
     */
    public OrderLine(Book book, int qty, double discount, String comments) {
        this.book = book;
        this.qty = qty;
        this.discount = discount;
        this.comments = comments;
    }

    /**
     * Gets the book associated with this order line.
     * @return The book.
     */
    public Book getBook() {
        return book;
    }

    /**
     * Gets the quantity of the book ordered.
     * @return The quantity.
     */
    public int getQty() {
        return qty;
    }

    /**
     * Gets the discount applied to this order line.
     * @return The discount value.
     */
    public double getDiscount() {
        return discount;
    }

    /**
     * Gets any comments associated with this order line.
     * @return The comments.
     */
    public String getComments() {
        return comments;
    }

}
