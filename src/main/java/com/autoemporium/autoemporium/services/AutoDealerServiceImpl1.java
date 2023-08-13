package com.autoemporium.autoemporium.services;

import com.autoemporium.autoemporium.dao.AutoDealerDAO;
import com.autoemporium.autoemporium.models.*;
import com.autoemporium.autoemporium.models.users.*;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class AutoDealerServiceImpl1 implements AutoDealerService {
    private final AutoDealerDAO autoDealerDAO;
    private PasswordEncoder passwordEncoder;

    @Override
    public ResponseEntity<String> createAutoDealer(String name, String location, List<AdministratorDealerDTO> adminDTOs, List<ManagerDealerDTO> managerDTOs, List<SellerDealerDTO> sellerDTOs, List<MechanicDealerDTO> mechanicDTOs, List<BuyerDTO> buyerDTOs) {
        try {
            if (autoDealerDAO.existsByNameOrLocation(name, location)) {
                return new ResponseEntity<>("AutoDealer with the same name or location already exists.", HttpStatus.BAD_REQUEST);
            }

            AutoDealer autoDealer = new AutoDealer();
            autoDealer.setName(name);
            autoDealer.setLocation(location);
            autoDealer.setCreatedAt(LocalDateTime.now().withNano(0));

            AutoDealer savedAutoDealer = autoDealerDAO.save(autoDealer);

            int autoDealerId = savedAutoDealer.getId();

            if (!adminDTOs.isEmpty()) {
                List<Administrator> adminEntities = new ArrayList<>();
                for (AdministratorDealerDTO adminDTO : adminDTOs) {
                    Administrator adminEntity = new Administrator();
                    User user = new User(adminDTO.getUsername(), passwordEncoder.encode(adminDTO.getPassword()), List.of(Role.DEALER_ADMIN),true);
                    adminEntity.setUser(user);
                    adminEntity.setCreatedAt(LocalDateTime.now().withNano(0));
                    adminEntity.setAutoDealer_id(autoDealerId);
                    adminEntities.add(adminEntity);
                }
                autoDealer.setAdmins(adminEntities);
            }

            if (!managerDTOs.isEmpty()) {
                List<Manager> managerEntities = new ArrayList<>();
                for (ManagerDealerDTO managerDTO : managerDTOs) {
                    Manager manager = new Manager();
                    manager.setFirstName(managerDTO.getUserFirstname());
                    manager.setLastName(managerDTO.getUserLastname());
                    manager.setPhone(managerDTO.getPhoneNumber());
                    manager.setCreatedAt(LocalDateTime.now().withNano(0));
                    manager.setAutoDealer_id(autoDealerId);
                    User managerUser = new User(managerDTO.getUsername(), passwordEncoder.encode(managerDTO.getPassword()),
                            List.of(Role.DEALER_MANAGER),true);
                    manager.setUser(managerUser);
                    managerEntities.add(manager);
                }
                autoDealer.setManagers(managerEntities);
            }

            if (!sellerDTOs.isEmpty()) {
                List<Seller> sellerEntities = new ArrayList<>();
                for (SellerDealerDTO sellerDTO : sellerDTOs) {
                    Seller seller = new Seller();
                    seller.setFirstName(sellerDTO.getUserFirstname());
                    seller.setLastName(sellerDTO.getUserLastname());
                    seller.setPhone(sellerDTO.getPhoneNumber());
                    seller.setStatus(Status.ACTIVE);
                    seller.setCreatedAt(LocalDateTime.now().withNano(0));
                    seller.setAutoDealer_id(autoDealerId);
                    User sellerUser = new User(sellerDTO.getUsername(), passwordEncoder.encode(sellerDTO.getPassword()),
                            List.of(Role.DEALER_SELLER),true);
                    seller.setUser(sellerUser);
                    sellerEntities.add(seller);
                }
                autoDealer.setSales(sellerEntities);
            }

            if (!mechanicDTOs.isEmpty()) {
                List<Mechanic> mechanicEntities = new ArrayList<>();
                for (MechanicDealerDTO mechanicDTO : mechanicDTOs) {
                    Mechanic mechanic = new Mechanic();
                    mechanic.setCreatedAt(LocalDateTime.now().withNano(0));
                    mechanic.setAutoDealer_id(autoDealerId);
                    User mechanicUser = new User(mechanicDTO.getUsername(), passwordEncoder.encode(mechanicDTO.getPassword()),
                            List.of(Role.DEALER_MECHANIC),true);
                    mechanic.setUser(mechanicUser);
                    mechanicEntities.add(mechanic);
                }
                autoDealer.setMechanics(mechanicEntities);
            }

            if (!buyerDTOs.isEmpty()) {
                List<Buyer> buyerEntities = new ArrayList<>();
                for (BuyerDTO buyerDTO : buyerDTOs) {
                    Buyer buyer = new Buyer();
                    buyer.setCreatedAt(LocalDateTime.now().withNano(0));
                    buyer.setAutoDealer_id(autoDealerId);
                    User buyerUser = new User(buyerDTO.getUsername(), passwordEncoder.encode(buyerDTO.getPassword()),
                            List.of(Role.BUYER),true);
                    buyer.setUser(buyerUser);
                    buyerEntities.add(buyer);
                }
                autoDealer.setBuyers(buyerEntities);
            }

            autoDealerDAO.save(autoDealer);

            return new ResponseEntity<>("AutoDealer created successfully with ID: "+ autoDealerId + ".", HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("Error creating AutoDealer." + e.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<String> addAdminsToDealer(int dealerId, List<AdministratorDealerDTO> adminDTOs) {
        try {
            Optional<AutoDealer> optionalDealer = autoDealerDAO.findById(dealerId);
            if (optionalDealer.isEmpty()) {
                return new ResponseEntity<>("Dealer with this ID was not found.", HttpStatus.NOT_FOUND);
            }

            AutoDealer dealer = optionalDealer.get();
            List<Administrator> adminEntities = new ArrayList<>();
            for (AdministratorDealerDTO adminDTO : adminDTOs) {
                Administrator adminEntity = new Administrator();
                User user = new User(adminDTO.getUsername(), passwordEncoder.encode(adminDTO.getPassword()), List.of(Role.DEALER_ADMIN),true);
                adminEntity.setUser(user);
                adminEntity.setCreatedAt(LocalDateTime.now().withNano(0));
                adminEntity.setAutoDealer_id(dealerId);
                adminEntities.add(adminEntity);
            }

            dealer.getAdmins().addAll(adminEntities);
            autoDealerDAO.save(dealer);

            return new ResponseEntity<>("The admins have been successfully added to the dealer.", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error adding admins."+ e.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @Override
    public ResponseEntity<String> addAdminToDealer(int dealerId, AdministratorDealerDTO adminDTO) {
        try {
            Optional<AutoDealer> optionalDealer = autoDealerDAO.findById(dealerId);
            if (((Optional<?>) optionalDealer).isEmpty()) {
                return new ResponseEntity<>("Dealer with this ID was not found.", HttpStatus.NOT_FOUND);
            }

            AutoDealer dealer = optionalDealer.get();

            Administrator adminEntity = new Administrator();
            User user = new User(adminDTO.getUsername(), passwordEncoder.encode(adminDTO.getPassword()), List.of(Role.DEALER_ADMIN),true);
            adminEntity.setUser(user);
            adminEntity.setCreatedAt(LocalDateTime.now().withNano(0));
            adminEntity.setAutoDealer_id(dealerId);
            dealer.getAdmins().add(adminEntity);

            AutoDealer dealerSaved = autoDealerDAO.save(dealer);
            dealerSaved.getAdmins().size();
            Administrator adminEntitySaved = dealer.getAdmins().get(dealer.getAdmins().size() - 1);

            return new ResponseEntity<>("The administrator has been successfully added to the dealer with ID: " + adminEntitySaved.getId(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error adding administrator."+ e.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<String> addManagersToDealer(int dealerId, List<ManagerDealerDTO> managerDTOs) {
        try {
            Optional<AutoDealer> optionalDealer = autoDealerDAO.findById(dealerId);
            if (optionalDealer.isEmpty()) {
                return new ResponseEntity<>("Dealer with this ID was not found.", HttpStatus.NOT_FOUND);
            }

            AutoDealer dealer = optionalDealer.get();
            List<Manager> managerEntities = new ArrayList<>();

            for (ManagerDealerDTO managerDTO : managerDTOs) {
                Manager manager = new Manager();
                manager.setFirstName(managerDTO.getUserFirstname());
                manager.setLastName(managerDTO.getUserLastname());
                manager.setPhone(managerDTO.getPhoneNumber());
                manager.setCreatedAt(LocalDateTime.now().withNano(0));
                manager.setAutoDealer_id(dealerId);
                User managerUser = new User(managerDTO.getUsername(), passwordEncoder.encode(managerDTO.getPassword()),
                        List.of(Role.DEALER_MANAGER),true);
                manager.setUser(managerUser);
                managerEntities.add(manager);
            }

            dealer.getManagers().addAll(managerEntities);

            autoDealerDAO.save(dealer);

            return new ResponseEntity<>("The managers have been successfully added to the dealer.", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error adding managers."+ e.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<String> addManagerToDealer(int dealerId, ManagerDealerDTO managerDTO) {
        try {
            Optional<AutoDealer> optionalDealer = autoDealerDAO.findById(dealerId);
            if (((Optional<?>) optionalDealer).isEmpty()) {
                return new ResponseEntity<>("Dealer with this ID was not found.", HttpStatus.NOT_FOUND);
            }

            AutoDealer dealer = optionalDealer.get();

            Manager manager = new Manager();
            manager.setFirstName(managerDTO.getUserFirstname());
            manager.setLastName(managerDTO.getUserLastname());
            manager.setPhone(managerDTO.getPhoneNumber());
            manager.setCreatedAt(LocalDateTime.now().withNano(0));
            manager.setAutoDealer_id(dealerId);
            User managerUser = new User(managerDTO.getUsername(), passwordEncoder.encode(managerDTO.getPassword()),
                    List.of(Role.DEALER_MANAGER),true);
            manager.setUser(managerUser);
            dealer.getManagers().add(manager);

            AutoDealer dealerSaved = autoDealerDAO.save(dealer);
            dealerSaved.getManagers().size();
            Manager managerSaved = dealer.getManagers().get(dealer.getAdmins().size() - 1);

            return new ResponseEntity<>("The manager has been successfully added to the dealer with ID: " + managerSaved.getId(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error adding manager."+ e.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<String> addSalesToDealer(int dealerId, List<SellerDealerDTO> salesDTOs) {
        try {
            Optional<AutoDealer> dealerOptional = autoDealerDAO.findById(dealerId);
            if (dealerOptional.isEmpty()) {
                return new ResponseEntity<>("Dealer with the specified ID not found.", HttpStatus.NOT_FOUND);
            }

            AutoDealer dealer = dealerOptional.get();

            List<Seller> sellerEntities = new ArrayList<>();
            for (SellerDealerDTO sellerDTO : salesDTOs) {
                Seller seller = new Seller();
                seller.setFirstName(sellerDTO.getUserFirstname());
                seller.setLastName(sellerDTO.getUserLastname());
                seller.setPhone(sellerDTO.getPhoneNumber());
                seller.setStatus(Status.ACTIVE);
                seller.setCreatedAt(LocalDateTime.now().withNano(0));
                seller.setAutoDealer_id(dealerId);
                User sellerUser = new User(sellerDTO.getUsername(), passwordEncoder.encode(sellerDTO.getPassword()),
                        List.of(Role.DEALER_SELLER),true);
                seller.setUser(sellerUser);
                sellerEntities.add(seller);
            }

            dealer.getSales().addAll(sellerEntities);
            autoDealerDAO.save(dealer);

            return new ResponseEntity<>("Sellers successfully added to the dealer.", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error adding sellers to the dealer."+ e.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<String> addMechanicsToDealer(int dealerId, List<MechanicDealerDTO> mechanicDTOs) {
        try {
            Optional<AutoDealer> dealerOptional = autoDealerDAO.findById(dealerId);
            if (dealerOptional.isEmpty()) {
                return new ResponseEntity<>("Dealer with the specified ID not found.", HttpStatus.NOT_FOUND);
            }

            AutoDealer dealer = dealerOptional.get();

            List<Mechanic> mechanicsEntities = new ArrayList<>();
              for (MechanicDealerDTO mechanicDTO : mechanicDTOs) {
                    Mechanic mechanic = new Mechanic();
                    mechanic.setCreatedAt(LocalDateTime.now().withNano(0));
                    mechanic.setAutoDealer_id(dealerId);
                    User mechanicUser = new User(mechanicDTO.getUsername(), passwordEncoder.encode(mechanicDTO.getPassword()),
                            List.of(Role.DEALER_MECHANIC),true);
                mechanic.setUser(mechanicUser);
                mechanicsEntities.add(mechanic);
            }

            dealer.getMechanics().addAll(mechanicsEntities);
            autoDealerDAO.save(dealer);

            return new ResponseEntity<>("Mechanics added to the dealer successfully.", HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("Error adding mechanics to the dealer."+ e.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<String> addSellerToDealer(int dealerId, SellerDealerDTO sellerDTO) {
        try {
            Optional<AutoDealer> dealerOptional = autoDealerDAO.findById(dealerId);
            if (dealerOptional.isEmpty()) {
                return new ResponseEntity<>("Dealer with the specified ID not found.", HttpStatus.NOT_FOUND);
            }

            AutoDealer dealer = dealerOptional.get();

            Seller seller = new Seller();
            seller.setFirstName(sellerDTO.getUserFirstname());
            seller.setLastName(sellerDTO.getUserLastname());
            seller.setPhone(sellerDTO.getPhoneNumber());
            seller.setStatus(Status.ACTIVE);
            seller.setCreatedAt(LocalDateTime.now().withNano(0));
            seller.setAutoDealer_id(dealerId);
            User sellerUser = new User(sellerDTO.getUsername(), passwordEncoder.encode(sellerDTO.getPassword()),
                    List.of(Role.DEALER_SELLER),true);
            seller.setUser(sellerUser);
            dealer.getSales().add(seller);

            AutoDealer dealerSaved = autoDealerDAO.save(dealer);
            dealerSaved.getSales().size();
           Seller  sellerSaved = dealer.getSales().get(dealer.getAdmins().size() - 1);

            return new ResponseEntity<>("Seller successfully added to the dealer with ID: " + sellerSaved.getId(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error adding seller to the dealer."+ e.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @Override
    public ResponseEntity<String> addMechanicToDealer(int dealerId, MechanicDealerDTO mechanicDTO) {
        try {
            Optional<AutoDealer> dealerOptional = autoDealerDAO.findById(dealerId);
            if (dealerOptional.isEmpty()) {
                return new ResponseEntity<>("Dealer with the specified ID not found.", HttpStatus.NOT_FOUND);
            }

            AutoDealer dealer = dealerOptional.get();

            Mechanic mechanic = new Mechanic();
            mechanic.setCreatedAt(LocalDateTime.now().withNano(0));
            mechanic.setAutoDealer_id(dealerId);
            User mechanicUser = new User(mechanicDTO.getUsername(),passwordEncoder.encode( mechanicDTO.getPassword()),
                    List.of(Role.DEALER_MECHANIC),true);
            mechanic.setUser(mechanicUser);

            dealer.getMechanics().add(mechanic);
            AutoDealer dealerSaved = autoDealerDAO.save(dealer);
            dealerSaved.getSales().size();
           Mechanic  mechanicSaved = dealer.getMechanics().get(dealer.getAdmins().size() - 1);

            return new ResponseEntity<>("Mechanic added to the dealer successfully with ID: " + mechanicSaved.getId(), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("Error adding mechanic to the dealer" + e.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<String> addBuyerToDealer(int dealerId, BuyerDTO buyerDTO) {
        try {
            Optional<AutoDealer> dealerOptional = autoDealerDAO.findById(dealerId);
            if (dealerOptional.isEmpty()) {
                return new ResponseEntity<>("Dealer with the specified ID not found.", HttpStatus.NOT_FOUND);
            }

            AutoDealer dealer = dealerOptional.get();

            Buyer buyer = new Buyer();
            buyer.setCreatedAt(LocalDateTime.now().withNano(0));
            buyer.setAutoDealer_id(dealerId);
            User buyerUser = new User(buyerDTO.getUsername(), passwordEncoder.encode(buyerDTO.getPassword()),
                    List.of(Role.BUYER),true);
            buyer.setUser(buyerUser);
            dealer.getBuyers().add(buyer);

            AutoDealer dealerSaved = autoDealerDAO.save(dealer);

            Buyer buyerSaved = dealerSaved.getBuyers().get(dealerSaved.getBuyers().size() - 1);

            return new ResponseEntity<>("Buyer added to the dealer successfully with ID: " + buyerSaved.getId(), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("Error adding buyer to the dealer."+ e.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<String> addBuyersToDealer(int dealerId, List<BuyerDTO> buyerDTOs) {
        try {
            Optional<AutoDealer> dealerOptional = autoDealerDAO.findById(dealerId);
            if (dealerOptional.isEmpty()) {
                return new ResponseEntity<>("Dealer with the specified ID not found.", HttpStatus.NOT_FOUND);
            }

            AutoDealer dealer = dealerOptional.get();
            List<Buyer> buyersEntities = new ArrayList<>();

            for (BuyerDTO buyerDTO : buyerDTOs) {
                Buyer buyerEntity = new Buyer();
                buyerEntity.setCreatedAt(LocalDateTime.now().withNano(0));
                buyerEntity.setAutoDealer_id(dealerId);
                User  buyerUser = new User( buyerDTO.getUsername(),  passwordEncoder.encode(buyerDTO.getPassword()),
                        List.of(Role.DEALER_MECHANIC),true);
                buyerEntity.setUser(buyerUser);
                buyersEntities.add(buyerEntity);
            }

            dealer.getBuyers().addAll(buyersEntities);
            autoDealerDAO.save(dealer);

            return new ResponseEntity<>("Buyers added to the dealer successfully.", HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("Error adding buyers to the dealer."+ e.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<List<AutoDealer>> getAllAutoDealers() {
        try {
            List<AutoDealer> autoDealers = autoDealerDAO.findAll();
            return new ResponseEntity<>(autoDealers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



    @Override
    public ResponseEntity<List<Administrator>> getAllAdminsByDealerId(int dealerId) {
        try {
            Optional<AutoDealer> optionalDealer = autoDealerDAO.findById(dealerId);
            if (optionalDealer.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            AutoDealer dealer = optionalDealer.get();
            return new ResponseEntity<>(dealer.getAdmins(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<List<Manager>> getAllManagersByDealerId(int dealerId) {

        try {
            Optional<AutoDealer> optionalDealer = autoDealerDAO.findById(dealerId);
            if (optionalDealer.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            AutoDealer dealer = optionalDealer.get();
            return new ResponseEntity<>(dealer.getManagers(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<List<Seller>> getAllSalesByDealerId(int dealerId) {
        try {
            Optional<AutoDealer> optionalDealer = autoDealerDAO.findById(dealerId);
            if (optionalDealer.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            AutoDealer dealer = optionalDealer.get();

            return new ResponseEntity<>(dealer.getSales(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<List<Mechanic>> getAllMechanicsByDealerId(int dealerId) {
        try {
            Optional<AutoDealer> optionalDealer = autoDealerDAO.findById(dealerId);
            if (optionalDealer.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            AutoDealer dealer = optionalDealer.get();

            return new ResponseEntity<>(dealer.getMechanics(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<List<Buyer>> getAllBuyersByDealerId(int dealerId) {
        try {
            Optional<AutoDealer> optionalDealer = autoDealerDAO.findById(dealerId);
            if (optionalDealer.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            AutoDealer dealer = optionalDealer.get();
            List<Buyer> buyers = dealer.getBuyers();

            return new ResponseEntity<>(buyers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
