package server;

import java.io.*;
import java.net.*;
import java.util.*;

public class Server {
    // List for active client threads
    static Vector<MultiThread> clientConnections;

    // Mapping of connection IDs to User objects
    private Map<Integer, User> connectedUsers = new HashMap<>();

    // Mapping of connection IDs to connection timestamps
    private Map<Integer, Long> connectionTimes = new HashMap<>();

    public static ServerSocket serversocket;
    private int nextId = 0;

    public static final int PORT = 8000;

    // Mock accounts
    private ArrayList<User> accounts = new ArrayList<>();
    User lynn = new User("lynnir", "blahblah123", "elliecavanagh002@gmail.com");
    User yasmine = new User("yafkir", "12345678", "yafkir@gmail.com");

    // -- SERVER BUILD --
    public static void main(String args[]) {
        new Server();
    }

    public Server() {
        clientConnections = new Vector<>();
        accounts.add(lynn);
        accounts.add(yasmine);
        listen();
    }

    // Returns Server Port
    public static int getPort() {
        return PORT;
    }

    // Method to remove a client connection by ID
    public void removeID(int id) {
        clientConnections.removeIf(cc -> cc.getId() == id);
        connectedUsers.remove(id); // Remove user
        connectionTimes.remove(id); // Remove timestamp
        System.out.println("SERVER: connection closed for client id " + id);
    }

    // Method to handle a new peer connection
    private void peerConnection(Socket socket) {
        Network networkConnection = new Network(nextId, socket);
        MultiThread connection = new MultiThread(networkConnection, socket, this);
        clientConnections.add(connection);

        // Log connection time
        connectionTimes.put(nextId, System.currentTimeMillis());

        connection.start();
        System.out.println("SERVER: connection received for id " + nextId);
        ++nextId;
    }

    // Opens server-side connection
    public void listen() {
        try {
            serversocket = new ServerSocket(getPort());
            while (true) {
                Socket socket = serversocket.accept();
                peerConnection(socket);
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    // -- USER MANAGEMENT --

    // Retrieve User by connection ID
    public User getUserByConnectionId(int id) {
        return connectedUsers.get(id);
    }

    // Retrieve connection time by connection ID
    public String getClientConnectionTime(int id) {
        Long connectionTime = connectionTimes.get(id);
        if (connectionTime == null) {
            return "N/A";
        }
        return new Date(connectionTime).toString(); // Format timestamp
    }

    // -- SERVER OPERATIONS --

    public String login(String user, String pass) {
        String res = "";
        for (int i = 0; i < accounts.size(); i++) {
            User account = accounts.get(i);
            if (!user.equals(account.getUser())) {
                res = "1"; // No user exists
            } else if (account.getLocked()) {
                res = "2"; // Account locked
                break;
            } else if (account.getLogged()) {
                res = "3"; // Already logged in
                break;
            } else if (!pass.equals(account.getPass())) {
                int strikes = account.getStrikes();
                if (strikes == 3) {
                    account.setLocked(true);
                    res = "2"; // Locked due to too many attempts
                } else {
                    int attempts = 3 - strikes;
                    res = "4" + strikes + ":" + attempts;
                    account.setStrikes(0);
                }
            } else {
                account.setLog(true);
                res = "0"; // Successful login
                connectedUsers.put(nextId - 1, account); // Link user to connection ID
                break;
            }
        }
        return res;
    }

    public String passRecover(String user) {
        SendEmail email = new SendEmail();
        String res = "";
        for (int i = 0; i < accounts.size(); i++) {
            User account = accounts.get(i);
            if (!user.equals(account.getUser())) {
                res = "1"; // User does not exist
            } else {
                String address = account.getEmail();
                String newPass = email.generateEmail(address);
                account.setPass(newPass);
                res = "0";
                break;
            }
        }
        return res;
    }

    public String register(String user, String pass, String add) {
        String res = "";
        boolean userExists = false;
        for (int i = 0; i < accounts.size(); i++) {
            User account = accounts.get(i);
            if (user.equals(account.getUser())) {
                userExists = true;
                res = "1";
                break;
            }
        }
        if (!userExists) {
            User account = new User(user, pass, add);
            accounts.add(account);
            res = "0"; // Successful registration
        }
        return res;
    }

    public String parseInput(String data) {
        System.out.println("Received data: " + data);
        char operation;
        String result = "";
        String response = "";
        if (data != null) {
            operation = data.charAt(0);
            if (data.length() > 1) {
                result = data.substring(1);
                String[] info = result.split(":");
                switch (operation) {
                    case '0':
                        response = "0"; // Connection successful
                        break;
                    case '1': // Login
                        String user = info[0];
                        String pass = info[1];
                        response = login(user, pass);
                        break;
                    case '2': // Register
                        String newUser = info[0];
                        String newPass = info[1];
                        String newEmail = info[2];
                        response = register(newUser, newPass, newEmail);
                        break;
                    case '3': // Password recovery
                        String username = info[0];
                        response = passRecover(username);
                        break;
                    case '4': // Logout
                        break;
                    case '5': // Disconnect
                        response = info[0];
                        break;
                    default:
                        response = "Error with switch loop.";
                }
            }
        }
        return response;
    }
}
