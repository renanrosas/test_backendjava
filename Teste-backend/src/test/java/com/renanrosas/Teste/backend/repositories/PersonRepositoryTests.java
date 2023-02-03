package com.renanrosas.Teste.backend.repositories;

import com.renanrosas.Teste.backend.entities.Person;
import com.renanrosas.Teste.backend.tests.Factory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

@DataJpaTest
public class PersonRepositoryTests {

    private long existingId;
    private long nonExistingId;
    private long countTotalPerson;

    @Autowired
    private PersonRepository repository;

    @BeforeEach
    void SetUp() throws Exception {
        existingId = 1;
        nonExistingId = 100;
        countTotalPerson = 3;
    }

    @Test
    public void findByIdShouldReturnNonEmptyOptionalWhenIdExists(){
        Optional<Person> result = repository.findById(existingId);
        Assertions.assertTrue(result.isPresent());
    }

    @Test
    public void findByIdShouldReturnEmptyOptionalWhenIdDoesNotExist(){
        Optional<Person> result = repository.findById(nonExistingId);
        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    public void saveShouldPersistWithAutoIncrementWhenIdIsNull(){
        Person person = Factory.createPerson();
        person.setId(0L);
        person = repository.save(person);
        Assertions.assertNotNull(person.getId());
        Assertions.assertEquals(countTotalPerson + 1, person.getId());
    }
}
