public class Client {
    private String host;
    private boolean isConnected = false;
    private boolean isLoggedIn = false;

    public Client(String host) {
        this.host = host;
    }

    public void connect() {
        if (!isConnected) {
            isConnected = true;
            System.out.println("Connected to " + host);
        } else {
            System.out.println("Already connected.");
        }
    }

    public void disconnect() {
        if (isConnected) {
            isConnected = false;
            isLoggedIn = false;  // Automatically log out on disconnect
            System.out.println("Disconnected from " + host);
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