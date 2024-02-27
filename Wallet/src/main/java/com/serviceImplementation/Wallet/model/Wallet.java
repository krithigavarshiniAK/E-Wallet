package com.serviceImplementation.Wallet.model;

import jakarta.persistence.*;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import javax.xml.transform.Source;

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

    @Column(name = "MobileNumber")
    private String mobileNumber;

    @Column(name = "Balance")
    private double balance;

}
