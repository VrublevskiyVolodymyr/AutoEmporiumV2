package com.autoemporium.autoemporium.services;

import com.autoemporium.autoemporium.dao.*;
import com.autoemporium.autoemporium.models.users.*;
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
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.nio.charset.StandardCharsets;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@AllArgsConstructor
public class UserService implements UserDetailsService {

    private SellerDAO sellerDAO;

    private UserDAO userDAO;

    private OwnerDAO ownerDAO;

    private BuyerDAO buyerDAO;

    private ManagerDAO managerDAO;

    private AdministratorDAO administratorDAO;

    private PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    private final MechanicDAO mechanicDAO;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User byUsername = userDAO.findByUsername(username);
        return userDAO.findByUsername(username);
    }

    public ResponseEntity<String> login(@RequestBody UserDTO userDTO) {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(userDTO.getUsername(), userDTO.getPassword());
        Authentication authenticate = authenticationManager.authenticate(usernamePasswordAuthenticationToken);

        if (authenticate != null) {
            String jwtToken = Jwts.builder()
                    .setSubject(authenticate.getName())
                    .signWith(SignatureAlgorithm.HS256, "okten".getBytes(StandardCharsets.UTF_8))
                    .compact();

            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.add("Authorization", "Bearer " + jwtToken);
            return new ResponseEntity<>("login :)", httpHeaders, HttpStatus.OK);
        }
        return new ResponseEntity<>("bad credentials", HttpStatus.FORBIDDEN);
    }

    public ResponseEntity<String> saveOwner(AdministratorDTO administratorDTO) {
        if (administratorDTO == null) {
            throw new NullPointerException("AdministratorDTO cannot be null");
        }
        Owner owner1 = ownerDAO.findAll().stream().findFirst().orElse(null);
        if (owner1 == null) {
            Owner owner = Owner.getInstance(administratorDTO.getUsername(), passwordEncoder.encode(administratorDTO.getPassword()));
            ownerDAO.save(owner);
            return new ResponseEntity<>("Owner is saved :)", HttpStatus.CREATED);
        }
        return new ResponseEntity<>("Owner already exists, you cannot create an Owner", HttpStatus.FORBIDDEN);
    }

    public ResponseEntity<String> saveAdmin(AdministratorDTO administratorDTO) {
        try {
            if (administratorDTO == null) {
                throw new NullPointerException("AdministratorDTO cannot be null");
            }
            Administrator administrator = new Administrator();
            administrator.setUser(new User(administratorDTO.getUsername(), passwordEncoder.encode(administratorDTO.getPassword()), List.of(Role.ADMIN), true));
            administrator.setCreatedAt(LocalDateTime.now().withNano(0));
            Administrator adminSaved = administratorDAO.save(administrator);

            return new ResponseEntity<>("The administrator has been successfully added to the dealer with ID: " + adminSaved.getId(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error saving administrator."+ e.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    public ResponseEntity<String> saveManager(ManagerDTO managerDTO) {
        try {
            if (managerDTO == null) {
                throw new NullPointerException("ManagerDTO cannot be null");
            }

            Manager manager = new Manager();
            manager.setUser(new User(managerDTO.getUsername(), passwordEncoder.encode(managerDTO.getPassword()), List.of(Role.MANAGER), true));
            manager.setFirstName(managerDTO.getUserFirstname());
            manager.setLastName(managerDTO.getUserLastname());
            manager.setPhone(managerDTO.getPhoneNumber());
            manager.setCreatedAt(LocalDateTime.now().withNano(0));
            Manager managerSaved = managerDAO.save(manager);

            return new ResponseEntity<>("The manager has been successfully saved with ID: " + managerSaved.getId(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error saving manager."+ e.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    public ResponseEntity<String> saveSeller(@RequestBody SellerDTO sellerDTO) {
        try {
            if (sellerDTO == null) {
                throw new NullPointerException("SellerDTO cannot be null");
            }

            String username = sellerDTO.getUsername();
            User user = userDAO.findByUsername(username);
            if (user != null) {
                List<String> authorities = loadUserByUsername(username).getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();

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

                    return new ResponseEntity<>("Seller has been successfully saved with ID: " + sellerSaved.getId(), HttpStatus.OK);
                }
            } else {
                Seller seller = new Seller();
                seller.setFirstName(sellerDTO.getUserFirstname());
                seller.setLastName(sellerDTO.getUserLastname());
                seller.setPhone(sellerDTO.getPhoneNumber());
                seller.setStatus(Status.ACTIVE);
                seller.setCreatedAt(LocalDateTime.now().withNano(0));
                seller.setUser(new User(sellerDTO.getUsername(), passwordEncoder.encode(sellerDTO.getPassword()), List.of(Role.SELLER), true));
                Seller sellerSaved = sellerDAO.save(seller);

                return new ResponseEntity<>("Seller has been successfully saved with ID: " + sellerSaved.getId(), HttpStatus.OK);
            }
            return new ResponseEntity<>("Seller is already exist", HttpStatus.FORBIDDEN);

        } catch (Exception e) {
            return new ResponseEntity<>("Error saving seller"+ e.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<String> saveMechanic(MechanicDTO mechanicDTO) {
        try {
            if (mechanicDTO == null) {
                throw new NullPointerException("MechanicDTO cannot be null");
            }
            Mechanic mechanic = new Mechanic();
            mechanic.setUser(new User(mechanicDTO.getUsername(), passwordEncoder.encode(mechanicDTO.getPassword()), List.of(Role.MECHANIC), true));
            mechanic.setCreatedAt(LocalDateTime.now().withNano(0));
            Mechanic mechanicSaved = mechanicDAO.save(mechanic);

            return new ResponseEntity<>("Mechanic has been saved successfully with ID: " + mechanicSaved.getId(), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("Error saving mechanic"+ e.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    public ResponseEntity<String> saveBuyer(BuyerDTO buyerDTO) {
        try {
            if (buyerDTO == null) {
                throw new NullPointerException("BuyerDTO cannot be null");
            }
            Buyer buyer = new Buyer();
            buyer.setUser(new User(buyerDTO.getUsername(), passwordEncoder.encode(buyerDTO.getPassword()), List.of(Role.BUYER), true));
            buyer.setStatus(Status.ACTIVE);
            buyer.setCreatedAt(LocalDateTime.now().withNano(0));
            Buyer buyerSaved = buyerDAO.save(buyer);

            return new ResponseEntity<>("Buyer is saved successfully with ID: " + buyerSaved.getId(), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("Error saving buyer."+ e.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<List<Administrator>> getAllAdmins() {
        return new ResponseEntity<>(administratorDAO.findAll(), HttpStatus.OK);
    }

    public ResponseEntity<List<Manager>> getAllManagers() {
        return new ResponseEntity<>(managerDAO.findAll(), HttpStatus.OK);
    }

    public ResponseEntity<List<Mechanic>> getAllMechanics() {
        return new ResponseEntity<>(mechanicDAO.findAll(), HttpStatus.OK);
    }

    public ResponseEntity<List<Buyer>> getAllBuyers() {
        return new ResponseEntity<>(buyerDAO.findAll(), HttpStatus.OK);
    }

    public ResponseEntity<List<User>> getAllUsers() {
        return new ResponseEntity<>(userDAO.findAll(), HttpStatus.OK);
    }

    public ResponseEntity<User> getUserById(Integer id) {
        return new ResponseEntity<>(userDAO.findUserById(id), HttpStatus.OK);
    }

    public ResponseEntity<User> getUserByUsername(String username) {
        return new ResponseEntity<>(userDAO.findByUsername(username), HttpStatus.OK);
    }

    public ResponseEntity<Buyer> getBuyerByUsername(String username) {
        return new ResponseEntity<>(buyerDAO.findBuyerByUsername(username), HttpStatus.OK);
    }


    public void changeSellerStatus(Integer id, Integer statusId) {
        Seller seller = sellerDAO.findById(id).orElse(null);

        if (seller != null & statusId > 0 & statusId <= 2) {
            if (statusId == 1) {
                seller.setStatus(Status.ACTIVE);
                seller.getUser().setStatus(true);
            } else {
                seller.setStatus(Status.BANNED);
                seller.getUser().setStatus(false);
            }
            sellerDAO.save(seller);
        }
    }

    public void changeBuyerStatus(Integer id, Integer statusId) {
        Buyer buyer = buyerDAO.findById(id).orElse(null);

        if (buyer != null & statusId > 0 & statusId <= 2) {
            if (statusId == 1) {
                buyer.setStatus(Status.ACTIVE);
                buyer.getUser().setStatus(true);
            } else {
                buyer.setStatus(Status.BANNED);
                buyer.getUser().setStatus(false);

            }
            buyerDAO.save(buyer);
        }
    }



    public String getManagerEmail() {
        String managerEmail = managerDAO.findAll().stream().findFirst().map(Manager::getUser).map(User::getUsername).orElse(null);

        if (!(managerEmail == null)) {
            return managerEmail;
        }
        return null;
    }
}
