package com.serviceImplementation.Wallet.Service;

import com.serviceImplementation.Wallet.CustomException.*;
import com.serviceImplementation.Wallet.CustomException.IllegalArgumentException;
import com.serviceImplementation.Wallet.model.Transaction;
import com.serviceImplementation.Wallet.model.Wallet;

import java.util.List;

public interface WalletService {


    public Wallet createWallet(Wallet newWallet) throws UserNotFoundException;
    
    public List<Wallet> getAllWallets() throws WalletNotFoundException;

    public Wallet topup(long walletId, Wallet walletRequest) throws IllegalArgumentException, TopUpLimitExceededException,WalletNotFoundException ;


    public Double checkBalance(long walletId) throws WalletNotFoundException;

    String deleteWalletById(long walletId)throws WalletNotFoundException;

    public List<Wallet> fundTransfer(long source, long target, Wallet transferAmount);

    void saveTransactions(Wallet sourceWallet, Wallet targetWallet, double transferBalance);

    public List<Transaction> getAllTransactionss()throws TransactionNotFoundException;

}
