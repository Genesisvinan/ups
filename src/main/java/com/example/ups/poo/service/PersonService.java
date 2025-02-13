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

    public PersonService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    private PersonDTO mapPersonToPersonDTO(Person person) {
        PersonDTO personDTO = new PersonDTO();
        personDTO.setName(person.getName() + " " + person.getLastname());
        personDTO.setAge(person.getAge());
        personDTO.setId(person.getPersonId());
        return personDTO;
    }

    private List<PersonDTO> fetchAllPeopleRecords() {
        Iterable<Person> personIterable = personRepository.findAll();
        List<PersonDTO> personDTOList = new ArrayList<>();
        for (Person per : personIterable) {
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

    private Person NameAndLastnameSplit(Person person, PersonDTO personDTO) {
        String[] nameArray = personDTO.getName().split(" ");
        String name = nameArray[0];
        String lastname = nameArray[1];
        person.setName(name);
        person.setLastname(lastname);
        person.setPersonId(personDTO.getId());
        return person;
    }

    private Person mapPersonDTOtoPerson(PersonDTO personDTO) {
        Person person = NameAndLastnameSplit(new Person(), personDTO);
        String personId = personDTO.getId();
        Integer age = personDTO.getAge();
        person.setPersonId(personId);
        person.setAge(age);
        return person;
    }

    public ResponseEntity createPerson(PersonDTO personDTO) {
        Optional<Person> existingPerson = personRepository.findByPersonId(personDTO.getId());
        if (existingPerson.isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("The ID is already registered");
        } else if (personDTO.getName() == null || personDTO.getName().isEmpty() || personDTO.getName().isBlank()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Name is required");
        } else if (personDTO.getAge() <= 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Age is required");
        }
        Person person = mapPersonDTOtoPerson(personDTO);
        personRepository.save(person);
        return ResponseEntity.status(HttpStatus.CREATED).body("Person successfully registered");
    }

    public ResponseEntity updatePerson(PersonDTO personDTO) {
        Optional<Person> personOptional = personRepository.findByPersonId(personDTO.getId());
        if (personOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Person with id: " + personDTO.getId() + " not found");
        }
        Person person = personOptional.get();
        if (personDTO.getName() != null && !personDTO.getName().isBlank()) {
            String fullName = personDTO.getName();
            if (fullName.contains(" ")) {
                person = NameAndLastnameSplit(person, personDTO);
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Both first and last names are required");
            }
        }
        if (personDTO.getAge() > 0) {
            person.setAge(personDTO.getAge());
        } else if (personDTO.getAge() != 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Age is incorrect. Please provide a valid age greater than 0.");
        }
        personRepository.save(person);
        return ResponseEntity.status(HttpStatus.OK).body("Person with Id: " + personDTO.getId() + " was successfully updated");
    }

    public ResponseEntity deletePersonById(String id) {
        Optional<Person> personOptional = personRepository.findByPersonId(id);
        if (personOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Person with id: " + id + " was not found");
        }
        personRepository.delete(personOptional.get());
        return ResponseEntity.status(HttpStatus.OK).body("Person with id: " + id + " was successfully deleted");
    }
}