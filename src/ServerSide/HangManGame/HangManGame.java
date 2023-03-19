package ServerSide.HangManGame;

import java.util.ArrayList;

public abstract class HangManGame {
    protected static  int MAX_WRONG_ATTEMPTS;
    protected static ArrayList<String> words = new ArrayList<String>();
    protected String word;
    protected String wordToGuess;


    public HangManGame() {
        //to get A Random Word when the game starts
        this.word = words.get((int)(Math.random()*words.size()));
        this.wordToGuess= formateWord(word);
    }
    //format the word to be guessed
    public String formateWord(String word){
        String wordToGuess = "";
        for(int i = 0; i<word.length(); i++){
            if((word.charAt(i)!= ' ')){
                wordToGuess += "-";
            }
            else{
                wordToGuess += word.charAt(i);
            }

        }
        return wordToGuess;
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
