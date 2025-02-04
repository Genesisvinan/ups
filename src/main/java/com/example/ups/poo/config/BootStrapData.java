package com.example.ups.poo.config;

import com.example.ups.poo.entity.Person;
import com.example.ups.poo.repository.PersonRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class BootStrapData implements CommandLineRunner {

    private final PersonRepository personRepository;

    public BootStrapData(PersonRepository PersonRepository){
        this.personRepository = PersonRepository;
    }
    @Override
    public void run(String... args) throws Exception {
        Person p1 = new Person();
        p1.setName("Genesis");
        p1.setLastname("Vinan");
        p1.setAge(21);
        p1.setPersonId("0950368654");

        Person p2 = new Person();
        p2.setName("Diego");
        p2.setLastname("Vinan");
        p2.setAge(24);
        p2.setPersonId("0950368688");

        personRepository.save(p1);
        personRepository.save(p2);

    }
}
