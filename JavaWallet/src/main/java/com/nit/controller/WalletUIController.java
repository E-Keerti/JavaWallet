package com.nit.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.nit.entity.Transaction;
import com.nit.sevice.WalletService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/wallet")
public class WalletUIController {

    @Autowired
    private WalletService walletService;

    @GetMapping("/{userId}")
    public String walletHome(@PathVariable Long userId, Model model) {
        BigDecimal balance = walletService.getBalance(userId);
        List<Transaction> transactions = walletService.getTransactionHistory(userId);
        
        model.addAttribute("userId", userId);
        model.addAttribute("balance", balance);
        model.addAttribute("transactions", transactions);

        return "wallet";
    }

    @PostMapping("/credit")
    public String addMoney(@RequestParam Long userId, @RequestParam BigDecimal amount) {
        walletService.addMoney(userId, amount);
        return "redirect:/wallet/" + userId;
    }

    @PostMapping("/debit")
    public String withdrawMoney(@RequestParam Long userId, @RequestParam BigDecimal amount, Model model) {
        String message = walletService.withdrawMoney(userId, amount);
        model.addAttribute("message", message);
        return "redirect:/wallet/" + userId;
    }
}

