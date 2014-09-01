package org.jboss.as.quickstarts.picketlink.authentication.jsf.exceptions;

import java.util.Iterator;

import javax.faces.FacesException;
import javax.faces.application.FacesMessage;
import javax.faces.application.NavigationHandler;
import javax.faces.context.ExceptionHandler;
import javax.faces.context.ExceptionHandlerWrapper;
import javax.faces.context.FacesContext;
import javax.faces.event.ExceptionQueuedEvent;
import javax.faces.event.ExceptionQueuedEventContext;

import org.picketlink.authentication.levels.InsufficientSecurityLevelException;
import org.picketlink.authentication.levels.internal.DefaultLevel;

/**
 * <p>This is only a custom JSF exception handler that redirects the user to a page if a {@link org.picketlink.authentication.levels.InsufficientSecurityLevelException}
 * was thrown.</p>
 *
 * @author Michal Trnka
 */
public class SimpleExceptionHandler extends ExceptionHandlerWrapper {

    private ExceptionHandler wrapped;

    SimpleExceptionHandler(ExceptionHandler exception) {
        this.wrapped = exception;
    }

    @Override
    public javax.faces.context.ExceptionHandler getWrapped() {
        return wrapped;
    }

    @Override
    public void handle() throws FacesException {
        // some general stuff to retrieve exception bellow
        final Iterator<ExceptionQueuedEvent> i = getUnhandledExceptionQueuedEvents().iterator();
        while (i.hasNext()) {
            ExceptionQueuedEvent event = i.next();
            ExceptionQueuedEventContext context = (ExceptionQueuedEventContext) event.getSource();
            Throwable t = context.getException();
            while (t instanceof FacesException) {
                t = t.getCause();
            }
            final FacesContext fc = FacesContext.getCurrentInstance();
            final NavigationHandler nav = fc.getApplication().getNavigationHandler();

            try {
                String page = "/error";
                // check whether it is InsufficientSecurityLevelException
                if (t instanceof InsufficientSecurityLevelException) {
                    InsufficientSecurityLevelException ex = (InsufficientSecurityLevelException) t;
                    //retrieves level and compares it
                    if (ex.getLevel().compareTo(new DefaultLevel(2)) == 0) {
                        page = "/home";
                        fc.addMessage(null, new FacesMessage(
                                "Authorization failure - You need to be logged in to make this action."));
                    } else if (ex.getLevel().compareTo(new DefaultLevel(3)) == 0) {
                        page = "/smsLogIn";
                    }
                }
                nav.handleNavigation(fc, null, page);
                fc.renderResponse();
            } finally {
                i.remove();
            }
        }
        getWrapped().handle();
    }
}
