package ServerSide;

import ServerSide.GameSetup.GameSetupService;
import ServerSide.HangManGame.HangManGame;
import ServerSide.HangManGame.MultiHangManGame;

import javax.sound.midi.SysexMessage;
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Server extends Thread {
    private Socket socket = null;
    DataOutputStream out = null;
    DataInputStream in = null;

    //Static Number of Clients to be connected
    private static int numClients = 0;
    //Static Number of Clients to be connected
    private static int minClients = 4;
    //All Registered Players in the game Server

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public void startGame() throws IOException {
        // intialize the input and output streams
        out = new DataOutputStream(socket.getOutputStream());
        in = new DataInputStream(new BufferedInputStream(socket.getInputStream()));

        // write a welcome message to the client
        out.writeUTF("Hello from HangMan ServerSide.Game server");

        out.writeUTF("choose from menu.\n 1. Register \n 2. Sign in");

        String choice = in.readUTF();

        if (choice.equals("1")) {
            // perform the registration process
            out.writeUTF("You chose to register. Please enter your username ,name and password \n separated by comma \n eg: name,username,password");
            // read the user's input for username and name and password
            String line = in.readUTF();
            String[] playerDetails = line.split(",");
            String name = playerDetails[0];
            String username = playerDetails[1];
            String password = playerDetails[2];
            Player player = new Player(name, username, password);
            out.writeUTF(player.register(name , username , password));


        } else if (choice.equals("")) {
            // perform the sign-in process
            out.writeUTF("You chose to sign in. Please enter your username and password.");
            // read the user's input for username and password
            String username = in.readUTF();
            String password = in.readUTF();




        } else {
            // handle invalid input
            out.writeUTF("Invalid choice. Please choose either 1 or 2.");
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
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(5000);
            System.out.println("Server started");
            System.out.println("Game Setup Started....");
            GameSetupService gameSetupService = new GameSetupService();
            gameSetupService.gameSetup();
            //print game configurations
            System.out.println("Game Configurations: ");
            System.out.println("Max Wrong Attempts: " + HangManGame.getMaxWrongAttempts());
            System.out.println("Max Number of Players per team: " + MultiHangManGame.getMaxNumberOfPlayers());
            System.out.println("Min Number of Players per team: " + MultiHangManGame.getMinNumberOfPlayers());

            System.out.println("Game Setup Finished....");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        // Make the server wait for clients to connect
        while (true) {
            try {
                // wait for a client to connect
                Socket socket = serverSocket.accept();
                System.out.println("Client accepted");
                //create a new thread to handle the client
                Server gameServer = new Server();
                gameServer.setSocket(socket);

                //start the thread
                gameServer.start();
                System.out.println("Thread #"  +gameServer.getId() + " started");

            } catch (IOException i) {
                System.out.println(i);
            }
        }
    }

    public Socket getSocket() {
        return socket;
    }

    public static int getNumClients() {
        return numClients;
    }

    public static void setNumClients(int numClients) {
        Server.numClients = numClients;
    }

    public static int getMinClients() {
        return minClients;
    }

    public static void setMinClients(int minClients) {
        Server.minClients = minClients;
    }

}