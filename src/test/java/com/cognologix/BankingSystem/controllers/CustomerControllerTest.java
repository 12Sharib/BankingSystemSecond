package com.cognologix.BankingSystem.controllers;

import com.cognologix.BankingSystem.Exceptions.InvalidCustomerId;
import com.cognologix.BankingSystem.Exceptions.InvalidDocument;
import com.cognologix.BankingSystem.Model.Customer;
import com.cognologix.BankingSystem.Repository.CustomerRepository;
import com.cognologix.BankingSystem.Response.SuccessResponse;
import com.cognologix.BankingSystem.Services.CustomerService;
import com.cognologix.BankingSystem.dto.AccountDTO;
import com.cognologix.BankingSystem.dto.CustomerDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
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

import static org.hamcrest.Matchers.in;
import static org.mockito.Mockito.when;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CustomerController.class)
class CustomerControllerTest {
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private CustomerRepository customerRepository;
    @MockBean
    private CustomerService customerService;
    @Autowired
    MockMvc mockMvc;

    @Nested
    class CreateCustomer{
        @Test
        @DisplayName("positive create customer")
        void createCustomer() throws InvalidDocument {
            try {
                //failed
                Customer customer = new Customer();
                customer.setCustomerName("Sharib Saifi");
                customer.setCustomerId(101);

                AccountDTO accountDTO = new AccountDTO();
                accountDTO.setAccountName("Sharib Saifi");
                accountDTO.setAccountStatus("Active");
                accountDTO.setAccountNumber(3);

                when(customerService.createCustomer(customer)).thenReturn(accountDTO);

                mockMvc.perform(MockMvcRequestBuilders.post("/customer/createCustomer/")
                                //    .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(accountDTO)))
                        .andExpect(MockMvcResultMatchers.jsonPath("$.accountNumber", is(accountDTO.getAccountNumber())))
                        .andExpect(MockMvcResultMatchers.jsonPath("$.accountName", is(accountDTO.getAccountName())))
                        .andExpect(MockMvcResultMatchers.status().isOk())
                        .andReturn();
            }catch (InvalidDocument invalidDocument){
                Assertions.assertTrue(invalidDocument instanceof InvalidDocument);
            }catch (Exception exception){
                throw new RuntimeException(exception.getMessage());
            }

        }
        @Test
        @DisplayName("negative, when details are invalid or unsupported")
        void negative_createCustomer() throws Exception {
            //when data is incorrect or empty request
            when(customerService.createCustomer(null)).thenReturn(null);
            mockMvc.perform(MockMvcRequestBuilders.post("/customer/createCustomer")
                            .content(objectMapper.writeValueAsString(null)))
                    .andExpect(MockMvcResultMatchers.status().isUnsupportedMediaType())
                    .andReturn();
        }
    }

   @Nested
    class AllCustomers{
       @Test
       @DisplayName("positive, all customers")
       void allCustomers() throws Exception {
           CustomerDTO firstCustomer = new CustomerDTO();
           firstCustomer.setCustomerName("Sharib Saifi");
           firstCustomer.setCustomerId(101);

           CustomerDTO secondCustomer = new CustomerDTO();
           secondCustomer.setCustomerId(102);

           List<CustomerDTO> customers = new ArrayList<>();
           customers.add(firstCustomer);
           customers.add(secondCustomer);

           when(customerService.allCustomer()).thenReturn(customers);

           mockMvc.perform(MockMvcRequestBuilders.get("/customer/allCustomers")
                           .contentType(MediaType.APPLICATION_JSON)
                           .content(objectMapper.writeValueAsString(customers)))
                   .andExpect(MockMvcResultMatchers.jsonPath("$[0].customerId",is(firstCustomer.getCustomerId())))
                   .andExpect(MockMvcResultMatchers.jsonPath("$.size()").value(customers.size()))
                   .andExpect(status().isFound())
                   .andReturn();
       }
       @Test
       @DisplayName("negative, when list is empty")
       void negative_allCustomers() throws Exception{
           //when all customers list is empty
           when(customerService.allCustomer()).thenReturn(null);
           mockMvc.perform(MockMvcRequestBuilders.get("/customers/allCustomer")
                           .content(objectMapper.writeValueAsString(null)))
                   .andExpect(MockMvcResultMatchers.status().isNotFound())
                   .andReturn();
       }
    }
    @Nested
    class SingleCustomer{
        @Test
        @DisplayName("positive, single cusotmer")
        void singleCustomer() throws InvalidCustomerId {
            try {
                CustomerDTO customer = new CustomerDTO();
                customer.setCustomerId(1);
                customer.setCustomerName("Sharib Saifi");

                List<CustomerDTO> customers = new ArrayList<>();
                customers.add(customer);

                when(customerService.customer(customer.getCustomerId())).thenReturn(customers);

                mockMvc.perform(MockMvcRequestBuilders.get("/customer/customerById/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(customer)))
                        .andExpect(MockMvcResultMatchers.jsonPath("$[0].customerId", is(customer.getCustomerId())))
                        .andExpect(MockMvcResultMatchers.jsonPath("$[0].customerName", is(customer.getCustomerName())))
                        .andExpect(status().isFound())
                        .andReturn();
            }catch (InvalidCustomerId invalidCustomerId){
                Assertions.assertTrue(invalidCustomerId instanceof InvalidCustomerId);
            }catch (Exception exception){
                throw new RuntimeException(exception.getMessage());
            }
        }
        @Test
        @DisplayName("negative, when customer id is invalid or not found")
        void negative_singleCustomer() throws Exception{
            //when customer id is invalid or not present
            Customer customer = new Customer();
            customer.setCustomerId(45);

            when(customerService.customer(-1)).thenReturn(null);
            mockMvc.perform(MockMvcRequestBuilders.get("/customer/singleCustomer/-1")
                            .content(objectMapper.writeValueAsString(customer)))
                    .andExpect(MockMvcResultMatchers.status().isNotFound())
                    .andReturn();
        }
    }
   @Nested
   class UpdateCustomer{
       @Test
       @DisplayName("positive, update customer")
       void updateCustomer() throws Exception{
           //failed
           Customer customer = new Customer();
           customer.setCustomerName("Sharib Saifi");
           customer.setCustomerId(1);

           Customer updateDetails = new Customer();
           updateDetails.setCustomerName("Suhail");

           when(customerService.updateCustomerDetails(customer,customer.getCustomerId())).thenReturn(updateDetails);

           mockMvc.perform(put("/customer/updateCustomer/1")
                           .contentType(MediaType.APPLICATION_JSON)
                           .content(objectMapper.writeValueAsString(updateDetails)))
                   .andExpect(MockMvcResultMatchers.jsonPath("$.customerName",is(updateDetails.getCustomerName())))
                   .andExpect(status().isCreated())
                   .andReturn();
       }
       @Test
       @DisplayName("negative, customer details are invalid or unsupported")
       void negative_updateCustomer() throws Exception{
           //when customer id is correct but the customer details is invalid
           Customer customer = new Customer();
           customer.setCustomerId(1);

           when(customerService.updateCustomerDetails(null,1)).thenReturn(customer);
           mockMvc.perform(put("/customer/updateCustomer/1")
                           .content(objectMapper.writeValueAsString(customer)))
                   .andExpect(MockMvcResultMatchers.status().isUnsupportedMediaType())
                   .andReturn();
       }
   }
    @Test
    void deleteAll() throws Exception {
        SuccessResponse response = new SuccessResponse("Delete Successfully",true);
        when(customerService.deleteAll()).thenReturn(response);

        mockMvc.perform(MockMvcRequestBuilders.delete("/customer/deleteAll")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(response)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message",is(response.getMessage())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.success",is(true)))
                .andExpect(status().isOk())
                .andReturn();
    }

}