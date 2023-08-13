package com.autoemporium.autoemporium.services.financialServices;

import com.autoemporium.autoemporium.services.financialServices.CurrencyService;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.io.IOException;

@AllArgsConstructor
@Configuration
@EnableScheduling
public class ScheduledTasks {
    private CurrencyService currencyService;

    @Scheduled(cron = "0 0 0 * * ?")
    public void updateCurrencyRates() throws IOException {
        currencyService.updateCurrencyRates();
    }
    @PostConstruct
    public void init() throws IOException {
        updateCurrencyRates();
    }
}
