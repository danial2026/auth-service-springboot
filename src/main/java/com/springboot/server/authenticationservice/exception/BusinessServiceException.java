package com.springboot.server.authenticationservice.exception;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import com.springboot.server.authenticationservice.enums.ErrorMessage;

@Slf4j
@Data
public class BusinessServiceException extends Exception {

    protected APIError apiError;

    public BusinessServiceException(ErrorMessage ErrorMessage) {
        apiError = new APIError(ErrorMessage);
    }

    /**
     * Replace defined logMessage with tow Stack Trace message and stick them with ',' separator into the one string.
     *
     * @param logMessage
     * @return stackTraceMessage
     */
    private String getStackTraceElementsMessage(String logMessage) {
        String stackTraceMessage = "";
        try {
            List<String> stackTraceElementsList =
                    Arrays.asList(getStackTrace())
                            .subList(1, 3)
                            .stream()
                            .map(StackTraceElement::getClassName)
                            .collect(Collectors.toList());
            Collections.reverse(stackTraceElementsList);
            stackTraceMessage = String.join(", ", stackTraceElementsList);
        } catch (Exception e) {
            //If happend any exception, return defined log message.
            stackTraceMessage = logMessage;
        }
        return stackTraceMessage;
    }

    /**
     * Convert String list into the one string
     *
     * @param errorMessagesList
     * @return error messages as one String
     */
    private String convertErrorMessagesListToString(List<String> errorMessagesList) {
        if (errorMessagesList.isEmpty()) {
            return "";
        }

        return
                errorMessagesList
                        .stream()
                        .map(String::valueOf)
                        .collect(Collectors.joining(",", "{", "}"));
    }
}