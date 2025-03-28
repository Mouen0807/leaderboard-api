package com.example.demo.repositories;

import com.example.demo.models.CustomerLogin;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface CustomerLoginRepository extends JpaRepository<CustomerLogin, UUID> {
    public CustomerLogin  findByLogin(String login);
}
