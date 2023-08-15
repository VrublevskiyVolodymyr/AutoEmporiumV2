package com.autoemporium.autoemporium.services.authService;

import com.autoemporium.autoemporium.dao.*;
import com.autoemporium.autoemporium.models.users.*;
import com.autoemporium.autoemporium.services.advertisementService.JwtService;
import com.autoemporium.autoemporium.services.mailService.MailService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class AuthenticationService {
    private UserDAO userDAO;
    private PasswordEncoder passwordEncoder;
    private JwtService jwtService;
    private AuthenticationManager authenticationManager;
    private MailService mailService;
    OwnerDAO ownerDAO;
    AdministratorDAO administratorDAO;
    ManagerDAO managerDAO;
    SellerDAO sellerDAO;
    MechanicDAO mechanicDAO;
    BuyerDAO buyerDAO;


    public ResponseEntity<AuthenticationResponse> registerOwner(AdministratorDTO administratorDTO) {
        if (administratorDTO == null) {
            throw new NullPointerException("AdministratorDTO cannot be null");
        }
        User user = new User();
        user.setUsername(administratorDTO.getUsername());
        user.setPassword(passwordEncoder.encode(administratorDTO.getPassword()));
        String username = administratorDTO.getUsername();
        String password = passwordEncoder.encode(administratorDTO.getPassword());
        String token = jwtService.generateToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        Owner owner1 = ownerDAO.findAll().stream().findFirst().orElse(null);
        if (owner1 == null) {
            Owner owner = Owner.getInstance(username, password, refreshToken);
            ownerDAO.save(owner);
            AuthenticationResponse response = AuthenticationResponse
                    .builder()
                    .token(token)
                    .refreshToken(refreshToken)
                    .build();
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }


    public AuthenticationResponse authenticate(UserDTO userDTO) {
        Authentication authenticate = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        userDTO.getUsername(),
                        userDTO.getPassword()
                )
        );
        User user = userDAO.findByUsername(userDTO.getUsername());
        String token = jwtService.generateToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);
        user.setRefreshToken(refreshToken);
        userDAO.save(user);

        return AuthenticationResponse.builder()
                .token(token)
                .refreshToken(refreshToken)
                .build();
    }

    public AuthenticationResponse refresh(RequestRefresh requestRefresh) {
        String token = requestRefresh.getRefreshToken();
        String username = jwtService.extractUsername(token);
        User user = userDAO.findByUsername(username);
        String newAccessToken = null;
        String newRefreshToken = null;

        if (user
                .getRefreshToken()
                .equals(token)) {

            newAccessToken = jwtService.generateToken(user);
            newRefreshToken = jwtService.generateRefreshToken(user);
            user.setRefreshToken(newRefreshToken);
            userDAO.save(user);
        }

        return AuthenticationResponse
                .builder()
                .token(newAccessToken)
                .refreshToken(newRefreshToken)
                .build();
    }

    public ResponseEntity<AuthenticationResponse> registerAdmin(AdministratorDTO administratorDTO) {

        try {
            if (administratorDTO == null) {
                throw new NullPointerException("AdministratorDTO cannot be null");
            }
        User user = new User();
        user.setUsername(administratorDTO.getUsername());
        user.setPassword(passwordEncoder.encode(administratorDTO.getPassword()));
        String token = jwtService.generateToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        Administrator administrator = new Administrator();
        administrator.setUser(new User(administratorDTO.getUsername(), passwordEncoder.encode(administratorDTO.getPassword()),refreshToken, List.of(Role.ADMIN), true));
        administrator.setCreatedAt(LocalDateTime.now().withNano(0));
        Administrator adminSaved = administratorDAO.save(administrator);
            AuthenticationResponse response = AuthenticationResponse
                    .builder()
                    .token(token)
                    .refreshToken(refreshToken)
                    .build();
            return new ResponseEntity<>(response, HttpStatus.CREATED);

        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<AuthenticationResponse> registerManager(ManagerDTO managerDTO) {
        try {
            if (managerDTO == null) {
                throw new NullPointerException("ManagerDTO cannot be null");
            }
            User user = new User();
            user.setUsername(managerDTO.getUsername());
            user.setPassword(passwordEncoder.encode(managerDTO.getPassword()));
            String token = jwtService.generateToken(user);
            String refreshToken = jwtService.generateRefreshToken(user);

            Manager manager = new Manager();
            manager.setUser(new User(managerDTO.getUsername(), passwordEncoder.encode(managerDTO.getPassword()), refreshToken, List.of(Role.MANAGER), true));
            manager.setFirstName(managerDTO.getUserFirstname());
            manager.setLastName(managerDTO.getUserLastname());
            manager.setPhone(managerDTO.getPhoneNumber());
            manager.setCreatedAt(LocalDateTime.now().withNano(0));
            Manager managerSaved = managerDAO.save(manager);

            AuthenticationResponse response = AuthenticationResponse
                    .builder()
                    .token(token)
                    .refreshToken(refreshToken)
                    .build();
            return new ResponseEntity<>(response, HttpStatus.CREATED);

        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<AuthenticationResponse> registerSeller(SellerDTO sellerDTO) {
        try {
            if (sellerDTO == null) {
                throw new NullPointerException("SellerDTO cannot be null");
            }

            String username = sellerDTO.getUsername();
            User user = userDAO.findByUsername(username);
            if (user != null) {
                List<String> authorities = user.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();

                if (!authorities.contains("SELLER")) {

                    Seller seller = new Seller();
                    seller.setFirstName(sellerDTO.getUserFirstname());
                    seller.setLastName(sellerDTO.getUserLastname());
                    seller.setPhone(sellerDTO.getPhoneNumber());
                    seller.setStatus(Status.ACTIVE);
                    seller.setCreatedAt(LocalDateTime.now().withNano(0));
                    user.getRoles().add(Role.SELLER);
                    if ((authorities.contains("MANAGER")) || (authorities.contains("ADMIN"))) {
                        seller.setAccountType(AccountType.PREMIUM);
                    }
                    seller.setUser(user);
                    Seller sellerSaved = sellerDAO.save(seller);

                    return new ResponseEntity<>(HttpStatus.CREATED);
                }
            } else {

                User user1 = new User();
                user1.setUsername(sellerDTO.getUsername());
                user1.setPassword(passwordEncoder.encode(sellerDTO.getPassword()));
                String token = jwtService.generateToken(user1);
                String refreshToken = jwtService.generateRefreshToken(user1);

                Seller seller = new Seller();
                seller.setFirstName(sellerDTO.getUserFirstname());
                seller.setLastName(sellerDTO.getUserLastname());
                seller.setPhone(sellerDTO.getPhoneNumber());
                seller.setStatus(Status.ACTIVE);
                seller.setCreatedAt(LocalDateTime.now().withNano(0));
                seller.setUser(new User(sellerDTO.getUsername(), passwordEncoder.encode(sellerDTO.getPassword()),refreshToken, List.of(Role.SELLER), true));
                 sellerDAO.save(seller);

                AuthenticationResponse response = AuthenticationResponse
                        .builder()
                        .token(token)
                        .refreshToken(refreshToken)
                        .build();
                return new ResponseEntity<>(response, HttpStatus.CREATED);
            }
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);

        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<AuthenticationResponse> registerMechanic(MechanicDTO mechanicDTO) {
        try {
            if (mechanicDTO == null) {
                throw new NullPointerException("MechanicDTO cannot be null");
            }
            User user = new User();
            user.setUsername(mechanicDTO.getUsername());
            user.setPassword(passwordEncoder.encode(mechanicDTO.getPassword()));
            String token = jwtService.generateToken(user);
            String refreshToken = jwtService.generateRefreshToken(user);
            Mechanic mechanic = new Mechanic();
            mechanic.setUser(new User(mechanicDTO.getUsername(), passwordEncoder.encode(mechanicDTO.getPassword()),refreshToken, List.of(Role.MECHANIC), true));
            mechanic.setCreatedAt(LocalDateTime.now().withNano(0));
             mechanicDAO.save(mechanic);

            AuthenticationResponse response = AuthenticationResponse
                    .builder()
                    .token(token)
                    .refreshToken(refreshToken)
                    .build();
            return new ResponseEntity<>(response, HttpStatus.CREATED);

        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<AuthenticationResponse> registerBuyer(BuyerDTO buyerDTO) {
        try {
            if (buyerDTO == null) {
                throw new NullPointerException("BuyerDTO cannot be null");
            }
            User user = new User();
            user.setUsername(buyerDTO.getUsername());
            user.setPassword(passwordEncoder.encode(buyerDTO.getPassword()));
            String token = jwtService.generateToken(user);
            String refreshToken = jwtService.generateRefreshToken(user);
            Buyer buyer = new Buyer();
            buyer.setUser(new User(buyerDTO.getUsername(), passwordEncoder.encode(buyerDTO.getPassword()),refreshToken, List.of(Role.BUYER), true));
            buyer.setStatus(Status.ACTIVE);
            buyer.setCreatedAt(LocalDateTime.now().withNano(0));
             buyerDAO.save(buyer);

            AuthenticationResponse response = AuthenticationResponse
                    .builder()
                    .token(token)
                    .refreshToken(refreshToken)
                    .build();
            return new ResponseEntity<>(response, HttpStatus.CREATED);

        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
