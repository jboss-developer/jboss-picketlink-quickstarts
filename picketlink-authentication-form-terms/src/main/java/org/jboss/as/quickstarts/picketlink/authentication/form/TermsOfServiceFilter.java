/*
 * JBoss, Home of Professional Open Source
 *
 * Copyright 2013 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jboss.as.quickstarts.picketlink.authentication.form;

import org.picketlink.common.util.StringUtil;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * An implementation of {@link javax.servlet.Filter} that is used to display
 * the Terms of Service page the first time an user authenticates to the
 * application.
 *
 * @author Anil Saldhana
 * @since March 25, 2014
 */
public class TermsOfServiceFilter implements Filter {
    /**
     * Name of the parameter that indicates clicking of
     * "Terms of Service"
     */
    protected String tosParameter = "tos";

    /**
     * The TOS HTML/JSP Page
     */
    protected String tosPage = "/termsofservice.html";

    /**
     * The HTML/JSP Page that is displayed when the user
     * disagrees to the Terms of Service
     */
    protected String tosDisagreedPage = "/termsofservice-disagreed.html";

    /**
     * Exclude URL Patterns that this filter should ignore
     */
    protected List<String> excludePatterns = new ArrayList<String>();

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        String tosValue = filterConfig.getInitParameter("tosParameter");
        if(tosValue != null && tosValue.isEmpty() == false){
            tosParameter = tosValue;
        }
        String tosPageValue = filterConfig.getInitParameter("tosPage");
        if(tosPageValue != null && tosPageValue.isEmpty() == false){
            tosPage = tosPageValue;
        }

        String tosDisagreedPageValue = filterConfig.getInitParameter("tosDisagreedPage");
        if(tosDisagreedPageValue != null && tosDisagreedPageValue.isEmpty() == false){
            tosDisagreedPage = tosDisagreedPageValue;
        }
        String excludePatternsValue = filterConfig.getInitParameter("excludePatterns");
        if(excludePatternsValue != null && excludePatternsValue.isEmpty() == false){
            excludePatterns.addAll(StringUtil.tokenize(excludePatternsValue,","));
        }else{
            excludePatterns.add("/images");
            excludePatterns.add("/css");
        }
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;

        //See if we are exclude patterns
        String path = httpServletRequest.getRequestURI().toString();
        for(String pattern: excludePatterns){
            if(path.contains(pattern)){
                chain.doFilter(request,response);
                return;
            }
        }

        //Check if we are coming from the tos page
        String tosClick = httpServletRequest.getParameter(tosParameter);
        if(tosClick != null){
            boolean tosAgreed = Boolean.parseBoolean(tosClick);
            if(tosAgreed){
                //Agreed to TOS
                saveTosChoiceInStore(httpServletRequest, true);
                httpServletRequest = restoreRequest(httpServletRequest);
                if(httpServletRequest == null){
                    throw new ServletException("Request restoration failed");
                }
                chain.doFilter(httpServletRequest,response);
            }else{
                //Disagreed - TOS
                saveTosChoiceInStore(httpServletRequest, false);
                sendToPage(httpServletRequest,httpServletResponse,tosDisagreedPage);
                return;
            }
        }
        if(!shouldDisplayTermsOfServicePage(httpServletRequest)){
            chain.doFilter(request, response);
        }else{
            //Need to display terms of service page
            saveRequest(httpServletRequest);
            sendToPage(httpServletRequest,httpServletResponse, tosPage);
            return;
        }
    }

    @Override
    public void destroy() {
    }

    /**
     * Send the request to the page
     * @param httpServletRequest
     * @param response
     * @param page
     * @throws ServletException
     * @throws IOException
     */
    protected void sendToPage(HttpServletRequest httpServletRequest, HttpServletResponse response, String page) throws ServletException, IOException {
        RequestDispatcher requestDispatcher = httpServletRequest.getRequestDispatcher(page);
        requestDispatcher.forward(httpServletRequest,response);
    }

    /**
     * Save the user's TOS choice in the store
     * @param httpServletRequest
     * @param storeValue
     */
    protected void saveTosChoiceInStore(HttpServletRequest httpServletRequest, boolean storeValue){
        //In this case, we save it in the session -  but in real world, you send it to DB or LDAP
        HttpSession httpSession = httpServletRequest.getSession(false);
        httpSession.setAttribute("TOS",storeValue);
    }

    /**
     * Check whether we need to display the Terms of Service Page
     * @param httpServletRequest
     * @return
     */
    protected boolean shouldDisplayTermsOfServicePage(HttpServletRequest httpServletRequest){
        //Here we check the session if the TOS attribute has been set. In real world,
        //the authentication process should set this attribute in the session from DB/LDAP
        HttpSession httpSession = httpServletRequest.getSession(false);

        Boolean tosValue = (Boolean) httpSession.getAttribute("TOS");
        if(tosValue == null){
            return true;
        }
        if(tosValue == Boolean.TRUE){
            return false;
        }
        return true;
    }

    /**
     * Save the current request in the user's session
     * @param httpServletRequest
     */
    protected void saveRequest(HttpServletRequest httpServletRequest){
        HttpSession session = httpServletRequest.getSession(false);
        session.setAttribute("TERMS_OF_SERVICE",httpServletRequest);
    }

    /**
     * Restore the request using the cached request
     * @param httpServletRequest
     * @return
     */
    protected HttpServletRequest restoreRequest(HttpServletRequest httpServletRequest){
        HttpSession session = httpServletRequest.getSession(false);
        return (HttpServletRequest) session.getAttribute("TERMS_OF_SERVICE");
    }
}