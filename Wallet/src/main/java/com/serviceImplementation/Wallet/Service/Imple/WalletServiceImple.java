package com.serviceImplementation.Wallet.Service.Imple;

import com.serviceImplementation.Wallet.CustomException.*;
import com.serviceImplementation.Wallet.Repository.TransactionRepo;
import com.serviceImplementation.Wallet.Repository.WalletRepo;
import com.serviceImplementation.Wallet.Service.WalletService;
import com.serviceImplementation.Wallet.model.Transaction;
import com.serviceImplementation.Wallet.model.Wallet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class WalletServiceImple implements WalletService {
    @Autowired
    WalletRepo walletrepo;

    @Autowired
    TransactionRepo transrepo;


    @Override
    public ResponseEntity<Wallet> createWallet(Wallet newWallet) {
     try {
        if (newWallet == null) {
            throw new ResourceNotFoundException("New wallet cannot be null!");
        }

        newWallet.setBalance(0);
        Wallet savedWallet = walletrepo.save(newWallet);
        return new ResponseEntity<>(savedWallet, HttpStatus.CREATED);
    } catch (ResourceNotFoundException e) {
        // Handle the custom exception
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    } catch (Exception e) {
        // Handle other exceptions
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
    @Override
    public ResponseEntity<Wallet> addWallet(Wallet wallet) {
        try {
            Wallet walletObj = walletrepo.save(wallet);
            return new ResponseEntity<>(walletObj, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @Override
    public ResponseEntity<List<Wallet>> getAllWallets() {
        try {
            List<Wallet> WalletList = new ArrayList<>();
            walletrepo.findAll().forEach(WalletList::add);

            if (WalletList.isEmpty()) {
                throw new WalletNotFoundException("No Wallets Found!");
            }

            return new ResponseEntity<>(WalletList, HttpStatus.OK);
        }catch(WalletNotFoundException e){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<Wallet> topup(long walletId, Wallet walletRequest) {
        try {
            Optional<Wallet> optionalWallet = walletrepo.findById(walletId);

            if (optionalWallet.isPresent()) {
                Wallet wallet = optionalWallet.get();
                double topUpAmount = walletRequest.getBalance(); // Assuming balance is used for top-up amount

                if (topUpAmount > 1000) {
                    throw new TopUpLimitExceededException("Top-up amount exceeds limit");
                }

                wallet.setBalance(wallet.getBalance() + topUpAmount);

                walletrepo.save(wallet);

                return new ResponseEntity<>(HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (TopUpLimitExceededException e) {

            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {

            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @Override
    public ResponseEntity<Double> checkBalance(long walletId) {
        try {
            Optional<Wallet> optionalWallet = walletrepo.findById(walletId);

            if (optionalWallet.isPresent()) {
                Wallet wallet = optionalWallet.get();
                double balance = wallet.getBalance();
                return new ResponseEntity<>(balance, HttpStatus.OK);
            } else {
                throw new WalletNotFoundException("Wallet not found with ID: " + walletId);
            }
        } catch (WalletNotFoundException e) {

            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {

            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @Override
    public ResponseEntity<String> deleteWalletById(long walletId) {
        Optional<Wallet> optionalWallet = walletrepo.findById(walletId);

        if (optionalWallet.isPresent()) {
            Wallet wallet = optionalWallet.get();
            walletrepo.delete(wallet);
            return new ResponseEntity<>("Wallet deleted successfully", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Wallet not found", HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public ResponseEntity<String> deleteAllWallets() {
        try {
            walletrepo.deleteAll();
            return new ResponseEntity<>("Wallets deleted successfully", HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>("No Wallet Found", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @Override
    public ResponseEntity<List<Wallet>> fundTransfer(long source, long target, Wallet transferAmount) {
        try {
            Optional<Wallet> optionalSource = walletrepo.findById(source);
            Optional<Wallet> optionalTarget = walletrepo.findById(target);
            if (optionalSource.isPresent() && optionalTarget.isPresent()) {
                Wallet sourceWallet = optionalSource.get();
                Wallet targetWallet = optionalTarget.get();
                double sourceBalance = sourceWallet.getBalance();
                double transferBalance = transferAmount.getBalance();



                if (sourceBalance >= transferBalance && transferBalance <= 100) {

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

                    sourceWallet.setBalance(sourceWallet.getBalance() - transferBalance);
                    targetWallet.setBalance(targetWallet.getBalance() + transferBalance);

                    // Save updated wallets
                    sourceWallet = walletrepo.save(sourceWallet);
                    targetWallet = walletrepo.save(targetWallet);

                    return new ResponseEntity<>(Arrays.asList(sourceWallet, targetWallet), HttpStatus.OK);
                } else {
                    throw new InsufficientBalanceException("Insufficient balance or transfer amount exceeds the limit.");
                }
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (InsufficientBalanceException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<List<Transaction>> getAllTransactionss() {
        try {
            List<Transaction> transactionList = new ArrayList<>();
            transrepo.findAll().forEach(transactionList::add);

            if (transactionList.isEmpty()) {
                throw new TransactionNotFoundException("No transactions found");
            }

            return new ResponseEntity<>(transactionList, HttpStatus.OK);
        } catch (TransactionNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
