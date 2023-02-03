package com.renanrosas.Teste.backend.service;

import com.renanrosas.Teste.backend.dto.AddressDTO;
import com.renanrosas.Teste.backend.entities.Address;
import com.renanrosas.Teste.backend.entities.Person;
import com.renanrosas.Teste.backend.repositories.AddressRepository;
import com.renanrosas.Teste.backend.repositories.PersonRepository;
import com.renanrosas.Teste.backend.services.AddressService;
import com.renanrosas.Teste.backend.services.exceptions.ResourceNotFoundException;
import com.renanrosas.Teste.backend.tests.Factory;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class AddressServiceTests {

    @InjectMocks
    private AddressService service;

    @Mock
    private AddressRepository repository;

    @Mock
    private PersonRepository personRepository;

    private long existingId;
    private long nonExistingId;
    private List<Address> list;
    private Address address;
    private AddressDTO addressDTO;

    private Person person;

    @BeforeEach
    void setUp() throws Exception {
        existingId = 1L;
        nonExistingId = 2L;
        person = Factory.createPerson();
        address = Factory.createAddress(person);
        addressDTO = Factory.createAddressDTO(person);
        list = new ArrayList<>();

        when(repository.save(ArgumentMatchers.any())).thenReturn(address);

        when(repository.getReferenceById(existingId)).thenReturn(address);
        when(repository.getReferenceById(nonExistingId)).thenThrow(EntityNotFoundException.class);
        when(personRepository.getReferenceById(existingId)).thenReturn(person);
        when(personRepository.getReferenceById(nonExistingId)).thenThrow(EntityNotFoundException.class);

        when(repository.findByPersonId(existingId)).thenReturn(list);
    }

    @Test
    public void insertShouldReturnAddressDTO(){
        AddressDTO result = service.insert(addressDTO, existingId);
        Assertions.assertNotNull(result);
    }

    @Test
    public void findAllAddressesByPersonIdShouldReturnListOfAddresses(){
        List<AddressDTO> result = service.findAllAddressesByPersonId(existingId);
        Assertions.assertNotNull(result);
        Mockito.verify(repository).findByPersonId(existingId);
    }

    @Test
    public void setMainAddressShouldReturnAddressDTOWhenIdExists(){
        AddressDTO result = service.setMainAddress(existingId);
        Assertions.assertNotNull(result);
    }

    @Test
    public void updateShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            service.setMainAddress(nonExistingId);
        });
    }
}
