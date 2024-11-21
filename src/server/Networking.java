package server;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class Networking extends Thread {
    //	Go is the Logged in Boolean
    private boolean clientIsConnected;
    private int id;
    private BufferedReader datain;
    private DataOutputStream dataout;
    private Server server;
    //  server.Server is a reference to the "parent" Server object which will be set at time of construction
    // -- the main server (port listener) will supply the socket
    //    the thread (this class) will provide the I/O streams
    //    BufferedReader is used because it handles String objects
    //    whereas DataInputStream does not (primitive types only)
    public Networking(int id, Socket socket, Server server) {
        this.server = server;
        this.id = id;
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
    public void run () {
        // Server thread runs until the client terminates the connection
        while (clientIsConnected) {
            try {
                /*  always receives a String object with a newline (\n)
                    on the end due to how BufferedReader readLine() works.
                    The client adds it to the user's string but the BufferedReader
                    readLine() call strips it off   */
                String txtIn = datain.readLine();
                String txtOut = Server.parseInput(txtIn);
                System.out.println("SERVER receive: " + txtIn);
                // -- if it is not the termination message, send it back adding the
                //    required (by readLine) "\n"
                //  If the disconnect string is received then close the socket, remove this thread object from the
                //  server's active client thread list, and terminate the thread
                if (txtOut.equals("disconnect")) {
                    datain.close();
                    Server.removeID(id);
                    clientIsConnected = false;
                }   //  End If
                else {
                    dataout.writeBytes(txtOut + "\n");
                    dataout.flush();
                }   //  End Else If
            }   //  End Try
            catch(IOException e) {
                e.printStackTrace();
                clientIsConnected = false;
            }
        }
    }
}
