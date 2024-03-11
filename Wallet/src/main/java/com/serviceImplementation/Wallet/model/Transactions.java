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

}

