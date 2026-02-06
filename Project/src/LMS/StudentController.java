private void addStudent() {
    try {
        String name = nameField.getText();
        String email = emailField.getText();
        String phone = phoneField.getText();

        // Validate name
        if (!InputValidator.validateNotEmpty(name)) {
            showError("Student name cannot be empty");
            return;
        }

        // Validate email
        if (!InputValidator.validateEmail(email)) {
            showError("Invalid email format. Example: student@email.com");
            return;
        }

        // Validate phone
        if (!InputValidator.validatePhone(phone)) {
            showError("Invalid phone number. Must be 10–11 digits");
            return;
        }

        // Proceed with adding student to database
        Student student = new Student(name, email, phone);
        studentDAO.insert(student);
        showSuccess("Student added successfully");

    } catch (SQLException e) {
        showError("Database error: " + e.getMessage());
    } catch (Exception e) {
        showError("Unexpected error: " + e.getMessage());
    }
}
