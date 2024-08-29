package com.example.demo.fb.model;

import lombok.Builder;

@Builder
public class User {

    private String id;
    private String name;
    private String email;
    private AccountStatus status;
    private UserCredentials userCredentials;

}
