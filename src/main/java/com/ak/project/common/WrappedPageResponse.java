package com.ak.project.common;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Value;
import java.util.List;

@Value
@JsonIgnoreProperties(ignoreUnknown = true)
public class WrappedPageResponse<T> {
    int code;
    List<T> data;
}
