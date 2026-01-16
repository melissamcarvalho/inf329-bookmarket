package dominio;

/*
 * Cart.java - Class stores the necessary components of a shopping cart.
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
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;

/**
 * Represents a user's shopping cart.
 * Manages the lifecycle of {@link CartLine} items and calculates totals,
 * taxes, and shipping costs.
 * <br><img src="./doc-files/Cart.png" alt="Cart Diagram">
 */
public class Cart implements Serializable {

    private static final long serialVersionUID = -4194553499937996531L;

    private final int id;
    private Date time;
    private HashMap<Integer, CartLine> linesByBookId;

    /**
     * Constructs a new Cart with a specific timestamp.
     * @param id Unique identifier for the cart.
     * @param time The creation timestamp. Must not be null.
     */
    public Cart(int id, Date time) {
        this.id = Validator.notNegative(id, "id");
        this.time = new Date(Validator.notNull(time, "time").getTime());
        clear();
    }

    /** @return The cart ID. */
    public int getId() {
        return id;
    }

    /** @return A copy of the cart timestamp. */
    public Date getTime() {
        return new Date(time.getTime());
    }

    /** @param time The new timestamp. Must not be null. */
    public void setTime(Date time) {
        this.time = new Date(Validator.notNull(time, "time").getTime());
    }

    /** Resets the cart by removing all items. */
    public void clear() {
        linesByBookId = new HashMap<>();
    }

    /** @return An array list of cart lines. */
    public ArrayList<CartLine> getLines() {
        return new ArrayList<>(linesByBookId.values());
    }

    /**
     * Increases the quantity of a book in the cart.
     * @param stock The stock item to add. Must not be null.
     * @param quantity The amount to add. If the sum with current value exceeds below zero the item will be removed from cart.
     * @throws IllegalArgumentException if requested quantity exceeds available stock.
     */
    public void increaseLine(Stock stock, int quantity) {
        Validator.notNull(stock, "stock");

        Book book = stock.getBook();
        CartLine line = linesByBookId.get(book.getId());
        int currentQty = (line == null) ? 0 : line.getQty();
        int newTotalQty = currentQty + quantity;

        if (stock.getQty() < newTotalQty) {
            throw new IllegalArgumentException("Insufficient stock for Book ID: " + book.getId()
                    + ". Current: " + stock.getQty() + ". Requested: " + newTotalQty);
        }

        if (line == null) {
            line = new CartLine(0, stock);
            linesByBookId.put(book.getId(), line);
        }

        line.setQty(newTotalQty);

        if (newTotalQty <= 0) {
            linesByBookId.remove(book.getId());
        }
    }

    /**
     * Changes the absolute quantity of a book in the cart.
     * @param stock The stock item. Must not be null.
     * @param quantity The new absolute quantity. If is negative item will be removed from cart.
     */
    public void changeLine(Stock stock, int quantity) {
        Validator.notNull(stock, "stock");

        if (stock.getQty() < quantity) {
            throw new IllegalArgumentException("Requested quantity exceeds available stock."
                    + ". Current: " + stock.getQty() + ". Requested: " + quantity);
        }

        Book book = stock.getBook();
        if (quantity <= 0) {
            linesByBookId.remove(book.getId());
            return;
        }

        CartLine line = linesByBookId.get(book.getId());
        if (line == null) {
            line = new CartLine(quantity, stock);
            linesByBookId.put(book.getId(), line);
            return;
        }

        line.setQty(quantity);
    }

    /**
     * Calculates the subtotal after applying a percentage discount.
     * @param discount Percentage (0-100).
     * @return The discounted subtotal.
     */
    public double subTotal(double discount) {
        Validator.notOverrangeInclusive(discount, 0, 100, "discount");
        double aggregateCost = this.getLines().stream()
                .mapToDouble(line -> line.getQty() * line.getStock().getCost())
                .sum();
        return aggregateCost * ((100 - discount) / 100.0);
    }

    /**
     * Calculates sales tax based on a fixed rate (8.25%).
     * @param discount User discount to apply to subtotal before tax.
     * @return Calculated tax amount.
     */
    public double tax(double discount) {
        return subTotal(discount) * 0.0825;
    }

    /**
     * Calculates total shipping cost for the cart.
     * @param customer The customer receiving the order.
     * @return Total shipping cost.
     */
    private double shipCost(Customer customer) {
        Validator.notNull(customer, "customer");
        return this.getLines().stream()
                .mapToDouble(line -> line.getQty() * shippingCostService(
                        line.getStock().getAddress(),
                        customer.getAddress(),
                        ShipTypes.AIR,
                        line.getStock().getBook().getWeight(),
                        line.getStock().getBook().getDimensions()))
                .sum();
    }

    /**
     * Calculates final total including subtotal, tax, and shipping.
     * @param customer The customer context for discounts and shipping.
     * @return Grand total.
     */
    public double total(Customer customer) {
        Validator.notNull(customer, "customer");
        double discount = customer.getDiscount();
        return subTotal(discount) + tax(discount) + shipCost(customer);
    }

    /**
     * Simulated service for logistics pricing.
     */
    private double shippingCostService(Address from, Address to, ShipTypes type, double weight, int[] dimensions) {
        Validator.notNull(from, "from");
        Validator.notNull(to, "to");
        Validator.notNull(type, "type");
        Validator.notNegative(weight, "weight");

        if (dimensions.length != 3) {
            throw new IllegalArgumentException("Field dimensions should have length of 3");
        }

        int aggregateQuantity = getLines().stream()
                .mapToInt(CartLine::getQty)
                .sum();

        return 3.00 + (1.00 * aggregateQuantity);
    }

}
