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
package org.jboss.as.quickstarts.picketlink.authentication.events;

import org.picketlink.authentication.event.LoggedInEvent;
import org.picketlink.authentication.event.PostLoggedOutEvent;
import org.picketlink.idm.event.IdentityTypeCreatedEvent;

import javax.enterprise.event.Observes;
import java.util.logging.Logger;

/**
 * <p>For a complete list PL related events, please check the documentation.</p>
 *
 * @author pedroigor
 */
public class ExampleObserver {

    private static final Logger logger = Logger.getLogger(ExampleObserver.class.getName());

    public void onPre(@Observes LoggedInEvent event) {
        logger.info("User login");
    }

    public void onPre(@Observes PostLoggedOutEvent event) {
        logger.info("User logout");
    }

    public void onPre(@Observes IdentityTypeCreatedEvent event) {
        logger.info("IdentityType created");
    }

}
