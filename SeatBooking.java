import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.*;
public class SeatBooking extends JFrame {
    String username, movie, time, theatre;
    JButton[][] seatButtons = new JButton[5][5];
    Set<String> selectedSeats = new HashSet<>();
    Map<String, Integer> moviePrices;

    public SeatBooking(String username, String movie, String time, String theatre) {
        this.username = username;
        this.movie = movie;
        this.time = time;
        this.theatre = theatre;

        // Set movie prices
        moviePrices = new HashMap<>();
        moviePrices.put("Jawan", 150);
        moviePrices.put("Leo", 180);
        moviePrices.put("Avatar", 200);
        moviePrices.put("KGF", 160);
        moviePrices.put("Pathaan", 170);

        setTitle("Select Seats");
        setSize(500, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel seatPanel = new JPanel(new GridLayout(5, 5));
        loadSeats(seatPanel); // load seat buttons into panel

        JButton payButton = new JButton("Proceed to Payment");
        payButton.setFont(new Font("Arial", Font.BOLD, 16));
        payButton.addActionListener(e -> showConfirmation());

        add(seatPanel, BorderLayout.CENTER);
        add(payButton, BorderLayout.SOUTH);
        setVisible(true);
    }

    // Load seats and mark booked/unbooked
    private void loadSeats(JPanel panel) {
        Set<String> booked = getBookedSeats(); // get already booked seats
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                String seat = (char) ('A' + i) + "" + (j + 1);
                JButton btn = new JButton(seat);
                if (booked.contains(seat)) {
                    btn.setBackground(Color.RED); // booked seats shown in red
                    btn.setEnabled(false);
                } else {
                    btn.setBackground(Color.GREEN); // available seats shown in green
                    btn.addActionListener(e -> {
                        if (selectedSeats.contains(seat)) {
                            selectedSeats.remove(seat); // unselect seat
                            btn.setBackground(Color.GREEN);
                        } else {
                            selectedSeats.add(seat); // select seat
                            btn.setBackground(Color.BLUE);
                        }
                    });
                }
                seatButtons[i][j] = btn;
                panel.add(btn);
            }
        }
    }

    // Read and return already booked seats from file
    private Set<String> getBookedSeats() {
        Set<String> booked = new HashSet<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("bookings.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                // Match movie, time, and theatre to find booked seats
                if (parts[1].equals(movie) && parts[2].equals(time) && parts[3].equals(theatre)) {
                    booked.addAll(Arrays.asList(parts).subList(4, parts.length));
                }
            }
        } catch (IOException ignored) {}
        return booked;
    }

    // Show confirmation popup before payment
    private void showConfirmation() {
        if (selectedSeats.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please select at least one seat.");
            return;
        }

        int pricePerSeat = moviePrices.getOrDefault(movie, 150);
        int totalAmount = selectedSeats.size() * pricePerSeat;

        StringBuilder sb = new StringBuilder();
        sb.append("Please confirm your booking:\n\n");
        sb.append("Username: ").append(username).append("\n");
        sb.append("Movie: ").append(movie).append("\n");
        sb.append("Time: ").append(time).append("\n");
        sb.append("Theatre: ").append(theatre).append("\n");
        sb.append("Seats: ").append(String.join(", ", selectedSeats)).append("\n");
        sb.append("Price per seat: ₹").append(pricePerSeat).append("\n");
        sb.append("Total amount: ₹").append(totalAmount).append("\n\n");
        sb.append("Do you want to proceed with payment?");

        int choice = JOptionPane.showConfirmDialog(this, sb.toString(), "Confirm Payment", JOptionPane.YES_NO_OPTION);

        if (choice == JOptionPane.YES_OPTION) {
            completeBooking();
        }
    }
    // Save booking and ask what to do next
    private void completeBooking() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("bookings.txt", true))) {
            writer.write(username + "," + movie + "," + time + "," + theatre);
            for (String seat : selectedSeats) {
                writer.write("," + seat);
            }
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Show options after successful payment
        int option = JOptionPane.showOptionDialog(
            this,
            "Payment successful!\nBooked seats: " + String.join(", ", selectedSeats)
                + "\n\nWhat would you like to do next?",
            "Booking Successful",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.INFORMATION_MESSAGE,
            null,
            new String[]{"Book Another Ticket", "Exit"},
            "Book Another Ticket"
        );
        dispose();
        if (option == JOptionPane.YES_OPTION) {
            new MovieSelection(username); // open movie selection again
        } else {
            System.exit(0); // exit app
        }
    }
}
