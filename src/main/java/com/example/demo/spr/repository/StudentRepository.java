package com.example.demo.spr.repository;


import com.example.demo.spr.Model.Student;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StudentRepository extends CrudRepository<Student, Integer> {

    Student findByName(String name);

    Optional<Student> findById(Integer id);

    Student findByAge(Integer age);

}
