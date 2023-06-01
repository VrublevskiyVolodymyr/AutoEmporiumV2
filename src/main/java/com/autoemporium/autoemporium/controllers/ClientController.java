package com.autoemporium.autoemporium.controllers;

import com.autoemporium.autoemporium.dao.ClientDAO;
import com.autoemporium.autoemporium.models.Client;
import com.autoemporium.autoemporium.models.ClientDTO;
import com.autoemporium.autoemporium.services.ClientService;
import com.autoemporium.autoemporium.views.Views;
import com.fasterxml.jackson.annotation.JsonView;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.util.List;

@AllArgsConstructor
@Controller
public class ClientController {
    private ClientService clientService;
    private ClientDAO clientDAO;


    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/clients/save")
    public void saveClient(@RequestBody ClientDTO clientDTO) {
        clientService.saveClient(clientDTO);
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/clients/login")
    public ResponseEntity<String> login(@RequestBody ClientDTO clientDTO) {
        return clientService.login(clientDTO);
    }

    //OPEN
    @GetMapping("/clients/all")
    @JsonView(value = Views.Level3.class)
    public ResponseEntity<List<Client>> getAllClientsWithoutSensetiveInformation() {
        return new ResponseEntity<>(clientDAO.findAll(), HttpStatus.OK);
    }

    //ADMIN
    @GetMapping("/admin/all")
    @JsonView(value = Views.Level1.class)
    public ResponseEntity<List<Client>> getAllClients() {
        return new ResponseEntity<>(clientDAO.findAll(), HttpStatus.OK);
    }
}
