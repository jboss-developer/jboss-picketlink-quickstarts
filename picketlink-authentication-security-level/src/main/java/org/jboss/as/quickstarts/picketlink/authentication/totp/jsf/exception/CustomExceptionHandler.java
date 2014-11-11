/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2012, Red Hat, Inc., and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.jboss.as.quickstarts.picketlink.authentication.totp.jsf.exception;

import org.picketlink.authentication.levels.InsufficientSecurityLevelException;

import javax.faces.FacesException;
import javax.faces.context.ExceptionHandler;
import javax.faces.context.ExceptionHandlerWrapper;
import javax.faces.context.FacesContext;
import javax.faces.event.ExceptionQueuedEvent;
import javax.faces.event.ExceptionQueuedEventContext;
import java.io.IOException;
import java.util.Iterator;

/**
 * <p>This is only a custom JSF exception handler that redirects the user to a page if a {@link org.picketlink.authentication.levels.InsufficientSecurityLevelException}
 * was thrown.</p>
 *
 * @author Pedro Igor
 */
public class CustomExceptionHandler extends ExceptionHandlerWrapper {

    private final ExceptionHandler wrapped;

    public CustomExceptionHandler(ExceptionHandler exceptionHandler) {
        this.wrapped = exceptionHandler;
    }

    @Override
    public void handle() throws FacesException {
        final Iterator<ExceptionQueuedEvent> i = getUnhandledExceptionQueuedEvents().iterator();

        while (i.hasNext()) {
            ExceptionQueuedEvent event = i.next();
            ExceptionQueuedEventContext context = (ExceptionQueuedEventContext) event.getSource();

            Throwable t = getRootCause(context.getException());

            try {
                if (InsufficientSecurityLevelException.class.isInstance(t) || InsufficientSecurityLevelException.class.isInstance(t.getCause())) {
                    final FacesContext fc = FacesContext.getCurrentInstance();
                    fc.getExternalContext().redirect(fc.getExternalContext().getRequestContextPath() + "/raiseLevel.jsf");
                }
            } catch (IOException e) {
            } finally {
                i.remove();
            }
        }

        getWrapped().handle();
    }

    @Override
    public ExceptionHandler getWrapped() {
        return this.wrapped;
    }
}
