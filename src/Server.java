import java.io.*;
import java.net.*;
import java.util.*;

public class Server {
    private static final int PORT = 8000;
    private static Map<String, String> accounts = new HashMap<>(); // Mock accounts storage (username -> password)
    private static Set<String> loggedInUsers = new HashSet<>(); // Set of logged-in usernames
    private static ServerSocket serverSocket;
    private static boolean isRunning = true;
    private Vector<Network> clientconnections;
    private int nextId = 0;

    public static void main (String args[]) {
        new Server();
    }

    public int getPort() { return PORT; }

    public Server () {
        clientconnections = new Vector<Network>();
        // make a Server Network object w/ listen() and the peer connection in it?
        listen();
    }

    private void peerconnection(Socket socket) {
        Network connection = new Network(nextId, socket, this);
        clientconnections.add(connection);
        connection.start();
        System.out.println("SERVER: connection received for id " + nextId + "\n");
        ++nextId;
    }

    private void listen()
    {
        try {
            // -- open the server socket
            serverSocket = new ServerSocket(getPort());

            // -- server runs until we manually shut it down
            while (true) {
                // -- block until a client comes along
                Socket socket = serverSocket.accept();

                // -- connection accepted, create a peer-to-peer socket
                //    between the server (thread) and client
                peerconnection(socket);
            }
        }
        catch (IOException e) {
            e.printStackTrace();
            System.exit(1);

        }
    }

    public void removeID(int id) {
        // -- find the object belonging to the client thread being terminated
        for (int i = 0; i < clientconnections.size(); ++i) {
            Network cc = clientconnections.get(i);
            long x = cc.getId();
            if (x == id) {
                // -- remove it from the active threads list
                //    the thread will terminate itself
                clientconnections.remove(i);

                // -- place some text in the area to let the server operator know
                //    what is going on
                System.out.println("SERVER: connection closed for client id " + id + "\n");
                break;
            }
        }
    }

    public static String parseInput(String data){
        char operation;
        String result = "";
        if(data != null){
            operation = data.charAt(0);
            result = data.substring(1);
            if(operation == 'U'){
                String username = result;
                if(username.equals("testUsername")){
                    result = "validUsername";
                } else {
                    result = "wrongUsername";
                }
            } else if (operation == 'P') {
                String password = result;
                if(password.equals("testUsernametestPassword")){
                    result = "validPassword";
                } else {
                    result = "wrongPassword";
                }
            } else if (operation == 'D') {
                result = "disconnect";
            } else if (operation == 'L') {
                result = "logout";
            }
        }
        return result;
    }


    // Mock Client-Server Communication

//    public static void main(String[] args) {
//        //loadAccounts(); // Load mock accounts
//        System.out.println("Server starting on port " + PORT + "...");
//
//        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
//            System.out.println("Server is running...");
//
//            // Start monitoring thread
//            new Thread(Server::monitorServerStatus).start();
//
//            while (isRunning) {
//                Socket clientSocket = serverSocket.accept(); // Accept a client connection
//                System.out.println("New client connected: " + clientSocket.getInetAddress());
//                new ClientHandler(clientSocket).start(); // Handle client in a new thread
//            }
//        } catch (IOException e) {
//            System.out.println("Error in server: " + e.getMessage());
//        }
//    }

//    // Mock loading accounts (username -> password)
//    private static void loadAccounts() {
//        accounts.put("user1", "pass1");
//        accounts.put("user2", "pass2");
//        accounts.put("admin", "adminpass");
//    }

//    // Monitor and display the server's status
//    private static void monitorServerStatus() {
//        Scanner scanner = new Scanner(System.in);
//        while (isRunning) {
//            System.out.println("\nServer Status:");
//            System.out.println("-------------------------");
//            System.out.println("Connection Status: Running");
//            System.out.println("Accounts Available: " + accounts.size());
//            System.out.println("Logged-In Accounts: " + loggedInUsers.size());
//            System.out.println("Who's On: " + loggedInUsers);
//            System.out.println("-------------------------");
//            System.out.println("Type 'exit' to shut down the server.");
//            System.out.print("> ");
//
//            String input = scanner.nextLine();
//            if (input.equalsIgnoreCase("exit")) {
//                isRunning = false;
//                System.out.println("Shutting down server...");
//            }
//        }
//        scanner.close();
//    }

    // Inner class to handle client communication
//    private static class ClientHandler extends Thread {
//        private Socket socket;
//
//        public ClientHandler(Socket socket) {
//            this.socket = socket;
//        }
//
//        public void run() {
//            try (
//                    BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
//                    PrintWriter out = new PrintWriter(socket.getOutputStream(), true)
//            ) {
//                String username;
//                String password;
//
//                // Handle login
//                out.println("Enter username:");
//                username = in.readLine();
//
//                out.println("Enter password:");
//                password = in.readLine();
//
//                if (authenticate(username, password)) {
//                    loggedInUsers.add(username); // Add to logged-in users
//                    out.println("Login successful! Welcome, " + username);
//                    System.out.println(username + " logged in.");
//                } else {
//                    out.println("Login failed. Invalid username or password.");
//                    System.out.println("Failed login attempt from " + socket.getInetAddress());
//                    socket.close();
//                    return;
//                }
//
//                // Handle client commands
//                String command;
//                while ((command = in.readLine()) != null) {
//                    if (command.equalsIgnoreCase("logout")) {
//                        loggedInUsers.remove(username); // Remove user on logout
//                        out.println("Logged out successfully.");
//                        System.out.println(username + " logged out.");
//                        break;
//                    } else {
//                        out.println("Unknown command. Type 'logout' to exit.");
//                    }
//                }
//            } catch (IOException e) {
//                System.out.println("Connection error: " + e.getMessage());
//            } finally {
//                try {
//                    socket.close();
//                } catch (IOException e) {
//                    System.out.println("Failed to close socket: " + e.getMessage());
//                }
//            }
//        }

//        // Authenticate the user
//        private boolean authenticate(String username, String password) {
//            return accounts.containsKey(username) && accounts.get(username).equals(password);
//        }
//    }
}