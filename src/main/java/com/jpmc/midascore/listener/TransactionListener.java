package com.jpmc.midascore.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.jpmc.midascore.entity.TransactionRecord;
import com.jpmc.midascore.entity.UserRecord;
import com.jpmc.midascore.foundation.Transaction;
import com.jpmc.midascore.repository.TransactionRepository;
import com.jpmc.midascore.repository.UserRepository;

@Component
public class TransactionListener {

	 	@Autowired
	    private UserRepository userRepository;

	    @Autowired
	    private TransactionRepository transactionRepository;
	
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

         // update balances
         sender.setBalance(
                 sender.getBalance() - transaction.getAmount()
         );

         recipient.setBalance(
                 recipient.getBalance() + transaction.getAmount()
         );

         // save updated users
         userRepository.save(sender);
         userRepository.save(recipient);

         // save transaction record
         TransactionRecord record =
                 new TransactionRecord(
                         sender.getId(),
                         recipient.getId(),
                         transaction.getAmount()
                 );

         transactionRepository.save(record);

         System.out.println("Transaction processed successfully");


    }
}