public class Client {
    private static String HOST;
    private boolean isConnected = false;
    private boolean isLoggedIn = false;
    private boolean passValid = false;
    private boolean emailValid = false;
    public Network clientConnection;
    String guiOut = "";

//    // Tester main so we don't have to launch the GUI every single time
//    public static void main(String[] args){
//        Client client = new Client(); // empty Client object to run commands on
//        client.connect("127.0.0.1"); // running connect - sets HOST and tells Network to connect
//        client.login("lynnir","blahblah123"); // testing login with hardcoded information
//        client.disconnect();
//    }

    // Empty Client Constructor to Build Client Object for GUI to use
    public Client() {
        HOST = "";
    }

    // Connects Client to Client Network Object -- working
    public String connect(String host) {
        this.HOST = host;
        String data = "0" + HOST;
        String res = "";
        if (isConnected == true) {
            System.out.println("Already connected");
            guiOut = "Already connected to " + HOST;
        } else {
            clientConnection = new Network(HOST); // client needs to get a response from this
            res = clientConnection.send(data); // sending connect request
            if (res == null) { // failed connection
                System.out.println("Error connecting.");
                guiOut = "Error connecting to " + HOST;
                isConnected = false;
            } else { // successful connection
                System.out.println("Successful connection.");
                guiOut = "Connection successful to " + HOST;
                isConnected = true;
            }
        }
        return guiOut;
    }

    // Works with main tester
    public String disconnect() {
        String data = "5disconnect";
        String res = "";
        if (isConnected) {
            res = clientConnection.send(data); // sending disconnect request
            System.out.println("CLIENT receive: " + res);
            if (res.equals("disconnect")){
                isConnected = false;
                isLoggedIn = false;  // Automatically log out on disconnect
                System.out.println("Disconnected from " + HOST);
                guiOut = "Disconnected from " + HOST;
            } else {
                System.out.println("Error disconnecting.");
                guiOut = "Error disconnecting from " + HOST;
            }
        } else {
            System.out.println("Not connected.");
            guiOut = "No connection found.";
        }
        return guiOut;
    }

    // Working with hard coded info
    public String login(String username, String password) {
        String data = "1" + username + ":" + password;
        String res = "";
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
                    break;
                case "3":
                    guiOut = "This account is already logged in.";
                    System.out.println(guiOut);
                    break;
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
        // Prepare data for registration request
        String data = "2" + username + ":" + password + ":" + email;
        passValid = RegexEmail.validPassword(password);
        emailValid = RegexEmail.validEmailAddress((email));
        if (isConnected) {
            // Send the registration data to the server
            String res = clientConnection.send(data);
            System.out.println("CLIENT receive: " + res);
            // Handle server response
            switch (res) {
                case "0":
                    System.out.println("User successfully registered.");
                    break;
                case "1":
                    System.out.println("Username already exists.");
                    break;
                default:
                    System.out.println("Registration failed. Please try again.");
                    break;
            }
        } else {
            System.out.println("Please connect to the server first.");
        }
    }

    // New recoverPassword method
    public void recoverPassword(String username) {
        String data = "3" + username;
        if (isConnected) {
            String res = clientConnection.send(data);
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