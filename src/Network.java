import java.io.*;
import java.net.*;

// Client: Send, then Receive/listen
// Server: Receive/Listen then Send

// Need to get the peerconnection and listen methods in this somehow

public class Network extends Thread {
    private static final int PORT = 8000;

    private boolean go;
    private String name;
    private int id;
    private int nextId;
    private boolean clientIsConnected = false;

    // handling peer to peer communication - I/O Streams
    private BufferedReader datain;
    private DataOutputStream dataout;

    private Server server;
    private Socket socket;

    // Create general network objects??
    // Client Network Object holds socket and I/O streams?
    // Server Network Object holds NetworkThread? - Do we need to put a NetworkThread class in here?

    // In given code: this was the public Client() method in Client.java
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
    public Network(int id, Socket socket, Server server) {
        this.server = server;
        this.id = id;
        this.name = Integer.toString(id);
        go = true;
        clientIsConnected = true;

        try {
            datain = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            dataout = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public String sendString(String msg) {
        String rtnmsg = "";

        try {
            // send String to Server
            dataout.writeBytes(msg + "\n");
            dataout.flush();

            // receive response from the Server
            rtnmsg = "";
            do {
                rtnmsg = datain.readLine();
            } while (rtnmsg.equals(""));
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
        return rtnmsg;
    }

    //    public String toString() {
//        return name;
//    }

//    public String getname() {
//        return name;
//    }


//    public void run() {
//        while (go) {
//            try {
//                String txt = datain.readLine();
//                System.out.println("SERVER receive: " + txt);
//
//                if (txt.equals("disconnect")) {
//                    datain.close();
//                    server.removeID(id);
//                    go = false;
//                } else if (txt.equals("hello")) {
//
//                    dataout.writeBytes("world!" + "\n");
//                    dataout.flush();
//
//                } else {
//                    System.out.println("unrecognized command >>" + txt + "<<");
//                    dataout.writeBytes(txt + "\n");
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//                go = false;
//            }
//        }
//
//    }
    // Pablo's run code
    public void run () {
        // Server thread runs until the client terminates the connection
        while (clientIsConnected) {
            try {
                /*  always receives a String object with a newline (\n)
                    on the end due to how BufferedReader readLine() works.
                    The client adds it to the user's string but the BufferedReader
                    readLine() call strips it off   */
                String txt = datain.readLine();
                String txtOut = Server.parseInput(txt);
                System.out.println("SERVER receive: " + txt);
                // -- if it is not the termination message, send it back adding the
                //    required (by readLine) "\n"

                //  If the disconnect string is received then close the socket, remove this thread object from the
                //  server's active client thread list, and terminate the thread
                if (txtOut.equals("disconnect")) {
                    datain.close();
                    //Server.removeID(id); // Error calling removeID()
                    clientIsConnected = false;
                }   //  End If
                else if (txtOut.equals("validUsername")) {
                    dataout.writeBytes("Valid username!" + "\n");
                    dataout.flush();
                }   //  End Else If
                else if (txtOut.equals("validPassword")) {
                    dataout.writeBytes("You have successfully logged in!");
                    dataout.flush();
                }   //  End Else If
                else if(txtOut.equals("wrongUsername")){
                    dataout.writeBytes("Unrecognized username.");
                    dataout.flush();
                }
                else if(txtOut.equals("wrongPassword")){
                    dataout.writeBytes("Incorrect password.");
                    dataout.flush();
                }
                else if(txtOut.equals("logout")){
                    dataout.writeBytes("You have successfully logged out!");
                    dataout.flush();
                }
                else {
                    System.out.println("unrecognized command >>" + txt + "<<");
                    dataout.writeBytes(txt + "\n");
                }   //  End Else
            }   //  End Try
            catch(IOException e) {
                e.printStackTrace();
                clientIsConnected = false;
            }
        }
    }
}

