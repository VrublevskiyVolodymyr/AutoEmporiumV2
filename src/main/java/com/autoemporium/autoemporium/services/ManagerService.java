package com.autoemporium.autoemporium.services;

import com.autoemporium.autoemporium.dao.ModelDAO;
import com.autoemporium.autoemporium.dao.ProducerDAO;
import com.autoemporium.autoemporium.dao.RegionDAO;
import com.autoemporium.autoemporium.dao.SellerDAO;
import com.autoemporium.autoemporium.models.Region;
import com.autoemporium.autoemporium.models.users.Seller;
import com.autoemporium.autoemporium.models.Model;
import com.autoemporium.autoemporium.models.Producer;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


import java.security.Principal;
import java.util.List;
import java.util.Objects;

@Service
@AllArgsConstructor
public class ManagerService {

    private ProducerDAO producerDAO;
    private ModelDAO modelDAO;
    private RegionDAO regionDAO;
    private SellerDAO sellerDAO;
    MailService mailService;
    UserService userService;

    public ResponseEntity<String> notifyMissingProducer(String producer, Principal principal) {
        String username = principal.getName();
        Seller seller = sellerDAO.findSellerByUsername(username);
        Producer producer1 = producerDAO.findAll()
                .stream()
                .filter(producer2 -> Objects.equals(producer2.getProducer(), producer))
                .findFirst()
                .orElse(null);
        if (producer1 == null) {
            notifyManager("Missing brand: " + producer + ". Said the seller id= " + seller.getId());
            return ResponseEntity.ok("Thank you for notifying us about the missing producer.");
        } else {
            return ResponseEntity.badRequest().body("Producer already exists.");
        }
    }

    public ResponseEntity<String> notifyMissingModel( String model,  Integer producerId, Principal principal ) {
        Producer producer = producerDAO.findById(producerId).orElse(null);
        String username = principal.getName();
        Seller seller = sellerDAO.findSellerByUsername(username);
        if (producer == null) {
            return ResponseEntity.badRequest().body("Producer not found.");
        } else {
            List<Model> models = producer.getModels();
            List<String> singleModels = models.stream().map(Model::getModel).toList();
            if (!singleModels.contains(model)) {
               notifyManager("Missing model: " + model + ". Said the seller id= " + seller.getId());
                return ResponseEntity.ok("Thank you for notifying us about the missing model.");
            } else {
                return ResponseEntity.badRequest().body("Model already exists.");
            }
        }
    }

    public ResponseEntity<String> saveProducers(List<Producer> producers) {
        List<String> singleProducersDAO = producerDAO.findAll().stream().map(Producer::getProducer).toList();
        List<String> singleProducer = producers.stream().map(Producer::getProducer).toList();
        boolean containsElement = CollectionUtils.containsAny(singleProducer, singleProducersDAO);

        if (containsElement) {
            return new ResponseEntity<>("One of producers already exists", HttpStatus.FORBIDDEN);
        } else {
            producerDAO.saveAll(producers);
            return new ResponseEntity<>("Producers is saved", HttpStatus.OK);
        }
    }

    public ResponseEntity<String> saveProducer(String producer) {
        List<String> singleProducersDAO = producerDAO.findAll().stream().map(Producer::getProducer).toList();
        Producer producer1 = new Producer(producer);

        if (singleProducersDAO.contains(producer)) {
            return new ResponseEntity<>("Producer already exists", HttpStatus.FORBIDDEN);
        } else {
            producerDAO.save(producer1);
            return new ResponseEntity<>("Producer is saved", HttpStatus.OK);
        }
    }

    public ResponseEntity<String> saveModels(List<Model> models, Integer producerId) {
        Producer producer = producerDAO.findById(producerId).orElseThrow(() -> new IllegalArgumentException("Producer not found"));
        List<String> singleModels = models.stream().map(Model::getModel).toList();
        List<String> singleModelsDAO = producer.getModels().stream().map(Model::getModel).toList();
        boolean containsElement = CollectionUtils.containsAny(singleModels, singleModelsDAO);

        if (containsElement) {
            return new ResponseEntity<>("One of models already exists", HttpStatus.FORBIDDEN);
        } else {
            producer.setModels(models);
            producerDAO.save(producer);
            return new ResponseEntity<>("models is saved", HttpStatus.OK);
        }
    }

    public ResponseEntity<String> saveModel(Model model, Integer producerId) {
        Producer producer = producerDAO.findById(producerId).orElseThrow(() -> new IllegalArgumentException("Producer not found"));
        List<Model> models = producer.getModels();
        List<String> singleModels = models.stream().map(Model::getModel).toList();
        String singleModel = model.getModel();

        if (singleModels.contains(singleModel)) {
            return new ResponseEntity<>("This model already exists", HttpStatus.FORBIDDEN);
        } else {
            model.setProducer(producer);
            modelDAO.save(model);
            return new ResponseEntity<>("model is saved", HttpStatus.OK);
        }
    }
    public void notifyManager(String message) {
        String managerEmail = userService.getManagerEmail();
        String subject = "Notification from Autoemporium: CarSalePlatformService";
        String body = "Notification: " + message;

        mailService.sendEmail(managerEmail, subject, body);
    }

    public ResponseEntity<String> saveRegion(String region) {
        List<String> singleRegionsDAO = regionDAO.findAll().stream().map(Region::getRegion).toList();
        Region region1 = new Region(region);

        if (singleRegionsDAO.contains(region)) {
            return new ResponseEntity<>("Region already exists", HttpStatus.FORBIDDEN);
        } else {
            regionDAO.save(region1 );
            return new ResponseEntity<>("Region is saved", HttpStatus.OK);
        }
    }

    public ResponseEntity<String> saveRegions(List<Region> regions) {
        List<String> singleRegionsDAO  = regionDAO.findAll().stream().map(Region::getRegion).toList();
        List<String> singleRegion = regions.stream().map(Region::getRegion).toList();
        boolean containsElement = CollectionUtils.containsAny(singleRegion, singleRegionsDAO);

        if (containsElement) {
            return new ResponseEntity<>("One of regions already exists", HttpStatus.FORBIDDEN);
        } else {
            regionDAO.saveAll(regions);
            return new ResponseEntity<>("Regions is saved", HttpStatus.OK);
        }
    }
}
