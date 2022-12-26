package com.cognologix.BankingSystem.Services;

import com.cognologix.BankingSystem.Exceptions.AccountsNotExist;
import com.cognologix.BankingSystem.Exceptions.InvalidCustomerId;
import com.cognologix.BankingSystem.Model.Account;
import com.cognologix.BankingSystem.Model.Customer;
import com.cognologix.BankingSystem.Repository.AccountRepo;
import com.cognologix.BankingSystem.Repository.CustomerRepository;
import com.cognologix.BankingSystem.Response.SuccessResponse;
import com.cognologix.BankingSystem.Services.ServicesImpl.CustomerServiceImpl;
import com.cognologix.BankingSystem.dto.AccountDTO;
import com.cognologix.BankingSystem.dto.CustomerDTO;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.extension.ExtendWith;

import static org.mockito.Mockito.when;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@Log4j2
class CustomerServiceTest {
    @MockBean
    private CustomerRepository customerRepository;
    @MockBean
    private AccountRepo accountRepo;
    @Autowired
    private CustomerServiceImpl customerService;
    @Nested
    class AllCustomers{
        @BeforeEach
        void init(){
            Customer firstCustomer = new Customer(1, 21, "Sharib Saifi",
                    "Savings", "SharibSaifi.SS@gmail.com", "8006590554",
                    "1111 2222 3333", "OGHPS2812E", "Muradanagar");

            Customer secondCustomer = new Customer(3, 25, "Sharib",
                    "Current", "SharibSaifi.KS@gmail.com", "8003590554",
                    "1111 2202 3333", "OGHPSOO12E", "Muradanagar");

            List<Customer> customers = List.of(firstCustomer,secondCustomer);
            when(customerRepository.findAll()).thenReturn(customers);
        }
        @Test
        @DisplayName("positive all customers")
        void allCustomers() {
            log.info("Start Positive allCustomers");
            Assertions.assertEquals(2, customerService.allCustomer().size());
            log.info("end");
        }
        @Test
        @DisplayName("negative, when list is empty")
        void negative_allCustomers(){
            log.info("Start negative allCustomers");
            when(customerRepository.findAll()).thenReturn(List.of());
            Assertions.assertTrue(customerService.allCustomer().isEmpty());
            log.info("end");
        }
    }

    @Nested
    class CreateCustomer{
        @BeforeEach
        void init(){
            Customer customer = new Customer(1, 21, "Sharib Saifi",
                    "Savings", "SharibSaifi.SS@gmail.com", "8006590554",
                    "1111 2222 3334", "OGHPS2812E", "Muradanagar");
            Account account = new Account();
            account.setCustomerId(1);
            account.setAccountName("Sharib Saifi");

            when(customerRepository.findBycustomerAadharNumber(customer.getCustomerAadharNumber())).thenReturn(null);
            when(accountRepo.save(account)).thenReturn(account);
            when(accountRepo.save(account)).thenReturn(account);
        }
        @Test
        @DisplayName("positive create customer")
        void createCustomer() {
            log.info("Start positive createCustomer");
                Customer customer = new Customer();

                AccountDTO accountDTO = customerService.createCustomer(customer);
                Assertions.assertEquals("Active", accountDTO.getAccountStatus());
            log.info("end");

        }

        @Test
        @DisplayName("negative create customer, when details are Invalid")
        void negative_createCustomer(){
            log.info("Start negative createCustomer");
            Customer customer = new Customer();
            customer.setCustomerAadharNumber("1100 0254 5265");

            when(customerRepository.findBycustomerAadharNumber("1100 0254 5265")).thenReturn(customer);
            Assertions.assertThrows(NullPointerException.class,
                    ()->customerService.createCustomer(customer));
            log.info("end");
        }
    }

    @Nested
    class UpdateCustomerDetails{
        @BeforeEach
        void init(){
            Customer customer = new Customer(1, 21, "Sharib Saifi",
                    "Savings", "SharibSaifi.SS@gmail.com", "8006590554",
                    "3334 3221 5548", "OGHPS2812E", "Muradanagar");


            when(customerRepository.existsByCustomerId(21)).thenReturn(true);
            when(customerRepository.findByCustomerId(21)).thenReturn(List.of(customer))
                    .thenThrow(new InvalidCustomerId("Invalid Customer Id"));
            customer.setCustomerName("Suhail Saifi");

            when(customerRepository.save(customer)).thenReturn(customer);
        }
        @Test
        @DisplayName("positive update customer details")
        void updateCustomerDetails() {
            log.info("Start positive updateCustomer");
               Customer customer = new Customer();

                CustomerDTO newCustomer = customerService.updateCustomerDetails(customer, 21);
                Assertions.assertEquals("Suhail Saifi", newCustomer.getCustomerName());
            log.info("end");

        }
        @Test
        @DisplayName("negative, No such element")
        void negative_updateUpdateDetails(){
            log.info("Start negative updateCustomer");
            Customer customer = new Customer();
            Assertions.assertThrows(InvalidCustomerId.class,
                    ()->customerService.updateCustomerDetails(customer,56));
            log.info("end");
        }
    }

    @Test
    void deleteAll(){
        SuccessResponse response = customerService.deleteAll();
        Assertions.assertEquals(true,response.getSuccess());
    }

    @Nested
    class SingleCustomer{
        @BeforeEach
        void init(){
            Customer customer = new Customer();
            customer.setCustomerId(101);

            List<Customer> customers = List.of(customer);

            when(accountRepo.existsByCustomerId(customer.getCustomerId())).thenReturn(true)
                    .thenThrow(new InvalidCustomerId("Invalid Customer Id"));
            when(customerRepository.findByCustomerId(customer.getCustomerId())).thenReturn(customers)
                    .thenThrow(new AccountsNotExist("Empty Database"));
        }
        @Test
        @DisplayName("positive single customer")
        void singleCustomer() {
            log.info("Start positive singleCustomer");
            Assertions.assertEquals(1,customerService.customer(101).size());
            log.info("error");
        }
        @Test
        @DisplayName("negative, invalid customer id")
        void negative_singleCustomer(){
            log.info("start negative singleCustomer");
           Assertions.assertThrows(InvalidCustomerId.class,
                   ()->customerService.customer(52));
            log.info("end");
        }
    }

}