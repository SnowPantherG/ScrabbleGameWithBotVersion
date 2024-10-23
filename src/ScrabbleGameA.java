import java.util.*;

/**
 * The ScrabbleGame class represents a simple Scrabble game implementation.
 * It manages players, the dictionary of words, the tile bag, and the game board.
 * The class allows multiple players to take turns, form words, and earn points.
 * This is a beta version for scrabble game controller , after create GUI view class, this class need to be rewritten
 *
 * @version V1.0 21st October 2024
 * @author Shenhao Gong
 *
 *
 * @version V1.1 22nd October 2024
 * @author Anique Ali
 * added docstrings
 *
 * @author Muhammad Maisam
 * @version V1.2 22nd October 2024
 * Another Implementation of Scrabble Game
 */
public class ScrabbleGameA {
    private final Board board;
    private final Bag tileBag;
    private final List<Player> players;
    private int currentPlayerIndex;
    public ScrabbleGame() {
        board = new Board();
        tileBag = new Bag();
        players = new ArrayList<>();
    }
    public void play(int numPlayers){
        for (int i = 0; i < numPlayers; i++) {
            players.add(new Player("Player " + (i + 1)));
        }
        currentPlayerIndex = 0;

        // Initialize the game by dealing tiles to players
        for (Player player : players) {
            for (int i = 0; i < 7; i++) {
                player.addTile(tileBag.removeTile());
            }
        }
    }
    public void challenge(){}
    public void pass(){}
    public void exchange(){}
    public void word(String word, String direction, int x, int y){}







}
