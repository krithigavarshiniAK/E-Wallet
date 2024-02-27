package com.serviceImplementation.Wallet.Service;

import com.serviceImplementation.Wallet.CustomException.*;
import com.serviceImplementation.Wallet.CustomException.IllegalArgumentException;
//import com.serviceImplementation.Wallet.model.Transactions;
import com.serviceImplementation.Wallet.model.Transactions;
import com.serviceImplementation.Wallet.model.Wallet;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface WalletService {


    public Wallet createWallet(Wallet newWallet) throws UserNotFoundException;

    public List<Wallet> getAllWallets() throws WalletNotFoundException;

    public Wallet topup(long walletId, Wallet walletRequest) throws IllegalArgumentException, TopUpLimitExceededException, WalletNotFoundException;


    public Double checkBalance(long walletId) throws WalletNotFoundException;

    String deleteWalletById(long walletId) throws WalletNotFoundException;

    public List<Wallet> fundTransfer(long source, long target, Wallet transferAmount);

    public List<Transactions> getAllTransactions() throws TransactionNotFoundException;


    public List<Transactions> getTransactionByAmount(double amount) throws TransactionNotFoundException;
}

