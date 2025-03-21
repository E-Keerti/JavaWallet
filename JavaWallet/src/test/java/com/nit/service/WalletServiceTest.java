package com.nit.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.nit.entity.Transaction;
import com.nit.entity.User;
import com.nit.repository.TransactionRepository;
import com.nit.repository.UserRepository;
import com.nit.sevice.WalletService;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WalletServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private WalletService walletService;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User("John Doe", new BigDecimal("500.00"));
        user.setId(1L);
    }

    // Test case 1: Get balance
    @Test
    void testGetBalance() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        BigDecimal balance = walletService.getBalance(1L);
        assertEquals(new BigDecimal("500.00"), balance);
    }

    // Test case 2: Add money to wallet
    @Test
    void testAddMoney() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        String response = walletService.addMoney(1L, new BigDecimal("100.00"));

        assertEquals("Amount credited successfully!", response);
        assertEquals(new BigDecimal("600.00"), user.getBalance());
        verify(transactionRepository, times(1)).save(any(Transaction.class));
    }

    // Test case 3: Withdraw money successfully
    @Test
    void testWithdrawMoney_Success() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        String response = walletService.withdrawMoney(1L, new BigDecimal("200.00"));

        assertEquals("Amount debited successfully!", response);
        assertEquals(new BigDecimal("300.00"), user.getBalance());
        verify(transactionRepository, times(1)).save(any(Transaction.class));
    }

    // Test case 4: Withdraw money with insufficient balance
    @Test
    void testWithdrawMoney_InsufficientBalance() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        String response = walletService.withdrawMoney(1L, new BigDecimal("600.00"));

        assertEquals("Insufficient balance!", response);
        assertEquals(new BigDecimal("500.00"), user.getBalance());
        verify(transactionRepository, times(0)).save(any(Transaction.class));
    }

    // Test case 5: Get transaction history
    @Test
    void testGetTransactionHistory() {
        Transaction t1 = new Transaction(user, "CREDIT", new BigDecimal("100.00"));
        Transaction t2 = new Transaction(user, "DEBIT", new BigDecimal("50.00"));

        when(transactionRepository.findByUserId(1L)).thenReturn(Arrays.asList(t1, t2));

        List<Transaction> transactions = walletService.getTransactionHistory(1L);

        assertEquals(2, transactions.size());
        assertEquals("CREDIT", transactions.get(0).getType());
        assertEquals("DEBIT", transactions.get(1).getType());
    }
}