package com.serviceImplementation.Wallet.Service;

import com.serviceImplementation.Wallet.model.Transaction;
import com.serviceImplementation.Wallet.model.Wallet;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface WalletService {

    ResponseEntity<Wallet> createWallet(Wallet newWallet);

    ResponseEntity<Wallet> addWallet(Wallet wallet);

    ResponseEntity<List<Wallet>> getAllWallets();

    ResponseEntity<Wallet> topup(long walletId, Wallet walletRequest);

    ResponseEntity<Double> checkBalance(long walletId);

    ResponseEntity<String> deleteWalletById(long walletId);

    ResponseEntity<String> deleteAllWallets();

    ResponseEntity<List<Wallet>> fundTransfer(long source, long target, Wallet transferAmount);

    ResponseEntity<List<Transaction>> getAllTransactionss();
}
