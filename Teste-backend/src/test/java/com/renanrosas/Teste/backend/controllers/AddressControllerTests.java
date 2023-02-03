package com.renanrosas.Teste.backend.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.renanrosas.Teste.backend.dto.AddressDTO;
import com.renanrosas.Teste.backend.entities.Person;
import com.renanrosas.Teste.backend.services.AddressService;
import com.renanrosas.Teste.backend.services.exceptions.ResourceNotFoundException;
import com.renanrosas.Teste.backend.tests.Factory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AddressController.class)
public class AddressControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AddressService service;

    @Autowired
    private ObjectMapper objectMapper;

    private long existingId;
    private long nonExistingId;

    private Person person;

    private AddressDTO addressDTO;
    private List<AddressDTO> list;

    @BeforeEach
    void setUp() throws Exception {

        existingId = 1L;
        nonExistingId = 2L;
        person = Factory.createPerson();
        addressDTO = Factory.createAddressDTO(person);
        list = new ArrayList<>();

        when(service.insert(any(),eq(existingId))).thenReturn(addressDTO);
        when(service.insert(any(),eq(nonExistingId))).thenThrow(ResourceNotFoundException.class);

        when(service.findAllAddressesByPersonId(existingId)).thenReturn(list);
        when(service.findAllAddressesByPersonId(nonExistingId)).thenThrow(ResourceNotFoundException.class);

        when(service.setMainAddress(existingId)).thenReturn(addressDTO);
        when(service.setMainAddress(nonExistingId)).thenThrow(ResourceNotFoundException.class);
    }

    @Test
    public void insertShouldReturnAddressDTOCreatedWhenPersonIdExists() throws Exception{
        String jsonBody = objectMapper.writeValueAsString(addressDTO);

        mockMvc.perform(post("/api/persons/{personId}/addresses", existingId)
                        .content(jsonBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.streetAddress").exists())
                .andExpect(jsonPath("$.zipCode").exists())
                .andExpect(jsonPath("$.number").exists())
                .andExpect(jsonPath("$.city").exists());
    }

    @Test
    public void insertShouldReturnNotFoundWhenPersonIdDoesNotExist() throws Exception{
        String jsonBody = objectMapper.writeValueAsString(addressDTO);

        mockMvc.perform(post("/api/persons/{personId}/addresses", nonExistingId)
                        .content(jsonBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void findAllAddressesByPersonIdShouldReturnListOfAddresses() throws Exception{
        mockMvc.perform(get("/api/persons/{personId}/addresses", existingId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void setMainAddressShouldReturnAddressDTOWhenIdExists() throws Exception{

        String jsonBody = objectMapper.writeValueAsString(addressDTO);

        mockMvc.perform(put("/api/persons/1/addresses/{id}", existingId)
                        .content(jsonBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void setMainAddressShouldReturnNotFoundWhenIdDoesNotExist() throws Exception{

        String jsonBody = objectMapper.writeValueAsString(addressDTO);

        mockMvc.perform(put("/api/persons/1/addresses/{id}", nonExistingId)
                        .content(jsonBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
