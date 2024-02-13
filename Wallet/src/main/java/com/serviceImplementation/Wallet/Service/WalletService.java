package com.serviceImplementation.Wallet.Service;

import com.serviceImplementation.Wallet.model.Transaction;
import com.serviceImplementation.Wallet.model.Wallet;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface WalletService {


    ResponseEntity<Wallet> createWallet(Wallet newWallet);
    
    ResponseEntity<List<Wallet>> getAllWallets();

    ResponseEntity<String> topup(long walletId, Wallet walletRequest);


    ResponseEntity<Object> checkBalance(long walletId);

    ResponseEntity<String> deleteWalletById(long walletId);

    ResponseEntity<List<Wallet>> fundTransfer(long source, long target, Wallet transferAmount);

    void saveTransactions(Wallet sourceWallet, Wallet targetWallet, double transferBalance);

    ResponseEntity<List<Transaction>> getAllTransactionss();
}
