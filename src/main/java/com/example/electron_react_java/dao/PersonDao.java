package com.example.electron_react_java.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.example.electron_react_java.models.Person;

@Repository
public interface PersonDao extends CrudRepository<Person, Integer> {
}