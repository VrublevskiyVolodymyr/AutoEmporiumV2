package com.autoemporium.autoemporium.dao;

import com.autoemporium.autoemporium.models.Client;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientDAO extends JpaRepository<Client,Integer> {
    Client findByEmail(String email);
}
