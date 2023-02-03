package com.renanrosas.Teste.backend.services;

import com.renanrosas.Teste.backend.dto.AddressDTO;
import com.renanrosas.Teste.backend.entities.Address;
import com.renanrosas.Teste.backend.entities.Person;
import com.renanrosas.Teste.backend.repositories.AddressRepository;
import com.renanrosas.Teste.backend.repositories.PersonRepository;
import com.renanrosas.Teste.backend.services.exceptions.ResourceNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AddressService {

    private final AddressRepository repository;

    private final PersonRepository personRepository;

    @Transactional
    public AddressDTO insert(AddressDTO dto, Long person_id) {
        Address entity = new Address();
        Person person = personRepository.getReferenceById(person_id);
        copyToEntity(dto, entity);
        entity.setPerson(person);
        entity = repository.save(entity);
        return new AddressDTO(entity);
    }

    @Transactional(readOnly = true)
    public List<AddressDTO> findAllAddressesByPersonId(Long personId) {
        List<Address> list = repository.findByPersonId(personId);
        return list.stream().map(x -> new AddressDTO(x)).collect(Collectors.toList());
    }

    @Transactional
    public AddressDTO setMainAddress(Long id) {
        try{
            Address entity = repository.getReferenceById(id);
            entity.setMainAddress(true);
            entity = repository.save(entity);
            return new AddressDTO(entity);
        }
        catch (EntityNotFoundException e){
            throw new ResourceNotFoundException("Id not found" + id);
        }
    }

    private void copyToEntity(AddressDTO dto, Address entity) {
        entity.setStreetAddress(dto.getStreetAddress());
        entity.setZipCode(dto.getZipCode());
        entity.setNumber(dto.getNumber());
        entity.setCity(dto.getCity());
        entity.setMainAddress(false);
    }
}
