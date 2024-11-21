import java.io.*;
import java.net.*;
import java.util.*;
import java.util.regex.*;

public class Client {
    private static String HOST;
    private static final int PORT = 8000;
    private boolean isConnected = false;
    private boolean isLoggedIn = false;
    private String username;
    private String password;
    private String email;
    private String serverIP;
    private boolean passValid = false;
    private boolean emailValid = false;
    private Socket socket;
    private static Network clientConnection;

    // Connect Client to Client Network Object
    public Client(String host) {
        Client.HOST = host;
       clientConnection = new Network(HOST);
    }

    public void connect() {
        if (!isConnected) {
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

    public void login(String username, String password) {
        if (isConnected && !isLoggedIn) {
            isLoggedIn = true;
            System.out.println("User " + username + " logged in.");
        } else if (!isConnected) {
            System.out.println("Please connect to the server first.");
        } else {
            System.out.println("Already logged in.");
        }
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

    public static void main(String[] args){
        String replyString2;
        Scanner kb = new Scanner(System.in);
        System.out.print("Server IP Address: ");
        String serverIP = kb.next();
        Client client = new Client(serverIP);
        System.out.print("Input Username: ");
        StringBuilder username = new StringBuilder();
        String input = kb.next();
        username.append('U').append(input);
        String replyString = clientConnection.sendString(username.toString());
        while(replyString.equals("Valid username!")){
            System.out.print("Input password: ");
            StringBuilder password = new StringBuilder();
            input = kb.next();
            password.append('P').append(username.deleteCharAt(0).append(input));
            System.out.print(username + " " + password);
            replyString2 = clientConnection.sendString(password.toString());
            System.out.println(replyString + " " + replyString2);
            replyString = "";
        }
        kb.close();
    }
}

//once u log in
//whos on how many acc are logged in how many accounts are not/ connection status

