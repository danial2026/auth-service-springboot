package com.springboot.server.authenticationservice.exception;

//import com.springboot.server.parent.enums.ErrorMessage;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

import com.springboot.server.authenticationservice.enums.ErrorMessage;

@Data
@Slf4j
public class APIError {
    private HttpStatus status;
    private String message;


    // private APIError(ErrorMessage ErrorMessage) {
    //     this.status = HttpStatus.INTERNAL_SERVER_ERROR;
    //     this.code = ErrorMessage.getMessage();
    //     // this.message = message;
    // }

    APIError(ErrorMessage ErrorMessage) {
        this.status = HttpStatus.INTERNAL_SERVER_ERROR;
        this.message = ErrorMessage.getMessage();
    }

    @Override
    public String toString() {
        return "ApiError{" +
                "status=" + status +
                ", message=" + message +
                '}';
    }

}