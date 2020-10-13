package com.ak.project.user.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Value;

@Value
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserResponse {
    long id;
    String name;
    String email;
    String gender;
    String status;
    @JsonAlias("created_at")
    String createdAt;
    @JsonAlias("updated_at")
    String updatedAt;
}
