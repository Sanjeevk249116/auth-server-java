package com.users_micro_server.payloadResponse;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiResponse<T> {
    private int statusCode;
    private boolean status;
    private String message;

    private T data;
}
