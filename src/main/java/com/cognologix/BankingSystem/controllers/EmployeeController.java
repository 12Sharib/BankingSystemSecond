package com.cognologix.BankingSystem.controllers;

import com.cognologix.BankingSystem.Model.Employee;
import com.cognologix.BankingSystem.Services.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/Bank")
public class EmployeeController {
    @Autowired
    EmployeeService employeeService;
    @PostMapping("/createEmployee")
    public ResponseEntity<Employee> createEmployeeInBank(@RequestBody Employee employee){
        Employee newEmployee = employeeService.createEmployee(employee);
        return new ResponseEntity<>(newEmployee, HttpStatus.OK);
    }
}
