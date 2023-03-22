package ServerSide.HangManGame;

import ServerSide.Player;
import ServerSide.Team;

import java.util.ArrayList;

public class MultiHangManGame extends HangManGame{

    private ArrayList<Team> teams = new ArrayList<Team>();

    private boolean isGameStarted = false;

    //wrong guesses for each team
    private int wrongGuessesTeam1;
    private int wrongGuessesTeam2;

    private int scoreTeam1;
    private int scoreTeam2;
    ArrayList <Boolean> turns = new ArrayList<>();
    private String alreadyGuessed = "";

    private String lastGuess="";
    public MultiHangManGame() {
        super();

    }

    //add a team to the game
    public void addTeam(Team team){
        if(team!=null && teams.size()<2 && !isGameStarted){
            team.setHasGame(true);
            teams.add(team);
        }
        //start the game if two teams are added
        if(teams.size()==2){
            isGameStarted = true;
            startGame();
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
        if(teams.size()>0){
            return teams.get(0);
        }
        return null;
    }
    //check if the game is over
    public boolean isGameOver(){
        if(wrongGuessesTeam1>=MAX_WRONG_ATTEMPTS || wrongGuessesTeam2>=MAX_WRONG_ATTEMPTS|| word.equalsIgnoreCase(wordToGuess)){
            return true;
        }
        return false;
    }

    //function start the game
    public void startGame(){
        wrongGuessesTeam1 = 0;
        wrongGuessesTeam2 = 0;
        scoreTeam1 = 0;
        scoreTeam2 = 0;
        //get the sum of sizes of the teams
        int sum = teams.get(0).getPlayers().size() +teams.get(1).getPlayers().size();
        //fill the array with false
        System.out.println("sum = "+sum);
        for(int i=0;i<sum;i++){
            turns.add(false);
        }
        //set the first team to play
        turns.set(0,true);
    }
    public boolean isMyTurn(Player player){
        if(getTeam1().getPlayers().contains(player)){
            return turns.get(getTeam1().getPlayers().indexOf(player));
        }
        else if(getTeam2().getPlayers().contains(player)){
            return turns.get(getTeam1().getPlayers().size()+getTeam2().getPlayers().indexOf(player));
        }
        return false;
    }

    //get my team wrong guesses
    public int getMyTeamWrongGuesses(Player player){
        if(getTeam1().getPlayers().contains(player)){
            return wrongGuessesTeam1;
        }
        else if(getTeam2().getPlayers().contains(player)){
            return wrongGuessesTeam2;
        }
        return -1;
    }
    //guss letter by a player and change the turn
    //code to modify tommorow
    public String guessLetter(char letter, Player player){
        //change the turn to the next player
        synchronized (this) {
            if (turns.get(turns.size() - 1)) {
                //set the first player to play
                turns.set(0, true);
                turns.set(turns.size() - 1, false);
            } else {
                for (int i = 0; i < turns.size(); i++) {
                    if (turns.get(i)) {
                        turns.set(i, false);
                        turns.set(i + 1, true);
                        break;
                    }
                }
            }
            //check if the letter is in the word
            String result = guess(letter);
            if (result.equals("Correct")) {
                //check if the team is the first team
                if (getTeam1().getPlayers().contains(player)) {
                    //set the score of the team
                    scoreTeam1++;
                } else if (getTeam2().getPlayers().contains(player)) {
                    scoreTeam2++;
                }
                synchronized (lastGuess) {
                    lastGuess = "Player :" + player.getUsername() + " has made Correct  guess (Team1 Wrong Guses ,Team2 W)" + "( " + wrongGuessesTeam1 + "-" + wrongGuessesTeam2 + " )";
                }
                return "Player :" + player.getUsername() + " has made Correct  guess (Team1 Wrong Guses ,Team2 W)" + "( " + wrongGuessesTeam1 + "-" + wrongGuessesTeam2 + " )";

            } else {
                //check if the team is the first team
                if (getTeam1().getPlayers().contains(player)) {
                    //set the score of the team
                    wrongGuessesTeam1++;
                } else if (getTeam2().getPlayers().contains(player)) {
                    wrongGuessesTeam2++;
                }
                synchronized (lastGuess) {
                    lastGuess = "Player :" + player.getUsername() + " has made Wrong guess (Team1 Wrong Gusses ,Team2 W)" + "( " + wrongGuessesTeam1 + "-" + wrongGuessesTeam2 + " )";
                }
                return "Player :" + player.getUsername() + " has made Wrong guess (Team1 Wrong Gusses ,Team2 Wrong Gusses)" + "( " + wrongGuessesTeam1 + "-" + wrongGuessesTeam2 + " )";
            }
        }
    }
    public String guess(char guess){
        synchronized (wordToGuess) {
            String result = "";
            if ((word.contains((guess + "").toLowerCase()) || word.contains((guess + "").toUpperCase()))
                    &&(alreadyGuessed.contains((guess + "").toLowerCase()) || alreadyGuessed.contains((guess + "").toUpperCase())) ) {
                result = "Correct";
                alreadyGuessed=alreadyGuessed+guess;
                for (int i = 0; i < word.length(); i++) {
                    //if the letter is the first letter
                    if (word.charAt(i) == (guess+"").toLowerCase().charAt(0) || word.charAt(i) == (guess+"").toUpperCase().charAt(0)) {
                        if (i == 0) {
                            wordToGuess = guess + wordToGuess.substring(i + 1);
                        }
                        else {
                            wordToGuess = wordToGuess.substring(0, i) + guess + wordToGuess.substring(i + 1);
                        }
                    }
                }
            } else {
                result = "Wrong";
            }
            return result;
        }
    }

    //function to get whose turn is it
    public String getTurn(){
        String result = "";
        for(int i=0;i<turns.size();i++){
            if(turns.get(i)){
                if(i<getTeam1().getPlayers().size()){
                    result = getTeam1().getPlayers().get(i).getUsername();
                }
                else{
                    result = getTeam2().getPlayers().get(i-getTeam1().getPlayers().size()).getUsername();
                }
            }
        }
        return result;
    }
    //function to check if My team won
    public boolean isMyTeamWon(Player player){
        if(getTeam1().getPlayers().contains(player)){
            if(scoreTeam1>scoreTeam2){
                return true;
            }
        }
        else if(getTeam2().getPlayers().contains(player)){
            if(scoreTeam2>scoreTeam1){
                return true;
            }
        }
        return false;
    }

    public int getMyTeamScore(Player player){
        if(getTeam1().getPlayers().contains(player)){
            return scoreTeam1;
        }
        else if(getTeam2().getPlayers().contains(player)){
            return scoreTeam2;
        }
        return -1;
    }

    public String getLastGuess() {
        return lastGuess;
    }

    public void setLastGuess(String lastGuess) {
        this.lastGuess = lastGuess;
    }

    public int getScoreTeam1() {
        return scoreTeam1;
    }

    public void setScoreTeam1(int scoreTeam1) {
        this.scoreTeam1 = scoreTeam1;
    }

    public int getScoreTeam2() {
        return scoreTeam2;
    }

    public void setScoreTeam2(int scoreTeam2) {
        this.scoreTeam2 = scoreTeam2;
    }
    //get second team
    public Team getTeam2(){
        if(teams.size()>1){
            return teams.get(1);
        }
        return null;
    }

    //function to set 2 teams to the game
    public void setTeams(Team team1, Team team2){
        if(team1!=null && team2!=null){
            teams.add(team1);
            teams.add(team2);
        }
    }


}
