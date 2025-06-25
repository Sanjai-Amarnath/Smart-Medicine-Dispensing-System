package org.prescription.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@RestController
@RequestMapping("/api/rfid")
public class RfidController {

    @PostMapping("/program")
    public ResponseEntity<?> programRfid(@RequestBody Map<String, String> request) {
        String prescriptionId = request.get("prescriptionId");

        RestTemplate restTemplate = new RestTemplate();
        String url = "http://192.168.48.113/program";

        try {
            restTemplate.postForObject(url, prescriptionId, String.class);
            return ResponseEntity.ok("RFID programmed via ESP");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to send to ESP");
        }
    }
}