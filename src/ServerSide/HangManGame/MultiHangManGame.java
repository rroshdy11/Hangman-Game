package ServerSide.HangManGame;

import ServerSide.Team;

import java.util.ArrayList;

public class MultiHangManGame extends HangManGame{
    private static int MAX_NUMBER_OF_PLAYERS;
    private static int MIN_NUMBER_OF_PLAYERS;

    private ArrayList<Team> teams = new ArrayList<Team>();

    private boolean isGameStarted = false;
    public MultiHangManGame() {
        super();

    }

    //function to set 2 teams to the game
    public void setTeams(Team team1, Team team2){
        if(team1!=null && team2!=null){
            teams.add(team1);
            teams.add(team2);
        }
    }
    //function to search for a game that is ready to start
    public static int getMaxNumberOfPlayers() {
        return MAX_NUMBER_OF_PLAYERS;
    }

    public static void setMaxNumberOfPlayers(int maxNumberOfPlayers) {
        MAX_NUMBER_OF_PLAYERS = maxNumberOfPlayers;
    }

    //function to check if the game is ready to start



    public static int getMinNumberOfPlayers() {
        return MIN_NUMBER_OF_PLAYERS;
    }

    public static void setMinNumberOfPlayers(int minNumberOfPlayers) {
        MIN_NUMBER_OF_PLAYERS = minNumberOfPlayers;
    }
    private boolean isGameReadyToStart(){
        if(teams.size() == 2){
            return true;
        }
        return false;
    }
    //add a team to the game
    public void addTeam(Team team){
        if(team!=null && teams.size()<2 && !isGameStarted){
            team.setHasGame(true);
            teams.add(team);
        }
        if (isGameReadyToStart()){
            isGameStarted = true;
        }
    }

    public ArrayList<Team> getTeams() {
        return teams;
    }

    public void setTeams(ArrayList<Team> teams) {
        this.teams = teams;
    }

    public boolean isGameStarted() {
        return isGameStarted;
    }

    public void setGameStarted(boolean gameStarted) {
        isGameStarted = gameStarted;
    }

    //get first team
    public Team getTeam1(){
        return teams.get(0);
    }
}
