package LMS;

import javax.swing.*;
import java.awt.*;
import java.awt.event.AWTEventListener;

public class MainController extends JFrame {

    private AWTEventListener activityListener;

    public MainController() {
        initialize();
    }

    private void initialize() {
        setTitle("Library Management System - Dashboard");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Top Panel with Logout
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton logoutButton = new JButton("Logout");
        logoutButton.addActionListener(e -> handleLogout());
        topPanel.add(logoutButton);
        add(topPanel, BorderLayout.NORTH);

        // Center Content
        JLabel welcomeLabel = new JLabel("Welcome to the Library Management System", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 24));
        add(welcomeLabel, BorderLayout.CENTER);

        // --- CR-05 CHANGE START ---
        setupInactivityDetection();
        // --- CR-05 CHANGE END ---

        setVisible(true);
    }

    /**
     * Adds event filters to capture ALL user interaction.
     * In Swing, we use the Toolkit to listen globally, which mirrors the root node filter in JavaFX.
     */
    private void setupInactivityDetection() {
        long eventMask = AWTEvent.MOUSE_EVENT_MASK | AWTEvent.MOUSE_MOTION_EVENT_MASK | AWTEvent.KEY_EVENT_MASK;
        
        activityListener = event -> SessionManager.getInstance().updateActivity();
        
        Toolkit.getDefaultToolkit().addAWTEventListener(activityListener, eventMask);
    }

    private void handleLogout() {
        // Stop the timer manually if the user clicks Logout button
        SessionManager.getInstance().stopSession();
        
        // Close Dashboard
        dispose();
        
        // Return to Login
        new LoginController();
    }

    @Override
    public void dispose() {
        if (activityListener != null) {
            Toolkit.getDefaultToolkit().removeAWTEventListener(activityListener);
        }
        super.dispose();
    }
}