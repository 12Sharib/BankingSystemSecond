package com.cognologix.BankingSystem.Repository;

import com.cognologix.BankingSystem.Model.Transactions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@EnableJpaRepositories
public interface TransactionsRepository extends JpaRepository<Transactions,Integer> {
    public List<Transactions> findByAccountNumber(Integer accountNumber);

    public List<Transactions> findByTransactionDate(String date);

    @Query(value = "SELECT * FROM transactions WHERE account_number=?1 LIMIT 5",nativeQuery = true)
    public List<Transactions> previousFiveTransactions(Integer accountNumber);

    public Boolean existsByAccountNumber(Integer accountNumber);
}
