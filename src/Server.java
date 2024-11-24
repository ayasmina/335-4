import java.io.*;
import java.net.*;
import java.util.*;
//import javax.mail.*;

public class Server {
    //  Instantiate list for active client threads
    public static Vector<MultiThread> clientConnections;
    //  Socket waits for client connections
    public static ServerSocket serversocket;
    // Int For Next Connection ID
    public int nextId = 0;
    // Port Server Will Be Listening To
    public static final int PORT = 8000;


    public static void main (String args[]) {
        new Server();
    }

    public Server() {
        //  Construct the list of active client threads
        clientConnections = new Vector<>();
        //  Listen for incoming connection requests
        listen();
    }

    //  Returns Server Port
    public static int getPort() {
        return PORT;
    }

    // Called by a ServerThread after a client is terminated
    public void removeID(int id) {
        //  Find the object belonging to the client thread being terminated
        for (int i = 0; i < clientConnections.size(); ++i) {
            MultiThread cc = clientConnections.get(i);
            long x = cc.getId();
            if (x == id) {
                // Remove ID from the clientConnections list and the connection thread will terminate itself
                clientConnections.remove(i);
                //  Place some text in the area to let the server operator know what is going on
                System.out.println("SERVER: connection closed for client id " + id + "\n");
                break;
            }   //  End if(x == id)
        }   //  End removeID(int id)
    }


    // adjust this so something gets sent back to the client so they know they connected
    private void peerConnection(Socket socket) {
        //  Create a thread communication when client arrives
        Network networkConnection = new Network(nextId, socket, this);
        MultiThread connection = new MultiThread(networkConnection, socket, this);
        //  Add the new thread to the active client threads list
        clientConnections.add(connection);
        //  Start the thread
        connection.start();
        //  Place some text in the area to let the server operator know what is going on
        System.out.println("SERVER: connection received for id " + nextId + "\n");
        ++nextId;
    }

    // Opens Server side connection - Stays in Server!
    public void listen(){
        try {
            //  Open the server socket
            serversocket = new ServerSocket(getPort()); // listens to Port 8000
            //  Server runs until we manually shut it down
            while(true) {   //  Infinite Loop Created
                //  Block until a client comes along
                Socket socket = serversocket.accept(); // client socket is connected to server now
                //  If connection is accepted then create a peer-to-peer socket
                peerConnection(socket); // client is talking to server now
            }   //  End While (true)
        }   //  End Try
        catch(IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    // Testing Variables - using to ensure connection is working
    private String username = "lynnir";
    private String password = "blahblah123";
    private boolean logged = false;

    // test login function
    public String login(String user, String pass){
        String res = "";
        if (!user.equals(this.username)){ // if the input equals the hard-coded username
            res = "1";
        } else if (!pass.equals(this.password)) {
            res = "2";
        } else if (logged == true) {
            res = "3";
        } else {
            logged = true;
            res = "0";
        }
        return res;
    }

//    public String passRecover(String user){
//        String res = "";
//        if(!user.equals(this.username)){
//            res = "0";
//        } else if (){
//
//        }
//    }

    // Using 0 and 1 for True and False responses in places applicable, extending beyond 0 and 1 when needed
    public String parseInput(String data){
        System.out.println("Received data: " + data);
        char operation;
        String result = "";
        String response = "";
        if(data != null) {
            operation = data.charAt(0); // grabbing operation from string
            System.out.println("1. Operation sent: " + operation);
            if(data.length() > 1) {
                //System.out.println("2. Entering if loop.");
                result = data.substring(1);
                String[] info = result.split(":");
                //System.out.println(info.length);
                //System.out.println("3. Remaining info: " + result);
                switch (operation) {
                    case '0':
                        // we wouldn't get here without the connection working so just say it's working?
                        response = "0"; // Connection successful
                        break;
                    case '1':
                        //System.out.println("Entering login case.");
                        // gathering user information from the substring
                        String user = info[0];
                        String pass = info[1];
                        System.out.println("User Info: username - " + user + " password - " + pass);
                        // calling login function here so the response can go back to Network
                        response = login(user, pass);
                        //System.out.println(response);
                        break;
                    case '2':
                        System.out.println("Entering register user case.");
                        // Gathering registration information from the substring
                        String newUser = info[0]; // Assume info[0] contains the username
                        String newPass = info[1]; // Assume info[1] contains the password
                        System.out.println("Registering user: username - " + newUser + ", password - " + newPass);
                        // Calling register function and storing the response
                        //response = register(newUser, newPass);
                        // Optionally print or log the response for debugging purposes
                        System.out.println("Register response: " + response);
                        break;
                    case '3':
                        System.out.println("Entering passRec.");

                        break;
                    case '4':
                        System.out.println("Logout");
                        break;
                    case '5':
                        System.out.println("Disconnect");
                        response = info[0];
                        break;
                    default : // in case it's not entering a case for some reason so we know
                       response = ("Error with switch loop.");
                }   //  End Switch (operation)
            }   //  End If (data length > 1)
        }   //  End If (Data is not null)
        //System.out.println("SERVER sending: " + response);
        return response;
    }
}


//public class Email {
//
//    // -- set the gmail host URL
//    final static private String host = "smtp.gmail.com";
//
//    // -- You must have a valid gmail username/password pair to use gmail as a SMTP service
//    static private String username = "csc335pablo";
//    static private String password = "bobp qxbv djxm tqvk\n";
//
//    public static void main(String[] args) {
//
//        Scanner kb = new Scanner(System.in);
//        System.out.print("Recipient email address: ");
//        String to = kb.next();
//        String messagetext = "Here is your password: " + "abcd$1234";
//
//        // -- set up host properties
//        //    refer to https://javaee.github.io/javamail/docs/api/com/sun/mail/smtp/package-summary.html for additional properties
//        Properties props = new Properties();
//        props.put("mail.smtp.auth", "true");
//        props.put("mail.smtp.host", host);
//        props.put("mail.smtp.starttls.enable", "true");
//        props.put("mail.smtp.ssl.enable", "true"); // -- to use port 465, the SSL port
//        props.put("mail.smtp.port", "465");        // -- TLS port is 587);
//
//        // -- Get the Session object.
//        Session session = Session.getInstance(props,
//                new javax.mail.Authenticator() {
//                    protected PasswordAuthentication getPasswordAuthentication() {
//                        return new PasswordAuthentication(username, password);
//                    }
//                });
//
//        // -- Set up the sender's email account information
//        String from = username + "@gmail.com";
//
//        try {
//            // -- Create a default MimeMessage object.
//            Message message = new MimeMessage(session);
//
//            // -- Set From: header field of the header.
//            message.setFrom(new InternetAddress(from));
//
//            // -- Set To: header field of the header.
//            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
//
//            // -- Set Subject: header field
//            message.setSubject("CSC335 Project Email");
//
//            // Now set the actual message
//            message.setText(messagetext);
//
//            // -- Send message
//            // -- use either these three lines...
//            // Transport t = session.getTransport("smtp");
//            // t.connect();
//            // t.sendMessage(message, message.getAllRecipients());
//
//            // -- ...or this one (which ultimately calls sendMessage(...)
//            Transport.send(message);
//
//            System.out.println("Sent message successfully....");
//
//        } catch (MessagingException e) {
//            throw new RuntimeException(e);
//        }
//    }
//}
// -- To prepare your gmail smtp account follow the steps described here
//    https://support.google.com/accounts/answer/185833
//
//    Sign in with App Passwords
//    Tip: App Passwords aren�t recommended and are unnecessary in most cases.
//         To help keep your account secure, use "Sign in with Google" to connect apps to your Google Account.
//
//    An App Password is a 16-digit passcode that gives a less secure app or
//    device permission to access your Google Account. App Passwords can only
//    be used with accounts that have 2-Step Verification turned on.
//                                    =============================
//    Basically, you are creating a password that is specific to the program you
//    are using to access the account. (this will bypass 2-step verification)
//
//    When to use App Passwords
//    =========================
//    Tip: iPhones and iPads with iOS 11 or up don�t require App Passwords.
//    Instead use �Sign in with Google.�
//
//    If the app doesn�t offer �Sign in with Google,� you can either:
//
//    Use App Passwords
//    =================
//    Switch to a more secure app or device
//    Create & use App Passwords
//    If you use 2-Step-Verification and get a "password incorrect" error when you sign in,
//    you can try to use an App Password.
//
//    Go to your Google Account.
//    1) Select Account -> Security.
//    2) Under "Signing in to Google," select App Passwords. You may need to sign in.
//       If you don't have this option, it might be because:
//       a) 2-Step Verification is not set up for your account.
//       b) 2-Step Verification is only set up for security keys.
//       c) Your account is through work, school, or other organization.
//       d) You turned on Advanced Protection.

//           Use that password when the program "logs" into your gmail account (see below)
//    3) At the bottom, choose Select app and choose the app you using and then
//       Select device and choose the device you�re using and then Generate.
//    4) Follow the instructions to enter the App Password.
//       The App Password is the 16-character code in the yellow bar on your device.
//       This is the password you will use in your code when connecting to the gmail
//       smtp server.
//    5) Tap Done.
//
//    If you don't have the App Password option, try this;
//		 Browse to: https://myaccount.google.com/apppasswords
//           Enter an application name
//           Copy the generated password
//
//    Tip: Most of the time, you'll only have to enter an App Password once per app or device,
//    so don't worry about memorizing it.


// -- Download JavaMail API from here: https://javaee.github.io/javamail/
// -- Download JavaBeans Activation Framework from here: http://www.oracle.com/technetwork/java/javasebusiness/downloads/java-archive-downloads-java-plat-419418.html#jaf-1.1.1-fcs-oth-JPR

