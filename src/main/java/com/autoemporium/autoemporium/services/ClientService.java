package com.autoemporium.autoemporium.services;

import com.autoemporium.autoemporium.dao.ClientDAO;
import com.autoemporium.autoemporium.models.AccountType;
import com.autoemporium.autoemporium.models.Client;
import com.autoemporium.autoemporium.models.ClientDTO;
import com.autoemporium.autoemporium.models.Status;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.nio.charset.StandardCharsets;

@Service
public class ClientService implements UserDetailsService {
    @Autowired
    private ClientDAO clientDAO;
    @Autowired
    private PasswordEncoder passwordEncoder;
    private AuthenticationManager authenticationManager;

    public ClientService(ClientDAO clientDAO, PasswordEncoder passwordEncoder, @Lazy AuthenticationManager authenticationManager) {
        this.clientDAO = clientDAO;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println(username);
        Client byEmail = clientDAO.findByEmail(username);
        System.out.println(byEmail);
        return clientDAO.findByEmail(username);
    }
    public void saveClient(@RequestBody ClientDTO clientDTO) {
        if (clientDTO == null) {
            throw new RuntimeException();
        }
        Client client = new Client();
        client.setFirstName(clientDTO.getUserFirstname());
        client.setLastName(clientDTO.getUserLastname());
        client.setEmail(clientDTO.getUsername());
        client.setStatus(Status.ACTIVE);
        client.setPassword(passwordEncoder.encode(clientDTO.getPassword()));
        clientDAO.save(client);
    }
    public ResponseEntity<String> login(@RequestBody ClientDTO clientDTO) {
        System.out.println(clientDTO);
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(clientDTO.getUsername(), clientDTO.getPassword());
        System.out.println(usernamePasswordAuthenticationToken);
        Authentication authenticate = authenticationManager.authenticate(usernamePasswordAuthenticationToken);
        System.out.println(authenticate);
        if (authenticate != null) {
            String jwtToken = Jwts.builder()
                    .setSubject(authenticate.getName())
                    .signWith(SignatureAlgorithm.HS256, "okten".getBytes(StandardCharsets.UTF_8))
                    .compact();
            System.out.println(jwtToken);

            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.add("Authorization", "Bearer " + jwtToken);
            return new ResponseEntity<>("login :)", httpHeaders, HttpStatus.OK);
        }
        return new ResponseEntity<>("bad credentials", HttpStatus.FORBIDDEN);
    }
}
