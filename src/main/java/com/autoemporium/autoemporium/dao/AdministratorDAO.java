package com.autoemporium.autoemporium.dao;

import com.autoemporium.autoemporium.models.users.Administrator;
import com.autoemporium.autoemporium.models.users.Manager;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdministratorDAO extends JpaRepository<Administrator,Integer> {

}
