import java.io.*;
import java.net.*;

public class Client {
    public static void main(String args[]) {
        Socket socket = null;
        DataOutputStream out = null;
        DataInputStream in = null;
        try {
            // create a new socket connected to the given address and port
            socket = new Socket("localhost", 5000);
            System.out.println("Connected");

            // create a new DataOutputStream to write data to the socket
            out = new DataOutputStream(socket.getOutputStream());

            // create a new BufferedReader to read data from the console
            BufferedReader console = new BufferedReader(new InputStreamReader(System.in));

            // create a new DataInputStream to read data from the socket
            in = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
            String output= in.readUTF();
            System.out.println(output);

            // read a line from the console
            while (true) {
                String login_menu = in.readUTF();
                System.out.println(login_menu);
                String choice = console.readLine();
                out.writeUTF(choice);
                output = in.readUTF();
                System.out.println(output);
                String userInfo = console.readLine();
                out.writeUTF(userInfo);
                output = in.readUTF();
                System.out.println(output);
                if(output.equals("logged in successfully") || output.equals("Player added successfully")){
                    break;
                }
            }



            out.close();
            socket.close();
        } catch(UnknownHostException u) {
            System.out.println(u);
        } catch(IOException i) {
            System.out.println(i);
        }
    }
}