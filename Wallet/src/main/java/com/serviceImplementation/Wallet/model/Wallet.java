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

    private String username;

    @Size(min = 10, max = 10,message = "Mobile number must be exactly 10 characters")
    private String mobileNumber;

    private double balance;

}
