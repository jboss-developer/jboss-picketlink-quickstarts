package com.gr.project.data;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.event.Observes;
import javax.enterprise.event.Reception;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;

import com.gr.project.model.Person;

@RequestScoped
public class PersonListProducer {

	@Inject
    private PersonDAO personRepository;

    private List<Person> persons;

    @Produces
    public List<Person> getPersons() {
        return persons;
    }

    public void onMemberListChanged(@Observes(notifyObserver = Reception.IF_EXISTS) final Person person) {
        retrieveAllMembersOrderedByName();
    }

    @PostConstruct
    public void retrieveAllMembersOrderedByName() {
        persons = personRepository.findAllOrderedByName();
    }
}
