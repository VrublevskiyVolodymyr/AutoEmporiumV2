package com.autoemporium.autoemporium.controllers;

import com.autoemporium.autoemporium.dao.OwnerDAO;
import com.autoemporium.autoemporium.dao.SellerDAO;
import com.autoemporium.autoemporium.models.users.*;
import com.autoemporium.autoemporium.services.userService.UserService;
import com.autoemporium.autoemporium.views.Views;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@Controller
public class UserController {
    private UserService userService;
    private SellerDAO sellerDAO;
    private OwnerDAO ownerDAO;


    @PatchMapping("/seller/{id}/status/{statusId}")
    public void changeSellerStatus(@PathVariable Integer id, @PathVariable Integer statusId) {
        userService.changeSellerStatus(id,statusId);
    }

    @ResponseStatus(HttpStatus.OK)
    @PatchMapping("/buyer/{id}/status/{statusId}")
    public void changeBuyerStatus(@PathVariable Integer id, @PathVariable Integer statusId) {
        userService.changeBuyerStatus(id,statusId);
    }

    @GetMapping("/admin/all")
    @JsonView(value = Views.Level1.class)
    public ResponseEntity<List<Administrator>> getAllAdmins() {
        return userService.getAllAdmins();
    }

    @GetMapping("/mechanics/all")
    @JsonView(value = Views.Level1.class)
    public ResponseEntity<List<Mechanic>> getAllAMechanics() {
        return userService.getAllMechanics();
    }

    @GetMapping("/owner/all")
    @JsonView(value = Views.Level1.class)
    public ResponseEntity<List<Owner>> getAllOwners() {
        return new ResponseEntity<>(ownerDAO.findAll(),HttpStatus.OK);
    }

    @GetMapping("/managers/all")
    @JsonView(value = Views.Level1.class)
    public ResponseEntity<List<Manager>> getAllManagers() {
        return userService.getAllManagers();
    }

    @GetMapping("/buyers/all")
    @JsonView(value = Views.Level1.class)
    public ResponseEntity<List<Buyer>> getAllBuyer() {
        return userService.getAllBuyers();
    }

    @GetMapping("/users/all")
    @JsonView(value = Views.Level1.class)
    public ResponseEntity<List<User>> getAllUser() {
        return userService.getAllUsers();
    }

    @GetMapping("/user/id/{id}")
    @JsonView(value = Views.Level1.class)
    public ResponseEntity<User> getUserById( @PathVariable Integer id) {
        return userService.getUserById(id);
    }

    @GetMapping("/user/username/{username}")
    @JsonView(value = Views.Level1.class)
    public ResponseEntity<User> getUserByUsername( @PathVariable String username) {
        return userService.getUserByUsername(username);
    }

    @GetMapping("/buyer/{username}")
    @JsonView(value = Views.Level1.class)
    public ResponseEntity<Buyer> getBuyerByUsername( @PathVariable String username) {
        return userService.getBuyerByUsername(username);
    }


    @GetMapping("/sellers/all")
    @JsonView(value = Views.Level1.class)
    public ResponseEntity<List<Seller>> getAllSellersWithoutSensetiveInformation() {
        return new ResponseEntity<>(sellerDAO.findAll(), HttpStatus.OK);
    }

    @GetMapping("/admin/seller/all")
    @JsonView(value = Views.Level1.class)
    public ResponseEntity<List<Seller>> getAllSellers() {
        return new ResponseEntity<>(sellerDAO.findAll(), HttpStatus.OK);
    }
}
