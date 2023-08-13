package com.autoemporium.autoemporium.controllers;

import com.autoemporium.autoemporium.models.*;
import com.autoemporium.autoemporium.models.users.*;
import com.autoemporium.autoemporium.services.AutoDealerService;
import com.autoemporium.autoemporium.views.Views;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/autodealers")
public class AutoDealerController {
    private final AutoDealerService autoDealerService;

    public AutoDealerController(@Qualifier("autoDealerServiceImpl1") AutoDealerService autoDealerService) {
        this.autoDealerService = autoDealerService;
    }

    @PostMapping("/save")
    public ResponseEntity<String> createAutoDealer(@RequestBody AutoDealerRequestDTO requestDTO) {
       return autoDealerService.createAutoDealer(requestDTO.getName(), requestDTO.getLocation(),
                requestDTO.getAdmins(), requestDTO.getManagers(), requestDTO.getSellers(), requestDTO.getMechanics(), requestDTO.getBuyers());
    }

    @PostMapping("/admins/{dealerId}/save")
    public ResponseEntity<String> addAdminsToDealer(@PathVariable int dealerId, @RequestBody List<AdministratorDealerDTO> adminDTOs) {
        return autoDealerService.addAdminsToDealer(dealerId, adminDTOs);
    }

    @PostMapping("/admin/{dealerId}/save")
    public ResponseEntity<String> addAdminToDealer(@PathVariable int dealerId, @RequestBody AdministratorDealerDTO adminDTO) {
        return autoDealerService.addAdminToDealer(dealerId, adminDTO);
    }

    @PostMapping("/managers/{dealerId}/save")
    public ResponseEntity<String> addManagersToDealer(@PathVariable int dealerId, @RequestBody List<ManagerDealerDTO> managerDTOs){
        return autoDealerService.addManagersToDealer(dealerId, managerDTOs);
    }

    @PostMapping("/manager/{dealerId}/save")
    public ResponseEntity<String> addManagerToDealer(@PathVariable int dealerId, @RequestBody ManagerDealerDTO managerDTO) {
        return autoDealerService.addManagerToDealer(dealerId, managerDTO);
    }

    @PostMapping("/sales/{dealerId}/save")
    public ResponseEntity<String> addSalesToDealer(@PathVariable int dealerId, @RequestBody List<SellerDealerDTO> salesDTOs){
        return autoDealerService.addSalesToDealer(dealerId, salesDTOs);
    }

    @PostMapping("/seller/{dealerId}/save")
    public ResponseEntity<String> addSellerToDealer(@PathVariable int dealerId, @RequestBody SellerDealerDTO sellerDTO) {
        return autoDealerService.addSellerToDealer(dealerId,sellerDTO);
    }

    @PostMapping("/mechanics/{dealerId}/save")
    public ResponseEntity<String> addMechanicsToDealer(@PathVariable int dealerId, @RequestBody List<MechanicDealerDTO> mechanicDTOs){
        return autoDealerService.addMechanicsToDealer(dealerId, mechanicDTOs);
    }

    @PostMapping("/mechanic/{dealerId}/save")
    public ResponseEntity<String> addMechanicToDealer(@PathVariable int dealerId, @RequestBody MechanicDealerDTO mechanicDTO) {
        return autoDealerService.addMechanicToDealer(dealerId,mechanicDTO);
    }

    @PostMapping("/buyers/{dealerId}/save")
    public ResponseEntity<String> addBuyersToDealer(@PathVariable int dealerId, @RequestBody List<BuyerDTO> buyerDTOs){
        return autoDealerService.addBuyersToDealer(dealerId, buyerDTOs);
    }

    @PostMapping("/buyer/{dealerId}/save")
    public ResponseEntity<String> addBuyerToDealer(@PathVariable int dealerId, @RequestBody BuyerDTO buyerDTO) {
        return autoDealerService.addBuyerToDealer(dealerId,buyerDTO);
    }

    @GetMapping("/all")
    @JsonView(value = Views.Level2.class)
    public ResponseEntity<List<AutoDealer>>getAllAutoDealers() {
        return autoDealerService.getAllAutoDealers();
    }


    @JsonView(value = Views.Level1.class)
    @GetMapping("/admins/{dealerId}/all")
    public ResponseEntity<List<Administrator>>getAllAdminsByDealerId(@PathVariable int dealerId) {
        return autoDealerService.getAllAdminsByDealerId(dealerId);
    }

    @JsonView(value = Views.Level1.class)
    @GetMapping("/managers/{dealerId}/all")
    public ResponseEntity<List<Manager>>getAllManagersByDealerId(@PathVariable int dealerId) {
        return autoDealerService.getAllManagersByDealerId(dealerId);
    }

    @JsonView(value = Views.Level1.class)
    @GetMapping("/sales/{dealerId}/all")
    public ResponseEntity<List<Seller>>getAllSalesByDealerId(@PathVariable int dealerId) {
        return autoDealerService.getAllSalesByDealerId(dealerId);
    }

    @JsonView(value = Views.Level1.class)
    @GetMapping("/mechanics/{dealerId}/all")
    public ResponseEntity<List<Mechanic>>getAllMechanicsByDealerId(@PathVariable int dealerId) {
        return autoDealerService.getAllMechanicsByDealerId(dealerId);
    }

    @JsonView(value = Views.Level1.class)
    @GetMapping("/buyers/{dealerId}/all")
    public ResponseEntity<List<Buyer>>getAllBuyersByDealerId(@PathVariable int dealerId) {
        return autoDealerService.getAllBuyersByDealerId(dealerId);
    }

}
