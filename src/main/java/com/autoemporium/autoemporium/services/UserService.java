package com.autoemporium.autoemporium.services;

import com.autoemporium.autoemporium.dao.*;
import com.autoemporium.autoemporium.models.users.*;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
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
import java.util.List;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {
    @Autowired
    private SellerDAO sellerDAO;
    @Autowired
    private UserDAO userDAO;
    @Autowired
    private OwnerDAO ownerDAO;
    @Autowired
    private BuyerDAO buyerDAO;
    @Autowired
    private ManagerDAO managerDAO;
    @Autowired
    private AdministratorDAO administratorDAO;
    @Autowired
    private PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;


    public UserService(UserDAO userDAO, PasswordEncoder passwordEncoder, @Lazy AuthenticationManager authenticationManager) {
        this.userDAO = userDAO;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
    }

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
        String managerEmail = managerDAO.findAll().stream().findFirst().map(Manager::getUser).map(User::getUsername).toString();
        if (!(managerEmail == null)) {
            return managerEmail;
        }
        return null;
    }

    public void saveSeller(@RequestBody SellerDTO sellerDTO) {
        if (sellerDTO == null) {
            throw new RuntimeException();
        }
        Seller seller = new Seller();
        seller.setFirstName(sellerDTO.getFirstname());
        seller.setLastName(sellerDTO.getLastname());
        seller.setStatus(Status.ACTIVE);
        seller.setUser(new User(sellerDTO.getUsername(), passwordEncoder.encode(sellerDTO.getPassword()), List.of(Role.SELLER)));
        sellerDAO.save(seller);
    }

    public ResponseEntity<String> login(@RequestBody UserDTO userDTO) {

        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(userDTO.getUsername(), userDTO.getPassword());
        System.out.println(usernamePasswordAuthenticationToken);
        Authentication authenticate = authenticationManager.authenticate(usernamePasswordAuthenticationToken);
        System.out.println(authenticate);
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
        buyer.setUser(new User(buyerDTO.getUsername(), passwordEncoder.encode(buyerDTO.getPassword()), List.of(Role.BUYER)));
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
        manager.setUser(new User(managerDTO.getUsername(), passwordEncoder.encode(managerDTO.getPassword()), List.of(Role.MANAGER)));
        manager.setFirstName(managerDTO.getFirstname());
        manager.setLastName(managerDTO.getLastname());
        manager.setPhone(managerDTO.getPhone());
        managerDAO.save(manager);
    }

    public void saveAdmin(AdministratorDTO administratorDTO) {
        if (administratorDTO == null) {
            throw new RuntimeException();
        }
        Administrator administrator = new Administrator();
        administrator.setUser(new User(administratorDTO.getUsername(), passwordEncoder.encode(administratorDTO.getPassword()), List.of(Role.ADMIN)));
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
        if(owner1 == null){
        Owner owner = Owner.getInstance(administratorDTO.getUsername(), passwordEncoder.encode(administratorDTO.getPassword()));
        ownerDAO.save(owner);
        return new ResponseEntity<>("Owner is saved :)", HttpStatus.CREATED);
        }
        return new ResponseEntity<>("Owner already exists, you cannot create an Owner", HttpStatus.FORBIDDEN);
    }

    public ResponseEntity<List<Administrator>> getAllAdmins() {
        return new ResponseEntity<>(administratorDAO.findAll(), HttpStatus.OK);
    }

    public void changeSellerStatus(Integer id,Integer statusId) {
        Seller seller = sellerDAO.findById(id).orElse(null);

        if (seller != null & statusId > 0 & statusId<=2 ) {
            if (statusId == 1) {
                seller.setStatus(Status.ACTIVE);
            } else {
                seller.setStatus(Status.BANNED);
            }
            sellerDAO.save(seller);
        }
    }

    public void changeBuyerStatus(Integer id, Integer statusId) {
        Buyer buyer = buyerDAO.findById(id).orElse(null);

        if (buyer != null & statusId > 0 & statusId<=2 ) {
            if (statusId == 1) {
                buyer.setStatus(Status.ACTIVE);
            } else {
                buyer.setStatus(Status.BANNED);
            }
            buyerDAO.save(buyer);
        }
    }
}
