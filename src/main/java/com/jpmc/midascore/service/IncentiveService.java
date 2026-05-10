package com.jpmc.midascore.service;

import com.jpmc.midascore.foundation.Incentive;
import com.jpmc.midascore.foundation.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class IncentiveService {

    @Autowired
    private RestTemplate restTemplate;

    private final String URL =
            "http://localhost:8080/incentive";

    public Incentive getIncentive(Transaction transaction) {

        return restTemplate.postForObject(
                URL,
                transaction,
                Incentive.class
        );
    }
}