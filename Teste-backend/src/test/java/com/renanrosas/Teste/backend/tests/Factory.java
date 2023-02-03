package com.renanrosas.Teste.backend.tests;

import com.renanrosas.Teste.backend.dto.AddressDTO;
import com.renanrosas.Teste.backend.dto.PersonDTO;
import com.renanrosas.Teste.backend.entities.Address;
import com.renanrosas.Teste.backend.entities.Person;

import java.util.Date;

public class Factory {

    public static Person createPerson(){
        return new Person(1L, "Jose", new Date(2005-01-01));
    }

    public static PersonDTO createPersonDTO(){
        Person person = createPerson();
        return new PersonDTO(person);
    }

    public static Address createAddress(Person person){
        return new Address(1L, "Street test", "10000100", "100", "City test", false, person);
    }

    public static AddressDTO createAddressDTO(Person person){
        Address address = createAddress(person);
        return new AddressDTO(address);
    }

}
