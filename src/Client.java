import java.io.IOException;

public class Client {
    private static String HOST;
    private boolean isConnected = false;
    private boolean isLoggedIn = false;
    private boolean passValid = false;
    private boolean emailValid = false;
    public Network clientConnection;
    private String response = "";

    // Tester main so we don't have to launch the GUI every single time
    public static void main(String[] args){
        Client client = new Client("127.0.0.1");
        client.connect("127.0.0.1");
        client.login("lynnir","blahblah");
    }

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

    public void login(String username, String password) {
        String data = "1" + username + ":" + password;
        String res = "";
        if (isConnected && !isLoggedIn) {
            isLoggedIn = true;
            res = clientConnection.send(data);
            System.out.println("CLIENT receive: " + res);
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
}
//add forgot password ??????!!!!!!!
//once u log in
//whos on how many acc are logged in how many accounts are not/ connection status