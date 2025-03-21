package com.nit.entity;


import jakarta.persistence.*; 
import java.math.BigDecimal;
import java.time.LocalDateTime;

//Token-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

@Entity
@Table(name = "transactions")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private String type; // "CREDIT" or "DEBIT"

    private BigDecimal amount;

    private LocalDateTime timestamp;

    public Transaction() {
    }

    public Transaction(User user, String type, BigDecimal amount) {
        this.user = user;
        this.type = type;
        this.amount = amount;
        this.timestamp = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}

