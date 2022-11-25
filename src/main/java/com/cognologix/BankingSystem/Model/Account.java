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
    private Integer accountNumber;
    private Integer customerId;
    private String accountName;
    private String accountType;
    private Double accountInitialBalance;
    private String accountStatus;


    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "customer_fk")
    private Customer customer;


}

