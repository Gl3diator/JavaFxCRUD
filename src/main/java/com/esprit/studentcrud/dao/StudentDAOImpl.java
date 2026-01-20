package com.esprit.studentcrud.dao;

import com.esprit.studentcrud.model.Student;
import com.esprit.studentcrud.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StudentDAOImpl implements StudentDAO {

    @Override
    public List<Student> findAll() {
        List<Student> list = new ArrayList<>();
        String sql = "SELECT id, name, age, email FROM students";

        try (
                Connection c = DBConnection.getConnection();
                PreparedStatement ps = c.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()
        ) {
            while (rs.next()) {
                list.add(new Student(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getInt("age"),
                        rs.getString("email")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    @Override
    public void insert(Student s) {
        String sql = "INSERT INTO students(name, age, email) VALUES(?,?,?)";

        try (
                Connection c = DBConnection.getConnection();
                PreparedStatement ps = c.prepareStatement(sql)
        ) {
            ps.setString(1, s.getName());
            ps.setInt(2, s.getAge());
            ps.setString(3, s.getEmail());

            ps.executeUpdate();

        } catch (SQLException e) {
            // 1062 = duplicate entry (UNIQUE)
            if (e.getErrorCode() == 1062) {
                throw new RuntimeException("DUPLICATE_EMAIL");
            }
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(Student s) {
        String sql = "UPDATE students SET name=?, age=?, email=? WHERE id=?";

        try (
                Connection c = DBConnection.getConnection();
                PreparedStatement ps = c.prepareStatement(sql)
        ) {
            ps.setString(1, s.getName());
            ps.setInt(2, s.getAge());
            ps.setString(3, s.getEmail());
            ps.setInt(4, s.getId());

            ps.executeUpdate();

        } catch (SQLException e) {
            if (e.getErrorCode() == 1062) {
                throw new RuntimeException("DUPLICATE_EMAIL");
            }
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(int id) {
        String sql = "DELETE FROM students WHERE id=?";

        try (
                Connection c = DBConnection.getConnection();
                PreparedStatement ps = c.prepareStatement(sql)
        ) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean existsByEmail(String email) {
        String sql = "SELECT 1 FROM students WHERE email=? LIMIT 1";

        try (
                Connection c = DBConnection.getConnection();
                PreparedStatement ps = c.prepareStatement(sql)
        ) {
            ps.setString(1, email);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean existsByEmailExceptId(String email, int id) {
        String sql = "SELECT 1 FROM students WHERE email=? AND id<>? LIMIT 1";

        try (
                Connection c = DBConnection.getConnection();
                PreparedStatement ps = c.prepareStatement(sql)
        ) {
            ps.setString(1, email);
            ps.setInt(2, id);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
