//case 1
//team 1 aph ---   (worng 6) in case of all charcters gussed  score is number of characters in the word
//team 2 --- h--   (worng 6)


// score criteria (num of chars in pharse)
// when two teams finishes their worng attempts

import ServerSide.Player;

import java.io.*;
import java.sql.*;
import java.util.ArrayList;


public class Main {

    public static void main(String[] args) throws SQLException, IOException {

        System.out.println("Hello world!");




    }
    //write new player in a file using file writer and buffered writer
    /*
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
            return "ServerSide.Player added successfully";
        }
        else {
            return "Username already exists";
        }
    }
*/



}

