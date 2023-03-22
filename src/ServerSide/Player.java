package ServerSide;
import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
public class Player {
        private String name;
        private String username;
        private String password;

        private static ArrayList<Player> players = new ArrayList<Player>();

        public static ArrayList<Player> getPlayers() {
                return players;
        }
        public static void setPlayers(ArrayList<Player> players) {
                Player.players = players;
        }
        //constructor
        public Player(String name, String username, String password) {
            this.name = name;
            this.username = username;
            this.password = password;
        }
    public Player(String username, String password) {
        this.username = username;
        this.password = password;
    }
        //getters and setters
        public String getName() {
                return name;
        }
        public void setName(String name) {
                this.name = name;
        }
        public String getUsername() {
                return username;
        }
        public void setUsername(String username) {
                this.username = username;
        }
        public String getPassword() {
                return password;
        }
        public void setPassword(String password) {
                this.password = password;
        }
        public  String register(String name, String username, String password ) throws IOException, IOException {

            boolean exists = false;
            //check if username already exists
            //sync the players between threads to prevent Multiple players with same username at the same time
            synchronized (players) {
                for (Player player : players) {
                    if (player.getUsername().equals(username)) {
                        exists = true;
                    }
                }
            }
            //if username doesn't exist write player in file
            if (!exists) {
                FileWriter fw = new FileWriter("src/ServerSide/GameSetup/Players.txt", true);
                BufferedWriter bw = new BufferedWriter(fw);
                bw.write(name + "," + username + "," + password);
                bw.newLine();
                bw.close();
                //sync the players between threads to prevent adding the players in the arraylist at the same time
                synchronized (players) {
                    players.add(new Player(name, username, password));
                }
                return "Player added successfully";

            } else {
                return "Username already exists";
            }
        }

    public String login(String username, String password) {
            boolean exists = false;
            boolean correctPassword = false;
        for (Player player : players) {
            if (player.getUsername().equals(username) ) {
                exists = true;
                if (player.getPassword().equals(password)) {
                    correctPassword = true;
                }
            }
        }
            if (!exists) {
                return "404 error, not found";
            } else if (!correctPassword) {
                return "401 error, unauthorized";
            }
            else {
                return "logged in successfully";
            }

    }
    public String getHistory() {
        String history = "";
        //read history from the file with the username
        try {
            FileReader fr = new FileReader("src/ServerSide/GameSetup/History/" + username + ".txt");
            BufferedReader br = new BufferedReader(fr);
            String line = br.readLine();
            while (line != null) {
                history += line + "\n";
                line = br.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return history;
    }
    //write history to the file with the username
    public void addtoHistory(String state, int score){
        try {
            FileWriter fw = new FileWriter("src/ServerSide/GameSetup/History/" + username + ".txt", true);
            BufferedWriter bw = new BufferedWriter(fw);
            //get the current date and time
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
            LocalDateTime now = LocalDateTime.now();

            bw.write( "("+dtf.format(now) +") - ("+state+"-" + score+")");
            bw.newLine();
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }





}
















