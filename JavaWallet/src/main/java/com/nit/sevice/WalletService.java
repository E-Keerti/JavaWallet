package com.nit.sevice;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nit.entity.Transaction;
import com.nit.entity.User;
import com.nit.repository.TransactionRepository;
import com.nit.repository.UserRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class WalletService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    // Get current balance of a user
    public BigDecimal getBalance(Long userId) {
        Optional<User> user = userRepository.findById(userId);
        return user.map(User::getBalance).orElse(BigDecimal.ZERO);
    }

    // Add money to wallet
    @Transactional
    public String addMoney(Long userId, BigDecimal amount) {
        Optional<User> userOptional = userRepository.findById(userId);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.addMoney(amount);
            userRepository.save(user);

            // Save transaction
            Transaction transaction = new Transaction(user, "CREDIT", amount);
            transactionRepository.save(transaction);

            return "Amount credited successfully!";
        }
        return "User not found!";
    }

    // Withdraw money from wallet
    @Transactional
    public String withdrawMoney(Long userId, BigDecimal amount) {
        Optional<User> userOptional = userRepository.findById(userId);

        if (userOptional.isPresent()) {
            User user = userOptional.get();

            if (user.withdrawMoney(amount)) {
                userRepository.save(user);

                // Save transaction
                Transaction transaction = new Transaction(user, "DEBIT", amount);
                transactionRepository.save(transaction);

                return "Amount debited successfully!";
            } else {
                return "Insufficient balance!";
            }
        }
        return "User not found!";
    }

    // Get transaction history
    public List<Transaction> getTransactionHistory(Long userId) {
        return transactionRepository.findByUserId(userId);
    }
}

