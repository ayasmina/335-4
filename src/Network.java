import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class Network extends Thread {
    private boolean go;
    private String name;
    private int id;
    private int nextId = 0;

    // handling peer to peer communication
    private BufferedReader datain;
    private DataOutputStream dataout;

    private Server server;
    private Socket socket;
    private static final int PORT = 8000;

    public int getPort() {
        return PORT;
    }

    public Network(int id, Socket socket, Server server) {
        this.server = server;
        this.id = id;
        this.name = Integer.toString(id);
        go = true;

        try {
            datain = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            dataout = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public String toString() {
        return name;
    }

    public String getname() {
        return name;
    }


    public void peerconnection(Socket socket) {

    }

    public void disconnect() {
        String text = "disconnect\n";
        try {
            // sending message to let server know client is disconnecting
            dataout.writeBytes(text);
            dataout.flush();
            // closing peer to peer socket
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public String sendString(String msg) {
        String rtnmsg = "";

        try {
            // send String to Server
            dataout.writeBytes(msg + "\n");
            dataout.flush();

            // receive response from the Server
            rtnmsg = "";
            do {
                rtnmsg = datain.readLine();
            } while (rtnmsg.equals(""));
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
        return rtnmsg;
    }

    private void listen() {
        try {
            serverSocket = new ServerSocket(getPort());

            while (true) {
                Socket socket = serverSocket.accept();

                peerconnection(socket);
            }
        } catch (IOException e) {
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

    public void run() {
        while (go) {
            try {
                String txt = datain.readLine();
                System.out.println("SERVER receive: " + txt);

                if (txt.equals("disconnect")) {
                    datain.close();
                    Network.removeID(id);
                    go = false;
                } else if (txt.equals("hello")) {

                    dataout.writeBytes("world!" + "\n");
                    dataout.flush();

                } else {
                    System.out.println("unrecognized command >>" + txt + "<<");
                    dataout.writeBytes(txt + "\n");
                }
            } catch (IOException e) {
                e.printStackTrace();
                go = false;
            }
        }

    }
}

