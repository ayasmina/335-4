package server;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class Networking extends Thread {
    //	Go is the Logged in Boolean
    private boolean clientIsConnected;
    private String name;
    private int id;

    // -- the main server (port listener) will supply the socket
    //    the thread (this class) will provide the I/O streams
    //    BufferedReader is used because it handles String objects
    //    whereas DataInputStream does not (primitive types only)
    private BufferedReader datain;
    private DataOutputStream dataout;
    private Server server;
    //  server.Server is a reference to the "parent" Server object which will be set at time of construction
    public Networking(int id, Socket socket, Server server) {
        this.server = server;
        this.id = id;
        this.name = Integer.toString(id);
        clientIsConnected = true;
        // Create the stream I/O objects on top of the socket
        try {
            datain = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            dataout = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
    public String toString () {
        return name;
    }
    public String getname ()
    {
        return name;
    }
    public void run () {
        // Server thread runs until the client terminates the connection
        while (clientIsConnected) {
            try {
                /*  always receives a String object with a newline (\n)
                    on the end due to how BufferedReader readLine() works.
                    The client adds it to the user's string but the BufferedReader
                    readLine() call strips it off   */
                String txt = datain.readLine();
                String txtOut = Server.parseInput(txt);
                System.out.println("SERVER receive: " + txt);
                // -- if it is not the termination message, send it back adding the
                //    required (by readLine) "\n"

                //  If the disconnect string is received then close the socket, remove this thread object from the
                //  server's active client thread list, and terminate the thread
                if (txtOut.equals("disconnect")) {
                    datain.close();
                    Server.removeID(id);
                    clientIsConnected = false;
                }   //  End If
                else if (txtOut.equals("validUsername")) {
                    dataout.writeBytes("Valid username!");
                    dataout.flush();
                }   //  End Else If
                else if (txtOut.equals("validPassword")) {
                    dataout.writeBytes("You have successfully logged in!");
                    dataout.flush();
                }   //  End Else If
                else if(txtOut.equals("wrongUsername")){
                    dataout.writeBytes("Unrecognized username.");
                    dataout.flush();
                }
                else if(txtOut.equals("wrongPassword")){
                    dataout.writeBytes("Incorrect password.");
                    dataout.flush();
                }
                else if(txtOut.equals("logout")){
                    dataout.writeBytes("You have successfully logged out!");
                    dataout.flush();
                }
                else {
                    System.out.println("unrecognized command >>" + txt + "<<");
                    dataout.writeBytes(txt + "\n");
                }   //  End Else
            }   //  End Try
            catch(IOException e) {
                e.printStackTrace();
                clientIsConnected = false;
            }
        }
    }
}