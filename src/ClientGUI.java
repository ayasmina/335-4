import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;

public class ClientGUI {
    private Client client;
    private JFrame loginFrame;
    private JFrame ForgottenpassFrame;
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
        loginFrame.setVisible(true);

        JLabel headingLabel = new JLabel("LOGIN");
        headingLabel.setFont(new Font("Times New Roman", Font.BOLD, 24));
        headingLabel.setHorizontalAlignment(SwingConstants.CENTER);
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
        JPasswordField passwordField = new JPasswordField(20);

        JButton loginButton = new JButton("Login");
        JLabel registerLabel = new JLabel("<html><a href='#'>Not registered? Register</a></html>");
        JLabel ForgottenpassLabel = new JLabel("<html><a href='#'>Forgot Password?</a></html>");
        JButton ConectButton = new JButton("Connect");

        // GridBagLayout constraints
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(20, 20, 20, 20);
        gbc.insets = new Insets(10, 10, 10, 10); // Add padding

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        loginFrame.add(headingLabel, gbc);

        gbc.gridy++;
        loginFrame.add(IPLabel, gbc);

        gbc.gridy++;
        loginFrame.add(IPField, gbc);

        gbc.gridy++;
        loginFrame.add(ConectButton, gbc);

        gbc.gridy++;
        loginFrame.add(usernameLabel, gbc);

        gbc.gridy++;
        loginFrame.add(usernameField, gbc);

        gbc.gridy++;
        loginFrame.add(passwordLabel, gbc);

        gbc.gridy++;
        loginFrame.add(passwordField, gbc);

        gbc.gridy++;
        loginFrame.add(loginButton, gbc);

        gbc.gridy++;
        loginFrame.add(ForgottenpassLabel, gbc);

        gbc.gridy++;
        loginFrame.add(registerLabel, gbc);

        // Action listener for login button
        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());

                if (!username.isEmpty() && !password.isEmpty()) {
                    client.connect(); // Attempt to connect first
                    client.login(username, password);
                    JOptionPane.showMessageDialog(loginFrame, "Logged in as " + username);
                } else {
                    JOptionPane.showMessageDialog(loginFrame, "Please fill in all fields!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Action listener for Forgotten Password link
        ForgottenpassLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                openForgottenPassWindow();
            }
        });

        // Action listener for Register link
        registerLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                openRegisterWindow();
            }
        });

        // Add Enter key listener to Login button
        loginFrame.getRootPane().setDefaultButton(loginButton);
    }

    private void openForgottenPassWindow() {
        String forgottenPassBackgroundPath = "/Users/yasmine/Downloads/imagess.jpeg"; // Update as needed
        ForgottenpassFrame = new JFrame("Recovery");
        ForgottenpassFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        ForgottenpassFrame.setContentPane(new BackgroundPanel(forgottenPassBackgroundPath));
        ForgottenpassFrame.setSize(500, 500);
        ForgottenpassFrame.setLayout(new GridBagLayout());

        JLabel headingLabel = new JLabel("RECOVERY");
        headingLabel.setFont(new Font("Times New Roman", Font.BOLD, 24));
        headingLabel.setForeground(Color.BLUE);

        JTextField usernameField = new JTextField(20);
        JButton recoverButton = new JButton("Recover");

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        gbc.gridx = 0;
        gbc.gridy = 0;
        ForgottenpassFrame.add(headingLabel, gbc);

        gbc.gridy++;
        ForgottenpassFrame.add(new JLabel("Username:"), gbc);

        gbc.gridy++;
        ForgottenpassFrame.add(usernameField, gbc);

        gbc.gridy++;
        ForgottenpassFrame.add(recoverButton, gbc);

        recoverButton.addActionListener((ActionEvent e) -> {
            String username = usernameField.getText();
            if (!username.isEmpty()) {
                client.connect();
                client.recoverPassword(username);
                JOptionPane.showMessageDialog(ForgottenpassFrame, "Recovery email sent to your registered email.");
            } else {
                JOptionPane.showMessageDialog(ForgottenpassFrame, "Please enter a username!", "Error", JOptionPane.ERROR_MESSAGE);
            }
            ForgottenpassFrame.dispose();
        });

        ForgottenpassFrame.setVisible(true);
        ForgottenpassFrame.getRootPane().setDefaultButton(recoverButton);
    }


// Method to open the registration window
        // Method to open the registration window
        private void openRegisterWindow () {
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
            JTextField usernameField = new JTextField(20);
            usernameLabel.setForeground(Color.LIGHT_GRAY);
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

                    client.connect(); // Attempt to connect first if not connected
                    client.register(username, password, email);
                    JOptionPane.showMessageDialog(registerFrame, "Registered successfully as " + username);

                    // Close the registration frame
                    registerFrame.dispose();
                }
            });

            // Show the registration frame
            registerFrame.setLocation(500, 300); // Example position (x: 500, y: 200)
            registerFrame.setVisible(true);
            registerFrame.getRootPane().setDefaultButton(registerButton);
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

// username
// once it works conf message 'temporary password sent to x email'


// add forgot password

//add when user clicks enter on their keyboard it should work
// find how to make server work with client ask prof on how to do that

//server

//use mysql workbech connect (check mark it in sql) the database add a query to the gui
//do it as seprate object
//connection thread and user interface goes to see whos logged in whos logged out talks to the database
//databse object everything static


//add IP section + button  - DONE

//change jlabel