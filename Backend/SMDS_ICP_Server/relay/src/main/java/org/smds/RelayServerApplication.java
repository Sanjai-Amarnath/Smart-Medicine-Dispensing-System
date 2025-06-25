package org.smds;

import org.springframework.web.bind.annotation.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@RestController
public class RelayServerApplication {

    // Store relay state (default OFF)
    private volatile boolean relayOn = false;

    public static void main(String[] args) {
        SpringApplication.run(RelayServerApplication.class, args);
    }

    // Endpoint to set relay state (called by your Flutter app)
    @PostMapping("/relay")
    public String setRelayState(@RequestParam("state") String state) {
        if ("on".equalsIgnoreCase(state)) {
            relayOn = true;
            System.out.println("DEBUG: Relay turned ON");
            return "Relay turned ON";
        } else if ("off".equalsIgnoreCase(state)) {
            relayOn = false;
            System.out.println("DEBUG: Relay turned OFF");
            return "Relay turned OFF";
        } else {
            System.out.println("DEBUG: Invalid state received: " + state);
            return "Invalid state, use 'on' or 'off'";
        }
    }

    // Endpoint for ESP8266 to get relay state
    @GetMapping("/relay/state")
    public RelayState getRelayState() {
        return new RelayState(relayOn);
    }

    // Simple DTO for relay state JSON response
    public static class RelayState {
        private boolean on;

        public RelayState(boolean on) {
            this.on = on;
        }

        public boolean isOn() {
            return on;
        }

        public void setOn(boolean on) {
            this.on = on;
        }
    }
}