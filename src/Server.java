import java.io.*;
import java.net.*;

public class Server {
    // initialize socket and input stream
    private Socket socket = null;
    private ServerSocket server = null;
    private DataInputStream in = null;

    // constructor with port
    public Server(int port) {
        try {
            // create a new ServerSocket instance bound to the given port
            server = new ServerSocket(port);
            System.out.println("Server started");
            System.out.println("Waiting for a client ...");

            // wait for a client to connect and accept the connection
            socket = server.accept();
            System.out.println("Client accepted");

            // create a new DataInputStream to read data from the client socket
            in = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
            String line = "";

            // read data from the client until "Over" is sent
            while (!line.equals("Over")) {
                // read a line of UTF-8 encoded text from the input stream
                line = in.readUTF();
                System.out.println(line);
            }

            System.out.println("Closing connection");
            // close the socket and input stream

            socket.close();
            in.close();
        } catch (IOException i) {
            System.out.println(i);
        }
    }

    public static void main(String args[]) {
        // create a new Server instance listening on port 5000
        Server server = new Server(5000);
    }
}