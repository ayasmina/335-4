package server;

import java.io.*;
import java.net.*;
import java.util.*;

public class Server {
    //  Instantiate list for active client threads
    private static Vector<MultiThread> clientConnections;
    //  Socket waits for client connections
    public static ServerSocket serversocket;
    // Int For Next Connection ID
    private int nextId = 0;
    // Port Server Will Be Listening To
    public static final int PORT = 8000;
    //public SendEmail email = new SendEmail();
    // mock accounts
    private ArrayList<User> accounts = new ArrayList<>();
    // Testing Variables - using to ensure connection is working
    User lynn = new User("lynnir", "blahblah123", "elliecavanagh002@gmail.com");
    User yasmine = new User ("yafkir", "12345678", "yafkir@gmail.com");


    // -- SERVER BUILD --
    public static void main (String args[]) {
        new Server();
    }

    public Server() {
        //  Construct the list of active client threads
        clientConnections = new Vector<>();
        accounts.add(lynn);
        accounts.add(yasmine);
        //  Listen for incoming connection requests
        listen();
    }

    //  Returns Server Port
    public static int getPort() {
        return PORT;
    }

    // Called by a ServerThread after a client is terminated
    public void removeID(int id) {
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

    // adjust this so something gets sent back to the client so they know they connected
    private void peerConnection(Socket socket) {
        //  Create a thread communication when client arrives
        Network networkConnection = new Network(nextId, socket);
        MultiThread connection = new MultiThread(networkConnection, socket, this);
        //  Add the new thread to the active client threads list
        clientConnections.add(connection);
        //  Start the thread
        connection.start();
        //  Place some text in the area to let the server operator know what is going on
        System.out.println("SERVER: connection received for id " + nextId + "\n");
        ++nextId;
    }

    // Opens Server side connection - Stays in Server!
    public void listen(){
        try {
            //  Open the server socket
            serversocket = new ServerSocket(getPort()); // listens to Port 8000
            //  Server runs until we manually shut it down
            while(true) {   //  Infinite Loop Created
                //  Block until a client comes along
                Socket socket = serversocket.accept(); // client socket is connected to server now
                //  If connection is accepted then create a peer-to-peer socket
                peerConnection(socket); // client is talking to server now
            }   //  End While (true)
        }   //  End Try
        catch(IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }


    // -- SERVER OPERATIONS --

    // test login function
    public String login(String user, String pass){ // not grabbing user properly
        String res = "";
        // if user exists
        for(int i = 0; i < accounts.size(); i++){
            User account = accounts.get(i);
            if(!user.equals(account.getUser())){ // if user doesn't exist / match
                res = "1"; // no user exists
            } else if (account.getLocked()){ // checks if acc is locked out
                res = "2"; // account is locked
                break;
            } else if (account.getLogged()){ // checks if account is already signed in
                res = "3"; // account is already signed in
                break;
            } else if (!pass.equals(account.getPass())){ // checks if pass matches
                int strikes = account.getStrikes();
                if(strikes == 3){
                    account.setLocked(true);
                    res = "2";
                } else {
                    int attempts = 3 - strikes;
                    res = "4" + strikes + ":" + attempts;
                    account.setStrikes(0);
                }
            } else { // successful login
                account.setLog(true);
                res = "0";
                break;
            }
        }
        return res;
    }

    // tester password recovery function -- working with mock accounts
    public String passRecover(String user) {
        SendEmail email = new SendEmail();
        String res = "";
        for (int i = 0; i < accounts.size(); i++) {
            User account = accounts.get(i);
            if (!user.equals(account.getUser())) {
                res = "1"; // user does not exist
            } else {
                String address = account.getEmail(); // getting user email
                String newPass = email.generateEmail(address); // sending email
                account.setPass(newPass); // setting accounts password to temp password
                res = "0"; // sending back to parseInput
                break;
            }
        }
        return res;
    }

    // tester register function
    public String register(String user, String pass, String add){
        String res = "";
        boolean userExists = false;
        for (int i = 0; i < accounts.size(); i++){
            User account = accounts.get(i);
            System.out.println(account.getUser());
            if (user.equals(account.getUser())){ // if account with user already exists
                userExists = true;
                res = "1";
                break;
            }
        }
        if (!userExists){
            User account = new User(user, pass, add); // create new user
            accounts.add(account); // add new user to arraylist
            System.out.println(accounts.size());
            res = "0"; // successful registration
        }
        return res;
    }

    // Using 0 and 1 for True and False responses in places applicable, extending beyond 0 and 1 when needed
    public String parseInput(String data){
        System.out.println("Received data: " + data);
        char operation;
        String result = "";
        String response = "";
        if(data != null) {
            operation = data.charAt(0); // grabbing operation from string
            System.out.println("1. Operation sent: " + operation);
            if(data.length() > 1) {
                //System.out.println("2. Entering if loop.");
                result = data.substring(1);
                String[] info = result.split(":");
                //System.out.println(info.length);
                //System.out.println("3. Remaining info: " + result);
                switch (operation) {
                    case '0':
                        // we wouldn't get here without the connection working so just say it's working?
                        response = "0"; // Connection successful
                        break;
                    case '1':
                        //System.out.println("Entering login case.");
                        // gathering user information from the substring
                        String user = info[0];
                        String pass = info[1];
                        //System.out.println("User Info: username - " + user + " password - " + pass);
                        // calling login function here so the response can go back to Network
                        response = login(user, pass);
                        //System.out.println(response);
                        break;
                    case '2': // needs register function in Server
                        System.out.println("Entering register user case.");
                        // Gathering registration information from the substring
                        String newUser = info[0]; // Assume info[0] contains the username
                        String newPass = info[1]; // Assume info[1] contains the password
                        String newEmail = info[2];
                        System.out.println("Registering user: username - " + newUser + ", password - " + newPass + ", email - " + newEmail);
                        // Calling register function and storing the response
                        response = register(newUser, newPass, newEmail);
                        // Optionally print or log the response for debugging purposes
                        System.out.println("Register response: " + response);
                        break;
                    case '3':
                        System.out.println("Entering passRec.");
                        String username = info[0];
                        response = passRecover(username);
                        break;
                    case '4':
                        System.out.println("Logout");
                        break;
                    case '5':
                        System.out.println("Disconnect");
                        response = info[0];
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
