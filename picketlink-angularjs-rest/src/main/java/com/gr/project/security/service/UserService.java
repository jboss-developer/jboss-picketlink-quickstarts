package com.gr.project.security.service;

import com.gr.project.data.PersonDAO;
import com.gr.project.data.PersonListProducer;
import com.gr.project.model.Person;
import com.gr.project.security.authorization.annotation.UserLoggedIn;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/users")
@Stateless
@UserLoggedIn
public class UserService {

    @Inject
    private PersonListProducer persons;

    @Inject
    private PersonDAO personDAO;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Person> listAllPersons() {
    	return persons.getPersons();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Person lookupPersonById(@PathParam("id") String id) {
    	Person person = personDAO.findById(id);
        if (person == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        return person;
    }
}
