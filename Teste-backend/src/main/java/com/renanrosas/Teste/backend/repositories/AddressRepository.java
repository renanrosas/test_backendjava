package com.renanrosas.Teste.backend.repositories;

import com.renanrosas.Teste.backend.entities.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AddressRepository extends JpaRepository<Address, Long> {

    @Query("SELECT a FROM Address a WHERE a.person.id = :personId")
    List<Address> findByPersonId(@Param("personId") Long personId);
}
