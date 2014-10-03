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

import org.jboss.quickstarts.picketlink.acl.model.Article;
import org.picketlink.Identity;
import org.picketlink.authorization.annotations.RequiresPermission;
import org.picketlink.idm.PermissionManager;
import org.picketlink.idm.model.basic.User;

import javax.ejb.Stateful;
import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import java.io.Serializable;
import java.util.List;

/**
 * Action bean for Article related operations
 *
 * @author Shane Bryzak
 * @author Pedro Igor
 */
@Named
@Stateful
@ConversationScoped
public class ArticleController implements Serializable {

    @Inject EntityManager entityManager;

    @Inject Conversation conversation;

    @Inject Identity identity;

    @Inject PermissionManager permissionManager;

    private Article article;

    @Inject
    private FacesContext facesContext;

    @RequiresPermission(resourceClass = Article.class, operation = "create")
    public String showCreate() {
        conversation.begin();
        article = new Article();
        return "/article.xhtml";
    }

    @RequiresPermission(resourceClass = Article.class, operation = "update")
    public String showUpdate(Article article) {
        conversation.begin();
        this.article = this.entityManager.find(article.getClass(), article.getId());
        return "/article.xhtml";
    }

    public String create() {
        User user = (User) identity.getAccount();
        article.setAuthor(user.getFirstName() + " " + user.getLastName());
        entityManager.persist(article);

        // Grant the creating user permission to delete the article
        permissionManager.grantPermission(user, article, "update, delete");

        conversation.end();

        return "/home.xhtml";
    }

    public String update() {
        Article articleToUpdate = getArticle();

        if (!this.identity.hasPermission(articleToUpdate, "update")) {
            facesContext.addMessage(
                null,
                new FacesMessage("You can not update this article. Are you the owner ?"));
        } else {
            Article storedArticle = this.entityManager.find(articleToUpdate.getClass(), articleToUpdate.getId());

            storedArticle.setContent(articleToUpdate.getContent());
            storedArticle.setTitle(articleToUpdate.getTitle());

            this.entityManager.merge(storedArticle);
        }

        conversation.end();

        return "/home.xhtml";
    }

    public String delete(Article articleToDelete) {
        if (!this.identity.hasPermission(articleToDelete, "delete")) {
            facesContext.addMessage(
                null,
                new FacesMessage("You can not delete this article. Are you the owner ?"));
        } else {
            Article storedArticle = this.entityManager.find(articleToDelete.getClass(), articleToDelete.getId());

            this.entityManager.remove(storedArticle);
        }

        return "/home.xhtml";
    }

    public List<Article> getArticles() {
        return this.entityManager.createQuery("select a from Article a").getResultList();
    }

    public Article getArticle() {
        return article;
    }
}
