package com.renanrosas.Teste.backend.repositories;

import com.renanrosas.Teste.backend.entities.Address;
import com.renanrosas.Teste.backend.entities.Person;
import com.renanrosas.Teste.backend.tests.Factory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

@DataJpaTest
public class AddressRepositoryTests {

    private long existingId;
    private long nonExistingId;
    private long countTotalPerson;
    private Person person;
    private Address address;

    @Autowired
    private AddressRepository repository;

    @BeforeEach
    void SetUp() throws Exception {
        existingId = 1;
        nonExistingId = 100;
        countTotalPerson = 3;
        person = Factory.createPerson();
        address = Factory.createAddress(person);
    }

    @Test
    public void findByPersonIdShouldReturnNonEmptyListWhenIdExists(){
        List<Address> result = repository.findByPersonId(existingId);
        Assertions.assertFalse(result.isEmpty());
    }

    @Test
    public void findByPersonIdShouldReturnEmptyOptionalWhenIdDoesNotExist(){
        List<Address> result = repository.findByPersonId(nonExistingId);
        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    public void saveShouldPersistWithAutoIncrementWhenIdIsNull(){
        address.setId(0L);
        address = repository.save(address);
        Assertions.assertNotNull(address.getId());
        Assertions.assertEquals(countTotalPerson + 1, address.getId());
    }
}
