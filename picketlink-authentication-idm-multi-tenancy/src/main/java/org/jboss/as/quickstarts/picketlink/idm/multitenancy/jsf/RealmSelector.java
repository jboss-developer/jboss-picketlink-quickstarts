/**
 * JBoss, Home of Professional Open Source
 * Copyright 2013, Red Hat, Inc. and/or its affiliates, and individual
 * contributors by the @authors tag. See the copyright.txt in the
 * distribution for a full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jboss.as.quickstarts.picketlink.idm.multitenancy.jsf;

import org.picketlink.annotations.PicketLink;
import org.picketlink.idm.PartitionManager;
import org.picketlink.idm.model.basic.Realm;

import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;

/**
 * <p>We use this class to hold the current realm for a specific user.</p>
 */
@SessionScoped
@Named
public class RealmSelector implements Serializable {

    @Inject
    private PartitionManager partitionManager;

    private Realm realm;

    @Produces
    @PicketLink
    public Realm select() {
        return this.realm;
    }

    public Resources.REALM getRealm() {
        if (this.realm == null) {
            return null;
        }

        return Resources.REALM.valueOf(this.realm.getName());
    }

    public void setRealm(Resources.REALM realm) {
        this.realm = this.partitionManager.getPartition(Realm.class, realm.name());
    }
}
