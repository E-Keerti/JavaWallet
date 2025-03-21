package com.nit.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.nit.entity.Transaction;
import com.nit.sevice.WalletService;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/wallet")
public class WalletController {

    @Autowired
    private WalletService walletService;

    // Get current balance of a user
    @GetMapping("/balance/{userId}")
    public ResponseEntity<BigDecimal> getBalance(@PathVariable Long userId)
    {
        return ResponseEntity.ok(walletService.getBalance(userId));
    }

    // Add money to wallet
    @PostMapping("/credit")
    public ResponseEntity<String> addMoney(@RequestParam Long userId, @RequestParam BigDecimal amount) {
        return ResponseEntity.ok(walletService.addMoney(userId, amount));
    }

    // Withdraw money from wallet
    @PostMapping("/debit")
    public ResponseEntity<String> withdrawMoney(@RequestParam Long userId, @RequestParam BigDecimal amount) {
        return ResponseEntity.ok(walletService.withdrawMoney(userId, amount));
    }

    // Get transaction history
    @GetMapping("/transactions/{userId}")
    public ResponseEntity<List<Transaction>> getTransactionHistory(@PathVariable Long userId) {
        return ResponseEntity.ok(walletService.getTransactionHistory(userId));
    }
}
