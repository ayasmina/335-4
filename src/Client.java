import java.io.*;
import java.net.*;
import java.util.*;
import java.util.regex.*;

public class Client {
    private static String HOST;
    private static final int PORT = 8000;
    private boolean isConnected = false;
    private boolean isLoggedIn = false;
    private boolean passValid = false;
    private boolean emailValid = false;
    public Network clientConnection;
    private String response = "";

    // Tester main so we don't have to launch the GUI every single time
//    public static void main(String[] args){
//        Client client = new Client("127.0.0.1");
//        client.connect("127.0.0.1");
//        client.login("lynnir","blahblah");
//    }

    // Connect Client to Client Network Object
    public Client(String host) {
       Client.HOST = host;
       clientConnection = new Network(HOST);
    }

    public void connect(String host) {
        Client.HOST = host;
        Client client = new Client(HOST);
        if (!isConnected) { // replace this statement with something that checks the response
            isConnected = true;
            System.out.println("Connected to " + HOST);
        } else {
            System.out.println("Already connected.");
        }
    }

    public void disconnect() {
        if (isConnected) {
            isConnected = false;
            isLoggedIn = false;  // Automatically log out on disconnect
            System.out.println("Disconnected from " + HOST);

        } else {
            System.out.println("Not connected.");
        }
    }

    public String login(String username, String password) {
        String data = "1" + username + ":" + password;
        String res = "";
        String guiOut = "";
        if (isConnected && !isLoggedIn) {
            res = clientConnection.send(data);
            System.out.println("CLIENT receive: " + res);
            switch (res){
                case "0":
                    guiOut = "User successfully signed in.";
                    System.out.println(guiOut);
                    isLoggedIn = true;
                    break;
                case "1":
                    guiOut = "No username matching our records.";
                    System.out.println(guiOut);
                    break;
                case "2":
                    guiOut = "Password is incorrect.";
                    System.out.println(guiOut);
            }
        } else if (!isConnected) {
            guiOut = "Please connect to the server first.";
            System.out.println(guiOut);
        } else {
            guiOut = "Already logged in.";
            System.out.println(guiOut);
        }
        return guiOut;
    }

    public void logout() {
        if (isLoggedIn) {
            isLoggedIn = false;
            System.out.println("Logged out.");
        } else {
            System.out.println("Not logged in.");
        }
    }
 //Needs pass and email validation
    public void register(String username, String password, String email) {
        if (isConnected) {
            System.out.println("User " + username + " registered with email " + email);
        } else {
            System.out.println("Please connect to the server first.");
        }
    }
    // New recoverPassword method
    public void recoverPassword(String username) {
        if (isConnected) {
            // Simulate sending a temporary password to the user's registered email
            String tempPassword = "Temp1234"; // Temporary password for demonstration purposes
            System.out.println("Temporary password sent to the email associated with username: " + username);
            System.out.println("Temporary password: " + tempPassword); // In real applications, do not log sensitive data
        } else {
            System.out.println("Please connect to the server first.");
        }
    }

    // Connection status method
    public void printConnectionStatus() {
        System.out.println("Server: " + HOST);
        System.out.println("Connected: " + isConnected);
        System.out.println("Logged In: " + isLoggedIn);
    }
}

//once u log in
//whos on how many acc are logged in how many accounts are not/ connection status