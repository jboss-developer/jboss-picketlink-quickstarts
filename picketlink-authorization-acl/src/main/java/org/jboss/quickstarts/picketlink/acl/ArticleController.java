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
package org.jboss.quickstarts.picketlink.acl;

import java.io.Serializable;

import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;

import org.apache.deltaspike.jpa.api.transaction.Transactional;
import org.jboss.quickstarts.picketlink.acl.model.Article;
import org.picketlink.Identity;
import org.picketlink.idm.PermissionManager;
import org.picketlink.idm.model.basic.User;

/**
 * Action bean for Article related operations
 *
 * @author Shane Bryzak
 */
@Named
@ConversationScoped
public class ArticleController implements Serializable {
    @Inject EntityManager em;

    @Inject Conversation conversation;

    @Inject Identity identity;

    @Inject PermissionManager permissionManager;

    private Article article;

    @Transactional
    @CanCreate(Article.class)
    public void createArticle() {
        conversation.begin();
        article = new Article();
    }

    public Article getArticle() {
        return article;
    }

    public void saveArticle() {
        User user = (User) identity.getAccount();
        article.setAuthor(user.getFirstName() + " " + user.getLastName());
        em.persist(article);

        // Grant the creating user permission to delete the article
        permissionManager.grantPermission(user, article, "delete");

        conversation.end();
    }
}
