package com.autoemporium.autoemporium.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.io.IOException;

@Configuration
@EnableScheduling
public class ScheduledTasks {
    @Autowired
    private CurrencyService currencyService;

    @Scheduled(fixedRate = 2000000)
    public void updateCurrencyRates() throws IOException {
        currencyService.updateCurrencyRates();
    }
}
