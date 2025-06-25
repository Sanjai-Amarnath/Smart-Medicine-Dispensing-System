package org.prescription;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan(basePackages = "org.prescription.model")
public class PrescriptionApplication {
    public static void main(String[] args) {
        SpringApplication.run(PrescriptionApplication.class, args);
    }
}