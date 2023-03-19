package ServerSide.HangManGame;

public class MultiHangManGame extends HangManGame{
    private static int MAX_NUMBER_OF_PLAYERS;
    private static int MIN_NUMBER_OF_PLAYERS;

    public MultiHangManGame(int wrongGuesses) {
        super();
    }

    public static int getMaxNumberOfPlayers() {
        return MAX_NUMBER_OF_PLAYERS;
    }

    public static void setMaxNumberOfPlayers(int maxNumberOfPlayers) {
        MAX_NUMBER_OF_PLAYERS = maxNumberOfPlayers;
    }

    public static int getMinNumberOfPlayers() {
        return MIN_NUMBER_OF_PLAYERS;
    }

    public static void setMinNumberOfPlayers(int minNumberOfPlayers) {
        MIN_NUMBER_OF_PLAYERS = minNumberOfPlayers;
    }
}
