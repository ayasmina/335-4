package server;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class Networking {
    //	Go is the Logged in Boolean
    public boolean clientIsConnected;
    public int id;
    public BufferedReader datain;
    public DataOutputStream dataout;
    public Server server;
    public Socket socket;
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
    // Send String Thread
    public String send(String msg) {
        String rtnmsg = "";
        try {
            // Write string to bytes
            dataout.writeBytes(msg + "\n");
            // Send string to server
            dataout.flush();
            // Receive response from the Server
            rtnmsg = "";
            // Read input while the response string is empty
            do {
                socket.setSoTimeout(20000); // Timeout of 20 seconds
                //  Set return message
                rtnmsg = datain.readLine();
            } while (rtnmsg.equals(""));
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
        return rtnmsg;
    }

    public String receive(){
        String res = "";
        try {
            res = datain.readLine();
        } catch (IOException e){
            e.printStackTrace();
            System.exit(1);
        }
        return res;
    }
}