import java.io.*;
import java.net.*;
import java.util.*;

public class Server {
    private static final int PORT = 12345;
    private static Map<String, String> accounts = new HashMap<>(); // Mock accounts storage (username -> password)
    private static Set<String> loggedInUsers = new HashSet<>(); // Set of logged-in usernames
    private static boolean isRunning = true;

    public static void main(String[] args) {
        loadAccounts(); // Load mock accounts
        System.out.println("Server starting on port " + PORT + "...");

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server is running...");

            // Start monitoring thread
            new Thread(Server::monitorServerStatus).start();

            while (isRunning) {
                Socket clientSocket = serverSocket.accept(); // Accept a client connection
                System.out.println("New client connected: " + clientSocket.getInetAddress());
                new ClientHandler(clientSocket).start(); // Handle client in a new thread
            }
        } catch (IOException e) {
            System.out.println("Error in server: " + e.getMessage());
        }
    }

    // Mock loading accounts (username -> password)
    private static void loadAccounts() {
        accounts.put("user1", "pass1");
        accounts.put("user2", "pass2");
        accounts.put("admin", "adminpass");
    }

    // Monitor and display the server's status
    private static void monitorServerStatus() {
        Scanner scanner = new Scanner(System.in);
        while (isRunning) {
            System.out.println("\nServer Status:");
            System.out.println("-------------------------");
            System.out.println("Connection Status: Running");
            System.out.println("Accounts Available: " + accounts.size());
            System.out.println("Logged-In Accounts: " + loggedInUsers.size());
            System.out.println("Who's On: " + loggedInUsers);
            System.out.println("-------------------------");
            System.out.println("Type 'exit' to shut down the server.");
            System.out.print("> ");

            String input = scanner.nextLine();
            if (input.equalsIgnoreCase("exit")) {
                isRunning = false;
                System.out.println("Shutting down server...");
            }
        }
        scanner.close();
    }

    // Inner class to handle client communication
    private static class ClientHandler extends Thread {
        private Socket socket;

        public ClientHandler(Socket socket) {
            this.socket = socket;
        }

        public void run() {
            try (
                    BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    PrintWriter out = new PrintWriter(socket.getOutputStream(), true)
            ) {
                String username;
                String password;

                // Handle login
                out.println("Enter username:");
                username = in.readLine();

                out.println("Enter password:");
                password = in.readLine();

                if (authenticate(username, password)) {
                    loggedInUsers.add(username); // Add to logged-in users
                    out.println("Login successful! Welcome, " + username);
                    System.out.println(username + " logged in.");
                } else {
                    out.println("Login failed. Invalid username or password.");
                    System.out.println("Failed login attempt from " + socket.getInetAddress());
                    socket.close();
                    return;
                }

                // Handle client commands
                String command;
                while ((command = in.readLine()) != null) {
                    if (command.equalsIgnoreCase("logout")) {
                        loggedInUsers.remove(username); // Remove user on logout
                        out.println("Logged out successfully.");
                        System.out.println(username + " logged out.");
                        break;
                    } else {
                        out.println("Unknown command. Type 'logout' to exit.");
                    }
                }
            } catch (IOException e) {
                System.out.println("Connection error: " + e.getMessage());
            } finally {
                try {
                    socket.close();
                } catch (IOException e) {
                    System.out.println("Failed to close socket: " + e.getMessage());
                }
            }
        }

        // Authenticate the user
        private boolean authenticate(String username, String password) {
            return accounts.containsKey(username) && accounts.get(username).equals(password);
        }
    }
}
