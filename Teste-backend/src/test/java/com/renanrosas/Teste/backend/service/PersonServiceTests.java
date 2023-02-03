package com.renanrosas.Teste.backend.service;

import com.renanrosas.Teste.backend.dto.PersonDTO;
import com.renanrosas.Teste.backend.entities.Person;
import com.renanrosas.Teste.backend.repositories.PersonRepository;
import com.renanrosas.Teste.backend.services.PersonService;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class PersonServiceTests {

    @InjectMocks
    private PersonService service;

    @Mock
    private PersonRepository repository;

    private long existingId;
    private long nonExistingId;
    private Person person;
    private PersonDTO personDTO;

    private PageImpl<Person> page;

    @BeforeEach
    void setUp() throws Exception {
        existingId = 1L;
        nonExistingId = 2L;
        person = Factory.createPerson();
        personDTO = Factory.createPersonDTO();
        page = new PageImpl<>(List.of(person));

        when(repository.findAll((Pageable) ArgumentMatchers.any())).thenReturn(page);
        when(repository.save(ArgumentMatchers.any())).thenReturn(person);
        when(repository.findById(existingId)).thenReturn(Optional.of(person));
        when(repository.findById(nonExistingId)).thenReturn(Optional.empty());

        when(repository.getReferenceById(existingId)).thenReturn(person);
        when(repository.getReferenceById(nonExistingId)).thenThrow(EntityNotFoundException.class);
    }

    @Test
    public void insertShouldReturnPersonDTO(){
        PersonDTO result = service.insert(personDTO);
        Assertions.assertNotNull(result);
    }

    @Test
    public void updateShouldReturnPersonDTOWhenIdExists(){
        PersonDTO result = service.update(existingId, personDTO);
        Assertions.assertNotNull(result);
    }

    @Test
    public void updateShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            service.update(nonExistingId, personDTO);
        });
    }

    @Test
    public void findAllPagedShouldReturnPage(){
        Pageable pageable = PageRequest.of(0, 10);
        Page<PersonDTO> result = service.findAllPaged(pageable);
        Assertions.assertNotNull(result);
        Mockito.verify(repository).findAll(pageable);
    }

    @Test
    public void findByIdShouldReturnPersonDTOWhenIdExists(){
        PersonDTO result = service.findById(existingId);
        Assertions.assertNotNull(result);
    }

    @Test
    public void findByIdShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            service.findById(nonExistingId);
        });
    }
}
