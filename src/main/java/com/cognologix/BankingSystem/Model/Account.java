package com.cognologix.BankingSystem.Model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;


import javax.persistence.*;

@Component
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer accountNumber;
    private String accountName;
    private String accountType;
    private Double accountInitialBalance;
    private Integer accountCustomerId;

}

