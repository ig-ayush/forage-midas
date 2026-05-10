package com.jpmc.midascore.entity;

import java.math.BigDecimal;

import jakarta.persistence.*;

@Entity
public class TransactionRecord {

    @Id
    @GeneratedValue
    private Long id;

    private Long senderId;

    private Long recipientId;

    private float amount;
    
    private BigDecimal incentive;

    public TransactionRecord() {
    }

    public TransactionRecord(Long senderId,
                             Long recipientId,
                             float amount,
                             BigDecimal incentive) {
        this.senderId = senderId;
        this.recipientId = recipientId;
        this.amount = amount;
        this.incentive = incentive;
    }

    public BigDecimal getIncentive() {
		return incentive;
	}

	public void setIncentive(BigDecimal incentive) {
		this.incentive = incentive;
	}

	public Long getId() {
        return id;
    }

    public Long getSenderId() {
        return senderId;
    }

    public Long getRecipientId() {
        return recipientId;
    }

    public float getAmount() {
        return amount;
    }
}