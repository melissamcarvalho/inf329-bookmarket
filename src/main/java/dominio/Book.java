package dominio;

/*
 * Book.java - Class used to store all the data associated with a single
 *             book. 
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

/**
 * Represents a book in the Bookmarket system.
 * This class acts as a central domain entity, managing metadata, pricing,
 * and relationships between titles.
 * <br><img src="./doc-files/Book.png" alt="Book Class Diagram">
 */
public class Book implements Serializable {

    private static final long serialVersionUID = 6505830715531808617L;

    private final int id;
    private final String title;
    private Date pubDate;
    private final String publisher;
    private final SUBJECTS subject;
    private final String desc;
    private Book related1;
    private Book related2;
    private Book related3;
    private Book related4;
    private Book related5;
    private String thumbnail;
    private String image;
    private double srp;
    private final Date avail;
    private final String isbn;
    private final int page;
    private final BACKINGS backing;
    private final int[] dimensions;
    private final double weight;
    private final Author author;

    /**
     * Constructs a new Book with strict validation and defensive copying.
     * @param id Unique identifier.
     * @param title Book title. Cannot be empty.
     * @param pubDate Publication date. Cannot be null.
     * @param publisher Publisher name. Cannot be empty.
     * @param subject Category. Cannot be null.
     * @param desc Description. Cannot be null.
     * @param thumbnail Thumbnail path. Cannot be null.
     * @param image Full image path. Cannot be null.
     * @param srp Suggested Retail Price. Must be non-negative.
     * @param avail Availability date. Cannot be null.
     * @param isbn ISBN code. Cannot be empty.
     * @param page Page count. Must be positive.
     * @param backing Binding type. Cannot be null.
     * @param dimensions Array [width, height, depth]. Cannot be null.
     * @param weight Weight in grams/ounces. Must be non-negative.
     * @param author The {@link Author} object. Cannot be null.
     */
    public Book(int id, String title, Date pubDate, String publisher,
            SUBJECTS subject, String desc, String thumbnail,
            String image, double srp, Date avail, String isbn,
            int page, BACKINGS backing, int[] dimensions, double weight, Author author) {

        this.id = Validator.notNegative(id, "id");

        this.title = Validator.notEmpty(title, "title");
        this.publisher = Validator.notEmpty(publisher, "publisher");
        this.desc = Validator.notEmpty(desc, "desc");
        this.thumbnail = Validator.notEmpty(thumbnail, "thumbnail");
        this.image = Validator.notEmpty(image, "image");
        this.isbn = Validator.notEmpty(isbn, "isbn");

        this.subject = Validator.notNull(subject, "subject");
        this.backing = Validator.notNull(backing, "backing");
        this.author = Validator.notNull(author, "author");

        this.srp = Validator.notNegative(srp, "srp");;
        this.page = Validator.notNegative(page, "page");
        this.weight = Validator.notNegative(weight, "weight");

        // Defensive copying for mutable types
        this.pubDate = new Date(Validator.notNull(pubDate, "pubDate").getTime());
        this.avail = new Date(Validator.notNull(avail, "avail").getTime());

        Validator.notNull(dimensions, "dimensions");
        if (dimensions.length != 3) {
            throw new IllegalArgumentException("dimensions array should have length of 3");
        }
        this.dimensions = dimensions.clone();

        this.related1 = null;
        this.related2 = null;
        this.related3 = null;
        this.related4 = null;
        this.related5 = null;

    }

    /**
     * Gets the title of the book.
     * @return The book title.
     */
    public final String getTitle() {
        return title;
    }

    /**
     * Gets the thumbnail image path or URL.
     * @return The thumbnail image.
     */
    public final String getThumbnail() {
        return thumbnail;
    }

    /**
     * Sets the thumbnail image path or URL.
     * @param thumbnail The thumbnail image.
     */
    public final void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }


    /**
     * Gets the main image path or URL.
     * @return The main image.
     */
    public final String getImage() {
        return image;
    }

    /**
     * Sets the main image path or URL.
     * @param image The main image.
     */
    public final void setImage(String image) {
        this.image = Validator.notEmpty(image, "image");
    }

    /**
     * Gets the author of the book.
     * @return The author.
     */
    public final Author getAuthor() {
        return author;
    }

    /**
     * Gets the suggested retail price.
     * @return The price.
     */
    public final double getSrp() {
        return srp;
    }

    /**
     * Sets the price cost
     * @param cost The price
     */
    public final void setSrp(double cost) {
        this.srp = Validator.notNegative(cost, "cost");
    }

    /**
     * Gets the description of the book.
     * @return The description.
     */
    public final String getDesc() {
        return desc;
    }

    /**
     * Gets the number of pages.
     * @return The page count.
     */
    public final int getPage() {
        return page;
    }

    /**
     * Gets the book binding type.
     * @return The backing type.
     */
    public final BACKINGS getBacking() {
        return backing;
    }

    /**
     * Gets the publication date.
     * @return The publication date.
     */
    public final Date getPubDate() {
        return new Date(pubDate.getTime());
    }

    /**
     * Sets the publication date.
     * @param pubDate The publication date.
     */
    public final void setPubDate(Date pubDate) {
        this.pubDate = new Date(pubDate.getTime());
    }

    /**
     * Gets the publisher name.
     * @return The publisher.
     */
    public final String getPublisher() {
        return publisher;
    }

    /**
     * Gets the ISBN code.
     * @return The ISBN.
     */
    public final String getIsbn() {
        return isbn;
    }

    /**
     * Gets the unique identifier of the book.
     * @return The book ID.
     */
    public final int getId() {
        return id;
    }

    /**
     * Gets the dimensions of the book.
     * @return The dimensions array.
     */
    public final int[] getDimensions() {
        return dimensions;
    }

    /**
     * Gets the weight of the book.
     * @return The weight.
     */
    public final double getWeight() {
        return weight;
    }

    /**
     * Gets the subject category of the book.
     * @return The subject.
     */
    public final SUBJECTS getSubject() {
        return subject;
    }

    /**
     * Gets the date when the book is available.
     * @return The availability date.
     */
    public final Date getAvail() {
        return new Date(avail.getTime());
    }

    /**
     * Gets the first related book.
     * @return The related book.
     */
    public final Book getRelated1() {
        return related1;
    }

    /**
     * Gets the second related book.
     * @return The related book.
     */
    public final Book getRelated2() {
        return related2;
    }

    /**
     * Gets the third related book.
     * @return The related book.
     */
    public final Book getRelated3() {
        return related3;
    }

    /**
     * Gets the fourth related book.
     * @return The related book.
     */
    public final Book getRelated4() {
        return related4;
    }

    /**
     * Gets the fifth related book.
     * @return The related book.
     */
    public final Book getRelated5() {
        return related5;
    }

    /**
     * Sets the first related book.
     * @param related The related book.
     */
    public final void setRelated1(Book related) {
        this.related1 = Validator.notNull(related, "related");
    }

    /**
     * Sets the second related book.
     * @param related The related book.
     */
    public final void setRelated2(Book related) {
        this.related2 = Validator.notNull(related, "related");
    }

    /**
     * Sets the third related book.
     * @param related The related book.
     */
    public final void setRelated3(Book related) {
        this.related3 = Validator.notNull(related, "related");
    }

    /**
     * Sets the fourth related book.
     * @param related The related book.
     */
    public final void setRelated4(Book related) {
        this.related4 = Validator.notNull(related, "related");
    }

    /**
     * Sets the fifth related book.
     * @param related The related book.
     */
    public final void setRelated5(Book related) {
        this.related5 = Validator.notNull(related, "related");
    }

    /**
     * Checks if this book is equal to another object based on the book ID.
     * @param o The object to compare.
     * @return True if the object is a Book with the same ID, false otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (o instanceof Book) {
            Book book = (Book) o;
            return id == book.id;
        }
        return false;
    }

    /**
     * Returns the hash code for this book, based on its ID.
     * @return The hash code.
     */
    @Override
    public int hashCode() {
        return id;
    }

    /**
     * Returns a string representation of the book, showing its ID.
     * @return String with book ID.
     */
    @Override
    public String toString() {
        return "Book{" + "id=" + id + ",cost=" + srp + '}';
    }
    
    

}
