package com.ak.project.user.model;

import lombok.Value;

@Value
public class UserRequest {
    String name;
    String email;
    String gender;
    String status;
}
