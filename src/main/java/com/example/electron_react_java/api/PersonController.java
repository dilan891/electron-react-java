package com.example.electron_react_java.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.electron_react_java.models.Person;
import com.example.electron_react_java.services.PersonService;

@RestController
@RequestMapping("/api")
public class PersonController {
    private final PersonService personService;

    @Autowired
    private PersonController(PersonService personService) {
        this.personService = personService;
    }

    @GetMapping("/people")
    public ResponseEntity<List<Person>> getPeople() {
        return new ResponseEntity<List<Person>>(personService.getAllPeople(), HttpStatus.OK);
    }

    @PostMapping("/person")
    public ResponseEntity<String> addPerson(@RequestBody Person person) {
        personService.addPerson(person);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
