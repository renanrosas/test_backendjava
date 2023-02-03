package com.renanrosas.Teste.backend.dto;

import com.renanrosas.Teste.backend.entities.Address;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddressDTO {

    private Long id;
    private String streetAddress;
    private String zipCode;
    private String number;
    private String city;
    private Boolean mainAddress;

    public AddressDTO(Address entity){
        id = entity.getId();
        streetAddress = entity.getStreetAddress();
        zipCode = entity.getZipCode();
        number = entity.getNumber();
        city = entity.getCity();
        mainAddress = entity.getMainAddress();
    }
}
