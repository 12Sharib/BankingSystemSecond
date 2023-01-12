package com.cognologix.BankingSystem.Model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.GenerationType;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

@Component
@XmlRootElement
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Integer customerId;

    @NotBlank(message = "Invalid Name, Provide Valid Name")
    private String customerName;

    @NotBlank(message = "Invalid Account Type, Provide Valid Account Type")
    private String cutomerAccountType;

    @NotEmpty(message = "Invalid Email, Provide valid email")
    private String customerEmail;

    @NotBlank(message = "Invalid Mobile number, should contain 10 characters only")
    @Size(min = 10,max = 10)
    private String customerMobileNumber;

    @NotBlank(message = "Invalid Aadhar number, should contain 12 characters only")
    @Size(min = 12,max = 14)
    private String customerAadharNumber;

    @NotEmpty(message = "Invalid Pancard number, should contain 10 characters only")
    @Size(min = 10,max = 10)
    private String customerPancard;

    @NotBlank(message = "Invalid Address, Provide valid address")
    @Size(min = 5,max = 30)
    private String customerAddress;

}
