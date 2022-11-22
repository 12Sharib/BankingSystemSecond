package com.cognologix.BankingSystem.Repository;

import com.cognologix.BankingSystem.Model.Customer;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer,Integer> {

    Customer findBycustomerAadharNumber(String s);

}
