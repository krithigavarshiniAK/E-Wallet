package com.serviceImplementation.Wallet.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name="wallet_config")
@Setter
@Getter
public class Wallet {
    public Wallet()
    {
        System.out.println("7");
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long WalletId;


    @Column(name = "Username")
    private String username;


    @Column(name = "MobileNumber")
    private String mobileNumber;

    @Column(name = "Balance")
    private double balance;

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public double getBalance() {
        return balance;
    }

    public Object getMobileNumber() {
		return mobileNumber;
	}
}
