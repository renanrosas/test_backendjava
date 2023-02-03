package com.renanrosas.Teste.backend.controllers;

import com.renanrosas.Teste.backend.dto.AddressDTO;
import com.renanrosas.Teste.backend.services.AddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(value = "/api/persons/{personId}/addresses")
@RequiredArgsConstructor
public class AddressController {

    private final AddressService service;

    @PostMapping
    public ResponseEntity<AddressDTO> insert(@RequestBody AddressDTO dto, @PathVariable Long personId){
        dto = service.insert(dto, personId);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(dto.getId()).toUri();
        return ResponseEntity.created(uri).body(dto);
    }

    @GetMapping
    public ResponseEntity<List<AddressDTO>> findAllAddressesByPersonId(@PathVariable Long personId){
        List<AddressDTO> list = service.findAllAddressesByPersonId(personId);
        return ResponseEntity.ok().body(list);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<AddressDTO> setMainAddress(@PathVariable Long id){
        AddressDTO dto = service.setMainAddress(id);
        return ResponseEntity.ok().body(dto);
    }
}
