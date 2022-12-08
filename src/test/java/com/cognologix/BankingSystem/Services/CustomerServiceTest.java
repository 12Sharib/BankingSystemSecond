package com.cognologix.BankingSystem.Services;

import com.cognologix.BankingSystem.Model.Account;
import com.cognologix.BankingSystem.Model.Customer;
import com.cognologix.BankingSystem.Repository.CustomerRepository;
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
    CustomerRepository customerRepository;

    @Autowired
    private CustomerServiceImpl customerService;

    @Test
    void updateCustomerDetails() {
         Customer customer = new Customer(1, 21, " ",
                "Savings", "SharibSaifi.SS@gmail.com", "8006590554",
                " ", "OGHPS2812E", "Muradanagar");

        doThrow(new Throwable("Invalid details"))
                .when(customerService).updateCustomerDetails(customer,1);
    }

    @Test
    void getAllCustomer() {
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
        Assertions.assertEquals(2,customerService.getAllCustomer().size());
    }

    @Test
    void createCustomer() {
        Customer customer = new Customer(1,21,"Sharib Saifi",
                "Savings", "SharibSaifi.SS@gmail.com","8006590554",
                "1111 2222 3333","OGHPS2812E","Muradanagar");

        Mockito.when(customerRepository.save(customer)).thenReturn(customer);
        Assertions.assertEquals(customer,customerRepository.save(customer));
    }
}