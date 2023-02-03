package com.renanrosas.Teste.backend.dto;

import com.renanrosas.Teste.backend.entities.Person;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PersonDTO {

    private Long id;
    private String name;
    private Date birthDate;

    public PersonDTO(Person entity){
        id = entity.getId();
        name = entity.getName();
        birthDate = entity.getBirthDate();
    }

}
