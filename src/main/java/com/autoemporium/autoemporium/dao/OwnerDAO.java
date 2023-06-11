package com.autoemporium.autoemporium.dao;

import com.autoemporium.autoemporium.models.users.Owner;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OwnerDAO extends JpaRepository<Owner, Integer> {
}
