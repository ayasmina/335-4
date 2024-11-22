import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.io.IOException;

public class ClientGUI {
    private Client client;
    private JFrame loginFrame;
    private JFrame registerFrame;

    public static void main(String[] args) {
        new ClientGUI();
    }

    // New BackgroundPanel class
    public class BackgroundPanel extends JPanel {
        private Image backgroundImage;

        public BackgroundPanel(String imagePath) {
            try {
                backgroundImage = Toolkit.getDefaultToolkit().getImage(imagePath);
            } catch (Exception e) {
                System.err.println("Error loading background image: " + e.getMessage());
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (backgroundImage != null) {
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            }
        }
    }

    public ClientGUI() {
        // Initialize the client
        client = new Client("localhost");

        // Setup the login frame with background image
        String loginBackgroundPath = "/Users/yasmine/Downloads/Background.jpg"; // Update the path as needed
        loginFrame = new JFrame("Login") {
            {
                setContentPane(new BackgroundPanel(loginBackgroundPath));
            }
        };
        loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        loginFrame.setSize(500, 500);
        loginFrame.setLayout(new GridBagLayout()); // Use GridBagLayout for centering
        // Show the login frame
        loginFrame.setVisible(true);

        JLabel headingLabel = new JLabel("LOGIN");
        headingLabel.setFont(new Font("Times New Roman", Font.BOLD, 24)); // Set font to bold and size 24
        headingLabel.setHorizontalAlignment(SwingConstants.CENTER); // Center-align the text
        //changing the color for login to blue thank god i finally found how
        headingLabel.setForeground(Color.BLUE);

        // Create components
        JLabel IPLabel = new JLabel("IP:");
        IPLabel.setForeground(Color.LIGHT_GRAY);
        JTextField IPField = new JTextField(20);

        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setForeground(Color.LIGHT_GRAY);

        JTextField usernameField = new JTextField(20);
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setForeground(Color.LIGHT_GRAY);
        ;
        JPasswordField passwordField = new JPasswordField(20);
        JButton loginButton = new JButton("Login");
        JLabel registerLabel = new JLabel("<html><a href='#'>Not registered? Register</a></html>");
        JButton ConectButton = new JButton("Connect");

        // GridBagLayout constraints
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(20, 20, 20, 20); // Add padding around components

        // Add heading label at the top using GridBagLayout constraints
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10); // Add padding
        gbc.gridx = 0; // Center horizontally
        gbc.gridy = 0; // Place at the top
        gbc.anchor = GridBagConstraints.CENTER; // Ensure center alignment
        loginFrame.add(headingLabel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.CENTER;
        loginFrame.add(IPLabel, gbc);

        gbc.gridx =0;
        gbc.gridy =2;
        loginFrame.add(IPField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        loginFrame.add(ConectButton, gbc);


        gbc.gridx = 0;
        gbc.gridy = 4;
        loginFrame.add(usernameLabel, gbc);

        // Add username field
        gbc.gridx = 0;
        gbc.gridy = 5;
        loginFrame.add(usernameField, gbc);

        // Add password label
        gbc.gridx = 0;
        gbc.gridy = 6;
        loginFrame.add(passwordLabel, gbc);

        // Add password field
        gbc.gridx = 0;
        gbc.gridy = 7;
        loginFrame.add(passwordLabel, gbc);

        // Add password field
        gbc.gridx = 0;
        gbc.gridy = 8;
        loginFrame.add(passwordField, gbc);

        // Add login button
        gbc.gridx = 0;
        gbc.gridy = 9;
        loginFrame.add(loginButton, gbc);

        // Add register link
        gbc.gridx = 0;
        gbc.gridy = 10;
        loginFrame.add(registerLabel, gbc);

        // Action listener for login button
        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());

                //client.connect(); // Attempt to connect first
                client.login(username, password);
                JOptionPane.showMessageDialog(loginFrame, "Logged in as " + username);
            }
        });

        // Action listener for register link
        registerLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                openRegisterWindow();
            }

        });

        ConectButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String serverIP = IPField.getText();
                client.connect(serverIP);
            }
        });


// Add heading label at the top using GridBagLayout constraints
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10); // Add padding
        gbc.gridx = 0; // Center horizontally
        gbc.gridy = 0; // Place at the top
        gbc.anchor = GridBagConstraints.CENTER; // Ensure center alignment
        loginFrame.add(headingLabel, gbc);
    }

    // Method to open the registration window
    // Method to open the registration window
    private void openRegisterWindow() {
        // Set up the registration frame with background image
        String registerBackgroundPath = "/Users/yasmine/Downloads/imagess.jpeg"; // Update the path as needed
        registerFrame = new JFrame("Register") {
            {
                setContentPane(new BackgroundPanel(registerBackgroundPath));
            }
        };
        registerFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Close only this frame when closed
        registerFrame.setSize(550, 550);
        registerFrame.setLayout(new GridBagLayout()); // Use GridBagLayout for centering
        registerFrame.setVisible(true);
        registerFrame.setLocation(500, 300); // Example position (x: 500, y: 300)

        JLabel headingLabel = new JLabel("REGISTER");
        headingLabel.setFont(new Font("Times New Roman", Font.BOLD, 24)); // Set font to bold and size 24
        headingLabel.setHorizontalAlignment(SwingConstants.CENTER); // Center-align the text
        headingLabel.setForeground(Color.BLUE); // Changing the color to gray

        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setForeground(Color.LIGHT_GRAY);

        JPasswordField passwordField = new JPasswordField(20);
        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setForeground(Color.LIGHT_GRAY);
        JTextField usernameField = new JTextField(20);
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setForeground(Color.LIGHT_GRAY);
        JTextField emailField = new JTextField(20);
        JButton registerButton = new JButton("Register");

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(20, 20, 20, 20); // Add padding around components

        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10); // Add padding
        gbc.gridx = 0; // Center horizontally
        gbc.gridy = 0; // Place at the top
        gbc.anchor = GridBagConstraints.CENTER; // Ensure center alignment
        registerFrame.add(headingLabel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        registerFrame.add(emailLabel, gbc);

        // Add email field
        gbc.gridx = 0;
        gbc.gridy = 2;
        registerFrame.add(emailField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        registerFrame.add(usernameLabel, gbc);

        // Add username field
        gbc.gridx = 0;
        gbc.gridy = 4;
        registerFrame.add(usernameField, gbc);

        // Add password label
        gbc.gridx = 0;
        gbc.gridy = 5;
        registerFrame.add(passwordLabel, gbc);

        // Add password field
        gbc.gridx = 0;
        gbc.gridy = 6;
        registerFrame.add(passwordField, gbc);

        // Add register button
        gbc.gridx = 0;
        gbc.gridy = 7;
        registerFrame.add(registerButton, gbc);

        // Action listener for register button
        registerButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());
                String email = emailField.getText();

                //client.connect(); // Attempt to connect first if not connected
                client.register(username, password, email);
                JOptionPane.showMessageDialog(registerFrame, "Registered successfully as " + username);

                // Close the registration frame
                registerFrame.dispose();
            }
        });

        // Show the registration frame
        registerFrame.setLocation(500, 300); // Example position (x: 500, y: 200)
        registerFrame.setVisible(true);
    }
    // add backround
    public class SwingDemo extends JFrame {
        Image img = Toolkit.getDefaultToolkit().getImage("/Users/yasmine/Downloads/background.jpg");

        public SwingDemo() {
            this.setContentPane(new JPanel() {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    g.drawImage(img, 0, 0, getWidth(), getHeight(), this);
                }
            });
            setSize(800, 600);
            setLocationRelativeTo(null);
            setVisible(true);
        }
    }

}
//add when user clicks enter on their keyboard it should work
// find how to make server work with client ask prof on how to do that

//use mysql workbech connect (check mark it in sql) the database add a query to the gui
//do it as seprate object
//connection thread and user interface goes to see whos logged in whos logged out talks to the database
//databse object everything static


//add IP section

