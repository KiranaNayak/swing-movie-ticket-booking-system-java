import javax.swing.*;
import java.awt.*;
import java.io.*;

public class Login extends JFrame {
    JTextField usernameField;
    JPasswordField passwordField;

    public Login() {
        setTitle("Login");  // Set window title
        setSize(300, 250);  // Set window size
        setDefaultCloseOperation(EXIT_ON_CLOSE);  // Close app when window is closed
        setLocationRelativeTo(null);  // Center window on screen
        setLayout(new GridLayout(5, 1));  // Set grid layout with 5 rows and 1 column

        usernameField = new JTextField();  // Create username input field
        passwordField = new JPasswordField();  // Create password input field

        JButton loginButton = new JButton("Login");  // Create login button
        JButton signupButton = new JButton("Signup");  // Create signup button

        // Add action listeners for buttons
        loginButton.addActionListener(e -> login());
        signupButton.addActionListener(e -> {
            dispose();  // Close login window
            new Signup();  // Open signup window
        });

        // Add components to the window
        add(new JLabel("Username:"));
        add(usernameField);
        add(new JLabel("Password:"));
        add(passwordField);
        add(loginButton);
        add(signupButton);

        setVisible(true);  // Display the window
    }

    // Method to handle login logic
    private void login() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());

        try (BufferedReader reader = new BufferedReader(new FileReader("users.txt"))) {
            String line;
            // Read each line in the users file
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                // Check if the entered username and password match any record
                if (parts[0].equals(username) && parts[1].equals(password)) {
                    JOptionPane.showMessageDialog(this, "Login successful!");
                    dispose();  // Close login window
                    new MovieSelection(username);  // Open movie selection screen
                    return;
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();  // Handle any IO exceptions
        }

        JOptionPane.showMessageDialog(this, "Invalid credentials.");  // Show error if login fails
    }

    public static void main(String[] args) {
        new Login();  // Launch the login window
    }
}
