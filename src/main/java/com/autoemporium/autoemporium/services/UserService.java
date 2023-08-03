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

//    public UserService(SellerDAO sellerDAO, UserDAO userDAO, OwnerDAO ownerDAO, BuyerDAO buyerDAO, ManagerDAO managerDAO, AdministratorDAO administratorDAO, PasswordEncoder passwordEncoder, @Lazy AuthenticationManager authenticationManager) {
//        this.sellerDAO = sellerDAO;
//        this.userDAO = userDAO;
//        this.ownerDAO = ownerDAO;
//        this.buyerDAO = buyerDAO;
//        this.managerDAO = managerDAO;
//        this.administratorDAO = administratorDAO;
//        this.passwordEncoder = passwordEncoder;
//        this.authenticationManager = authenticationManager;
//    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        System.out.println(username);
        User byUsername = userDAO.findByUsername(username);
//        System.out.println(byEmail);
        return userDAO.findByUsername(username);
    }

//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        System.out.println(username);
//        Client byEmail = clientDAO.findByEmail(username);
//        System.out.println(byEmail);
//        return clientDAO.findByEmail(username);
//    }

    public String getManagerEmail() {
//        List<User> managerClients = userDAO.findByRolesContaining(Role.MANAGER);
//        if (!((List<?>) managerClients).isEmpty()) {
//            User managerClient = managerClients.get(0);
//            return managerClient.getUsername();
//        }
//        return null;
        String managerEmail = managerDAO.findAll().stream().findFirst().map(Manager::getUser).map(User::getUsername).orElse(null);;
        if (!(managerEmail == null)) {
            return managerEmail;
        }
        return null;
    }

    public ResponseEntity<String> saveSeller(@RequestBody SellerDTO sellerDTO) {
        if (sellerDTO == null) {
            throw new RuntimeException();
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
                user.getRoles().add(Role.SELLER);
                if ((authorities.contains("MANAGER"))||(authorities.contains("ADMIN"))) {
                seller.setAccountType(AccountType.PREMIUM);
                }
                seller.setUser(user);
                sellerDAO.save(seller);
                return new ResponseEntity<>(HttpStatus.OK);
            }
        } else {
            Seller seller = new Seller();
            seller.setFirstName(sellerDTO.getUserFirstname());
            seller.setLastName(sellerDTO.getUserLastname());
            seller.setPhone(sellerDTO.getPhoneNumber());
            seller.setStatus(Status.ACTIVE);
            seller.setUser(new User(sellerDTO.getUsername(), passwordEncoder.encode(sellerDTO.getPassword()), List.of(Role.SELLER), true));
            sellerDAO.save(seller);
            return new ResponseEntity<>(HttpStatus.OK);
        }


        return new ResponseEntity<>("Seller is already exist", HttpStatus.FORBIDDEN);
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

    public void saveBuyer(BuyerDTO buyerDTO) {
        if (buyerDTO == null) {
            throw new RuntimeException();
        }
        Buyer buyer = new Buyer();
        buyer.setUser(new User(buyerDTO.getUsername(), passwordEncoder.encode(buyerDTO.getPassword()), List.of(Role.BUYER),true));
        buyer.setStatus(Status.ACTIVE);
        buyerDAO.save(buyer);
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

    public void saveManager(ManagerDTO managerDTO) {
        if (managerDTO == null) {
            throw new RuntimeException();
        }
        Manager manager = new Manager();
        manager.setUser(new User(managerDTO.getUsername(), passwordEncoder.encode(managerDTO.getPassword()), List.of(Role.MANAGER),true));
        manager.setFirstName(managerDTO.getUserFirstname());
        manager.setLastName(managerDTO.getUserLastname());
        manager.setPhone(managerDTO.getPhoneNumber());
        managerDAO.save(manager);
    }


    public void saveAdmin(AdministratorDTO administratorDTO) {
        if (administratorDTO == null) {
            throw new RuntimeException();
        }
        Administrator administrator = new Administrator();
        administrator.setUser(new User(administratorDTO.getUsername(), passwordEncoder.encode(administratorDTO.getPassword()), List.of(Role.ADMIN),true));
        administratorDAO.save(administrator);
    }


    public ResponseEntity<List<Manager>> getAllManagers() {
        return new ResponseEntity<>(managerDAO.findAll(), HttpStatus.OK);
    }


    public ResponseEntity<String> saveOwner(AdministratorDTO administratorDTO) {
        if (administratorDTO == null) {
            throw new RuntimeException();
        }
        Owner owner1 = ownerDAO.findAll().stream().findFirst().orElse(null);
        if (owner1 == null) {
            Owner owner = Owner.getInstance(administratorDTO.getUsername(), passwordEncoder.encode(administratorDTO.getPassword()));
            ownerDAO.save(owner);
            return new ResponseEntity<>("Owner is saved :)", HttpStatus.CREATED);
        }
        return new ResponseEntity<>("Owner already exists, you cannot create an Owner", HttpStatus.FORBIDDEN);
    }

    public ResponseEntity<List<Administrator>> getAllAdmins() {
        return new ResponseEntity<>(administratorDAO.findAll(), HttpStatus.OK);
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
}
