package com.serviceImplementation.Wallet.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name="Wallet_s")
@Setter
@Getter

public class Wallet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long WalletId;

    private String username;

    private String password;

    private String email;

    private double balance;

}
