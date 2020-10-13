package com.ak.project.common;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Value;


@Value
@JsonIgnoreProperties(ignoreUnknown = true)
public class WrappedResponse<T> {
    int code;
    T data;
}
