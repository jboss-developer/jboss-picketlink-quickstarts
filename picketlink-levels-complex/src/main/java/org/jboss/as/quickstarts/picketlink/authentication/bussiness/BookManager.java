package org.jboss.as.quickstarts.picketlink.authentication.bussiness;

import java.util.LinkedList;
import java.util.List;

import javax.inject.Named;

import org.jboss.as.quickstarts.picketlink.authentication.model.Book;

/**
 * <p>Simulates persistance layer. It provides the application with books.</p>
 *
 * @author Michal Trnka
 */
@Named
public class BookManager {

    public List<Book> getBooks() {
        List<Book> books = new LinkedList<Book>();
        books.add(new Book("1984", "Goerge Orwell", 100));
        books.add(new Book("The Lord of the Rings", "J. R. R. Tolkien", 200));
        books.add(new Book("The Little Prince", "Antoine de Saint-Exupéry", 50));
        books.add(new Book("And Then There Were None", "Agatha Christie", 150));
        books.add(new Book("A Tale of Two Cities", "Charles Dickens", 125));
        return books;
    }
}
