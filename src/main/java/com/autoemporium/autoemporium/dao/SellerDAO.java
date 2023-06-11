package com.autoemporium.autoemporium.dao;

import com.autoemporium.autoemporium.models.users.Buyer;
import com.autoemporium.autoemporium.models.users.Seller;
import com.autoemporium.autoemporium.models.users.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SellerDAO extends JpaRepository<Seller,Integer> {
    @Query("select s from Seller s where s.user.username=:username")
    Seller findSellerByUsername(@Param("username") String username);

}
