package org.jboss.as.quickstarts.picketlink.angularjs.service;

import org.jboss.as.quickstarts.picketlink.angularjs.model.Person;
import org.picketlink.authorization.annotations.LoggedIn;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/private/person")
@Stateless
@LoggedIn
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
}
