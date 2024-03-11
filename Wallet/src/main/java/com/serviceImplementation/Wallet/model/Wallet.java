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

    @Column(name = "Username")
    private String username;

    public Wallet() {
        System.out.println("2");
    }

    @Column(name = "MobileNumber")
    private String mobileNumber;

    @Column(name = "Balance")
    private double balance;

}
