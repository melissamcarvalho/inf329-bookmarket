package dominio;

/*
 * Order.java - Order class stores data pertinent to a single order.
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
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import util.Validator;

/**
 * Represents a completed purchase order in the system.
 * This class captures the state of a transaction, including pricing,
 * shipping details, and payment confirmation at the moment of creation.
 * <br><img src="./doc-files/Order.png" alt="Order Diagram">
 */
public class Order implements Serializable {

    private static final long serialVersionUID = -1106285234830970111L;

    private final int id;
    private final Customer customer;
    private final Date date;
    private final double subtotal;
    private final double tax;
    private final double total;
    private final ShipTypes shipType;
    private final Date shipDate;
    private final StatusTypes status;
    private final Address billingAddress;
    private final Address shippingAddress;
    private final CCTransaction cc;
    private final List<OrderLine> lines;

    /**
     * Constructs a new Order by converting a Cart's state into a permanent record.
     * @param id The unique order ID.
     * @param customer The customer placing the order. Must not be null.
     * @param date The order placement date. Must not be null.
     * @param cart The shopping cart containing the items. Must not be null.
     * @param comment An optional comment for the order lines.
     * @param shipType The selected shipping method. Must not be null.
     * @param shipDate The scheduled shipping date. Must not be null.
     * @param status The current status of the order. Must not be null.
     * @param billingAddress The address for billing. Must not be null.
     * @param shippingAddress The address for delivery. Must not be null.
     * @param cc The credit card transaction details. Must not be null.
     */
    public Order(int id, Customer customer, Date date, Cart cart,
            String comment, ShipTypes shipType, Date shipDate, StatusTypes status,
            Address billingAddress, Address shippingAddress, CCTransaction cc) {
        this.id = Validator.notNegative(id, "id");
        this.customer = Validator.notNull(customer, "customer");
        this.date = new Date(Validator.notNull(date, "date").getTime());

        Validator.notNull(cart, "cart");
        this.subtotal = cart.subTotal(customer.getDiscount());
        this.tax = 8.25;
        this.total = cart.total(customer);

        this.shipType = Validator.notNull(shipType, "shipType");
        this.shipDate = new Date(Validator.notNull(shipDate, "shipDate").getTime());
        this.status = Validator.notNull(status, "status");

        this.billingAddress = Validator.notNull(billingAddress, "billingAddress");
        this.shippingAddress = Validator.notNull(shippingAddress, "shippingAddress");
        this.cc = Validator.notNull(cc, "cc");

        Validator.notNull(comment, "comment");

        // Convert CartLines to OrderLines
        ArrayList<OrderLine> tempLines = new ArrayList<>(cart.getLines().size());
        for (CartLine cartLine : cart.getLines()) {
            OrderLine line = new OrderLine(
                    cartLine.getBook(),
                    cartLine.getQty(),
                    customer.getDiscount(),
                    comment);
            tempLines.add(line);
        }
        this.lines = Collections.unmodifiableList(tempLines);
    }

    /** @return The unique order identifier. */
    public int getId() {
        return id;
    }

    /** @return The customer who placed the order. */
    public Customer getCustomer() {
        return customer;
    }

    /** @return A copy of the order date. */
    public Date getDate() {
        return date;
    }

    /** @return The subtotal amount. */
    public double getSubtotal() {
        return subtotal;
    }

    /** @return The tax amount. */
    public double getTax() {
        return tax;
    }

    /** @return The grand total. */
    public double getTotal() {
        return total;
    }

    /** @return The shipping method used. */
    public ShipTypes getShipType() {
        return shipType;
    }

    /** @return A copy of the scheduled shipping date. */
    public Date getShipDate() {
        return new Date(shipDate.getTime());
    }

    /** @return The current order status. */
    public StatusTypes getStatus() {
        return status;
    }

    // Status Helper Methods
    public boolean isDenined() {
        return this.getStatus().equals(StatusTypes.DENIED);
    }
    public boolean isPending() {
        return this.getStatus().equals(StatusTypes.PENDING);
    }
    public boolean isShipped() {
        return this.getStatus().equals(StatusTypes.SHIPPED);
    }
    public boolean isProcessed() {
        return this.getStatus().equals(StatusTypes.PROCESSING);
    }

    /** @return The address used for billing. */
    public Address getBillingAddress() {
        return billingAddress;
    }

    /** @return The address used for shipping. */
    public Address getShippingAddress() {
        return shippingAddress;
    }

    /** @return The associated credit card transaction. */
    public CCTransaction getCC() {
        return cc;
    }

    /** @return An unmodifiable list of the order line items. */
    public List<OrderLine> getLines() {
        return lines;
    }

}
