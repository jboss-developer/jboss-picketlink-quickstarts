package org.jboss.as.quickstarts.picketlink.authentication.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * <p>Entity representing order.</p>
 *
 * @author Michal Trnka
 */
public class Order {
    private List<Book> books = new ArrayList<Book>();
    private Date created;

    public void setDate(Date created){
        this.created = created;
    }

    public Date getCreated(){
        return created;
    }

    public void addBook(Book book) {
        books.add(book);
    }

    public List<Book> getBooks() {
        return books;
    }

    public int getBookPrice(){
        int price = 0;
        for(Book b : books){
            price += b.getPrice();
        }
        return price;
    }

    public int getBookCount(){
        return books.size();
    }
}
