package com.autoemporium.autoemporium.services;

import com.autoemporium.autoemporium.models.*;
import com.autoemporium.autoemporium.models.users.*;
import org.springframework.http.ResponseEntity;

import java.util.List;

public class AutoDealerServiceImpl2 implements AutoDealerService{
    @Override
    public ResponseEntity<String> createAutoDealer(String name, String location, List<AdministratorDealerDTO> admin, List<ManagerDealerDTO> managers, List<SellerDealerDTO> sellers, List<MechanicDealerDTO> mechanics, List<BuyerDTO> buyers) {
        return null;
    }

    @Override
    public ResponseEntity<String> addAdminToDealer(int dealerId, AdministratorDealerDTO adminDTO) {
        return null;
    }

    @Override
    public ResponseEntity<String> addManagersToDealer(int dealerId, List<ManagerDealerDTO> managerDTOs) {
        return null;
    }

    @Override
    public ResponseEntity<String> addManagerToDealer(int dealerId, ManagerDealerDTO managerDTO) {
        return null;
    }

    @Override
    public ResponseEntity<List<Manager>> getAllManagersByDealerId(int dealerId) {
        return null;
    }

    @Override
    public ResponseEntity<List<Administrator>> getAllAdminsByDealerId(int dealerId) {
        return null;
    }

    @Override
    public ResponseEntity<String> addAdminsToDealer(int dealerId, List<AdministratorDealerDTO> adminDTOs) {
        return null;
    }

    @Override
    public ResponseEntity<String> addSalesToDealer(int dealerId, List<SellerDealerDTO> salesDTOs) {
        return null;
    }

    @Override
    public ResponseEntity<String> addSellerToDealer(int dealerId, SellerDealerDTO sellerDTO) {
        return null;
    }

    @Override
    public ResponseEntity<String> addMechanicsToDealer(int dealerId, List<MechanicDealerDTO> mechanicDTOs) {
        return null;
    }

    @Override
    public ResponseEntity<String> addMechanicToDealer(int dealerId, MechanicDealerDTO mechanicDTO) {
        return null;
    }

    @Override
    public ResponseEntity<List<Seller>> getAllSalesByDealerId(int dealerId) {
        return null;
    }

    @Override
    public ResponseEntity<List<Mechanic>> getAllMechanicsByDealerId(int dealerId) {
        return null;
    }

    @Override
    public ResponseEntity<List<Buyer>> getAllBuyersByDealerId(int dealerId) {
        return null;
    }

    @Override
    public ResponseEntity<String> addBuyerToDealer(int dealerId, BuyerDTO buyerDTO) {
        return null;
    }

    @Override
    public ResponseEntity<String> addBuyersToDealer(int dealerId, List<BuyerDTO> buyerDTOs) {
        return null;
    }

    @Override
    public ResponseEntity<List<AutoDealer>> getAllAutoDealers() {
        return null;
    }

}
