package com.cognologix.BankingSystem.controllers;

import com.cognologix.BankingSystem.Model.Customer;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.assertj.core.internal.bytebuddy.matcher.ElementMatchers.is;

@SpringBootTest
@AutoConfigureMockMvc
class CustomerControllerTest {
    @InjectMocks
    private CustomerController customerController;
    @Autowired
    private MockMvc mockMvc;


    @Test
    void createCustomer() throws Exception {
        Customer customer = new Customer(10,12,"Sharib Saifi","Current"
                ,"www.SharibSaifi@gmail.com","8006590554","1111 2222 3333"
                ,"OGHPS2812D","Muradnagar");
        mockMvc.perform(MockMvcRequestBuilders.post("/customer/createCustomer/{customer}"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect((ResultMatcher) MockMvcResultMatchers.jsonPath("$.accountStatus",is("Active")))
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andReturn();
    }

    @Test
    void allCustomers() {
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