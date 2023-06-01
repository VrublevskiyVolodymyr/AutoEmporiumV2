package com.autoemporium.autoemporium.services;

import com.autoemporium.autoemporium.dao.ModelDAO;
import com.autoemporium.autoemporium.dao.ProducerDAO;
import com.autoemporium.autoemporium.models.Model;
import com.autoemporium.autoemporium.models.Producer;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class CarSalePlatformService {

    @Autowired
private ProducerDAO  producerDAO;
    private ModelDAO modelDAO;


    public void saveProducers(List<Producer> producers) {
        producerDAO.saveAll(producers);
    }

    public void saveProducer(Producer producer) {
        producerDAO.save(producer);
    }

    public ResponseEntity<List<Producer>> getAllProducers() {
        List<Producer> all = producerDAO.findAll();
        return new ResponseEntity<>(all, HttpStatus.OK);
    }

    public void saveModels(List<Model> models, Integer producerId) {
        Producer producer = producerDAO.findById(producerId).orElseThrow(() -> new IllegalArgumentException("Producer not found"));
        producer.setModel(models);
        producerDAO.save(producer);
    }

    public ResponseEntity<List<Model>> getAllModels(Integer producerId) {
        Producer producer = producerDAO.findById(producerId).orElseThrow(() -> new IllegalArgumentException("Producer not found"));
        List<Model> models = producer.getModel();
        return new ResponseEntity<>(models, HttpStatus.OK);
    }

    public void saveModel(Model model, Integer producerId) {
        Producer producer = producerDAO.findById(producerId).orElseThrow(() -> new IllegalArgumentException("Producer not found"));
        model.setProducer(producer);
        modelDAO.save(model);
    }
}
