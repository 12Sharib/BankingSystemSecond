package com.cognologix.BankingSystem.Services.ServicesImpl;

import com.cognologix.BankingSystem.Exceptions.InvalidAccountNumber;
import com.cognologix.BankingSystem.Exceptions.InvalidCustomerId;
import com.cognologix.BankingSystem.Exceptions.InvalidDocument;
import com.cognologix.BankingSystem.Model.Account;
import com.cognologix.BankingSystem.Model.Customer;
import com.cognologix.BankingSystem.Repository.AccountRepo;
import com.cognologix.BankingSystem.Repository.CustomerRepository;
import com.cognologix.BankingSystem.Response.SuccessResponse;
import com.cognologix.BankingSystem.Services.CustomerService;
import com.cognologix.BankingSystem.convertor.AccountConvertor;
import com.cognologix.BankingSystem.convertor.CustomerConvertor;
import com.cognologix.BankingSystem.dto.AccountDTO;
import com.cognologix.BankingSystem.dto.CustomerDTO;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicReference;

@Service
@Log4j2
public class CustomerServiceImpl implements CustomerService{
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private Account account;
    @Autowired
    private AccountRepo accountRepo;

    /*
    * create a customer with account details;
    * returns a customer with account details with account number and customer id;
     */
    @Override
    public AccountDTO createCustomer(Customer newCustomer) throws InvalidDocument{
        log.info("Access Create Customer Method");
        //check is there any customer in this aadhar number;
        Customer prevCustomer = customerRepository.findBycustomerAadharNumber(newCustomer.getCustomerAadharNumber());
        if (prevCustomer == null) {
            //new account current aur savings
            account = setCustomerInAccount(newCustomer);
            //set new customer customer id;
            newCustomer.setCustomerId(account.getCustomerId());
            //set customer in account
            account.setCustomer(newCustomer);
            //save account in database;
            log.info("Saved New Account In Database");
            accountRepo.save(account);
            //return new account it is savings aur current

        } else {
            if (prevCustomer.getCustomerAadharNumber().equals(newCustomer.getCustomerAadharNumber())) {
                if (prevCustomer.getCutomerAccountType().equals(newCustomer.getCutomerAccountType())) {
                    log.error("Invalid Document Provide New Document");
                    throw new InvalidDocument("Account already exist");
                } else {
                    //make newCustomer with his previous aadhar card
                    //if previous account is savings then new account will be current or reverse;
                    account = setCustomerInAccount(newCustomer);
                    //set customer id for new customer with previous customer id on savings or current
                    newCustomer.setCustomerId(prevCustomer.getCustomerId());
                    account.setCustomerId(prevCustomer.getCustomerId());
                    account.setCustomer(newCustomer);

                    log.info("Saved Account With Previous Details");
                    accountRepo.save(account);
                }
            }
        }
        return AccountConvertor.convertEntityToDTO(account);
    }

    /*
    * delete all customers
     */
    @Override
    public SuccessResponse deleteAll() {
        log.info("Access DeleteAll");
        customerRepository.deleteAll();
        return new SuccessResponse("Delete Successfully",true);
    }

    @Override
    public List<CustomerDTO> customer(Integer customerId) throws InvalidCustomerId {
        log.info("Access Single Customer Method");
        if(accountRepo.existsByCustomerId(customerId)){
            List<CustomerDTO> customerDTO = new ArrayList<>();
            customerRepository.findByCustomerId(customerId).forEach(customer -> {
                customerDTO.add(CustomerConvertor.entityToDto(customer));
            });
            log.info("return Successfully");
            return customerDTO;
        }else
            log.error("Invalid Customer Id: " + customerId);
            throw new InvalidCustomerId("Invalid Customer Id");
    }

    private Account setCustomerInAccount(Customer newCustomer){
        log.info("Making Customer In Bank");
        Random random = new Random();
        Integer customerId = random.nextInt(50);
        account.setAccountNumber(random.nextInt(50));
        account.setCustomerId(customerId);
        account.setAccountStatus("Active");
        account.setAccountName(newCustomer.getCustomerName());
        account.setAccountType(newCustomer.getCutomerAccountType());
        account.setAccountInitialBalance(1000.0);
        return account;
    }

    @Override
    public CustomerDTO updateCustomerDetails(Customer updatedDetails,Integer customerId) throws InvalidCustomerId{
        log.info("Access Update Customer Details Method");
        if(customerRepository.existsByCustomerId(customerId)) {
            List<Customer> customers = customerRepository.findByCustomerId(customerId);
            List<Customer> updatedCustomer = new ArrayList<>();
            if (customers.size() == 1) {
                customers.forEach(singleCustomer -> {
                    if (singleCustomer.getCutomerAccountType().equals(updatedDetails.getCutomerAccountType())) {
                        singleCustomer.setCustomerName(updatedDetails.getCustomerName());
                        singleCustomer.setCustomerEmail(updatedDetails.getCustomerEmail());
                        singleCustomer.setCustomerAddress(updatedDetails.getCustomerAddress());
                        singleCustomer.setCustomerMobileNumber(updatedDetails.getCustomerMobileNumber());

                        updatedCustomer.add(updatedDetails);
                        customerRepository.save(singleCustomer);
                    }else
                        log.error("For Updation Account, Provide Valid Account Type");
                        throw new InvalidDocument("Does not exist this type of account, In this customer id");
                });
                log.info("return updated Customer");
                return CustomerConvertor.entityToDto(updatedCustomer.get(0));
            } else {
                log.info("If Two Or More Accounts");
                for (Customer singleCustomer : customers) {
                    if (singleCustomer.getCutomerAccountType().equals(updatedDetails.getCutomerAccountType())) {
                        singleCustomer.setCustomerName(updatedDetails.getCustomerName());
                        singleCustomer.setCustomerEmail(updatedDetails.getCustomerEmail());
                        singleCustomer.setCustomerAddress(updatedDetails.getCustomerAddress());
                        singleCustomer.setCustomerMobileNumber(updatedDetails.getCustomerMobileNumber());

                        updatedCustomer.add(singleCustomer);
                        customerRepository.save(singleCustomer);
                    }
                }
                log.info("return updated Customer");
                return CustomerConvertor.entityToDto(updatedCustomer.get(0));
            }
        }else
            log.error("Invalid Customer Id For Update Customer");
            throw new InvalidCustomerId("Invalid customer id for Update");

    }

    /*
    * get all customers
     */
    @Override
    public List<CustomerDTO> allCustomer() {
        log.info("Access AllCustomer Method");
        List<CustomerDTO> customerDTOS = new ArrayList<>();
        customerRepository.findAll().forEach(
                customer -> customerDTOS.add(CustomerConvertor.entityToDto(customer))
        );
        return customerDTOS;
    }


}


