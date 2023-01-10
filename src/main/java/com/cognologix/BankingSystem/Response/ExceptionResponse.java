package com.cognologix.BankingSystem.Response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ExceptionResponse {
    private Integer errorCode;
    private String errorMessage;
    private Boolean Success;
    private LocalDateTime errorDateTime;
}

