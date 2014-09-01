package org.jboss.as.quickstarts.picketlink.authentication.bussiness;

import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.jboss.as.quickstarts.picketlink.authentication.model.Book;
import org.jboss.as.quickstarts.picketlink.authentication.model.Order;
import org.picketlink.authorization.annotations.RequiresLevel;

/**
 * <p>Simulates persistence layer</p>
 *
 * <p>This class keep track on submitted Orders. It retrieves the orders and it adds orders.</p>
 *
 * @author Michal Trnka
 */
@ApplicationScoped
@Named
public class OrderManager {
    List<Order> orders = new LinkedList<Order>();

    @Inject
    BookManager bookManager;

    @PostConstruct
    public void setUp() {
        Calendar cal = Calendar.getInstance();
        List<Book> books = bookManager.getBooks();
        Order first = new Order();
        first.addBook(books.get(0));
        first.addBook(books.get(2));
        first.addBook(books.get(3));
        cal.set(2014, 11, 21, 14, 31);
        first.setDate(cal.getTime());
        orders.add(first);

        Order second = new Order();
        second.addBook(books.get(1));
        second.addBook(books.get(4));
        cal.set(2014, 10, 18, 20, 15);
        second.setDate(cal.getTime());
        orders.add(second);

        Order third = new Order();
        third.addBook(books.get(1));
        cal.set(2014, 8, 1, 8, 55);
        third.setDate(cal.getTime());
        orders.add(third);
    }

    @RequiresLevel("2")
    public List<Order> getOrders() {
        return orders;
    }

    @RequiresLevel("3")
    public void makeOrder(Order o){
        o.setDate(Calendar.getInstance().getTime());
        orders.add(o);
    }
}
