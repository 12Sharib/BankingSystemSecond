package com.cognologix.BankingSystem.Services.ServicesImpl;

import com.cognologix.BankingSystem.Exceptions.InvalidDocument;
import com.cognologix.BankingSystem.Exceptions.MoreThanTwoAccountInOneDocument;
import com.cognologix.BankingSystem.Model.Account;
import com.cognologix.BankingSystem.Model.Customer;
import com.cognologix.BankingSystem.Repository.AccountRepo;
import com.cognologix.BankingSystem.Repository.CustomerRepository;
import com.cognologix.BankingSystem.Services.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class CustomerServiceImpl implements CustomerService {
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private Account account;
    @Autowired
    private AccountRepo accountRepo;

/*
    create customer as well as account;
 */
    @Override
    public Account createCustomer(Customer newCustomer) throws InvalidDocument {

        Random random = new Random();
        newCustomer.setCustomerAccountNumber(random.nextInt(50));
        newCustomer.setCustomerAccountBalance(1000.0);
        newCustomer.setCustomerId(random.nextInt(100));

        Customer prevCustomer = customerRepository.findBycustomerAadharNumber(newCustomer.getCustomerAadharNumber());

  //      System.out.println(prevCustomer);
        if (prevCustomer == null) {
            //new account current aur savings
            account.setAccountCustomerId(newCustomer.getCustomerId());
            account.setAccountNumber(newCustomer.getCustomerAccountNumber());
            account.setAccountName(newCustomer.getCustomerName());
            account.setAccountType(newCustomer.getCutomerAccountType());
            account.setAccountInitialBalance(newCustomer.getCustomerAccountBalance());

            customerRepository.save(newCustomer);
            accountRepo.save(account);

            return account;
        } else if (prevCustomer.getCustomerAadharNumber().equals(newCustomer.getCustomerAadharNumber())) {
            if (prevCustomer.getCutomerAccountType().equals(newCustomer.getCutomerAccountType())){
                throw new InvalidDocument("Account already exist");
            }else{
                account.setAccountCustomerId(prevCustomer.getCustomerId());
                account.setAccountNumber(newCustomer.getCustomerAccountNumber());
                account.setAccountName(newCustomer.getCustomerName());
                account.setAccountType(newCustomer.getCutomerAccountType());
                account.setAccountInitialBalance(newCustomer.getCustomerAccountBalance());

                newCustomer.setCustomerId(prevCustomer.getCustomerId());

                customerRepository.save(newCustomer);
                accountRepo.save(account);

                return account;
            }
        }
        return null;
    }
/*
    Update customer details;
 */
    @Override
    public Customer updateCustomerDetails(Customer updatedDetails,Integer accountNumber){

            Customer prevCustomer = customerRepository.findById(accountNumber).get();

            prevCustomer.setCustomerName(updatedDetails.getCustomerName());
            prevCustomer.setCustomerEmail(updatedDetails.getCustomerEmail());
            prevCustomer.setCustomerAddress(updatedDetails.getCustomerAddress());
            prevCustomer.setCustomerMobileNumber(updatedDetails.getCustomerMobileNumber());

            customerRepository.save(prevCustomer);
            Account prevAccount = accountRepo.findById(accountNumber).get();
            prevAccount.setAccountName(prevCustomer.getCustomerName());
            accountRepo.save(prevAccount);

            return prevCustomer;

    }
/*
    get all customers;
 */
    @Override
    public Iterable<Customer> getAllCustomer() {
        return customerRepository.findAll();
    }
}
