package ServerSide;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.ArrayList;

public class Team {
    private String name;

    private int numberOfPlayersPerTeam=4;
    private int scoreOfTeam;
    private boolean hasGame = false;
   private ArrayList<Player> players = new ArrayList<Player>();
   private ArrayList<Server> clients = new ArrayList<>();

   private static ArrayList<Team> AllTeams = new ArrayList<Team>();
   private static ArrayList<Team> AllReadyTeams = new ArrayList<Team>();

   private boolean foundGame=false;

   //constructor
    public Team() {
        //add the team to the list of Ready teams
        //sync the players between threads to prevent Multiple players to add to list at the same time
        name= "Team " + (AllTeams.size() + 1);
        synchronized (AllTeams) {
            AllTeams.add(this);
        }
    }

    public String getName() {
        return name;
    }

    public boolean setName(String name) {
        //check if the name is unique
        synchronized (AllTeams) {
            if (AllTeams.size() == 1) {
                this.name = name;
                return true;
            }
            for (Team team : AllTeams) {
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

    public ArrayList<Server> getClients() {
        return clients;
    }

    public void setClients(ArrayList<Server> clients) {
        this.clients = clients;
    }

    //add a player to the team
    public void addPlayer(Player player){
        if(player!=null && players.size()<numberOfPlayersPerTeam){
            players.add(player);
        }
        if(players.size()==numberOfPlayersPerTeam){
            addTeamToReadyTeams();
        }
    }
    //add to client list
    public void addClient(Server client){
        clients.add(client);
    }
    //remove a team from the list of ready teams
    public void removeTeamFromReadyTeams(){
        synchronized (AllReadyTeams) {
            AllReadyTeams.remove(this);
        }
    }
    //add a team to the list of ready teams
    public void addTeamToReadyTeams(){
        synchronized (AllReadyTeams) {
            AllReadyTeams.add(this);
        }
    }
    //remove team from the list of all teams
    public void removeTeamFromAllTeams(){
        synchronized (AllTeams) {
            AllTeams.remove(this);
        }
    }
    //add team to the list of all teams
    public void addTeamToAllTeams(){
        synchronized (AllTeams) {
            AllTeams.add(this);
        }
    }
    //search for a team by name
    public static Team searchTeamByName(String name){
        synchronized (AllTeams) {
            for (Team team : AllTeams) {
                if (team.getName().equals(name)) {
                    return team;
                }
            }
        }
        return null;
    }
    //check if the team is in the list of ready teams
    public boolean isReady(){
        synchronized (AllReadyTeams) {
            for (Team team : AllReadyTeams) {
                if (team.getName().equals(name)) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isFoundGame() {
        return foundGame;
    }

    public void setFoundGame(boolean foundGame) {
        this.foundGame = foundGame;
    }
}
