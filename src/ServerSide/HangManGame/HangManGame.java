package ServerSide.HangManGame;

import java.util.ArrayList;

public abstract class HangManGame {
    protected static  int MAX_WRONG_ATTEMPTS = 6;
    protected static ArrayList<String> words = new ArrayList<String>();
    protected String word;
    protected String wordToGuess;
    protected int wrongGuesses;


    public HangManGame(String word, String wordToGuess, int wrongGuesses) {
        this.word = word;
        this.wordToGuess = wordToGuess;
        this.wrongGuesses = wrongGuesses;

    }






    //check if the game is over if he has reached the max number of wrong guesses or if he has guessed the word
    public boolean isGameOver(){
        if(wrongGuesses == MAX_WRONG_ATTEMPTS|| word.equals(wordToGuess)){
            return true;
        }
        return false;
    }

    //make a guess and return the result
    public String guess(char guess){
        String result = "";
        if(word.contains(guess+"")){
            result = "Correct";
            for(int i = 0; i<word.length(); i++){
                if(word.charAt(i) == guess){
                    wordToGuess = wordToGuess.substring(0,i) + guess + wordToGuess.substring(i+1);
                }
            }
        }
        else{
            result = "Wrong";
            wrongGuesses++;
        }
        return result;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getWordToGuess() {
        return wordToGuess;
    }

    public void setWordToGuess(String wordToGuess) {
        this.wordToGuess = wordToGuess;
    }

    public int getWrongGuesses() {
        return wrongGuesses;
    }

    public void setWrongGuesses(int wrongGuesses) {
        this.wrongGuesses = wrongGuesses;
    }

    public static int getMaxWrongAttempts() {
        return MAX_WRONG_ATTEMPTS;
    }

    public static void setMaxWrongAttempts(int maxWrongAttempts) {
        MAX_WRONG_ATTEMPTS = maxWrongAttempts;
    }

    public static ArrayList<String> getWords() {
        return words;
    }

    public static void setWords(ArrayList<String> words) {
        HangManGame.words = words;
    }
}
