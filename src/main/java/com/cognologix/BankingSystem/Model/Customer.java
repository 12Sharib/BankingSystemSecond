package com.cognologix.BankingSystem.Model;

import lombok.Data;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import javax.validation.constraints.*;
import javax.xml.bind.annotation.XmlRootElement;
@Component
@XmlRootElement
@Data
@Entity
@Table
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Integer customerId;

    @NotBlank(message = "Provide Valid Name")
    private String customerName;

    @NotBlank(message = "Provide Valid Account Type")
    private String cutomerAccountType;

    @NotEmpty(message = "Provide Email")
    private String customerEmail;

    @NotBlank(message = "Provide Valid Mobile Number")
    @Size(min = 10,max = 10)
    private String customerMobileNumber;

    @NotBlank(message = "provide valid Aadhar number")
    @Size(min = 12,max = 14)
    private String customerAadharNumber;

    @NotEmpty(message = "provide valid valid Pancard")
    @Size(min = 10,max = 10)
    private String customerPancard;

    @NotBlank(message = "provide valid Address")
    @Size(min = 5,max = 30)
    private String customerAddress;



}
