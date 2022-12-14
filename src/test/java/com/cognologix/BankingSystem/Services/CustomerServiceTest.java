package com.cognologix.BankingSystem.Services;

import com.cognologix.BankingSystem.Exceptions.AccountsNotExist;
import com.cognologix.BankingSystem.Exceptions.InvalidCustomerId;
import com.cognologix.BankingSystem.Exceptions.InvalidDocument;
import com.cognologix.BankingSystem.Model.Account;
import com.cognologix.BankingSystem.Model.Customer;
import com.cognologix.BankingSystem.Repository.AccountRepo;
import com.cognologix.BankingSystem.Repository.CustomerRepository;
import com.cognologix.BankingSystem.Response.SuccessResponse;
import com.cognologix.BankingSystem.Services.ServicesImpl.CustomerServiceImpl;
import com.cognologix.BankingSystem.dto.AccountDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.extension.ExtendWith;

import static org.mockito.Mockito.when;

import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@SpringBootTest
@ExtendWith(SpringExtension.class)
class CustomerServiceTest {
    @MockBean
    private CustomerRepository customerRepository;
    @MockBean
    private AccountRepo accountRepo;

    @Autowired
    private CustomerServiceImpl customerService;

    @Test
    void allCustomers() {
        List<Customer> customers = new ArrayList<>();

        Customer firstCustomer = new Customer(1, 21, "Sharib Saifi",
                "Savings", "SharibSaifi.SS@gmail.com", "8006590554",
                "1111 2222 3333", "OGHPS2812E", "Muradanagar");

        Customer secondCustomer = new Customer(3, 25, "Sharib",
                "Current", "SharibSaifi.KS@gmail.com", "8003590554",
                "1111 2202 3333", "OGHPSOO12E", "Muradanagar");

        customers.add(firstCustomer);
        customers.add(secondCustomer);
        customerRepository.saveAll(customers);

        Mockito.when(customerRepository.findAll()).thenReturn(customers);
        Assertions.assertEquals(2, customerService.allCustomer().size());
    }

    @Test
    void createCustomer() {
        try {
            Customer customer = new Customer(1, 21, "Sharib Saifi",
                    "Savings", "SharibSaifi.SS@gmail.com", "8006590554",
                    "1111 2222 3334", "OGHPS2812E", "Muradanagar");

            Account account = new Account();
            account.setCustomerId(1);
            account.setAccountName("Sharib Saifi");

            when(customerRepository.findBycustomerAadharNumber(customer.getCustomerAadharNumber())).thenReturn(null);
            when(accountRepo.save(account)).thenReturn(account);
            when(accountRepo.save(account)).thenReturn(account);

            AccountDTO accountDTO = customerService.createCustomer(customer);

            Assertions.assertEquals("Active", accountDTO.getAccountStatus());
        }catch (Exception exception) {
            System.out.println(exception.getMessage());
            Assertions.assertTrue(exception instanceof InvalidDocument);
        }
    }
    @Test
    void updateCustomerDetails() throws InvalidCustomerId{
        try {
            Customer customer = new Customer(1, 21, "Sharib Saifi",
                    "Savings", "SharibSaifi.SS@gmail.com", "8006590554",
                    "3334 3221 5548", "OGHPS2812E", "Muradanagar");

            when(customerRepository.findById(1)).thenReturn(Optional.of(customer))
                    .thenThrow(new InvalidCustomerId("Invalid Customer Id"));
            customer.setCustomerName("Suhail Saifi");

            when(customerRepository.save(customer)).thenReturn(customer);
            Customer newCustomer = customerService.updateCustomerDetails(customer, 1);
            Assertions.assertEquals("Suhail Saifi", newCustomer.getCustomerName());
        }catch (InvalidDocument exception){
            Assertions.assertTrue(exception instanceof InvalidDocument);
        }catch (Exception exception){
            System.out.println(exception.getMessage());
        }
    }
    @Test
    void deleteAll(){
        SuccessResponse response = customerService.deleteAll();
        Assertions.assertEquals(true,response.getSuccess());
    }
    @Test
    void singleCustomer() throws InvalidCustomerId{
        Customer customer = new Customer();
        customer.setCustomerId(101);

        List<Customer> customers = List.of(customer);

        when(accountRepo.existsByCustomerId(customer.getCustomerId())).thenReturn(true)
                .thenThrow(new InvalidCustomerId("Invalid Customer Id"));
        when(customerRepository.findByCustomerId(customer.getCustomerId())).thenReturn(customers)
                .thenThrow(new AccountsNotExist("Empty Database"));

        Assertions.assertEquals(1,customerService.customer(customer.getCustomerId()).size());
    }
}