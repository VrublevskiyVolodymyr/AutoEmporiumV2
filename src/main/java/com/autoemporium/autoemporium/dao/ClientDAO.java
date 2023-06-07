package com.autoemporium.autoemporium.dao;

import com.autoemporium.autoemporium.models.Client;
import com.autoemporium.autoemporium.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ClientDAO extends JpaRepository<Client,Integer> {
    Client findByEmail(String email);
    List<Client> findByRolesContaining(Role role);
}
