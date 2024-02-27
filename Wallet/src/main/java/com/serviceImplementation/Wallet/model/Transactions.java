package com.serviceImplementation.Wallet.model;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
@Entity
@Getter
@Setter
@Table(name = "transaction_s")
public class Transactions {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "wallet_id")
    private Wallet wallet;

    private double amount;

    private String TransactionType;

    @Temporal(TemporalType.TIMESTAMP)
    private Date timestamp;

}

