package ServerSide.GameSetup;

import ServerSide.HangManGame.HangManGame;
import ServerSide.HangManGame.MultiHangManGame;
import ServerSide.Player;
import ServerSide.Server;

import java.io.*;
import java.util.ArrayList;

public class GameSetupService {

    public void gameSetup(){
        try {
            HangManGame.setWords(readWords());
            Server.setPlayers(readPlayersFromFile());
            HangManGame.setMaxWrongAttempts((Integer) readGameConfigurations().get(0));
            MultiHangManGame.setMaxNumberOfPlayers((Integer) readGameConfigurations().get(1));
            MultiHangManGame.setMinNumberOfPlayers((Integer) readGameConfigurations().get(2));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Read All words from the file and add them to arraylist
    public  ArrayList<String> readWords() throws IOException {
        ArrayList<String> words = new ArrayList<String>();
        FileReader fr = new FileReader("src/ServerSide/GameSetup/phrases.txt");
        BufferedReader br = new BufferedReader(fr);
        String line = br.readLine();
        while (line != null) {
            words.add(line);
            line = br.readLine();
        }
        br.close();
        return words;
    }

    public  String writePlayerToFile(String name, String username, String password) throws IOException, IOException {
        ArrayList<Player> players = readPlayersFromFile();
        boolean exists = false;
        //check if username already exists
        for (Player player : players) {
            if (player.getUsername().equals(username)) {
                exists = true;
            }
        }
        //if username doesn't exist write player in file
        if(!exists){
            FileWriter fw = new FileWriter("src/ServerSide/GameSetup/Players.txt", true);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(name + "," + username + "," + password);
            bw.newLine();
            bw.close();
            return "ServerSide.Player added successfully";
        }
        else {
            return "Username already exists";
        }
    }
    //read players from file using file reader and buffered reader
    public  ArrayList<Player> readPlayersFromFile() throws IOException {
        ArrayList<Player> players = new ArrayList<Player>();
        FileReader fr = new FileReader("src/ServerSide/GameSetup/Players.txt");
        BufferedReader br = new BufferedReader(fr);
        String line = br.readLine();
        while (line != null) {
            String[] player = line.split(",");
            Player p = new Player(player[0], player[1], player[2]);
            players.add(p);
            line = br.readLine();
        }
        br.close();
        return players;
    }

    // read Game Configurations from file
    public  ArrayList readGameConfigurations() throws IOException {
        ArrayList gameConfigurations = new ArrayList();
        FileReader fr = new FileReader("src/ServerSide/GameSetup/GameConfig.txt");
        BufferedReader br = new BufferedReader(fr);
        String line = br.readLine();

        String[] config = line.split("-");
        gameConfigurations.add(Integer.parseInt(config[0]));
        gameConfigurations.add(Integer.parseInt(config[1]));
        gameConfigurations.add(Integer.parseInt(config[2]));
        gameConfigurations.add(Integer.parseInt(config[3]));

        br.close();
        return gameConfigurations;
    }

}
