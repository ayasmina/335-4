import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.Socket;
import java.util.Vector;

public class ServerGUI {
    private JFrame serverFrame;
    private JTextArea logArea;
    private JLabel connectionStatusLabel;
    private JLabel activeConnectionsLabel;
    private JList<String> clientConnectionsList;
    private DefaultListModel<String> connectionsModel;
    private JButton startServerButton;
    private JButton stopServerButton;

    private Server server;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ServerGUI::new);
    }

    public ServerGUI() {
        setupGUI();
    }

    private void setupGUI() {
        serverFrame = new JFrame("Server Dashboard");
        serverFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        serverFrame.setSize(600, 400);
        serverFrame.setLayout(new BorderLayout());

        // Top panel: Connection Status
        JPanel topPanel = new JPanel(new GridLayout(2, 1));
        connectionStatusLabel = new JLabel("Connection Status: Stopped", SwingConstants.CENTER);
        connectionStatusLabel.setForeground(Color.RED);
        connectionStatusLabel.setFont(new Font("Arial", Font.BOLD, 16));

        activeConnectionsLabel = new JLabel("Active Connections: 0", SwingConstants.CENTER);
        activeConnectionsLabel.setFont(new Font("Arial", Font.PLAIN, 14));

        topPanel.add(connectionStatusLabel);
        topPanel.add(activeConnectionsLabel);

        // Center panel: Log area and client connections list
        JPanel centerPanel = new JPanel(new GridLayout(1, 2));

        logArea = new JTextArea();
        logArea.setEditable(false);
        JScrollPane logScrollPane = new JScrollPane(logArea);

        connectionsModel = new DefaultListModel<>();
        clientConnectionsList = new JList<>(connectionsModel);
        JScrollPane connectionsScrollPane = new JScrollPane(clientConnectionsList);

        centerPanel.add(logScrollPane);
        centerPanel.add(connectionsScrollPane);

        // Bottom panel: Start and Stop server buttons
        JPanel bottomPanel = new JPanel();
        startServerButton = new JButton("Start Server");
        stopServerButton = new JButton("Stop Server");
        stopServerButton.setEnabled(false);

        startServerButton.addActionListener(e -> startServer());
        stopServerButton.addActionListener(e -> stopServer());

        bottomPanel.add(startServerButton);
        bottomPanel.add(stopServerButton);

        // Add panels to the frame
        serverFrame.add(topPanel, BorderLayout.NORTH);
        serverFrame.add(centerPanel, BorderLayout.CENTER);
        serverFrame.add(bottomPanel, BorderLayout.SOUTH);

        serverFrame.setVisible(true);
    }

    private void startServer() {
        log("Starting server...");
        server = new Server();
        new Thread(server::listen).start(); // Start the server in a new thread
        connectionStatusLabel.setText("Connection Status: Running");
        connectionStatusLabel.setForeground(Color.GREEN);
        startServerButton.setEnabled(false);
        stopServerButton.setEnabled(true);
        log("Server started on port " + Server.PORT);
    }

    private void stopServer() {
        log("Stopping server...");
        if (server != null && Server.serversocket != null && !Server.serversocket.isClosed()) {
            try {
                Server.serversocket.close();
                connectionStatusLabel.setText("Connection Status: Stopped");
                connectionStatusLabel.setForeground(Color.RED);
                stopServerButton.setEnabled(false);
                startServerButton.setEnabled(true);
                log("Server stopped.");
            } catch (Exception e) {
                log("Error stopping server: " + e.getMessage());
            }
        }
    }

    private void log(String message) {
        logArea.append(message + "\n");
        logArea.setCaretPosition(logArea.getDocument().getLength());
    }

    public void updateConnectionList(Vector<Network> connections) {
        connectionsModel.clear();
        for (Network connection : connections) {
            connectionsModel.addElement("Client ID: " + connection.getId());
        }
        activeConnectionsLabel.setText("Active Connections: " + connections.size());
    }
}
