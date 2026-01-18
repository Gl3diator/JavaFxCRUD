package com.esprit.studentcrud.util;

import com.esprit.studentcrud.dao.StudentDAO;
import com.esprit.studentcrud.dao.StudentDAOImpl;
import com.esprit.studentcrud.model.Student;

public class TestDB {
    public static void main(String[] args) {

        StudentDAO dao = new StudentDAOImpl();

        System.out.println(" Students table:");
        for (Student s : dao.findAll()) {
            System.out.println(s);
        }
    }
}
