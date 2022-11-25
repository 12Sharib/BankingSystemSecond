package com.cognologix.BankingSystem.Repository;

import com.cognologix.BankingSystem.Model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountRepo extends JpaRepository<Account, Integer> {
    List<Account> findAllByCustomerId(Integer customerId);


    //List<Account> findAllByAccountCustomerId(Integer customerId);
}
