package ServerSide.HangManGame;

import ServerSide.Player;

public class SingleHangManGame extends HangManGame{
    private Player player1;

    public SingleHangManGame(String word, String wordToGuess, int wrongGuesses, Player player1) {
        super(word, wordToGuess, wrongGuesses);
        this.player1 = player1;
    }


}
