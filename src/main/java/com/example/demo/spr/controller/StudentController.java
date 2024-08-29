package com.example.demo.spr.controller;

import com.example.demo.spr.Model.Student;
import com.example.demo.spr.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController(value = "/student")
public class StudentController {

    @Autowired
    StudentRepository studentRepository;

    @RequestMapping(value = "/getStudent", method = RequestMethod.GET)
    public Student getStudent(Integer id) {
        return studentRepository.findById(id).get();
    }

    @RequestMapping(value = "/putStudent", method = RequestMethod.POST)
    public void putStudent(Student student) {
        studentRepository.save(student);
    }

}
