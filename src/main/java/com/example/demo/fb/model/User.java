package com.example.demo.fb.model;

import com.example.demo.fb.model.enums.AccountStatus;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.SuperBuilder;

@Builder
public class User {

    private String id;
    private String name;
    private String email;
    private AccountStatus status;
    private UserCredentials userCredentials;

}
