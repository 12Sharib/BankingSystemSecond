package com.cognologix.BankingSystem.controllers;

import com.cognologix.BankingSystem.Model.Customer;
import com.cognologix.BankingSystem.Repository.CustomerRepository;
import com.cognologix.BankingSystem.Response.SuccessResponse;
import com.cognologix.BankingSystem.Services.CustomerService;
import com.cognologix.BankingSystem.convertor.CustomerConvertor;
import com.cognologix.BankingSystem.dto.AccountDTO;
import com.cognologix.BankingSystem.dto.CustomerDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
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

import static org.mockito.Mockito.when;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CustomerController.class)
@Log4j2
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
        void createCustomer() throws Exception {
            log.info("Starting positive createCustomer....");
                Customer customer = new Customer(1, 21, "Sharib Saifi",
                        "Savings", "SharibSaifi.SS@gmail.com", "8006590554",
                        "3334 3221 5548", "OGHPS2812E", "Muradanagar");

                AccountDTO accountDTO = new AccountDTO();
                accountDTO.setAccountName("Sharib Saifi");
                accountDTO.setAccountStatus("Active");
                accountDTO.setAccountNumber(5);

                when(customerService.createCustomer(customer)).thenReturn(accountDTO);
                mockMvc.perform(MockMvcRequestBuilders.post("/customer/createCustomer")
                                    .contentType(MediaType.APPLICATION_JSON)
                               .content(objectMapper.writeValueAsString(customer)))
                        .andExpect(MockMvcResultMatchers.jsonPath("$.accountNumber", is(accountDTO.getAccountNumber())))
                        .andExpect(MockMvcResultMatchers.jsonPath("$.accountName", is(accountDTO.getAccountName())))
                        .andExpect(MockMvcResultMatchers.status().isCreated())
                        .andReturn();
            log.info("Completed....");
        }
        @Test
        @DisplayName("negative, when details are invalid or unsupported")
        void negative_createCustomer() throws Exception {
            log.info("Starting negative createCustomer....");
            //when data is incorrect or empty request
            when(customerService.createCustomer(null)).thenReturn(null);
            mockMvc.perform(MockMvcRequestBuilders.post("/customer/createCustomer")
                            .content(objectMapper.writeValueAsString(null)))
                    .andExpect(MockMvcResultMatchers.status().isUnsupportedMediaType())
                    .andReturn();
            log.info("Completed....");
        }
    }

    @Nested
    class AllCustomers{
       @Test
       @DisplayName("positive, all customers")
       void allCustomers() throws Exception {
           log.info("Starting positive allCustomer....");
           CustomerDTO firstCustomer = new CustomerDTO();
           firstCustomer.setCustomerName("Sharib Saifi");
           firstCustomer.setCustomerId(101);

           CustomerDTO secondCustomer = new CustomerDTO();
           secondCustomer.setCustomerId(102);

           List<CustomerDTO> customers = List.of(firstCustomer,secondCustomer);

           when(customerService.allCustomer()).thenReturn(customers);

           mockMvc.perform(MockMvcRequestBuilders.get("/customer/allCustomers")
                           .contentType(MediaType.APPLICATION_JSON)
                           .content(objectMapper.writeValueAsString(customers)))
                   .andExpect(MockMvcResultMatchers.jsonPath("$[0].customerId",is(firstCustomer.getCustomerId())))
                   .andExpect(MockMvcResultMatchers.jsonPath("$.size()").value(customers.size()))
                   .andExpect(status().isFound())
                   .andReturn();
           log.info("Completed....");
       }
       @Test
       @DisplayName("negative, when list is empty")
       void negative_allCustomers() throws Exception{
           log.info("Starting negative allCustomer....");
           //when all customers list is empty
           when(customerService.allCustomer()).thenReturn(null);
           mockMvc.perform(MockMvcRequestBuilders.get("/customers/allCustomer")
                           .content(objectMapper.writeValueAsString(null)))
                   .andExpect(MockMvcResultMatchers.status().isNotFound())
                   .andReturn();
           log.info("Completed....");
       }
    }
    @Nested
    class SingleCustomer{
        @Test
        @DisplayName("positive, single cusotmer")
        void singleCustomer() throws Exception {
            log.info("Starting positive singleCustomer....");
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
            log.info("Completed....");
        }
        @Test
        @DisplayName("negative, when customer id is invalid or not found")
        void negative_singleCustomer() throws Exception{
            log.info("Starting negative singleCustomer....");
            //when customer id is invalid or not present
            Customer customer = new Customer();
            customer.setCustomerId(45);

            when(customerService.customer(-1)).thenReturn(null);
            mockMvc.perform(MockMvcRequestBuilders.get("/customer/singleCustomer/-1")
                            .content(objectMapper.writeValueAsString(customer)))
                    .andExpect(MockMvcResultMatchers.status().isNotFound())
                    .andReturn();
            log.info("Completed....");
        }
    }
   @Nested
   class UpdateCustomer{
       @Test
       @DisplayName("positive, update customer")
       void updateCustomer() throws Exception{
           log.info("Starting positive updateCustomer....");
           Customer customer = new Customer(1, 21, "Sharib Saifi",
                   "Savings", "SharibSaifi.SS@gmail.com", "8006590554",
                   "3334 3221 5548", "OGHPS2812E", "Muradanagar");

          CustomerDTO customerDTO = CustomerConvertor.entityToDto(customer);

           when(customerService.updateCustomerDetails(customer,customer.getCustomerId())).thenReturn(customerDTO);
           mockMvc.perform(put("/customer/updateCustomer/21")
                           .contentType(MediaType.APPLICATION_JSON)
                           .content(objectMapper.writeValueAsString(customer)))
                   .andExpect(MockMvcResultMatchers.jsonPath("$.customerName",is(customerDTO.getCustomerName())))
                   .andExpect(MockMvcResultMatchers.jsonPath("$.customerId",is(customerDTO.getCustomerId())))
                   .andExpect(status().isCreated())
                   .andReturn();
           log.info("Completed....");
       }
       @Test
       @DisplayName("negative, customer details are invalid or unsupported")
       void negative_updateCustomer() throws Exception{
           log.info("Starting negative updateCustomer....");
           //when customer id is correct but the customer details is invalid
           CustomerDTO customer = new CustomerDTO();
           customer.setCustomerId(1);

           when(customerService.updateCustomerDetails(null,1)).thenReturn(customer);
           mockMvc.perform(put("/customer/updateCustomer/1")
                           .content(objectMapper.writeValueAsString(customer)))
                   .andExpect(MockMvcResultMatchers.status().isUnsupportedMediaType())
                   .andReturn();
           log.info("Completed....");
       }
   }
    @Test
    void deleteAll() throws Exception {
        log.info("Starting deleteAll....");
        SuccessResponse response = new SuccessResponse("Delete Successfully",true);
        when(customerService.deleteAll()).thenReturn(response);

        mockMvc.perform(MockMvcRequestBuilders.delete("/customer/deleteAll")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(response)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message",is(response.getMessage())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.success",is(true)))
                .andExpect(status().isOk())
                .andReturn();
        log.info("Completed....");
    }
}