package com.pi.Centrale_Achat.controller;
import java.util.Map;

import com.pi.Centrale_Achat.entities.Product;
import com.pi.Centrale_Achat.serviceImpl.PythonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
public class PredictionController {

    @Autowired
    private PythonService pythonService;

    private ObjectMapper objectMapper = new ObjectMapper();

    @PostMapping("/predict/product")
    public ResponseEntity<Double> predictAcceptance(@RequestBody Product productData) {
        try {
            // Convert the product data to JSON string
            String jsonProductData = objectMapper.writeValueAsString(productData);

            // Call the Python service to predict the acceptance
            Double pred = pythonService.predictAcceptance(jsonProductData);

            // Return the predicted acceptance
            return ResponseEntity.ok(pred);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }
}
