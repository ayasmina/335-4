package server;

import java.io.IOException;
import java.net.Socket;

public class MultiThread extends Thread {
    private boolean clientIsConnected;
    private int id;
    private Network network;
    private Server server;
    private Socket socket;

    public MultiThread(Network network, Socket socket, Server server) {
        this.network = network;
        this.socket = socket;
        this.server = server;
        this.clientIsConnected = true;
        this.id = network.getId();
    }

    @Override
    public void run() {
        // Server thread runs until the client terminates the connection
        while (clientIsConnected) {
            try {
                // Receive message from client
                String txtIn = network.receive();

                if (txtIn != null) {
                    System.out.println("SERVER received from client " + id + ": " + txtIn);

                    // Process the message via the server instance
                    String txtOut = server.parseInput(txtIn);

                    // If the response is valid, send it back to the client
                    if (txtOut == null || txtOut.isEmpty()) {
                        System.out.println("SERVER: Empty response, waiting...");
                        socket.setSoTimeout(1000); // Prevent client from waiting indefinitely
                    } else {
                        if (txtOut.equals("5")) { // Special case for disconnect message
                            network.send("0"); // Send final response
                            disconnect();
                            System.out.println("SERVER: Disconnected client " + id);
                        } else {
                            System.out.println("SERVER responding to client " + id + ": " + txtOut);
                            network.send(txtOut); // Send response back to client
                        }
                    }
                } else {
                    clientIsConnected = false; // No input, disconnect the client
                }

            } catch (IOException e) {
                System.out.println("SERVER: IOException for client " + id + ": " + e.getMessage());
                clientIsConnected = false; // In case of exception, disconnect the client
            }
        }
    }

    // Disconnect the client and clean up
    private void disconnect() {
        clientIsConnected = false;
        try {
            if (socket != null) {
                socket.close();
            }
        } catch (IOException e) {
            System.err.println("Error closing socket for client " + id + ": " + e.getMessage());
        }
        server.removeID(id); // Remove client from the server
    }

    // Getter for client ID
    public int getClientId() {
        return id;
    }
}
