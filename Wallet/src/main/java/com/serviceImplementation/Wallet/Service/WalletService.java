package com.serviceImplementation.Wallet.Service;

import com.serviceImplementation.Wallet.CustomException.*;
import com.serviceImplementation.Wallet.CustomException.IllegalArgumentException;
import com.serviceImplementation.Wallet.model.Transaction;
import com.serviceImplementation.Wallet.model.Wallet;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface WalletService {


    public Wallet createWallet(Wallet newWallet) throws ResourceNotFoundException;
    
    public List<Wallet> getAllWallets() throws WalletNotFoundException;

    public Wallet topup(long walletId, Wallet walletRequest) throws IllegalArgumentException, TopUpLimitExceededException,WalletNotFoundException ;


    public Double checkBalance(long walletId) throws WalletNotFoundException;

    String deleteWalletById(long walletId)throws WalletNotFoundException;

    public List<Wallet> fundTransfer(long source, long target, Wallet transferAmount);

    void saveTransactions(Wallet sourceWallet, Wallet targetWallet, double transferBalance);

<<<<<<< HEAD
    public List<Transaction> getAllTransactionss()throws TransactionNotFoundException;
=======
    ResponseEntity<List<Transaction>> getAllTransactionss();

    ResponseEntity<Wallet> addWallet(Wallet wallet);
>>>>>>> 44de8ed8c7a32d670ba95f59dba4e291d73e3a0b
}
