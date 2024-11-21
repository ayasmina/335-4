package server;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

public class Server {
    private static int nextId = 0;
    public Server() {
        // Construct the list of active client threads
        clientconnections = new Vector<>();
        // Listen for incoming connection requests
        listen();
    }
    private void peerConnection(Socket socket) {
        // Create a thread communication when client arrives
        Networking connection = new Networking(nextId, socket, this);
        // Add the new thread to the active client threads list
        clientconnections.add(connection);
        // Start the thread
        connection.start();
        // Place some text in the area to let the server operator know what is going on
        System.out.println("SERVER: connection received for id " + nextId + "\n");
        ++nextId;
    }
    public static void removeID(int id) {
        // Find the object belonging to the client thread being terminated
        for (int i = 0; i < clientconnections.size(); ++i) {
            Networking cc = clientconnections.get(i);
            long x = cc.getId();
            if (x == id) {
                // -- remove it from the active threads list
                //    the thread will terminate itself
                clientconnections.remove(i);
                // Place some text in the area to let the server operator know what is going on
                System.out.println("SERVER: connection closed for client id " + id + "\n");
                break;
            }   //  End if(x == id)
        }   //  End removeID(int id)
    }
    public void listen(){
        try {
            // Open the server socket
            serversocket = new ServerSocket(Server.getPort());
            // Server runs until we manually shut it down
            while(true) {  //  Infinite Loop Created
                // Block until a client comes along
                Socket socket = serversocket.accept();
                // If connection is accepted then create a peer-to-peer socket
                peerConnection(socket);
            }   //  End While (true)

        }   //  End Try
        catch(IOException e) {
            e.printStackTrace();    //  !!!POTENTIAL PROBLEM!!!
            System.exit(1);
        }
    }
    public static Vector<Networking> clientconnections;

    //  Starts the server
    public static void startServer() {
        // Instantiate the server anonymously
        new Server();
        // No need to keep a reference to the object since it will run in its own thread
    }
    public static void stopServer() {
        try {
            serversocket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    // Assign each client connection an ID
    // Socket waits for client connections
    public static ServerSocket serversocket;
    // Port number used for client communication
    private static final int PORT = 8000;
    //  Instantiate list for active client threads
    public static int getPort() {
        return PORT;
    }
    // Called by a ServerThread after a client is terminated


    public static String parseInput(String data){
        char operation;
        String result = "";
        if(data != null){
            operation = data.charAt(0);
            result = data.substring(1);
            if(operation == 'U'){
                String username = result;
                if(username.equals("testUsername")){
                    result = "validUsername";
                } else {
                    result = "wrongUsername";
                }
            } else if (operation == 'P') {
                String password = result;
                if(password.equals("testUsernametestPassword")){
                    result = "validPassword";
                } else {
                    result = "wrongPassword";
                }
            } else if (operation == 'D') {
                result = "disconnect";
            } else if (operation == 'L') {
                result = "logout";
            }
        }
        return result;
    }
}
