package server;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;

public class Network {
    private static final int PORT = 8000;

    private String name;
    private int id;

    // Handling peer-to-peer communication
    private BufferedReader datain;
    private DataOutputStream dataout;
    private Socket socket;

    // Client Network Object (Client Side)
    public Network(String host) {
        try {
            // Construct the peer-to-peer socket
            socket = new Socket(host, PORT);
            // Wrap the socket in stream I/O objects
            datain = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            dataout = new DataOutputStream(socket.getOutputStream());
        } catch (UnknownHostException e) {
            System.out.println("Host " + host + " at port " + PORT + " is unavailable.");
            System.exit(1);
        } catch (IOException e) {
            System.out.println("Unable to create I/O streams.");
            System.exit(1);
        }
    }

    // Server Network Object (Server Side)
    public Network(int id, Socket socket) {
        this.id = id;
        this.name = Integer.toString(id);

        try {
            datain = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            dataout = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            System.out.println("Error creating I/O streams: " + e.getMessage());
            System.exit(1);
        }
    }

    // Send message to the Server and receive the response
    public String send(String msg) {
        String rtnmsg = "";
        try {
            // Send String to Server
            dataout.writeBytes(msg + "\n");
            dataout.flush(); // Send string to server

            // Receive response from the Server
            rtnmsg = ""; // Empty string for response
            do {
                socket.setSoTimeout(5000); // Timeout of 5 seconds
                rtnmsg = datain.readLine();
            } while (rtnmsg.equals("")); // Keep checking until we get a response
        } catch (IOException e) {
            System.out.println("Error sending message: " + e.getMessage());
            System.exit(1);
        }
        return rtnmsg;
    }

    // Receive a message
    public String receive() {
        String res = "";
        try {
            res = datain.readLine();
        } catch (IOException e) {
            System.out.println("Error receiving message: " + e.getMessage());
            System.exit(1);
        }
        return res;
    }

    // Getter for ID
    public int getId() {
        return id;
    }
}
