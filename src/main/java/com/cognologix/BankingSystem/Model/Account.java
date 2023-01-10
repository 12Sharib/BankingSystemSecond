package com.cognologix.BankingSystem.Model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.CascadeType;
import javax.persistence.JoinColumn;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


@Component
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table
public class Account {
    @Id
    private Integer accountNumber;
    @NotNull
    private Integer customerId;
    @NotBlank
    private String accountName;
    @NotBlank
    private String accountType;
    @NotNull
    private Double accountInitialBalance;
    @NotBlank
    private String accountStatus;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "customer_fk")
    private Customer customer;


}

