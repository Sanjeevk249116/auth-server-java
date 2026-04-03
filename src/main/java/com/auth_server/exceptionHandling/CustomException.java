package com.auth_server.exceptionHandling;

import lombok.Data;
import lombok.EqualsAndHashCode;



@EqualsAndHashCode(callSuper = true)
@Data
public class CustomException extends RuntimeException {
    private int code;

    public CustomException(String message) {
        super(message);
    }

    public CustomException(String message, int status) {
        super(message);
        this.code = status;
    }


}
