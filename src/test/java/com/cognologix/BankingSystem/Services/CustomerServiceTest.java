package com.cognologix.BankingSystem.Services;

import com.cognologix.BankingSystem.Model.Account;
import com.cognologix.BankingSystem.Model.Customer;
import com.cognologix.BankingSystem.Repository.AccountRepo;
import com.cognologix.BankingSystem.Repository.CustomerRepository;
import com.cognologix.BankingSystem.Response.SuccessResponse;
import com.cognologix.BankingSystem.Services.ServicesImpl.CustomerServiceImpl;
import com.cognologix.BankingSystem.convertor.AccountConvertor;
import com.cognologix.BankingSystem.dto.AccountDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
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

        Customer firstCustomer = new Customer(1,21,"Sharib Saifi",
                "Savings", "SharibSaifi.SS@gmail.com","8006590554",
                "1111 2222 3333","OGHPS2812E","Muradanagar");

        Customer secondCustomer = new Customer(3,25,"Sharib",
                "Current", "SharibSaifi.KS@gmail.com","8003590554",
                "1111 2202 3333","OGHPSOO12E","Muradanagar");

        customers.add(firstCustomer);
        customers.add(secondCustomer);
        customerRepository.saveAll(customers);

        Mockito.when(customerRepository.findAll()).thenReturn(customers);
        Assertions.assertEquals(2,customerService.allCustomer().size());
    }

    @Test
    void createCustomer() {
        Customer customer = new Customer(1,21,"Sharib Saifi",
                "Savings", "SharibSaifi.SS@gmail.com","8006590554",
                "1111 2222 3334","OGHPS2812E","Muradanagar");

        Account account = new Account();
        account.setCustomerId(1);
        account.setAccountName("Sharib Saifi");

        when(customerRepository.findBycustomerAadharNumber(customer.getCustomerAadharNumber())).thenReturn(null);
        when(accountRepo.save(account)).thenReturn(account);
        when(accountRepo.save(account)).thenReturn(account);

        AccountDTO accountDTO = customerService.createCustomer(customer);

        Assertions.assertEquals("Active",accountDTO.getAccountStatus());
    }
    @Test
    void updateCustomerDetails() {
        Customer customer = new Customer(1, 21, "Sharib Saifi",
                "Savings", "SharibSaifi.SS@gmail.com", "8006590554",
                "3334 3221 5548", "OGHPS2812E", "Muradanagar");

        when(customerRepository.findById(1)).thenReturn(Optional.of(customer));
        customer.setCustomerName("Suhail Saifi");

        when(customerRepository.save(customer)).thenReturn(customer);
        Customer newCustomer = customerService.updateCustomerDetails(customer,1);
        Assertions.assertEquals("Suhail Saifi",newCustomer.getCustomerName());
    }
    @Test
    void deleteAll(){
        SuccessResponse response = customerService.deleteAll();
        Assertions.assertEquals(true,response.getSuccess());
    }
}