package com.autoemporium.autoemporium.services;

import com.autoemporium.autoemporium.dao.ModelDAO;
import com.autoemporium.autoemporium.dao.ProducerDAO;
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

    @Autowired
    private ProducerDAO producerDAO;
    private ModelDAO modelDAO;
    MailService mailService;
    UserService clientService;

    public ResponseEntity<String> notifyMissingProducer(String producer, Principal principal) {
        String username = principal.getName();
        Seller seller = (Seller) clientService.loadUserByUsername(username);
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
        Seller seller = (Seller) clientService.loadUserByUsername(username);
        if (producer == null) {
            return ResponseEntity.badRequest().body("Producer not found.");
        } else {
            List<Model> models = producer.getModel();
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

    public ResponseEntity<List<Producer>> getAllProducers() {
        List<Producer> all = producerDAO.findAll();
        return new ResponseEntity<>(all, HttpStatus.OK);
    }

    public ResponseEntity<String> saveModels(List<Model> models, Integer producerId) {
        Producer producer = producerDAO.findById(producerId).orElseThrow(() -> new IllegalArgumentException("Producer not found"));
        List<String> singleModels = models.stream().map(Model::getModel).toList();
        List<String> singleModelsDAO = producer.getModel().stream().map(Model::getModel).toList();
        boolean containsElement = CollectionUtils.containsAny(singleModels, singleModelsDAO);

        if (containsElement) {
            return new ResponseEntity<>("One of models already exists", HttpStatus.FORBIDDEN);
        } else {
            producer.setModel(models);
            producerDAO.save(producer);
            return new ResponseEntity<>("models is saved", HttpStatus.OK);
        }
    }

    public ResponseEntity<List<Model>> getAllModels(Integer producerId) {
        Producer producer = producerDAO.findById(producerId).orElseThrow(() -> new IllegalArgumentException("Producer not found"));
        List<Model> models = producer.getModel();
        return new ResponseEntity<>(models, HttpStatus.OK);
    }

    public ResponseEntity<String> saveModel(Model model, Integer producerId) {
        Producer producer = producerDAO.findById(producerId).orElseThrow(() -> new IllegalArgumentException("Producer not found"));
        List<Model> models = producer.getModel();
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

        String managerEmail = clientService.getManagerEmail();
        String subject = "Notification from Autoemporium: CarSalePlatformService";
        String body = "Notification: " + message;

        mailService.sendEmail(managerEmail, subject, body);
    }
}
