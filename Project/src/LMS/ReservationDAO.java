package LMS;

import java.sql.*;

public class ReservationDAO {

    public boolean createReservation(Reservation res) {
        String sql = "INSERT INTO RESERVATION (bookId, studentId, status, reservationDate) VALUES (?, ?, ?, ?)";
        try (Connection conn = Library.getInstance().makeConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            if (conn == null) return false;

            pstmt.setString(1, res.getBookId());
            pstmt.setString(2, res.getStudentId());
            pstmt.setString(3, "PENDING");
            pstmt.setTimestamp(4, new Timestamp(System.currentTimeMillis()));
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Check if user already has a pending reservation for this book
    public boolean hasPendingReservation(String bookId, String studentId) {
        String sql = "SELECT * FROM RESERVATION WHERE bookId = ? AND studentId = ? AND status = 'PENDING'";
        try (Connection conn = Library.getInstance().makeConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            if (conn == null) return false;

            pstmt.setString(1, bookId);
            pstmt.setString(2, studentId);
            ResultSet rs = pstmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Get the first person in line (FIFO)
    public Reservation getNextPendingReservation(String bookId) {
        // Derby syntax for LIMIT is FETCH FIRST ... ROWS ONLY
        String sql = "SELECT * FROM RESERVATION WHERE bookId = ? AND status = 'PENDING' ORDER BY reservationDate ASC FETCH FIRST 1 ROWS ONLY";
        try (Connection conn = Library.getInstance().makeConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            if (conn == null) return null;

            pstmt.setString(1, bookId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return new Reservation(
                    rs.getInt("reservationId"),
                    rs.getString("bookId"),
                    rs.getString("studentId"),
                    rs.getTimestamp("reservationDate"),
                    rs.getString("status")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // No reservations found
    }

    public void updateStatus(int reservationId, String newStatus) {
        String sql = "UPDATE RESERVATION SET status = ? WHERE reservationId = ?";
        try (Connection conn = Library.getInstance().makeConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            if (conn == null) return;

            pstmt.setString(1, newStatus);
            pstmt.setInt(2, reservationId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}