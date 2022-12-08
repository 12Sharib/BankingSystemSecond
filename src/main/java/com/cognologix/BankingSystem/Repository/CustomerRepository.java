package com.cognologix.BankingSystem.Repository;

import com.cognologix.BankingSystem.Model.Customer;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerRepository extends JpaRepository<Customer,Integer> {

    public Customer findBycustomerAadharNumber(String s);

    public List<Customer> findByCustomerId(Integer customerId);
}
