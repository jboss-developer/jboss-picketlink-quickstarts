package org.jboss.as.quickstarts.picketlink.authentication.jsf;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.NavigationHandler;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.jboss.as.quickstarts.picketlink.authentication.bussiness.OrderManager;
import org.jboss.as.quickstarts.picketlink.authentication.model.Book;
import org.jboss.as.quickstarts.picketlink.authentication.model.Order;

/**
 * @author Michal Trnka
 */
@SessionScoped
@Named
public class OrderController implements Serializable {

    private static final long serialVersionUID = 1L;

    private Order order;

    @Inject
    private OrderManager om;

    @Inject
    FacesContext facesContext;

    @PostConstruct
    private void setUp() {
        order = new Order();
    }

    public void addBook(Book b) {
        order.addBook(b);
    }

    /**
     * <p>This method actually calls restricted {@link org.jboss.as.quickstarts.picketlink.authentication.bussiness.OrderManager#makeOrder(Order)}}</p>
     */
    public void makePayment() {
        om.makeOrder(order);
        order = new Order();
        NavigationHandler nav = facesContext.getApplication().getNavigationHandler();
        nav.handleNavigation(facesContext, null,"/home");
    }

    public Order getOrder() {
        return order;
    }
}
