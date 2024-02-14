package com.serviceImplementation.Wallet.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
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

    @Size(min = 10, max = 12,message = "{Wallet.contact.invalid}")
    private String mobileNumber;

    private double balance;

}
