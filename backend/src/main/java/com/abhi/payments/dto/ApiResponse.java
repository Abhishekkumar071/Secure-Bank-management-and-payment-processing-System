package com.abhi.payments.dto;

public record ApiResponse<T>(
        boolean success,
        String message,
        T data
)
{

}