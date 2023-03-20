package ServerSide;

import ServerSide.GameSetup.GameSetupService;
import ServerSide.HangManGame.HangManGame;
import ServerSide.HangManGame.MultiHangManGame;
import ServerSide.HangManGame.SingleHangManGame;

import javax.sound.midi.SysexMessage;
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Server extends Thread {
    private Socket socket = null;
    DataOutputStream out = null;
    DataInputStream in = null;
    private static ArrayList<Server> clients = new ArrayList<Server>();
    private static ArrayList<MultiHangManGame> games = new ArrayList<>();

    //Static Number of Clients to be connected
    private static int numClients = 0;
    //Static Number of Clients to be connected
    private static int minClients = 4;

    private  Player player;

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public void startGame() throws IOException {
        // intialize the input and output streams
        out = new DataOutputStream(socket.getOutputStream());
        in = new DataInputStream(new BufferedInputStream(socket.getInputStream()));

        // write a welcome message to the client
        out.writeUTF("Hello from HangMan ServerSide.Game server");
        loginMenu(in,out);
        if(player!=null){
            gameMenu(out,in);
        }
        // close the socket and input stream
        out.writeUTF("Closing connection");
        socket.close();
        in.close();
    }

    public void loginMenu(DataInputStream in,DataOutputStream out) throws IOException {
        while (true) {
            out.writeUTF("choose from menu.\n 1. Register \n 2. Sign in \n (-) to exit from server");

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
                Player p = new Player(name, username, password);
                String register = p.register(name, username, password);
                out.writeUTF(register);
                if (register.equals("Player added successfully")) {
                    player= p;
                    break;
                }


            } else if (choice.equals("2")) {
                // perform the sign-in process
                out.writeUTF("You chose to sign in. Please enter your username and password \n separated by comma \n eg: username,password\"");
                // read the user's input for username and password
                String line = in.readUTF();
                String[] playerDetails = line.split(",");
                String username = playerDetails[0];
                String password = playerDetails[1];
                Player p = new Player(username, password);
                String login = p.login(username, password);
                out.writeUTF(login);
                if (login.equals("logged in successfully")) {
                    player = p;
                    break;
                }

            } else if (choice.equals("-")) {
                break;
            } else{
                // handle invalid input
                out.writeUTF("Invalid choice. Please choose either 1 or 2.");
            }
        }
    }
        public void gameMenu(DataOutputStream out ,DataInputStream in) throws IOException {
            while (true) {
                out.writeUTF("\n------------------------------------------------\n" +
                        "choose from menu.\n---------------------\n" +
                        " 1. Single Player \n" +
                        " 2. Multi Player \n" +
                        " 3. Show Player Scores \n" +
                        " 4. Exit.");

                String choicePlayer = in.readUTF();
                if (choicePlayer.contains("1")) {
                    // perform the single player game
                    out.writeUTF("Starting Single Player Game... press any key to start ");
                    SingleHangManGame singleHangManGame = new SingleHangManGame(player);
                    boolean exited = false;
                    in.readUTF();
                    while (!singleHangManGame.isGameOver()) {
                        out.writeUTF("Word to guess: " + singleHangManGame.getWordToGuess()+"\n"
                                +"Wrong guesses: " +
                                singleHangManGame.getWrongGuesses()+
                                "\n"+"Enter your guess: ");
                        String line = in.readUTF();
                        char guess = line.charAt(0);
                        if(guess=='-'){
                            exited = true;
                            out.writeUTF("You exited The Game." );
                            break;
                        }
                        String result = singleHangManGame.guess(guess);
                        if(singleHangManGame.isGameOver() && singleHangManGame.hasWon())
                            out.writeUTF("Congratulations! You won the game.");
                        else if(singleHangManGame.isGameOver() && !singleHangManGame.hasWon()) {
                            out.writeUTF("Game Over! You lost . The word was: " + singleHangManGame.getWord() + "\n" +
                                    "You made " + singleHangManGame.getWrongGuesses() + " wrong guesses.");
                        }
                        else{
                            out.writeUTF("\n Result: "+result+" \n-----------------------" );
                        }
                    }
                    if(exited){

                        continue;
                    }
                    singleHangManGame.saveToPlayerHistory();
                    //startGame();
                } else if (choicePlayer.contains("2")) {
                    out.writeUTF("Starting Multi Player Game .... \n"+
                            "1- Create Team \n"+ "2- Join Team \n"+ "3- Exit");
                    String choice = in.readUTF();
                    if(choice.equals("1")) {
                        createTeam_StartGame(in, out);
                    }
                    else if(choice.equals("2")){
                       // joinTeam_StartGame(in, out);
                        continue;
                    }
                    else if(choice.equals("3")){
                        continue;
                    }
                    else{
                        out.writeUTF("Invalid choice. Please choose either 1 or 2 or 3.");
                    }
                    //startGame();
                } else if (choicePlayer.contains("3")) {
                    out.writeUTF("player scores History . \n---------------------------\n " +
                            "eg:(Game EndDate EndTime) - (state-score) \n" +
                            "-----------------------press any key to get history-----------------\n ");
                    in.readUTF();
                    out.writeUTF(player.getHistory());
                }
                else if (choicePlayer.contains("4")) {
                    // perform the exit process
                    out.writeUTF("You chose to exit . Thank you for playing HangMan Game");
                    break;
                } else {
                    // handle invalid input
                    out.writeUTF("Invalid choice. Please choose either 1 or 2 or 3 or 4.\n");
                }
            }
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
            //create 10 MultiPlayerGame objects
            for (int i = 0; i < 10; i++) {
                games.add(new MultiHangManGame());
            }
            //print game configurations
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
                //add the client to the list of clients
                clients.add(gameServer);

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

    //return the player with the username
    public static Player searchForOnlinePlayer(String username){
        for (Server client : clients) {
            if(client.getPlayer().getUsername().equals(username)){
                return client.getPlayer();
            }
        }
        return null;
    }

    //return a thread with the username
    public static Server searchForOnlinePlayerThread(String username){
        for (Server client : clients) {
            if(client.getPlayer().getUsername().equals(username)){
                return client;
            }
        }
        return null;
    }

    public void createTeam_StartGame(DataInputStream in, DataOutputStream out) throws IOException {
        //create team
        out.writeUTF("Enter the number of players in the team:");
        String line = in.readUTF();
        int numPlayers = Integer.parseInt(line);
        //create the team
        Team team = new Team();
        team.setNumberOfPlayersPerTeam(numPlayers);
        out.writeUTF("Team Created Successfully\n Insert the Team name: ");
        while (true) {
            // read the user's input for team name till he enter a valid name
            String teamName = in.readUTF();
            if (team.setName(teamName)) {
                out.writeUTF("Team name is valid");
                break;
            } else {
                out.writeUTF("Team name already exists Try Another one\n");
            }
        }
        //Search for a team with the same number of players and Create a new MultiHangManGame
        //searchForGame_Start(numPlayers,team,clients);
    }
    //search for a game with the same number of player per team
    public void searchForGame_Start(int numPlayers,Team team,ArrayList<Server> clients) {
        synchronized (games) {
            for (MultiHangManGame game : games) {
                if (game.getTeam1().getNumberOfPlayersPerTeam() == numPlayers &&game.isGameStarted()) {
                    game.addTeam(team);
                }
            }
        }
    }

    //joinTeam_StartGame
    /*
    public void joinTeam_StartGame(DataInputStream in, DataOutputStream out) throws IOException {
        //join team
        out.writeUTF("Enter the Team name: ");
        String teamName = in.readUTF();
        Team team = Team.searchForTeam(teamName);
        if (team == null) {
            out.writeUTF("Team not found");
            return;
        }
        //Search for a team with the same number of players and Create a new MultiHangManGame
        searchForGame_Start(team.getNumberOfPlayersPerTeam(),team,clients);
    }
*/



}