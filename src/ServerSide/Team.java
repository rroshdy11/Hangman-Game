package ServerSide;

import java.util.ArrayList;

public class Team {
    private String name;

    private int numberOfPlayersPerTeam=4;
    private int scoreOfTeam;
    private boolean hasGame = false;
   private ArrayList<Player> players = new ArrayList<Player>();

   private static ArrayList<Team> AllReadyTeams = new ArrayList<Team>();

   //constructor
    public Team() {
        //add the team to the list of Ready teams
        //sync the players between threads to prevent Multiple players to add to list at the same time
        name= "Team " + (AllReadyTeams.size() + 1);
        synchronized (AllReadyTeams) {
            AllReadyTeams.add(this);
        }
    }

    public String getName() {
        return name;
    }

    public boolean setName(String name) {
        //check if the name is unique
        synchronized (AllReadyTeams) {
            if (AllReadyTeams.size() == 1) {
                this.name = name;
                return true;
            }
            for (Team team : AllReadyTeams) {
                if (team.getName().equals(name)) {
                    return false;
                }
            }
            this.name = name;
            return true;
        }
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

    public boolean isHasGame() {
        return hasGame;
    }

    public void setHasGame(boolean hasGameStarted) {
        this.hasGame = hasGameStarted;
    }

    public static ArrayList<Team> getAllReadyTeams() {
        return AllReadyTeams;
    }

    public static void setAllReadyTeams(ArrayList<Team> allReadyTeams) {
        AllReadyTeams = allReadyTeams;
    }

    public int getNumberOfPlayersPerTeam() {
        return numberOfPlayersPerTeam;
    }

    public void setNumberOfPlayersPerTeam(int numberOfPlayersPerTeam) {
        this.numberOfPlayersPerTeam = numberOfPlayersPerTeam;
    }
    //add a player to the team
    public void addPlayer(Player player){
        if(player!=null && players.size()<numberOfPlayersPerTeam){
            players.add(player);
        }
    }
}
