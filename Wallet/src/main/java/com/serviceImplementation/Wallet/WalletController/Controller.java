package com.serviceImplementation.Wallet.WalletController;

import com.serviceImplementation.Wallet.CustomException.*;
import com.serviceImplementation.Wallet.CustomException.IllegalArgumentException;
import com.serviceImplementation.Wallet.Service.WalletService;
import com.serviceImplementation.Wallet.model.Transaction;
import com.serviceImplementation.Wallet.model.Wallet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/wallet")

public class Controller {

    @Autowired
    WalletService walletService;


    @GetMapping("/first")
    public String first() {
        return "Hello, World!";
    }


    @PreAuthorize("hasRole('ADMIN')")
   // @CrossOrigin
    @PostMapping("/createWallet")
    public ResponseEntity<Wallet> createWallet(@RequestBody Wallet newWallet) throws UserNotFoundException {
        Wallet savedWallet = walletService.createWallet(newWallet);
        return new ResponseEntity<Wallet>(savedWallet, HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/getAllWallets")
    public ResponseEntity<List<Wallet>> getAllWallets() throws WalletNotFoundException {

        List<Wallet> WalletList = walletService.getAllWallets();
        return new ResponseEntity<List<Wallet>>(WalletList, HttpStatus.OK);
    }

    @PostMapping("/topUpById")
    public ResponseEntity<Wallet> topUp(@RequestParam long walletId, @RequestBody Wallet walletRequest)throws IllegalArgumentException, TopUpLimitExceededException,WalletNotFoundException {
        Wallet wallet = walletService.topup(walletId, walletRequest);
        return new ResponseEntity<Wallet>(wallet, HttpStatus.ACCEPTED);
    }

    @GetMapping("/checkBalance")
    public ResponseEntity<Double> checkBalance(@RequestParam long walletId) throws WalletNotFoundException{
         Double balance = walletService.checkBalance(walletId);
         return new ResponseEntity<>(balance, HttpStatus.OK);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteWalletById(@RequestParam long walletId) throws WalletNotFoundException{
       String result=walletService.deleteWalletById(walletId);
       return new ResponseEntity<>(result,HttpStatus.OK);
    }


    @PostMapping("/fundTransfer")
    public ResponseEntity<List<Wallet>> fundTransfer(
            @RequestParam long source,
            @RequestParam long target,
            @RequestBody Wallet transferAmount) {
        List<Wallet> WalletList = walletService.fundTransfer(source, target, transferAmount);
        return new ResponseEntity<List<Wallet>>(WalletList,HttpStatus.OK);

    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/getAllTransactions")
    public ResponseEntity<List<Transaction>> getAllTransactionss() throws TransactionNotFoundException {
       List<Transaction> transactionList = walletService.getAllTransactionss();
       return new ResponseEntity<>(transactionList,HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/getTransactionByAmount")
    public ResponseEntity<List<Transaction>> getTransactionByAmount(@RequestParam double amount) throws TransactionNotFoundException{
        List<Transaction> allTransaction = walletService.getTransactionByAmount(amount);
        return new ResponseEntity<>(allTransaction,HttpStatus.OK);
    }
}


























































