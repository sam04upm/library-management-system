package LMS;

import javax.swing.*;
import java.awt.*;

public class ReservationController extends JFrame {

    private JTextField bookIdField;
    private JTextField studentIdField;
    private JButton reserveButton;
    private JButton closeButton;

    private ReservationDAO reservationDAO = new ReservationDAO();

    public ReservationController() {
        setTitle("Reserve Book");
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(3, 2, 10, 10));

        add(new JLabel("  Book ID:"));
        bookIdField = new JTextField();
        add(bookIdField);

        add(new JLabel("  Student ID:"));
        studentIdField = new JTextField();
        add(studentIdField);

        reserveButton = new JButton("Reserve");
        reserveButton.addActionListener(e -> handleReserveBook());
        add(reserveButton);

        closeButton = new JButton("Close");
        closeButton.addActionListener(e -> dispose());
        add(closeButton);

        setVisible(true);
    }

    private void handleReserveBook() {
        String bookIdStr = bookIdField.getText().trim();
        String studentIdStr = studentIdField.getText().trim();

        if (bookIdStr.isEmpty() || studentIdStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter both Book ID and Student ID.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Validate IDs are numeric
        int bookId;
        try {
            bookId = Integer.parseInt(bookIdStr);
            Integer.parseInt(studentIdStr); // Validate student ID format
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "IDs must be numeric.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // 1. Validation: Can only reserve if book is currently BORROWED (Issued)
        Book book = findBookById(bookId);
        if (book == null) {
            JOptionPane.showMessageDialog(this, "Book not found.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!book.getIssuedStatus()) {
            JOptionPane.showMessageDialog(this, "This book is currently available. You can borrow it directly.", "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        // 2. Validation: Check if already reserved by this student
        if (reservationDAO.hasPendingReservation(bookIdStr, studentIdStr)) {
            JOptionPane.showMessageDialog(this, "You already have a pending reservation for this book.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // 3. Create Reservation
        Reservation newRes = new Reservation(bookIdStr, studentIdStr);
        if (reservationDAO.createReservation(newRes)) {
            JOptionPane.showMessageDialog(this, "Book reserved successfully! You are in the queue.", "Success", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to reserve book.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private Book findBookById(int id) {
        Library lib = Library.getInstance();
        for (Book b : lib.getBooks()) {
            if (b.getID() == id) {
                return b;
            }
        }
        return null;
    }
}