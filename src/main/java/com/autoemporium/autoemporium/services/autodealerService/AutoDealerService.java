package com.autoemporium.autoemporium.services.autodealerService;


import com.autoemporium.autoemporium.models.autodealer.*;
import com.autoemporium.autoemporium.models.users.*;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface AutoDealerService {
   public ResponseEntity<String> createAutoDealer(String name, String location, List<AdministratorDealerDTO> admin, List<ManagerDealerDTO> managers, List<SellerDealerDTO> sellers, List<MechanicDealerDTO> mechanics, List<BuyerDTO> buyers);

    public ResponseEntity<AuthenticationResponse> addAdminToDealer(int dealerId, AdministratorDealerDTO adminDTO);

    public ResponseEntity<String> addManagersToDealer(int dealerId, List<ManagerDealerDTO> managerDTOs);

   public ResponseEntity<AuthenticationResponse> addManagerToDealer(int dealerId, ManagerDealerDTO managerDTO);

   public ResponseEntity<List<Manager>> getAllManagersByDealerId(int dealerId);

  public   ResponseEntity<List<Administrator>> getAllAdminsByDealerId(int dealerId);

   public ResponseEntity<String> addAdminsToDealer(int dealerId, List<AdministratorDealerDTO> adminDTOs);

   public ResponseEntity<String> addSalesToDealer(int dealerId, List<SellerDealerDTO> salesDTOs);

  public ResponseEntity<AuthenticationResponse> addSellerToDealer(int dealerId, SellerDealerDTO sellerDTO);

   public ResponseEntity<String> addMechanicsToDealer(int dealerId, List<MechanicDealerDTO> mechanicDTOs);

   public ResponseEntity<AuthenticationResponse> addMechanicToDealer(int dealerId, MechanicDealerDTO mechanicDTO);

   public ResponseEntity<List<Seller>> getAllSalesByDealerId(int dealerId);

   public ResponseEntity<List<Mechanic>> getAllMechanicsByDealerId(int dealerId);

   public ResponseEntity<List<Buyer>> getAllBuyersByDealerId(int dealerId);

   public ResponseEntity<AuthenticationResponse> addBuyerToDealer(int dealerId, BuyerDTO buyerDTO);

   public ResponseEntity<String> addBuyersToDealer(int dealerId, List<BuyerDTO> buyerDTOs);

   public ResponseEntity<List<AutoDealer>> getAllAutoDealers();

}
