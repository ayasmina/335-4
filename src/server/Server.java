package server;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

public class Server {
    //  Instantiate list for active client threads
    public static Vector<MultiThread> clientConnections;
    //  Assign each client connection an ID
    private static int nextId = 0;
    //  Socket waits for client connections
    public static ServerSocket serversocket;
    //  Port number used for client communication
    private static final int PORT = 8000;
    private Server server;

    public Server() {
        //  Construct the list of active client threads
        clientConnections = new Vector<>();
        //  Listen for incoming connection requests
        listen();
    }
    private void peerConnection(Socket socket) {
        Networking networkConnection = new Networking(nextId, socket, this);
        //  Create a thread communication when client arrives
        MultiThread connection = new MultiThread(networkConnection, socket, this);
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
            MultiThread cc = clientConnections.get(i);
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

    // Testing Variables - using to ensure connection is working
    private String username = "lynnir";
    private String password;
    // test login function
    public String login(String user, String pass){
        String res = "";
        if (user.equals(this.username)){ // if the input equals the hard-coded username
            res = user + " Logged In Successfully.";
        } else {
            res = "Error Logging In.";
        }
        return res;
    }
    public String parseInput(String data){
        System.out.println("Received data: " + data);
        char operation;
        String result = "";
        String response = "";
        if(data != null) {
            operation = data.charAt(0); // grabbing operation from string
            //System.out.println("1. Operation sent: " + operation);
            if(data.length() > 1) {
                //System.out.println("2. Entering if loop.");
                result = data.substring(1);
                String[] info = result.split(":");
                //System.out.println(info.length);
                //System.out.println("3. Remaining info: " + result);

                switch (operation) {
                    case '0':
                        System.out.println("entering connect case.");
                        System.out.println("Connect");
                        break;
                    case '1':
                        System.out.println("Entering login case.");
                        // gathering user information from the substring
                        String user = info[0];
                        String pass = info[1];
                        System.out.println("User Info: username - " + user + " password - " + pass);
                        // calling login function here so the response can go back to Network
                        response = login(user, pass);
                        //System.out.println(response);
                        break;
                    case '2':
                        System.out.println("Register User");
                        break;
                    case '3':
                        System.out.println("Password Recovery");
                        break;
                    case '4':
                        System.out.println("Logout");
                        break;
                    case '5':
                        System.out.println("Disconnect");
                        break;
                    default : // in case it's not entering a case for some reason so we know
                        response = ("Error with switch loop.");
                }   //  End Switch (operation)
            }   //  End If (data length > 1)
        }   //  End If (Data is not null)
        //System.out.println("SERVER sending: " + response);
        return response;
    }
}
