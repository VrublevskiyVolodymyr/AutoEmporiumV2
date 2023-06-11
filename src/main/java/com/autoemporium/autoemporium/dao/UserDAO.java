package com.autoemporium.autoemporium.dao;

import com.autoemporium.autoemporium.models.users.Role;
import com.autoemporium.autoemporium.models.users.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface UserDAO extends JpaRepository<User,Integer> {
    User findByUsername(String username);
    User findUserById(Integer id);
    List<User> findByRolesContaining(Role role);
}
