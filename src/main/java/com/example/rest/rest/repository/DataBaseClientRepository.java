package com.example.rest.rest.repository;
import com.example.rest.rest.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DataBaseClientRepository extends JpaRepository<Client, Long> {

}