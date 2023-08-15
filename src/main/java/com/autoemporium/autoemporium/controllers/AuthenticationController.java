package com.autoemporium.autoemporium.controllers;

import com.autoemporium.autoemporium.models.users.*;
import com.autoemporium.autoemporium.services.authService.AuthenticationService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/v1/auth")
@AllArgsConstructor
public class AuthenticationController {
    private AuthenticationService authenticationService;

    @PostMapping("/register/owner")
    public ResponseEntity<AuthenticationResponse> registerOwner(@RequestBody AdministratorDTO administratorDTO) {
        return authenticationService.registerOwner(administratorDTO);
    }

    @PostMapping("/register/admin")
    public ResponseEntity<AuthenticationResponse> registerAdmin(@RequestBody AdministratorDTO administratorDTO) {
        return authenticationService.registerAdmin(administratorDTO);
    }

    @PostMapping("/register/manager")
    public ResponseEntity<AuthenticationResponse> registerManager(@RequestBody ManagerDTO managerDTO) {
        return authenticationService.registerManager(managerDTO);
    }

    @PostMapping("/register/seller")
    public ResponseEntity<AuthenticationResponse> registerSeller(@RequestBody SellerDTO sellerDTO) {
        return authenticationService.registerSeller(sellerDTO);
    }

    @PostMapping("/register/mechanic")
    public ResponseEntity<AuthenticationResponse> registerMechanic(@RequestBody MechanicDTO mechanicDTO) {
        return authenticationService.registerMechanic(mechanicDTO);
    }

    @PostMapping("/register/buyer")
    public ResponseEntity<AuthenticationResponse> registerBuyer(@RequestBody BuyerDTO buyerDTO) {
        return authenticationService.registerBuyer(buyerDTO);
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse>  authenticate(@RequestBody UserDTO userDTO) {
        return ResponseEntity.ok(authenticationService.authenticate(userDTO));
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthenticationResponse> refresh(@RequestBody RequestRefresh requestRefresh) {

        return ResponseEntity.ok(authenticationService.refresh(requestRefresh));
    }

}
