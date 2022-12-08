package com.cognologix.BankingSystem.Repository;

import com.cognologix.BankingSystem.Model.Transactions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionsRepository extends JpaRepository<Transactions,Integer> {
    List<Transactions> findByAccountNumber(Integer accountNumber);
    // public List<Transactions> findByToAccountNumber(String toString);

}
