package dominio;

/*
 * Book.java - Class used to store all of the data associated with a single
 *             book. 
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
import java.util.Date;

/**
 * *<img src="./doc-files/Book.png" alt="Book">
 * <br><a href="./doc-files/Book.html"> code </a>
 */
/**
 * Represents a book in the Bookmarket system, encapsulating all data associated with a single book.
 * <p>
 * This class is a core domain entity and contains information such as title, author, subject,
 * publication details, pricing, and related books. It is used throughout the system for book management,
 * searching, and recommendation features.
 * </p>
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
    private final double srp;
    private final Date avail;
    private final String isbn;
    private final int page;
    private final BACKINGS backing;
    private final int[] dimensions;
    private final double weight;
    private final Author author;

    /**
     * Constructs a new Book with all its attributes.
     *
     * @param id Unique identifier for the book.
     * @param title Title of the book.
     * @param pubDate Publication date.
     * @param publisher Publisher name.
     * @param subject Subject category.
     * @param desc Description of the book.
     * @param thumbnail URL or path to the thumbnail image.
     * @param image URL or path to the main image.
     * @param srp Suggested retail price.
     * @param avail Date when the book is available.
     * @param isbn ISBN code.
     * @param page Number of pages.
     * @param backing Book binding type.
     * @param dimensions Array with book dimensions.
     * @param weight Weight of the book.
     * @param author Author of the book.
     */
    public Book(int id, String title, Date pubDate, String publisher,
            SUBJECTS subject, String desc, String thumbnail,
            String image, double srp, Date avail, String isbn,
            int page, BACKINGS backing, int[] dimensions,double weight, Author author
            ) {
        this.id = id;
        this.title = title;
        this.pubDate = pubDate;
        this.publisher = publisher;
        this.subject = subject;
        this.desc = desc;
        this.related1 = null;
        this.related2 = null;
        this.related3 = null;
        this.related4 = null;
        this.related5 = null;
        this.thumbnail = thumbnail;
        this.image = image;
        this.srp = srp;
        this.avail = avail;
        this.isbn = isbn;
        this.page = page;
        this.backing = backing;
        this.dimensions = dimensions;
        this.weight = weight;
        this.author = author;
    }

    /**
     * Gets the title of the book.
     * @return The book title.
     */
    public String getTitle() {
        return title;
    }

    /**
     * Gets the thumbnail image path or URL.
     * @return The thumbnail image.
     */
    public String getThumbnail() {
        return thumbnail;
    }

    /**
     * Sets the thumbnail image path or URL.
     * @param thumbnail The thumbnail image.
     */
    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }


    /**
     * Gets the main image path or URL.
     * @return The main image.
     */
    public String getImage() {
        return image;
    }

    /**
     * Sets the main image path or URL.
     * @param image The main image.
     */
    public void setImage(String image) {
        this.image = image;
    }

    /**
     * Gets the author of the book.
     * @return The author.
     */
    public Author getAuthor() {
        return author;
    }

    /**
     * Gets the suggested retail price.
     * @return The price.
     */
    public double getSrp() {
        return srp;
    }


    /**
     * Gets the description of the book.
     * @return The description.
     */
    public String getDesc() {
        return desc;
    }

    /**
     * Gets the number of pages.
     * @return The page count.
     */
    public int getPage() {
        return page;
    }

    /**
     * Gets the book binding type.
     * @return The backing type.
     */
    public BACKINGS getBacking() {
        return backing;
    }

    /**
     * Gets the publication date.
     * @return The publication date.
     */
    public Date getPubDate() {
        return pubDate;
    }

    /**
     * Sets the publication date.
     * @param pubDate The publication date.
     */
    public void setPubDate(Date pubDate) {
        this.pubDate = pubDate;
    }

    /**
     * Gets the publisher name.
     * @return The publisher.
     */
    public String getPublisher() {
        return publisher;
    }

    /**
     * Gets the ISBN code.
     * @return The ISBN.
     */
    public String getIsbn() {
        return isbn;
    }

    /**
     * Gets the unique identifier of the book.
     * @return The book ID.
     */
    public int getId() {
        return id;
    }

    /**
     * Gets the dimensions of the book.
     * @return The dimensions array.
     */
    public int[] getDimensions() {
        return dimensions;
    }

    /**
     * Gets the weight of the book.
     * @return The weight.
     */
    public double getWeight() {
        return weight;
    }
    
    

    /**
     * Gets the subject category of the book.
     * @return The subject.
     */
    public SUBJECTS getSubject() {
        return subject;
    }

    /**
     * Gets the date when the book is available.
     * @return The availability date.
     */
    public Date getAvail() {
        return avail;
    }

    /**
     * Gets the first related book.
     * @return The related book.
     */
    public Book getRelated1() {
        return related1;
    }

    /**
     * Gets the second related book.
     * @return The related book.
     */
    public Book getRelated2() {
        return related2;
    }

    /**
     * Gets the third related book.
     * @return The related book.
     */
    public Book getRelated3() {
        return related3;
    }

    /**
     * Gets the fourth related book.
     * @return The related book.
     */
    public Book getRelated4() {
        return related4;
    }

    /**
     * Gets the fifth related book.
     * @return The related book.
     */
    public Book getRelated5() {
        return related5;
    }

    /**
     * Sets the first related book.
     * @param related1 The related book.
     */
    public void setRelated1(Book related1) {
        this.related1 = related1;
    }

    /**
     * Sets the second related book.
     * @param related2 The related book.
     */
    public void setRelated2(Book related2) {
        this.related2 = related2;
    }

    /**
     * Sets the third related book.
     * @param related3 The related book.
     */
    public void setRelated3(Book related3) {
        this.related3 = related3;
    }

    /**
     * Sets the fourth related book.
     * @param related4 The related book.
     */
    public void setRelated4(Book related4) {
        this.related4 = related4;
    }

    /**
     * Sets the fifth related book.
     * @param related5 The related book.
     */
    public void setRelated5(Book related5) {
        this.related5 = related5;
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
        return "Book{" + "id=" + id + '}';
    }
    
    

}
