import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.*;
import java.util.*;

public class ServerGUI {
    private JFrame serverFrame;
    private JTextArea logArea;
    private JLabel connectionStatusLabel;
    private JLabel totalAccountsLabel;
    private JLabel loggedInUsersLabel;
    private JList<String> loggedInUsersList;
    private DefaultListModel<String> loggedInUsersModel;
    private JButton stopServerButton;

    private static final int PORT = 12345;
    private Map<String, String> accounts = new HashMap<>(); // Mock accounts storage (username -> password)
    private Set<String> loggedInUsers = new HashSet<>(); // Set of logged-in usernames
    private ServerSocket serverSocket;
    private boolean isRunning = true;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ServerGUI::new);
    }

    public ServerGUI() {
        setupGUI();
        loadAccounts();
        //startServer();
    }

    private void setupGUI() {
        // Main server frame
        serverFrame = new JFrame("Server Dashboard");
        serverFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        serverFrame.setSize(500, 400);
        serverFrame.setLayout(new BorderLayout());

        // Top panel: Connection Status
        JPanel topPanel = new JPanel(new GridLayout(3, 1));
        connectionStatusLabel = new JLabel("Connection Status: Running", SwingConstants.CENTER);
        connectionStatusLabel.setForeground(Color.GREEN);
        connectionStatusLabel.setFont(new Font("Arial", Font.BOLD, 16));

        totalAccountsLabel = new JLabel("Total Accounts Registered: 0", SwingConstants.CENTER);
        loggedInUsersLabel = new JLabel("Logged In Users: 0", SwingConstants.CENTER);

        topPanel.add(connectionStatusLabel);
        topPanel.add(totalAccountsLabel);
        topPanel.add(loggedInUsersLabel);

        // Center panel: Log area and logged-in users list
        JPanel centerPanel = new JPanel(new GridLayout(1, 2));

        logArea = new JTextArea();
        logArea.setEditable(false);
        JScrollPane logScrollPane = new JScrollPane(logArea);

        loggedInUsersModel = new DefaultListModel<>();
        loggedInUsersList = new JList<>(loggedInUsersModel);
        JScrollPane usersScrollPane = new JScrollPane(loggedInUsersList);

        centerPanel.add(logScrollPane);
        centerPanel.add(usersScrollPane);

        // Bottom panel: Stop server button
        JPanel bottomPanel = new JPanel();
        stopServerButton = new JButton("Stop Server");
        //stopServerButton.addActionListener(e -> stopServer());
        bottomPanel.add(stopServerButton);

        // Add panels to the frame
        serverFrame.add(topPanel, BorderLayout.NORTH);
        serverFrame.add(centerPanel, BorderLayout.CENTER);
        serverFrame.add(bottomPanel, BorderLayout.SOUTH);

        serverFrame.setVisible(true);
    }

    private void loadAccounts() {
        // Mock loading accounts (username -> password)
        accounts.put("user1", "pass1");
        accounts.put("user2", "pass2");
        accounts.put("admin", "adminpass");

        updateAccountCount();
    }

    private void updateAccountCount() {
        totalAccountsLabel.setText("Total Accounts Registered: " + accounts.size());
    }

    private void updateLoggedInCount() {
        loggedInUsersLabel.setText("Logged In Users: " + loggedInUsers.size());
    }
}
