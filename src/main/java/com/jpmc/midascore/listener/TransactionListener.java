package com.jpmc.midascore.listener;

import org.springframework.stereotype.Component;

import com.jpmc.midascore.foundation.Transaction;

import org.springframework.kafka.annotation.KafkaListener;

@Component
public class TransactionListener {
	
	@KafkaListener(topics = "${general.kafka-topic}")
	public void listener(Transaction transaction) {
		System.out.println("Received: " + transaction.getAmount());
	}
}
