package com.serviceImplementation.Wallet.Repository;

import com.serviceImplementation.Wallet.model.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface WalletRepo extends JpaRepository<Wallet, Long> {

}
