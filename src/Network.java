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

    public Network(ServerSocket server){

    }


    //In given code: this was the ConnectionThread Constructor
    // -- creates I/O objects on top of the socket
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

    public void peerconnection(Socket socket) {
        Network connection = new Network(nextId, socket, server);
        clientconnections.add(connection);
        connection.start();
        System.out.println("SERVER: connection received for id " + nextId + "\n");
        ++nextId;
    }

    public void listen()
    {
        try {
            // -- open the server socket
            serverSocket = new ServerSocket(getPort());

            // -- server runs until we manually shut it down
            while (true) {
                // -- block until a client comes along
                Socket socket = serverSocket.accept();

                // -- connection accepted, create a peer-to-peer socket
                //    between the server (thread) and client
                peerconnection(socket);
            }
        }
        catch (IOException e) {
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

// Does listen() go in here or Server?

//    public static void listen() {
//        try {
//            serverSocket = new ServerSocket(getPort());
//
//            while (true) {
//                Socket socket = serverSocket.accept();
//
//                peerconnection(socket);
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//            System.exit(1);
//        }
//    }


    public void run() {
        while (go) {
            try {
                String txt = datain.readLine();
                System.out.println("SERVER receive: " + txt);

                if (txt.equals("disconnect")) {
                    datain.close();
                    server.removeID(id);
                    go = false;
                } else if (txt.equals("hello")) {

                    dataout.writeBytes("world!" + "\n");
                    dataout.flush();

                } else {
                    System.out.println("unrecognized command >>" + txt + "<<");
                    dataout.writeBytes(txt + "\n");
                }
            } catch (IOException e) {
                e.printStackTrace();
                go = false;
            }
        }

    }
}

