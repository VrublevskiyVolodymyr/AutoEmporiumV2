package com.autoemporium.autoemporium.services.userService;

import com.autoemporium.autoemporium.dao.*;
import com.autoemporium.autoemporium.models.users.*;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.util.List;



@Service
@AllArgsConstructor
public class UserService {

    private SellerDAO sellerDAO;

    private UserDAO userDAO;

    private BuyerDAO buyerDAO;

    private ManagerDAO managerDAO;

    private AdministratorDAO administratorDAO;

    private final MechanicDAO mechanicDAO;


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
