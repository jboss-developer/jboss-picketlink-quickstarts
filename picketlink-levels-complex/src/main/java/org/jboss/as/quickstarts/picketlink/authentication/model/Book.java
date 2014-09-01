package org.jboss.as.quickstarts.picketlink.authentication.model;

/**
 * <p>Entity representing book.</p>
 *
 * @author Michal Trnka
 */
public class Book {
    String name;

    String author;

    int price;

    public Book(String name, String author, int price) {
        this.name = name;
        this.author = author;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
