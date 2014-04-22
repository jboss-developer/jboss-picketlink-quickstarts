package com.gr.project.service;

import com.gr.project.model.Person;
import com.gr.project.security.authorization.annotation.UserLoggedIn;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/users")
@Stateless
@UserLoggedIn
public class PersonService {

    @Inject
    private EntityManager em;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Person> getAll() {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Person> criteria = cb.createQuery(Person.class);
        Root<Person> person = criteria.from(Person.class);

        criteria.select(person).orderBy(cb.asc(person.get("firstName")));

        return em.createQuery(criteria).getResultList();

    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Person findById(@PathParam("id") String id) {
    	Person person = em.find(Person.class, id);

        if (person == null) {
            throw new IllegalArgumentException("Invalid identifier.");
        }

        return person;
    }
}
