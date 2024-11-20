import java.io.IOException;
import java.net.Socket;

public class Client {
    private String HOST;
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

    public Client(String host) {
        try {
            this.HOST = host;
            socket = new Socket(HOST, PORT);
        } catch (UnknownHostException | IOException e) {
            System.out.println("Host: " + HOST + " at port " + PORT + " + is unavailable.");
        }
    }
}

//    public void connect() {
//        if (!isConnected) {
//            isConnected = true;
//            System.out.println("Connected to " + HOST);
//        } else {
//            System.out.println("Already connected.");
//        }
//    }

//    public void disconnect() {
//        if (isConnected) {
//            isConnected = false;
//            isLoggedIn = false;  // Automatically log out on disconnect
//            System.out.println("Disconnected from " + HOST);
//
//        } else {
//            System.out.println("Not connected.");
//        }
//    }

//    public void login(String username, String password) {
//        if (isConnected && !isLoggedIn) {
//            isLoggedIn = true;
//            System.out.println("User " + username + " logged in.");
//        } else if (!isConnected) {
//            System.out.println("Please connect to the server first.");
//        } else {
//            System.out.println("Already logged in.");
//        }
//    }

//    public void logout() {
//        if (isLoggedIn) {
//            isLoggedIn = false;
//            System.out.println("Logged out.");
//        } else {
//            System.out.println("Not logged in.");
//        }
//    }
// Needs pass and email validation
//    public void register(String username, String password, String email) {
//        if (isConnected) {
//            System.out.println("User " + username + " registered with email " + email);
//        } else {
//            System.out.println("Please connect to the server first.");
//        }
//    }
//}
//add forgot password ??????!!!!!!!
//once u log in
//whos on how many acc are logged in how many accounts are not/ connection status