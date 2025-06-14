package com.ecomfsrwebflux.client_service.timer;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.springframework.stereotype.Service;

@Service
public class PerformanceService {

    private final Timer timer;

    public PerformanceService(MeterRegistry meterRegistry) {
        this.timer = meterRegistry.timer("custom.endpoint.timer");
    }

    public String process() {
        return timer.record(() -> {
            // Simule une opération qui prend du temps
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            return "Processed";
        });
    }
}