package server;

import java.io.IOException;
import java.net.Socket;

public class MultiThread extends Thread {
    private final Network networkConnection; // Marked final
    private final Socket socket; // Marked final
    private final Server serverInstance; // Marked final
    private final int clientId; // Assigned through a getter
    private boolean isRunning;

    public MultiThread(Network networkConnection, Socket socket, Server serverInstance) {
        this.networkConnection = networkConnection;
        this.socket = socket;
        this.serverInstance = serverInstance;
        this.clientId = networkConnection.getId(); // Use getter to access `id`
        this.isRunning = true;
    }

    @Override
    public void run() {
        try {
            while (isRunning) {
                String input = networkConnection.receive(); // Use `receive()` instead of `read()`
                if (input != null) {
                    System.out.println("SERVER: Received from client " + clientId + ": " + input);

                    // Parse and process input from the client
                    String response = serverInstance.parseInput(input);
                    if (response != null) {
                        networkConnection.send(response); // Use `send()` instead of `write()`
                    }
                }
            }
        } catch (Exception e) { // Updated to catch generic exceptions
            System.out.println("SERVER: Error while communicating with client " + clientId + ": " + e.getMessage());
        } finally {
            disconnect();
        }
    }

    // Disconnect client and clean up
    public void disconnect() {
        isRunning = false;
        try {
            if (socket != null) {
                socket.close();
            }
        } catch (IOException e) {
            System.err.println("Error closing socket for client " + clientId + ": " + e.getMessage());
        }

        // Remove client from server connections
        serverInstance.removeID(clientId);
    }

    // Getter for client ID
    public int getClientId() {
        return clientId;
    }
}
