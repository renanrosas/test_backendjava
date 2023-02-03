package com.renanrosas.Teste.backend.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.renanrosas.Teste.backend.dto.PersonDTO;
import com.renanrosas.Teste.backend.services.PersonService;
import com.renanrosas.Teste.backend.services.exceptions.ResourceNotFoundException;
import com.renanrosas.Teste.backend.tests.Factory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PersonController.class)
public class PersonControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PersonService service;

    @Autowired
    private ObjectMapper objectMapper;

    private long existingId;
    private long nonExistingId;
    private PersonDTO personDTO;
    private PageImpl<PersonDTO> page;

    @BeforeEach
    void setUp() throws Exception {

        existingId = 1L;
        nonExistingId = 2L;

        page = new PageImpl<>(List.of());
        personDTO = Factory.createPersonDTO();

        when(service.insert(any())).thenReturn(personDTO);

        when(service.update(eq(existingId), any())).thenReturn(personDTO);
        when(service.update(eq(nonExistingId), any())).thenThrow(ResourceNotFoundException.class);

        when(service.findById(existingId)).thenReturn(personDTO);
        when(service.findById(nonExistingId)).thenThrow(ResourceNotFoundException.class);

        when(service.findAllPaged(any())).thenReturn(page);
    }

    @Test
    public void insertShouldReturnPersonDTOCreated() throws Exception{
        String jsonBody = objectMapper.writeValueAsString(personDTO);

        mockMvc.perform(post("/api/persons")
                        .content(jsonBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").exists())
                .andExpect(jsonPath("$.birthDate").exists());
    }

    @Test
    public void updateShouldReturnPersonDTOWhenIdExits() throws Exception{

        String jsonBody = objectMapper.writeValueAsString(personDTO);

        mockMvc.perform(put("/api/persons/{id}", existingId)
                        .content(jsonBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").exists())
                .andExpect(jsonPath("$.birthDate").exists());
    }

    @Test
    public void updateShouldReturnNotFoundWhenIdDoesNotExist() throws Exception{
        String jsonBody = objectMapper.writeValueAsString(personDTO);

        mockMvc.perform(put("/api/persons/{id}", nonExistingId)
                        .content(jsonBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void findByIdShouldReturnPersonDTOWhenIdExits() throws Exception{
        mockMvc.perform(get("/api/persons/{id}", existingId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").exists())
                .andExpect(jsonPath("$.birthDate").exists());
    }

    @Test
    public void findByIdShouldReturnNotFoundWhenIdDoesNotExist() throws Exception{
        mockMvc.perform(get("/api/persons/{id}", nonExistingId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void findAllShouldReturnPage() throws Exception{
        mockMvc.perform(get("/api/persons")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
