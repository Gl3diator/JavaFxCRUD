package com.esprit.studentcrud.dao;

import com.esprit.studentcrud.model.Student;
import java.util.List;

public interface StudentDAO {

    List<Student> findAll();

    void insert(Student s);

    void update(Student s);

    void delete(int id);
}
