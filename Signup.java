import javax.swing.*;
import java.awt.*;
import java.io.*;

public class Signup extends JFrame {
    JTextField usernameField;
    JPasswordField passwordField, confirmPasswordField;

    public Signup() {
        setTitle("Signup"); // Window title
        setSize(300, 250); // Window size
        setDefaultCloseOperation(EXIT_ON_CLOSE); // Close app on exit
        setLocationRelativeTo(null); // Center window
        setLayout(new GridLayout(6, 1)); // Layout with 6 rows

        usernameField = new JTextField(); // Username input
        passwordField = new JPasswordField(); // Password input
        confirmPasswordField = new JPasswordField(); // Confirm password input

        JButton signupButton = new JButton("Signup"); // Signup button
        signupButton.addActionListener(e -> signup()); // Handle signup click

        // Add components to window
        add(new JLabel("Username:"));
        add(usernameField);
        add(new JLabel("Password:"));
        add(passwordField);
        add(new JLabel("Confirm Password:"));
        add(confirmPasswordField);
        add(signupButton);

        setVisible(true); // Show the window
    }

    // Method to handle signup logic
    private void signup() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());
        String confirm = new String(confirmPasswordField.getPassword());

        // Check for empty fields
        if (username.isEmpty() || password.isEmpty() || confirm.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields are required.");
            return;
        }

        // Check if passwords match
        if (!password.equals(confirm)) {
            JOptionPane.showMessageDialog(this, "Passwords do not match.");
            return;
        }

        // Check if username already exists
        try (BufferedReader reader = new BufferedReader(new FileReader("users.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.split(",")[0].equals(username)) {
                    JOptionPane.showMessageDialog(this, "Username already exists.");
                    return;
                }
            }
        } catch (IOException ignored) {}

        // Save new user to file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("users.txt", true))) {
            writer.write(username + "," + password);
            writer.newLine();
            JOptionPane.showMessageDialog(this, "Signup successful!");
            dispose();
            new Login(); // Redirect to login screen
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
