import javax.swing.*;
import java.awt.*;
import java.util.*;

public class MovieSelection extends JFrame {
    JComboBox<String> movieBox, timeBox, theatreBox;
    JTextArea descArea;
    String username;
    Map<String, String[]> movieInfo; // movie mapped to [description, rating, genre, duration]

    public MovieSelection(String username) {
        this.username = username;

        setTitle("Movie Selection");
        setSize(550, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // Movie information
        movieInfo = new HashMap<>();
        movieInfo.put("Jawan", new String[]{
                "A high-octane action thriller starring Shah Rukh Khan as a vigilante hero fighting corruption.",
                " 8.2", "Action, Thriller", "2h 45m"});
        movieInfo.put("Leo", new String[]{
                "A mysterious man with a dark past is forced to confront his demons in this Tamil blockbuster.",
                " 8.5", "Action, Drama", "2h 38m"});
        movieInfo.put("Avatar", new String[]{
                "A sci-fi epic set on Pandora where humans and the Na'vi clash in a fight for survival.",
                " 9.0", "Sci-Fi, Adventure", "2h 42m"});
        movieInfo.put("KGF", new String[]{
                "A gritty underworld saga of power and ambition set in the Kolar Gold Fields.",
                " 8.8", "Action, Crime", "2h 50m"});
        movieInfo.put("Pathaan", new String[]{
                "An Indian spy goes rogue on a globe-trotting mission to save his country.",
                " 7.9", "Action, Spy", "2h 36m"});

        // Panel for selection inputs
        JPanel topPanel = new JPanel(new GridLayout(4, 2, 10, 10));

        JLabel movieLabel = new JLabel("Select Movie:");
        movieBox = new JComboBox<>(movieInfo.keySet().toArray(new String[0]));
        movieBox.addActionListener(e -> updateMovieDetails());

        JLabel timeLabel = new JLabel("Select Time:");
        timeBox = new JComboBox<>(new String[]{"10:00 AM", "1:00 PM", "4:00 PM", "7:00 PM"});

        JLabel theatreLabel = new JLabel("Select Theatre:");
        theatreBox = new JComboBox<>(new String[]{"INOX", "Bharath Cinemas", "Ashirvad Theatre", "Cine Galaxy"});

        // Proceed button
        JButton proceedBtn = new JButton("Proceed to Seat Booking");
        proceedBtn.addActionListener(e -> {
            String selectedMovie = movieBox.getSelectedItem().toString();
            String selectedTime = timeBox.getSelectedItem().toString();
            String selectedTheatre = theatreBox.getSelectedItem().toString();

            dispose();
            new SeatBooking(username, selectedMovie, selectedTime, selectedTheatre);
        });

        // Add components to top panel
        topPanel.add(movieLabel);
        topPanel.add(movieBox);
        topPanel.add(timeLabel);
        topPanel.add(timeBox);
        topPanel.add(theatreLabel);
        topPanel.add(theatreBox);
        topPanel.add(new JLabel());
        topPanel.add(proceedBtn);

        // Text area for movie details
        descArea = new JTextArea();
        descArea.setWrapStyleWord(true);
        descArea.setLineWrap(true);
        descArea.setEditable(false);
        descArea.setFont(new Font("Arial", Font.PLAIN, 14));
        JScrollPane scrollPane = new JScrollPane(descArea);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Movie Details"));

        // Add panels to frame
        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        updateMovieDetails(); // Load details for the first movie

        setVisible(true);
    }

    // Update movie details when selection changes
    private void updateMovieDetails() {
        String selectedMovie = movieBox.getSelectedItem().toString();
        String[] info = movieInfo.getOrDefault(selectedMovie, new String[]{"No info", "-", "-", "-"});
        String fullText = String.format(
                "Description:\n%s\n\nRating: %s\nGenre: %s\nDuration: %s",
                info[0], info[1], info[2], info[3]
        );
        descArea.setText(fullText);
    }
}
