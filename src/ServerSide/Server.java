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
    private static ArrayList<MultiHangManGame> games = new ArrayList<>();

    //Static Number of Clients to be connected
    private static int numClients = 0;
    //Static Number of Clients to be connected
    private static int minClients = 4;

    private  Player player;


    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public void startGame() throws IOException, InterruptedException {
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
                if (line.split(",").length != 3){
                    out.writeUTF("Invalid input. Please enter your username ,name and password \n separated by comma \n eg: name,username,password");
                    continue;
                }
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
                if (line.split(",").length != 2){
                    out.writeUTF("Invalid input. Please enter your username and password \n separated by comma \n eg: username,password");
                    continue;
                }
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
        public void gameMenu(DataOutputStream out ,DataInputStream in) throws IOException, InterruptedException {
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
                            "1- Create Team and Start play \n"+ "2- Join Team and Start Play \n"+ "3- Exit From Game \n");
                    String choice = in.readUTF();
                    if(choice.contains("1")) {
                        createTeam_StartGame(in, out);
                    }
                    else if(choice.contains("2")){
                        joinTeam_StartGame(in, out);
                    }
                    else if(choice.contains("3")){
                        break;
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
        } catch (InterruptedException e) {
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

                //start the thread
                gameServer.start();
                System.out.println("Thread #"  +gameServer.getId() + " started");

            } catch (IOException i) {
                System.out.println(i);
            }
        }
    }


    public void createTeam_StartGame(DataInputStream in, DataOutputStream out) throws IOException, InterruptedException {
        //create team
        Team team = new Team();
            out.writeUTF("Enter the number of players in the team : Maximum number of players in a team is "+Team.getMaxNumberOfPlayers());
            out.writeUTF(""+Team.getMaxNumberOfPlayers());
            String line = in.readUTF();
            int numPlayers = Integer.parseInt(line);
            //create the team

            //check for mx number of plyers
            team.setNumberOfPlayersPerTeam(numPlayers);


        out.writeUTF("Team Created Successfully\n Insert the Team name: ");
        while (true) {
            // read the user's input for team name till he enter a valid name
            String teamName = in.readUTF();
            if (team.setName(teamName)) {
                out.writeUTF("Team name is valid");
                //add the player to the team
                team.addPlayer(player);
                break;
            } else {
                out.writeUTF("Team name already exists Try Another one\n");
            }
        }

        while (!team.isReady()){
            //wait for the team to be ready
        }
        MultiHangManGame game= searchForGame(team.getNumberOfPlayersPerTeam(),team);
        if(game!=null){
            out.writeUTF("Game Found Successfully waiting for other team to join... press any key to continue");
        }
        else{
            out.writeUTF("Game not not Found");
        }
        String anyKey = in.readUTF();
        //start the game
        startGame(in,out,game);


    }
    //search for a game with the same number of player per team
    public MultiHangManGame searchForGame(int numPlayers,Team team) {
        synchronized (games) {
            for (MultiHangManGame game : games) {
                if (game.getTeam1()==null ||game.getTeam1().getNumberOfPlayersPerTeam() == numPlayers && !game.isGameStarted()) {
                    game.addTeam(team);
                    team.setFoundGame(true);
                    return game;
                }
            }
        }
        return null;
    }

    //joinTeam_StartGame

    public void joinTeam_StartGame(DataInputStream in, DataOutputStream out) throws IOException, InterruptedException {
        //join team
        out.writeUTF("To Join a team Enter the Team name: ");
        Team team;
        while (true) {
            // read the user's input for team name till he enter a valid name
            String teamName = in.readUTF();
            team = Team.searchTeamByName(teamName);
            if (team == null) {
                out.writeUTF(" Team Does not Exist ->Try Another Name one\n");
            }
            else{
                out.writeUTF("Team found");
                //add the player to the team
                team.addPlayer(player);
                break;
            }
        }

        while (!team.isReady()){
            //wait for the team to be ready and the game to join
        }
        //Search for my game
        while (!team.isFoundGame()){
            //wait for the game to be found
        }
        MultiHangManGame game = searchForMyGame(team);
        if(game!=null){
            out.writeUTF("Game found Successfully waiting for other team to join... press any key to continue");
        }
        else{
            out.writeUTF("Game not started");
        }
        String anyKey = in.readUTF();
        //start the game
        startGame(in,out,game);
    }
    //search for my game in the list of games
    public MultiHangManGame searchForMyGame(Team team) {
        synchronized (games) {
            for (MultiHangManGame game : games) {
                //get the game that contains my team
                if (game.getTeam1().equals(team) || game.getTeam2().equals(team)) {
                    return game;
                }
            }
        }
        return null;
    }

    public void startGame(DataInputStream in,DataOutputStream out,MultiHangManGame game) throws IOException, InterruptedException {
        String state="";
        while(true){
            if(!game.isMyTurn(player)&& !game.isGameOver()){
                //wait for my turn
                out.writeUTF(game.getLastGuess()+"It's not your turn "+"Word to guess: " + game.getWordToGuess()+"\n"
                        +"Wrong guesses for my Teaam: " +
                        game.getMyTeamWrongGuesses(player)+
                        "\n"+"Its "+game.getTurn()+" turn please wait ....\n");
                continue;
            }
            sleep(100);
            if(game.isGameOver()&&game.isMyTeamWon(player)){
                state="Your Team Won";
                out.writeUTF("Your team Won "+"Game Over\n"
                        +"The word was: "+game.getWord()+"\n"+
                        "Your team score: "+game.getMyTeamScore(player)+"\n");
                break;
            }
            else if(game.isGameOver()&&!game.isMyTeamWon(player)){
                state="Your Team Lost";
                out.writeUTF("Your team Lost "+"Game Over\n" +
                        "The word was: "
                        +game.getWord()+"\n"+
                        "Your team score: "+game.getMyTeamScore(player)+"\n");
                break;
            } else if (game.isGameOver()) {
                state="Draw";
                out.writeUTF("Draw"+"Game Over\n" +
                        "The word was: "
                        +game.getWord()+"\n"+
                        "Your team score: "+game.getMyTeamScore(player)+"\n");
                break;
            } else if (!game.isGameOver()){
                out.writeUTF(game.getLastGuess()+"\n" +
                        "Word to guess: " + game.getWordToGuess()+"\n"
                    +"Wrong guesses for my Team: " +
                    game.getMyTeamWrongGuesses(player)+
                    "\n"+"Enter your guess: ");
                String charc = in.readUTF();
                String result = game.guessLetter(charc.charAt(0), player);
                out.writeUTF(result);
            }
        }

        //save the game to player history
        if(game.getTeam1().getPlayers().contains(player)) {
            player.addtoHistory(state,game.getScoreTeam1());
        }
        else if(game.getTeam2().getPlayers().contains(player)) {
            player.addtoHistory(state,game.getScoreTeam2());
        }
        //remove the game from the list of games
        games.remove(game);
        //remove the team from the list of teams
        Team.removeTeamFromAllTeams(game.getTeam1());
        Team.removeTeamFromAllTeams(game.getTeam2());
        //add another game to the list of games
        games.add(new MultiHangManGame());

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