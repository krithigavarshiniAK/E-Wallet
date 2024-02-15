package com.serviceImplementation.Wallet.Service.Imple;

import com.serviceImplementation.Wallet.CustomException.IllegalArgumentException;
import com.serviceImplementation.Wallet.CustomException.*;
import com.serviceImplementation.Wallet.Repository.TransactionRepo;
import com.serviceImplementation.Wallet.Repository.WalletRepo;
import com.serviceImplementation.Wallet.Service.WalletService;
import com.serviceImplementation.Wallet.model.Transaction;
import com.serviceImplementation.Wallet.model.Wallet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class WalletServiceImple implements WalletService {
    @Autowired
    WalletRepo walletrepo;

    @Autowired
    TransactionRepo transrepo;

    @Value("${fund.transfer.limit}")
    private double transferlimit;

    @Value("${top.Up.limit}")
    private double topuplimit;


    @Override
    public Wallet createWallet(Wallet newWallet) throws UserNotFoundException,UserAlreadyExistException {
        Optional<Wallet> existingMobileNumber = walletrepo.findByMobileNumber(newWallet.getMobileNumber());
        Optional<Wallet> existingUsername = walletrepo.findByUsername(newWallet.getUsername());

        if (existingMobileNumber.isPresent()) {
            throw new UserAlreadyExistException("Mobile Number already exists: " + newWallet.getMobileNumber());
        }

        if (existingUsername.isPresent()) {
            throw new UserAlreadyExistException("Username already exists: " + newWallet.getUsername());
        }

        newWallet.setBalance(0);
        Wallet savedWallet = walletrepo.save(newWallet);

        if (savedWallet != null) {
            return savedWallet;
        } else {
            throw new UserNotFoundException("OOPs!,Wallet Creation UnSuccessful.");
        }
    }


    @Override
    public List<Wallet> getAllWallets() throws WalletNotFoundException {

        List<Wallet> WalletList = new ArrayList<>();
        walletrepo.findAll().forEach(WalletList::add);

        if (WalletList.isEmpty()) {
            throw new WalletNotFoundException("No Wallets Found!");
        } else {
            return WalletList;

        }

    }


    @Override
    public Wallet topup(long walletId, Wallet walletRequest) throws IllegalArgumentException,TopUpLimitExceededException,WalletNotFoundException {

            Optional<Wallet> optionalWallet = walletrepo.findById(walletId);

            if (optionalWallet.isPresent()) {
                Wallet wallet = optionalWallet.get();
                double topUpAmount = walletRequest.getBalance();

                if (topUpAmount < 0) {
                    throw new IllegalArgumentException("Top-up amount cannot be negative");
                }


                if (topUpAmount > topuplimit) {
                    throw new TopUpLimitExceededException("Top-up amount exceeds limit");
                }

                wallet.setBalance(wallet.getBalance() + topUpAmount);

                walletrepo.save(wallet);

                return wallet;
            } else {
                throw new WalletNotFoundException("Wallet No Present"+walletId);
            }

    }


    @Override
    public Double checkBalance(long walletId) throws WalletNotFoundException{

            Optional<Wallet> optionalWallet = walletrepo.findById(walletId);

            if (optionalWallet.isPresent()) {
                Wallet wallet = optionalWallet.get();
                double balance = wallet.getBalance();
                return balance;
            } else {
                throw new WalletNotFoundException("Wallet not found with ID: " + walletId);
            }
    }

    @Override
    public String deleteWalletById(long walletId) throws WalletNotFoundException{
        Optional<Wallet> optionalWallet = walletrepo.findById(walletId);

        if (optionalWallet.isPresent()) {
            Wallet wallet = optionalWallet.get();
            if (wallet.getBalance() == 0) {
                walletrepo.delete(wallet);
                return "Wallet Deleted Successfully!";
            } else {
                return "Balance Is not Zero";
            }
        } else {
            throw new WalletNotFoundException("Wallet Not Found");
        }
    }


    @Override
    public List<Wallet> fundTransfer(long source, long target, Wallet transferAmount) throws InsufficientBalanceException, WalletNotFoundException{
            Optional<Wallet> optionalSource = walletrepo.findById(source);
            Optional<Wallet> optionalTarget = walletrepo.findById(target);
            if (optionalSource.isPresent() && optionalTarget.isPresent()) {
                Wallet sourceWallet = optionalSource.get();
                Wallet targetWallet = optionalTarget.get();
                double sourceBalance = sourceWallet.getBalance();
                double transferBalance = transferAmount.getBalance();

                if (sourceBalance >= transferBalance && transferBalance <= transferlimit) {

                    saveTransactions(sourceWallet, targetWallet, transferBalance);

                    sourceWallet.setBalance(sourceWallet.getBalance() - transferBalance);
                    targetWallet.setBalance(targetWallet.getBalance() + transferBalance);

                    // Save updated wallets
                    sourceWallet = walletrepo.save(sourceWallet);
                    targetWallet = walletrepo.save(targetWallet);

                    return Arrays.asList(sourceWallet, targetWallet);
                } else {
                    throw new InsufficientBalanceException("Insufficient balance or transfer amount exceeds the limit.");
                }
            } else {
                throw new WalletNotFoundException("Desired Wallet Not Found");
            }
        }

    @Override
    public void saveTransactions(Wallet sourceWallet, Wallet targetWallet, double transferBalance) {

            Transaction sourceTransaction = new Transaction();
            sourceTransaction.setWallet(sourceWallet);
            sourceTransaction.setAmount(-transferBalance);
            sourceTransaction.setTimestamp(new Date());
            transrepo.save(sourceTransaction);

            Transaction targetTransaction = new Transaction();
            targetTransaction.setWallet(targetWallet);
            targetTransaction.setAmount(transferBalance);
            targetTransaction.setTimestamp(new Date());
            transrepo.save(targetTransaction);
        }

    @Override
    public List<Transaction> getAllTransactionss()throws TransactionNotFoundException{

            List<Transaction> transactionList = new ArrayList<>();
            transrepo.findAll().forEach(transactionList::add);

            if (transactionList.isEmpty()) {
                throw new TransactionNotFoundException("No transactions found");
            }

            return transactionList;
        }
    }





