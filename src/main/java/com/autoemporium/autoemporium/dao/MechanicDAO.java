package com.autoemporium.autoemporium.dao;

import com.autoemporium.autoemporium.models.users.Mechanic;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MechanicDAO extends JpaRepository<Mechanic, Integer> {
}
