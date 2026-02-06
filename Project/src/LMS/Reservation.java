package LMS;

import java.sql.Timestamp;

public class Reservation {
    private int reservationId;
    private String bookId;
    private String studentId;
    private Timestamp reservationDate;
    private String status;

    public Reservation(String bookId, String studentId) {
        this.bookId = bookId;
        this.studentId = studentId;
        this.status = "PENDING";
    }

    public Reservation(int reservationId, String bookId, String studentId, Timestamp reservationDate, String status) {
        this.reservationId = reservationId;
        this.bookId = bookId;
        this.studentId = studentId;
        this.reservationDate = reservationDate;
        this.status = status;
    }

    // Getters and Setters
    public int getReservationId() { return reservationId; }
    public void setReservationId(int reservationId) { this.reservationId = reservationId; }

    public String getBookId() { return bookId; }
    public void setBookId(String bookId) { this.bookId = bookId; }

    public String getStudentId() { return studentId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }

    public Timestamp getReservationDate() { return reservationDate; }
    public void setReservationDate(Timestamp reservationDate) { this.reservationDate = reservationDate; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}