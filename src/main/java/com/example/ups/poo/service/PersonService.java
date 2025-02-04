package com.example.ups.poo.service;

import com.example.ups.poo.dto.Person;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PersonService {

    List<Person> personList = new ArrayList<>();

    public ResponseEntity getAllPeople() {
        if (personList.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Person list is empty");
        }
        return ResponseEntity.status(HttpStatus.OK).body(personList);
    }

    // TODO: Create method that finds and returns person by id.
    public ResponseEntity getPeronById(String id) {
        for (Person person : personList) {
            if (id.equalsIgnoreCase(person.getId())) {
                return ResponseEntity.status(HttpStatus.OK).body(person);
            }
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Person with id: " + id + " not found");
    }

    public ResponseEntity createPerson(Person person) {
        for (Person registeredPerson : personList) {
            if (registeredPerson.getId().equals(person.getId())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("The ID is already registered");
            }
        }
        if (person.getName() == null || person.getName().isEmpty() || person.getName().isBlank()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Name is required");
        } else if (person.getLastname() == null || person.getLastname().isEmpty() || person.getLastname().isBlank()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Lastname is required");
        } else if (person.getAge() <= 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Age is required");
        } else if (person.getId() == null || person.getId().isEmpty() || person.getId().isBlank()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Id is required");
        }

        personList.add(person);
        return ResponseEntity.status(HttpStatus.OK).body("Person successfully registered");
    }

    public ResponseEntity updatePerson(Person person) {
        for (Person per : personList) {
            if (per.getId().equalsIgnoreCase(person.getId())) {
                if (person.getName() != null) {
                    per.setName(person.getName());
                }
                if (person.getLastname() != null) {
                    per.setLastname(person.getLastname());
                }
                if (person.getAge() > 0) {
                    per.setAge(person.getAge());
                }
                return ResponseEntity.status(HttpStatus.OK).body("Person with id: " + person.getId() + " was successfully updated");
            }
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Person with id: " + person.getId() + " not found");
    }

    public ResponseEntity deletePersonById(String id) {
        if (id != null && id.length() < 10) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("id: " + id + "does not have the required length (10 chars min");
        }
        for (Person person : personList) {
            if (id.equalsIgnoreCase(person.getId())) {
                personList.remove(person);
                return ResponseEntity.status(HttpStatus.OK).body("Person with id: " + id + " was succesfully deleted");
            }
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Person with id: " + id + "was not found");
    }
}
