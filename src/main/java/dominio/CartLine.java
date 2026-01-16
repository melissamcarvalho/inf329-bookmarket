package dominio;

/* 
 * CartLine.java - Class stores the necessary data for a single item in
 *                 a single shopping cart.
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
 * Represents a single line item within a shopping cart.
 * Links a specific {@link Stock} item with a requested quantity.
 * <br><img src="./doc-files/CartLine.png" alt="CartLine Diagram">
 */
public class CartLine implements Serializable {

    private static final long serialVersionUID = 7390646727961714957L;

    private int qty;
    private final Stock stock;

    /**
     * Constructs a new CartLine with validation.
     * @param qty The quantity of the item. Must be zero or positive.
     * @param stock The stock item being added. Must not be null.
     * @throws IllegalArgumentException if qty is negative.
     * @throws NullPointerException if stock is null.
     */
    public CartLine(int qty, Stock stock) {
        this.qty = Validator.notNegative(qty, "qty");
        this.stock = Validator.notNull(stock, "stock");
    }

    /**
     * Updates the quantity of this line item.
     * @param qty The new quantity. Must be zero or positive.
     * @throws IllegalArgumentException if qty is negative.
     */
    public void setQty(int qty) {
        this.qty = Validator.notNegative(qty, "qty");
    }

    /**
     * @return The current quantity.
     */
    public int getQty() {
        return qty;
    }

    /**
     * Helper method to retrieve the book associated with this cart line.
     * @return The {@link Book} instance from the associated stock.
     */
    public Book getBook() {
        return stock.getBook();
    }

    /**
     * @return The associated {@link Stock} item.
     */
    public Stock getStock() {
        return stock;
    }

}
