package com.example.ups.poo.service;

import com.example.ups.poo.dto.Person;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PersonService {
    public List<Person> getAllPeople(){
        List<Person>personList = new ArrayList<>();
        Person p1 = new Person();
        p1.setName("Genesis");
        p1.setLastname("Vinan");
        p1.setAge(21);
        p1.setId("0950368654");

        Person p2 = new Person("diego", "vinan", 24, "0950368688");
        personList.add(p1);
        personList.add(p2);
        return personList;
    }
}
