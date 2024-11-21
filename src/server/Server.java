package server;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;
public class Server {
    public Server() {
        // Construct the list of active client threads
        clientconnections = new Vector<>();
        // Listen for incoming connection requests
        Networking.
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
