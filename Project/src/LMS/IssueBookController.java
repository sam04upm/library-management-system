import util.InputValidator;

public void issueBook(String studentId, String bookId) {

    if (!InputValidator.isNotEmpty(studentId)) {
        showError("Student ID cannot be empty.");
        return;
    }

    if (!InputValidator.isNotEmpty(bookId)) {
        showError("Book ID cannot be empty.");
        return;
    }

    if (!studentDAO.exists(studentId)) {
        showError("Student ID does not exist.");
        return;
    }

    if (!bookDAO.exists(bookId)) {
        showError("Book ID does not exist.");
        return;
    }

    issueDAO.issueBook(studentId, bookId);
    showMessage("Book issued successfully.");
}