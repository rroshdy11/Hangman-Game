import java.io.*;
import java.net.*;
import java.util.ArrayList;

public class GameServer extends Thread {
    private Socket socket = null;
    DataOutputStream out = null;
    DataInputStream in = null;

    //Static Number of Clients to be connected
    private static int numClients = 0;
    //Static Number of Clients to be connected
    private static int minClients = 4;
    private static ArrayList<Player> loggedInPlayers = new ArrayList<Player>();


    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public void startGame() throws IOException {
        // intialize the input and output streams
        out = new DataOutputStream(socket.getOutputStream());
        in = new DataInputStream(new BufferedInputStream(socket.getInputStream()));

        // write a welcome message to the client
        out.writeUTF("Hello from HangMan Game server");

        // create a new DataInputStream to read data from the client socket
        String line = "";
        // read data from the client until "Over" is sent
        while (!line.equals("Over")) {
            // read a line of UTF-8 encoded text from the input stream
            line = in.readUTF();
            System.out.println(line);
        }
        // close the socket and input stream
        System.out.println("Closing connection");
        socket.close();
        in.close();
    }
    // override the run() method of the Thread class
    public void run()
    {
        try {
            startGame();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public static void main(String args[]) {
        // create a new ServerSocket instance bound to the given port
        ServerSocket server = null;
        try {
            server = new ServerSocket(5000);
            System.out.println("Server started");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        while (true) {
            try {
                // wait for a client to connect
                Socket socket = server.accept();
                System.out.println("Client accepted");
                //create a new thread to handle the client
                GameServer gameServer = new GameServer();
                gameServer.setSocket(socket);
                //start the thread
                gameServer.start();
                System.out.println("Thread #"  +gameServer.getId() + " started");

            } catch (IOException i) {
                System.out.println(i);
            }
        }
    }
}