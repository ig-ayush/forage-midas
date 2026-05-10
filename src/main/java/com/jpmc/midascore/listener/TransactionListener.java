package com.jpmc.midascore.listener;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.jpmc.midascore.entity.TransactionRecord;
import com.jpmc.midascore.entity.UserRecord;
import com.jpmc.midascore.foundation.Incentive;
import com.jpmc.midascore.foundation.Transaction;
import com.jpmc.midascore.repository.TransactionRepository;
import com.jpmc.midascore.repository.UserRepository;
import com.jpmc.midascore.service.IncentiveService;

@Component
public class TransactionListener {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private IncentiveService incentiveService;

    @KafkaListener(
            topics = "${general.kafka-topic}",
            groupId = "midas-group"
    )
    public void listen(Transaction transaction) {

        System.out.println("========== KAFKA MESSAGE RECEIVED ==========");
        System.out.println("Amount: " + transaction.getAmount());

        UserRecord sender =
                userRepository.findById(transaction.getSenderId());

        UserRecord recipient =
                userRepository.findById(transaction.getRecipientId());

        // validation
        if (sender == null || recipient == null) {
            return;
        }

        if (sender.getBalance() < transaction.getAmount()) {
            return;
        }

        // call incentive API
        Incentive incentive =
                incentiveService.getIncentive(transaction);

        BigDecimal incentiveAmount =
                incentive.getAmount();

        // update balances
        sender.setBalance(
                sender.getBalance() - transaction.getAmount()
        );

        recipient.setBalance(
                (float) (
                        recipient.getBalance()
                        + transaction.getAmount()
                        + incentiveAmount.floatValue()
                )
        );

        // save updated users
        userRepository.save(sender);
        userRepository.save(recipient);

        // save transaction record
        TransactionRecord record =
                new TransactionRecord(
                        sender.getId(),
                        recipient.getId(),
                        transaction.getAmount(),
                        incentiveAmount
                );

        transactionRepository.save(record);

        System.out.println("Transaction processed successfully");
    }
}