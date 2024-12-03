package server;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class MultiThread extends Thread {
    private boolean clientIsConnected;
    private int id;
    private BufferedReader datain;
    private DataOutputStream dataout;
    private Server server;
    private Socket socket;
    private Network network;

    public MultiThread(Network network, Socket socket, Server server) {
        this.network = network;
        this.socket = socket;
        this.server = server;
        clientIsConnected = true;
        // Create the stream I/O objects on top of the socket
        try {
            datain = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            dataout = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
    public void run () {
        // Server thread runs until the client terminates the connection
        while (clientIsConnected) {
            try {
                String txtOut = "";
                /*  always receives a String object with a newline (\n)
                    on the end due to how BufferedReader readLine() works.
                    The client adds it to the user's string but the BufferedReader
                    readLine() call strips it off   */

                // Using receive() instead of datain.readLine() cause...idk
                String txtIn = network.receive();
                if(txtIn != null) {
                    System.out.println("SERVER receive: " + txtIn);

                    // Process the message via the server instance
                    txtOut = server.parseInput(txtIn);

                    // If the response is valid, send it back to the client
                    if (txtOut == null || txtOut.isEmpty()) {
                        System.out.println("Response is empty.");
                        socket.setSoTimeout(5000); // prevents client from waiting for forever and ever and ever
                    } else {
                        if (txtOut.equals("5")) { // Special case for disconnect message
                            network.send("0"); // Send final response
                            disconnect();
                            System.out.println("SERVER: Disconnected client " + id);
                        } else {
                            System.out.println("SERVER responding: " + txtOut);
                            dataout.writeBytes(txtOut + "\n");
                            dataout.flush();
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
}