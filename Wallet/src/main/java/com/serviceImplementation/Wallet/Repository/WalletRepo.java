package com.serviceImplementation.Wallet.Repository;

import com.serviceImplementation.Wallet.model.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;


public interface WalletRepo extends JpaRepository<Wallet, Long> {
    Wallet findByUsername(String username);
}
