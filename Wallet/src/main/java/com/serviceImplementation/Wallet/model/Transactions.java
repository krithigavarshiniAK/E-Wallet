package com.serviceImplementation.Wallet.model;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
@Entity
@Getter
@Setter
@Table(name = "transaction_summary")
public class Transactions {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public Transactions() {
        System.out.println("3");
    }

    @ManyToOne
    @JoinColumn(name = "wallet_id")
    private Wallet wallet;

    @Column
    private double amount;

    @Column
    private String TransactionType;

    @Column
    @Temporal(TemporalType.TIMESTAMP)
    private Date timestamp;

	public void setTimestamp(Date date) {
		this.timestamp = date;
	}

	public void setAmount(double transferBalance) {
		this.amount = transferBalance;
		
	}

	public void setTransactionType(String string) {
		this.TransactionType = string;
	}

	public void setWallet(Wallet targetWallet) {
		this.wallet = targetWallet;
	}

}

