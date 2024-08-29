package com.example.demo.spr.Model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@Entity(name = "student")
public class Student {

    @Id
    Long id;
    String name;
    String lastName;
    String universityId;
    Integer age;
    String email;
}
