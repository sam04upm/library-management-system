package LMS.dao;

import LMS.database.ConnectionPool;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class StudentDAO {

    public void addStudent(Student student) {

        String sql = "INSERT INTO students (name, email, phone) VALUES (?, ?, ?)";

        try (Connection conn = ConnectionPool.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, student.getName());
            ps.setString(2, student.getEmail());
            ps.setString(3, student.getPhone());
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}