package LMS;

import javax.swing.*;
import java.awt.*;

public class LoginController extends JFrame {

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;

    public LoginController() {
        setTitle("Library System - Login");
        setSize(350, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(3, 2, 10, 10));

        add(new JLabel("  User ID:"));
        usernameField = new JTextField();
        add(usernameField);

        add(new JLabel("  Password:"));
        passwordField = new JPasswordField();
        add(passwordField);

        loginButton = new JButton("Login");
        loginButton.addActionListener(e -> handleLogin());
        add(loginButton);

        setVisible(true);
    }

    private void handleLogin() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        if (validateUser(username, password)) {
            // Define session expiry behavior
            Runnable onSessionExpire = () -> {
                JOptionPane.showMessageDialog(null, "Session Timeout. Logging out.");
                for (Window w : Window.getWindows()) {
                    w.dispose();
                }
                new LoginController(); // Return to login
            };

            SessionManager.getInstance().startSession(onSessionExpire);
            loadDashboard();
        } else {
            JOptionPane.showMessageDialog(this, "Invalid Credentials");
        }
    }

    private boolean validateUser(String user, String pass) {
        try {
            int id = Integer.parseInt(user);
            Library lib = Library.getInstance();
            
            // Check Persons (Borrowers, Clerks)
            for (Person p : lib.getPersons()) {
                if (p.getID() == id && p.getPassword().equals(pass)) {
                    return true;
                }
            }
            
            // Check Librarian
            Librarian librarian = lib.getLibrarian();
            if (librarian != null && librarian.getID() == id && librarian.getPassword().equals(pass)) {
                return true;
            }
        } catch (NumberFormatException e) {
            // Username is not an ID
        }
        return false;
    }

    private void loadDashboard() {
        // Hide Login Window
        this.setVisible(false);
        new MainController();
    }
}