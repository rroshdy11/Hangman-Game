import java.io.*;
import java.net.*;

public class Client {
    // initialize socket and output stream
    private Socket socket = null;
    private DataOutputStream out = null;

    public Client(String address, int port) {
        try {
            // create a new socket connected to the given address and port
            socket = new Socket(address, port);
            System.out.println("Connected");


            // create a new DataOutputStream to write data to the socket
            out = new DataOutputStream(socket.getOutputStream());

            // create a new BufferedReader to read data from the console
            BufferedReader console = new BufferedReader(new InputStreamReader(System.in));

            String line = "";

            // read input from the console and write it to the socket until "Over" is entered
            while (!line.equals("Over")) {

                line = console.readLine();
                out.writeUTF(line);

            }
            out.close();
            socket.close();
        } catch(UnknownHostException u) {
            System.out.println(u);
        } catch(IOException i) {
            System.out.println(i);
        }
    }

    public static void main(String args[]) {
        // create a new Client instance connected to localhost:5000
        Client client = new Client("localhost", 5000);
    }
}