package com.example.ups.poo.service;

import com.example.ups.poo.dto.PersonDTO;
import com.example.ups.poo.entity.Person;
import com.example.ups.poo.repository.PersonRepository;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PersonService {

    private final PersonRepository personRepository;

    public PersonService(PersonRepository personRepository){
        this.personRepository = personRepository;
    }

    private PersonDTO mapPersonToPersonDTO(Person person){
        PersonDTO personDTO = new PersonDTO();
        personDTO.setName(person.getName() + " " + person.getLastname());
        personDTO.setAge(person.getAge());
        personDTO.setId(person.getPersonId());
        return personDTO;

    }

    private List<PersonDTO> fetchAllPeopleRecords(){
        Iterable<Person> personIterable= personRepository.findAll();
        List<PersonDTO> personDTOList = new ArrayList<>();
        for (Person per: personIterable) {
            PersonDTO personDTO = mapPersonToPersonDTO(per);
            personDTOList.add(personDTO);
        }
        return personDTOList;
    }

    public ResponseEntity getAllPeople() {
        List<PersonDTO> personDTOList = fetchAllPeopleRecords();
    if (personDTOList.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Person list is empty");
        }
        return ResponseEntity.status(HttpStatus.OK).body(personDTOList);
    }

    // TODO: Create method that finds and returns person by id.
    public ResponseEntity getPersonById(String id) {
        Optional<Person> personOptional = personRepository.findByPersonId(id);
        if (personOptional.isPresent()) {
            PersonDTO personDTO = mapPersonToPersonDTO(personOptional.get());
            return ResponseEntity.status(HttpStatus.OK).body(personDTO);
        } else {
            String message = "Person with id: " + id + "not found";
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
        }
    }

//    public ResponseEntity createPerson(PersonDTO person) {
//        for (PersonDTO registeredPerson : personDTOList) {
//            if (registeredPerson.getId().equals(person.getId())) {
//                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("The ID is already registered");
//            }
//        }
//        if (person.getName() == null || person.getName().isEmpty() || person.getName().isBlank()) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Name is required");
//        } else if (person.getLastname() == null || person.getLastname().isEmpty() || person.getLastname().isBlank()) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Lastname is required");
//        } else if (person.getAge() <= 0) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Age is required");
//        } else if (person.getId() == null || person.getId().isEmpty() || person.getId().isBlank()) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Id is required");
//        }
//
//        personList.add(person);
//        return ResponseEntity.status(HttpStatus.OK).body("Person successfully registered");
//    }
//
//    public ResponseEntity updatePerson(PersonDTO person) {
//        for (PersonDTO per : personDTOList) {
//            if (per.getId().equalsIgnoreCase(person.getId())) {
//                if (person.getName() != null) {
//                    per.setName(person.getName());
//                }
//                if (person.getLastname() != null) {
//                    per.setLastname(person.getLastname());
//                }
//                if (person.getAge() > 0) {
//                    per.setAge(person.getAge());
//                }
//                return ResponseEntity.status(HttpStatus.OK).body("Person with id: " + person.getId() + " was successfully updated");
//            }
//        }
//        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Person with id: " + person.getId() + " not found");
//    }
//
//    public ResponseEntity deletePersonById(String id) {
//        if (id != null && id.length() < 10) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("id: " + id + "does not have the required length (10 chars min");
//        }
//        for (PersonDTO person : personList) {
//            if (id.equalsIgnoreCase(person.getId())) {
//                personList.remove(person);
//                return ResponseEntity.status(HttpStatus.OK).body("Person with id: " + id + " was successfully deleted");
//            }
//        }
//        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Person with id: " + id + "was not found");
//    }
}
