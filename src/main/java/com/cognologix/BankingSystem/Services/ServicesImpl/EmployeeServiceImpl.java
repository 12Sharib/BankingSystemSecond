package com.cognologix.BankingSystem.Services.ServicesImpl;

import com.cognologix.BankingSystem.Model.Employee;
import com.cognologix.BankingSystem.Services.EmployeeService;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class EmployeeServiceImpl implements EmployeeService {
    @Override
    public Employee createEmployee(Employee employee) {
        Random random = new Random();
        employee.setEmployeeId(random.nextInt(100));
        employee.setBranchId(random.nextInt(50));
        employee.setEmployeeLogInId(random.nextInt(500));
        employee.setEmployeeLogInIdPassword(random.toString());
        employee.setEmployeePosition("Manager Loan Department");


        return employee;
    }
}
