package com.example.demo.fb.model;

import com.example.demo.fb.model.enums.AccountStatus;


import java.util.List;

//@Builder
//@Data
public class Member extends User {

    private List<Member> friends;
    private List<Group> groups;

    Member(String id, String name, String email, AccountStatus status, UserCredentials userCredentials) {
        super(id, name, email, status, userCredentials);
    }
}
