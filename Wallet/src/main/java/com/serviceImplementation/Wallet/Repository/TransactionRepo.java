package com.serviceImplementation.Wallet.Repository;


import com.serviceImplementation.Wallet.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;


public interface TransactionRepo extends JpaRepository<Transaction, Long>{

}
