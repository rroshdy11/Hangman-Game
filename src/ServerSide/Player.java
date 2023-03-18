package ServerSide;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.*;


public class Player {
        private String name;
        private String username;
        private String password;
        private int score; //score of the player
        private int wins; //number of wins of the player
        private int losses; //number of losses of the player
        private int draws; //number of draws of the player
        //constructor
        public Player(String name, String username, String password) {
            this.name = name;
            this.username = username;
            this.password = password;
            this.score = 0;
            this.wins = 0;
            this.losses = 0;
            this.draws = 0;
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

        public int getScore() {
                return score;
        }

        public void setScore(int score) {
                this.score = score;
        }

        public int getWins() {
                return wins;
        }

        public void setWins(int wins) {
                this.wins = wins;
        }

        public int getLosses() {
                return losses;
        }

        public void setLosses(int losses) {
                this.losses = losses;
        }

        public int getDraws() {
                return draws;
        }

        public void setDraws(int draws) {
                this.draws = draws;
        }
}






