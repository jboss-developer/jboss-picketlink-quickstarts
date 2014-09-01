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
package org.picketlink.quickstarts.http.servlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Pedro Igor
 */
@WebServlet ("/servlet")
public class AuthenticationServlet extends HttpServlet {

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (isLogout(req)) {
            req.logout();
        }

        tryAuthentication(req);

        resp.sendRedirect(req.getContextPath() + "/index.jsp");
    }

    private boolean isLogout(HttpServletRequest req) {
        return req.getUserPrincipal() != null && req.getParameter("logout") != null;
    }

    private void tryAuthentication(HttpServletRequest req) throws ServletException {
        if (req.getUserPrincipal() == null) {
            String username = req.getParameter("username");
            String password = req.getParameter("password");

            if (username != null && password != null) {
                req.login(username, password);
            }
        }
    }
}
