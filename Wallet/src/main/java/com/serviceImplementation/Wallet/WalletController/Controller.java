package com.serviceImplementation.Wallet.WalletController;

import com.serviceImplementation.Wallet.Service.WalletService;
import com.serviceImplementation.Wallet.model.Transaction;
import com.serviceImplementation.Wallet.model.Wallet;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@Api(tags = "Wallet API")
public class Controller {

    @Autowired
    WalletService walletService;


    @GetMapping("/first")
    public String first() {
        return "Hello, World!";
    }


    @PreAuthorize("hasRole('ADMIN')")
    @CrossOrigin
    @PostMapping("/createWallet")
    public ResponseEntity<Wallet> createWallet(@RequestBody Wallet newWallet) {
        return walletService.createWallet(newWallet);
    }


  @PostMapping("/addWallet")
    public ResponseEntity<Wallet> addWallet(@RequestBody Wallet wallet) {
        return walletService.addWallet(wallet);
    }
    //@PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/getAllWallets")
    public ResponseEntity<List<Wallet>> getAllWallets() {
        return walletService.getAllWallets();
    }

    @PostMapping("/topUpById/{walletId}")
    public ResponseEntity<Wallet> topUp(@PathVariable long walletId, @RequestBody Wallet walletRequest) {
        return walletService.topup(walletId, walletRequest);
    }

    @GetMapping("/checkBalance/{walletId}")
    public ResponseEntity<Double> checkBalance(@PathVariable long walletId) {
        return walletService.checkBalance(walletId);
    }

    @DeleteMapping("/delete/{walletId}")
    public ResponseEntity<String> deleteWalletById(@PathVariable long walletId) {
        return walletService.deleteWalletById(walletId);
    }


    @PostMapping("/fundTransfer/{source}/{target}")
    public ResponseEntity<List<Wallet>> fundTransfer(
            @PathVariable long source,
            @PathVariable long target,
            @RequestBody Wallet transferAmount) {
        return walletService.fundTransfer(source,target,transferAmount);

    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/getAllTransactions")
    public ResponseEntity<List<Transaction>> getAllTransactionss() {
        return walletService.getAllTransactionss();
    }

}


























































