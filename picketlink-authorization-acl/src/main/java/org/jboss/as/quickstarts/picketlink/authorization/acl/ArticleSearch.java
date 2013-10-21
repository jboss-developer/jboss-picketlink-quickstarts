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
package org.jboss.as.quickstarts.picketlink.authorization.acl;

import java.util.List;

import javax.enterprise.inject.Model;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import org.jboss.as.quickstarts.picketlink.authorization.acl.model.Article;

/**
 * Controller bean for Article search operations
 *
 * @author Shane Bryzak
 */
@Model
public class ArticleSearch {
    @Inject EntityManager em;

    private List<Article> articles;


    private void loadArticles() {
        articles = em.createQuery("select a from Article a").getResultList();
    }

    public List<Article> getArticles() {
        if (articles == null) {
            loadArticles();
        }
        return articles;
    }
}
