package com.renanrosas.Teste.backend.repositories;

import com.renanrosas.Teste.backend.entities.Person;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonRepository extends JpaRepository<Person, Long> {
}
