package LMS;

import javax.swing.*;
import java.awt.*;
import java.util.Date;

public class ReturnBookController extends JFrame {

    private JTextField bookIdField;
    private JButton returnButton;
    private JButton closeButton;

    private ReservationDAO reservationDAO = new ReservationDAO();

    public ReturnBookController() {
        setTitle("Return Book");
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(3, 2, 10, 10));

        add(new JLabel("  Book ID:"));
        bookIdField = new JTextField();
        add(bookIdField);

        add(new JLabel("")); // Spacer

        returnButton = new JButton("Return Book");
        returnButton.addActionListener(e -> handleReturnBook());
        add(returnButton);

        closeButton = new JButton("Close");
        closeButton.addActionListener(e -> dispose());
        add(closeButton);

        setVisible(true);
    }

    private void handleReturnBook() {
        String bookIdStr = bookIdField.getText().trim();

        if (bookIdStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter Book ID.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int bookId;
        try {
            bookId = Integer.parseInt(bookIdStr);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Book ID must be numeric.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // 1. Find the active loan for this book
        Loan activeLoan = findActiveLoan(bookId);
        if (activeLoan == null) {
            JOptionPane.showMessageDialog(this, "This book is not currently issued.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // 2. Process Standard Return Logic
        // Note: We implement logic here instead of calling Book.returnBook() to avoid console Scanner blocking the GUI
        
        // Calculate Fine
        double fine = activeLoan.computeFine1();
        if (fine > 0) {
            int response = JOptionPane.showConfirmDialog(this, 
                "Total Fine: Rs " + fine + "\nDo you want to pay now?", 
                "Fine Payment", JOptionPane.YES_NO_OPTION);
            
            activeLoan.setFineStatus(response == JOptionPane.YES_OPTION);
        } else {
            activeLoan.setFineStatus(true); // No fine implies paid/cleared
        }

        // Update Loan and Book records
        activeLoan.getBook().setIssuedStatus(false);
        activeLoan.setReturnedDate(new Date());
        
        // Set Receiver (Default to Librarian if available, or generic Staff)
        Staff receiver = Library.getInstance().getLibrarian();
        activeLoan.setReceiver(receiver);

        // Remove from Borrower's current loans
        activeLoan.getBorrower().removeBorrowedBook(activeLoan);

        // 3. CR-06 MODIFICATION: Automatic Fulfillment Logic
        Reservation pendingRes = reservationDAO.getNextPendingReservation(String.valueOf(bookId));

        if (pendingRes != null) {
            // CASE A: Someone is waiting for this book
            
            // 1. Mark the reservation as FULFILLED
            reservationDAO.updateStatus(pendingRes.getReservationId(), "FULFILLED");
            
            // 2. Notify the Librarian (Book remains 'Available' in system but physically reserved)
            String waitingStudent = pendingRes.getStudentId();
            String alertMsg = "Book Returned Successfully.\n\n" +
                              "ALERT: This book is RESERVED for Student ID: " + waitingStudent + "\n" +
                              "Do not shelve! Place on Hold Shelf.";
            
            JOptionPane.showMessageDialog(this, alertMsg, "Reservation Alert", JOptionPane.WARNING_MESSAGE);
            
        } else {
            // CASE B: No one is waiting
            JOptionPane.showMessageDialog(this, "Book returned successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
        }
        
        bookIdField.setText("");
    }

    private Loan findActiveLoan(int bookId) {
        // Iterate through all borrowers to find who has this book
        Library lib = Library.getInstance();
        for (Person p : lib.getPersons()) {
            if (p instanceof Borrower) {
                Borrower b = (Borrower) p;
                for (Loan l : b.getBorrowedBooks()) {
                    if (l.getBook().getID() == bookId) {
                        return l;
                    }
                }
            }
        }
        return null;
    }
}