package com.renanrosas.Teste.backend.services;

import com.renanrosas.Teste.backend.dto.PersonDTO;
import com.renanrosas.Teste.backend.entities.Person;
import com.renanrosas.Teste.backend.repositories.PersonRepository;
import com.renanrosas.Teste.backend.services.exceptions.ResourceNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PersonService {

    private final PersonRepository repository;

    @Transactional
    public PersonDTO insert(PersonDTO dto) {
        Person entity = new Person();
        entity.setName(dto.getName());
        entity.setBirthDate(dto.getBirthDate());
        entity = repository.save(entity);
        return new PersonDTO(entity);
    }

    @Transactional
    public PersonDTO update(Long id, PersonDTO dto) {
        try {
            Person entity = repository.getReferenceById(id);
            entity.setName(dto.getName());
            entity.setBirthDate(dto.getBirthDate());
            entity = repository.save(entity);
            return new PersonDTO(entity);
        }
        catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException("Id not found " + id);
        }
    }

    @Transactional(readOnly = true)
    public Page<PersonDTO> findAllPaged(Pageable pageable){
        Page<Person> list = repository.findAll(pageable);
        return list.map(x -> new PersonDTO(x));
    }

    @Transactional(readOnly = true)
    public PersonDTO findById(Long id){
        Optional<Person> obj = repository.findById(id);
        Person entity = obj.orElseThrow(() -> new ResourceNotFoundException("Entity not found"));
        return new PersonDTO(entity);
    }
}
