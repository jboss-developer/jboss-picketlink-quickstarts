package org.jboss.as.quickstarts.picketlink.authentication.jsf.exceptions;

import javax.faces.context.ExceptionHandler;
import javax.faces.context.ExceptionHandlerFactory;

/**
 * @author Michal Trnka
 */
public class SimpleExceptionHandlerFactory extends ExceptionHandlerFactory {

    private ExceptionHandlerFactory parent;

    public SimpleExceptionHandlerFactory(ExceptionHandlerFactory parent) {
     this.parent = parent;
    }

    @Override
    public ExceptionHandler getExceptionHandler() {
        return new SimpleExceptionHandler(parent.getExceptionHandler());
    }

}
