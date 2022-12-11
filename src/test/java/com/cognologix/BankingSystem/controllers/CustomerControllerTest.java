package com.cognologix.BankingSystem.controllers;

import com.cognologix.BankingSystem.Model.Customer;
import com.cognologix.BankingSystem.Repository.AccountRepo;
import com.cognologix.BankingSystem.Repository.CustomerRepository;
import com.cognologix.BankingSystem.Services.CustomerService;
import com.cognologix.BankingSystem.dto.AccountDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;


import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.hamcrest.Matchers.is;

@WebMvcTest(CustomerController.class)
class CustomerControllerTest {
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private CustomerRepository customerRepository;
    @MockBean
    private AccountRepo accountRepo;
    @MockBean
    private CustomerService customerService;

    @Autowired
    MockMvc mockMvc;

    @Test
    void createCustomer() throws Exception{
        //failed
        Customer customer = new Customer();
        customer.setCustomerName("Sharib Saifi");
        customer.setCustomerId(101);

        AccountDTO accountDTO = new AccountDTO();
        accountDTO.setAccountName("Sharib Saifi");
        accountDTO.setAccountStatus("Active");
        accountDTO.setAccountNumber(3);

        when(customerService.createCustomer(customer)).thenReturn(accountDTO);

        mockMvc.perform(MockMvcRequestBuilders.post("/customer/createCustomer/customer")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(accountDTO)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.accountNumber",is(accountDTO.getAccountNumber())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.accountName",is(accountDTO.getAccountName())))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
    }

    @Test
    void allCustomers() throws Exception {
        Customer firstCustomer = new Customer();
        firstCustomer.setCustomerName("Sharib Saifi");
        firstCustomer.setCustomerId(101);

        Customer secondCustomer = new Customer();
        secondCustomer.setCustomerId(102);

        List<Customer> customers = new ArrayList<>();
        customers.add(firstCustomer);
        customers.add(secondCustomer);

        when(customerService.allCustomer()).thenReturn(customers);

        mockMvc.perform(MockMvcRequestBuilders.get("/customer/allCustomers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(customers)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].customerId",is(firstCustomer.getCustomerId())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()").value(customers.size()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
    }

    @Test
    void findById() {
    }

    @Test
    void deleteAll() {
    }

    @Test
    void updateCustomer() {
    }
}