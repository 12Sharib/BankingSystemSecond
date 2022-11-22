package com.cognologix.BankingSystem.Services;

import com.cognologix.BankingSystem.Model.Employee;
import org.springframework.stereotype.Service;

@Service
public interface EmployeeService {
    Employee createEmployee(Employee employee);
}
