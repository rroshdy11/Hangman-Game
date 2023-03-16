//case 1
//team 1 aph ---   (worng 6) in case of all charcters gussed  score is number of characters in the word
//team 2 --- h--   (worng 6)


// score criteria (num of chars in pharse)
// when two teams finishes their worng attempts

import java.io.*;
import java.sql.*;
import java.util.ArrayList;


public class Main {

    public static void main(String[] args) throws SQLException, IOException {

        System.out.println("Hello world!");
        writePlayerToFile("reda", "username", "password");
        ArrayList<Player> players = readPlayersFromFile();
        //print players
        for (Player player : players) {
           System.out.println(player.getName() + " " + player.getUsername() + " " + player.getPassword());
        }



    }
    //write new player in a file using file writer and buffered writer
    public static String writePlayerToFile(String name, String username, String password) throws IOException, IOException {
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
            FileWriter fw = new FileWriter("Users.txt", true);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(name + "," + username + "," + password);
            bw.newLine();
            bw.close();
            return "Player added successfully";
        }
        else {
            return "Username already exists";
        }
    }

    //read players from file using file reader and buffered reader
    public static ArrayList<Player> readPlayersFromFile() throws IOException {
        ArrayList<Player> players = new ArrayList<Player>();
        FileReader fr = new FileReader("Players.txt");
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





}

