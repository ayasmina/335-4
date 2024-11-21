package server;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

public class Server {
    //  Instantiate list for active client threads
    public static Vector<Networking> clientConnections;
    //  Assign each client connection an ID
    private static int nextId = 0;
    //  Socket waits for client connections
    public static ServerSocket serversocket;
    //  Port number used for client communication
    private static final int PORT = 8000;

    public Server() {
        //  Construct the list of active client threads
        clientConnections = new Vector<>();
        //  Listen for incoming connection requests
        listen();
    }
    private void peerConnection(Socket socket) {
        //  Create a thread communication when client arrives
        Networking connection = new Networking(nextId, socket, this);
        //  Add the new thread to the active client threads list
        clientConnections.add(connection);
        //  Start the thread
        connection.start();
        //  Place some text in the area to let the server operator know what is going on
        System.out.println("SERVER: connection received for id " + nextId + "\n");
        ++nextId;
    }
    public void listen(){
        try {
            //  Open the server socket
            serversocket = new ServerSocket(Server.getPort());
            //  Server runs until we manually shut it down
            while(true) {   //  Infinite Loop Created
                //  Block until a client comes along
                Socket socket = serversocket.accept();
                //  If connection is accepted then create a peer-to-peer socket
                peerConnection(socket);
            }   //  End While (true)
        }   //  End Try
        catch(IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
    // Called by a ServerThread after a client is terminated
    public static void removeID(int id) {
        //  Find the object belonging to the client thread being terminated
        for (int i = 0; i < clientConnections.size(); ++i) {
            Networking cc = clientConnections.get(i);
            long x = cc.getId();
            if (x == id) {
                // Remove ID from the clientConnections list and the connection thread will terminate itself
                clientConnections.remove(i);
                //  Place some text in the area to let the server operator know what is going on
                System.out.println("SERVER: connection closed for client id " + id + "\n");
                break;
            }   //  End if(x == id)
        }   //  End removeID(int id)
    }
    //  Returns Sever Port
    public static int getPort() {
        return PORT;
    }
    //  Starts the server
    public static void startServer() {
        // Instantiate the server anonymously
        new Server();
        // No need to keep a reference to the object since it will run in its own thread
    }
    //  Stops the server
    public static void stopServer() {
        try {
            //  Attempts to stop the server
            serversocket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static String parseInput(String data){
        char operation;
        String result = "";
        if(data != null) {
            operation = data.charAt(0);
            if(data.length() > 1) {
                result = data.substring(1);
                switch (operation) {
                    case 0:
                        System.out.println("Connect");
                        break;
                    case 1:
                        System.out.println("Login");
                        break;
                    case 2:
                        System.out.println("Register User");
                        break;
                    case 3:
                        System.out.println("Password Recovery");
                        break;
                    case 4:
                        System.out.println("Logout");
                        break;
                    case 5:
                        System.out.println("Disconnect");
                        break;
                }   //  End Switch (operation)
            }   //  End If (data length > 1)
        }   //  End If (Data is not null)
        return result;
    }
}
