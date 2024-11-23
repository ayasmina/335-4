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
    private Networking network;
    public MultiThread(Networking network, Socket socket, Server server) {
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

                    // Sending txtIn to server instance of Server to parse the input and go through the operations
                    // txtOut is the response that parseInput returns after Server completes a process
                    txtOut = server.parseInput(txtIn);
                    if (txtOut == null || txtOut.trim().isEmpty()) {
                        System.out.println("Server response is empty!");
                    } else {
                        System.out.println("SERVER responding: " + txtOut);
                    }
                    dataout.writeBytes(txtOut + "\n");
                    dataout.flush();
                } else {
                    txtOut = "No input";
                    clientIsConnected = false;
                }

                // Sending response to client???
//               dataout.writeBytes(txtOut + "\n");
//               dataout.flush();
            }   //  End Try
            catch(IOException e) {
                e.printStackTrace();
                clientIsConnected = false;
            }
        }
    }
}
