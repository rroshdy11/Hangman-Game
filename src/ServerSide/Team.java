package ServerSide;

import java.util.ArrayList;

public class Team {
    private String name;
    private int scoreOfTeam;
   private ArrayList<Player> players = new ArrayList<Player>();

   private static ArrayList<Team> teams = new ArrayList<Team>();

   //constructor
    public Team() {
        this.scoreOfTeam = 0;
    }



    public String getName() {
        return name;
    }

    public boolean setName(String name) {
        //check if the name is unique
        for (Team team : teams) {
            if(team.getName().equals(name)){
                return false;
            }
        }
        this.name = name;
        return true;
    }


    public int getScoreOfTeam() {
        return scoreOfTeam;
    }

    public void setScoreOfTeam(int scoreOfTeam) {
        this.scoreOfTeam = scoreOfTeam;
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public void setPlayers(ArrayList<Player> players) {
        this.players = players;
    }
}
