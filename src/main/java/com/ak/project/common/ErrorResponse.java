package com.ak.project.common;

import lombok.Data;
import lombok.Value;

@Data
public class ErrorResponse {
    String field;
    String message;
}