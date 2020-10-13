package com.ak.project;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ServiceException extends RuntimeException {
    private static final long serialVersionUID = 5548744981005277678L;

    int errorCode;
    String errorMessage;
    int statusCode;
}
