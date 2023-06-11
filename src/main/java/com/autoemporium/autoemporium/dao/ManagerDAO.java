package com.autoemporium.autoemporium.dao;

import com.autoemporium.autoemporium.models.users.Buyer;
import com.autoemporium.autoemporium.models.users.Manager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ManagerDAO extends JpaRepository<Manager,Integer>{
}
