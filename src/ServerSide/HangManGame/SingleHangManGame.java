package ServerSide.HangManGame;

import ServerSide.Player;

public class SingleHangManGame extends HangManGame{
    private Player player1;
    private int wrongGuesses;

    private boolean win;
    private int gameFinalScore;

    public SingleHangManGame( Player player1) {
        super();
        wrongGuesses = 0;
        this.player1 = player1;
    }
    //check if the game is over if he has reached the max number of wrong guesses or if he has guessed the word
    public boolean isGameOver(){
        if(wrongGuesses == MAX_WRONG_ATTEMPTS|| word.equalsIgnoreCase(wordToGuess)){
            return true;
        }
        return false;
    }

    //make a guess and return the result
    public String guess(char guess){
        String result = "";
        if(word.contains((guess+"").toLowerCase())||word.contains((guess+"").toUpperCase())){
            result = "Correct";
            for(int i = 0; i<word.length(); i++){
                if(word.charAt(i) == (guess+"").toLowerCase().charAt(0)|| word.charAt(i) == (guess+"").toUpperCase().charAt(0) ){
                    if(i==0){
                        wordToGuess = guess + wordToGuess.substring(i+1);
                    }
                    else{
                        wordToGuess = wordToGuess.substring(0,i) + guess + wordToGuess.substring(i+1);
                    }

                }
            }
        }
        else{
            result = "Wrong";
            wrongGuesses++;
        }
        return result;
    }

    //check if the player has won
    public boolean hasWon(){
        if(word.equals(wordToGuess)){
            win = true;
            return true;
        }
        win = false;
        return false;
    }
    public Player getPlayer1() {
        return player1;
    }

    public void setPlayer1(Player player1) {
        this.player1 = player1;
    }

    public int getWrongGuesses() {
        return wrongGuesses;
    }

    public void setWrongGuesses(int wrongGuesses) {
        this.wrongGuesses = wrongGuesses;
    }
    public void saveToPlayerHistory(){
        if(hasWon()){
            gameFinalScore = word.length();
            player1.addtoHistory("Won",gameFinalScore);
        }
        else{
            gameFinalScore=0;
            player1.addtoHistory("Lost",gameFinalScore);
        }
    }
}
