package LMS;

import javafx.application.Platform;
import java.util.Timer;
import java.util.TimerTask;

public class SessionManager {

    private static SessionManager instance;
    private Timer timer;
    private long lastActivityTime;
    private boolean isActive;
    private Runnable logoutHandler;

    // CONFIGURATION: 30 Minutes in milliseconds
    private static final long SESSION_TIMEOUT = 30 * 60 * 1000;
    // CHECK INTERVAL: Check every 1 minute
    private static final long CHECK_INTERVAL = 60 * 1000;

    private SessionManager() {
        // Private constructor for Singleton pattern
    }

    public static synchronized SessionManager getInstance() {
        if (instance == null) {
            instance = new SessionManager();
        }
        return instance;
    }

    /**
     * Starts the session timer.
     * @param logoutHandler The method to run when the session expires (e.g., switching to Login view).
     */
    public void startSession(Runnable logoutHandler) {
        this.logoutHandler = logoutHandler;
        this.isActive = true;
        this.lastActivityTime = System.currentTimeMillis();

        // Prevent duplicate timers
        if (timer != null) {
            timer.cancel();
        }

        timer = new Timer("SessionTimer", true); // Run as Daemon thread
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                checkInactivity();
            }
        }, CHECK_INTERVAL, CHECK_INTERVAL);

        System.out.println("[SessionManager] Session started.");
    }

    /**
     * Stops the session (used during manual logout).
     */
    public void stopSession() {
        isActive = false;
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        System.out.println("[SessionManager] Session stopped.");
    }

    /**
     * Resets the inactivity timer. Call this on user interaction.
     */
    public void updateActivity() {
        if (isActive) {
            this.lastActivityTime = System.currentTimeMillis();
        }
    }

    private void checkInactivity() {
        if (!isActive) return;

        long currentTime = System.currentTimeMillis();
        long idleTime = currentTime - lastActivityTime;

        if (idleTime > SESSION_TIMEOUT) {
            System.out.println("[SessionManager] Timeout reached. Logging out...");
            stopSession();

            // UI updates must happen on the JavaFX Application Thread
            if (logoutHandler != null) {
                Platform.runLater(logoutHandler);
            }
        }
    }
}