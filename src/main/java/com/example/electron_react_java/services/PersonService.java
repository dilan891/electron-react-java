package com.example.electron_react_java.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.electron_react_java.dao.PersonDao;
import com.example.electron_react_java.models.Person;

@Service
public class PersonService {
    private final PersonDao personDao;

    @Autowired
    private PersonService(PersonDao personDao) {
        this.personDao = personDao;
    }

    public List<Person> getAllPeople() {
        List<Person> people = new ArrayList<>();
        personDao.findAll().forEach(people::add);
        return people;
    }

    public void addPerson(Person person) {
        System.err.println(person.getFirstName());
        personDao.save(person);
    }
}
