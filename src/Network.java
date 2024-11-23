import java.io.*;
import java.net.*;
import java.util.*;

// Client: Send, then Receive/listen
// Server: Receive/Listen then Send
// Alternating modes

public class Network extends Thread {
    private static final int PORT = 8000;

    private boolean go;
    private String name;
    private int id;

    // handling peer to peer communication - I/O Streams
    private BufferedReader datain;
    private DataOutputStream dataout;

    private Server server;
    private Socket socket;


    // In given code: this was the public Client() method in Client.java
    // Client Network Object
    public Network(String host){
        try {
            // -- construct the peer to peer socket
            socket = new Socket(host, PORT);
            // -- wrap the socket in stream I/O objects
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

    //In given code: this was the ConnectionThread Constructor
    // -- creates I/O objects on top of the socket
    // Server Network Object
    public Network(int id, Socket socket, Server server) {
        this.server = server;
        this.id = id;
        this.name = Integer.toString(id);
        go = true;

        try {
            datain = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            dataout = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

//    public String toString() {
//        return name;
//    }

//    public String getname() {
//        return name;
//    }


    // ClientGUI -> Client -> Network -> Server -> Network -> Client -> ClientGUI
    // Client -> Network - clientConnection.send(String)
    // Network -> Server - server.parseInput(txtIn)
    // Server -> Network - the return message from parseInput()
    public String send(String msg) {
        String rtnmsg = "";
        try {
            // send String to Server
            dataout.writeBytes(msg + "\n"); // write string to bytes
            dataout.flush(); // send string to server


//            // receive response from the Server - automated - rn Server doesnt actually get the message
            rtnmsg = ""; // empty string for response
            do { // read for input while the response string is empty
                //socket.setSoTimeout(5000); // Timeout of 5 seconds - makes it so the client wont wait forever and ever if something is wrong
                rtnmsg = datain.readLine();
            } while (rtnmsg.equals(""));

        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
        return rtnmsg;
    }

    public String receive(){
        String res = "";
      try {
          res = datain.readLine();
      } catch (IOException e){
          e.printStackTrace();
          System.exit(1);
      }
      return res;
    }

    // Getting Broken Pipe Error After Pseudo Logging In
    public void run () {
        // Server thread runs until the client terminates the connection
        while (go) {
            try {
                String txtOut = "";
                /*  always receives a String object with a newline (\n)
                    on the end due to how BufferedReader readLine() works.
                    The client adds it to the user's string but the BufferedReader
                    readLine() call strips it off   */

                    // Using receive() instead of datain.readLine() cause...idk
                    String txtIn = receive();
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
                    go = false;
                }

                // Sending response to client???
//               dataout.writeBytes(txtOut + "\n");
//               dataout.flush();
            }   //  End Try
            catch(IOException e) {
                e.printStackTrace();
                go = false;
            }
        }
    }
}

