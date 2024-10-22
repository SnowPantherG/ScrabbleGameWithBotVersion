import java.util.*;

/**
 * The Player class represents a player in a Scrabble game.
 * It contains attributes such as the player's name, tiles (cards), remaining turns, current score, number of wins, reroll count, and
 * whether it is currently their turn. The class provides methods to manage tiles, update scores, track winning, manage turns, and
 * determine if a player can form a word with their tiles.
 *
 * @version v1, 20th October, 2024
 * @author Shenhao Gong
 *
 * @version v1.1 21th October 2024
 * @author Shenhao Gong
 * added canFormWord() and removeTilesForWord()
 *
 * @version v1.2, 22nd October 2024
 * @author Anique Ali
 * added docstrings
 */

public class Player {
    private String name;
    private List<Tile> tiles; // playing card
    private int remainingTurns; // turns left
    private int currentScore; // current score
    private int wins; // round of wins

    private int rerollCount; //how many times of reroll left
    private boolean isCurrentPlayer;


    /**
     * Constructor to create a new player object with specified name
     * Initializes the player's tiles, remaining turns, score, wins, reroll count, and turn
     * @param name The player's name
     */
    public Player(String name) {
        this.name = name;
        this.tiles = new ArrayList<Tile>(); // Initializes the tile list as empty
        this.remainingTurns = 3; //default turns to 3
        this.currentScore = 0;
        this.wins = 0;
        this.isCurrentPlayer = false; // not play in current round
        this.rerollCount=10;
    }

    /**
     * Get the player's name.
     *
     * @return The player's name
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the player's current list of tiles.
     *
     * @return A list of Tile objects representing the player's tiles.
     */
    public List<Tile> getTiles() {
        return tiles;
    }

    /**
     * Adds a tile to the player's list of tiles.
     *
     * @param tile The tile to be added.
     */
    public void addTile(Tile tile) {
        tiles.add(tile);
    }

    /**
     * Removes a specified tile from the player's list of tiles.
     *
     * @param tile The tile to be removed.
     */
    public void removeTile(Tile tile) {
        tiles.remove(tile);
    }

    /**
     * Returns how many turns left for player.
     *
     * @return The number of remaining turns.
     */
    public int getRemainingTurns() {
        return remainingTurns;
    }

    /**
     * Decrease the player's remaining turns and ensure the turns
     * do not go below zero
     */
    public void decrementTurns() {
        if (remainingTurns > 0) {
            remainingTurns--;
        }
    }

    /**
     * Getting the player's current score.
     *
     * @return The current score of the player.
     */
    public int getCurrentScore() {
        return currentScore;
    }

    /**
     * Update the players current score by adding specified numbers of points
     * @param points The points to be added
     */
    public void updateScore(int points) {
        currentScore += points;
    }

    /**
     * Gets total number of wins for player
     * @return The total number of wins
     */

    public int getWins() {
        return wins;
    }

    /**
     * Increase win count by one
     */
    public void incrementWins() {
        wins++;
    }

    /**
     * Checks if it is currently player's turn
     * @return true if it is players turn, false otherwise
     */
    public boolean isCurrentPlayer() {
        return isCurrentPlayer;
    }

    /**
     * Indicates whether it is currently the player's turn using true or false
     * @param isCurrentPlayer true if it's that players turn, false otherwise
     */
    public void setCurrentPlayer(boolean isCurrentPlayer) {
        this.isCurrentPlayer = isCurrentPlayer;
    }

    /**
     * Decrease reroll count by one as long as reroll is more than zero
     */
    public void useReroll() {
        if (rerollCount > 0) {
            rerollCount--;
        }
    }

    /**
     * Gets the player's current reroll count
     * @return The number of rerolls left
     */
    public int getRerollCount(){
        return rerollCount;
    }

    /**
     * Resetting player's reroll count to 3
     */
    public void resetRerollCount() {
        this.rerollCount = 3;
    }

    /**
     * Check if player has the tiles for the target word
     * @param word The word to check
     * @return True if player can form word, false otherwise
     */
    public boolean canFormWord(String word) {
        // Create a copy of the player's tiles for validation
        List<Tile> tilesCopy = new ArrayList<>(tiles);

        // Iterate over each letter in the word to check if the player has enough tiles
        for (char letter : word.toCharArray()) {
            boolean tileFound = false;
            for (Tile tile : tilesCopy) {
                if (tile.getLetter() == letter) {
                    tilesCopy.remove(tile);
                    tileFound = true;
                    break;
                }
            }
            if (!tileFound) {
                return false; // If a required tile is not found, the player cannot form the word
            }
        }

        return true; // If all letters are found, the player can form the word
    }

    /**
     * After the player get the score, remove used tiles from players hand
     * @param word The word that was formed
     */
    public void removeTilesForWord(String word) {
        for (char letter : word.toCharArray()) {
            for (Tile tile : tiles) {
                if (tile.getLetter() == letter) {
                    tiles.remove(tile);
                    break;
                }
            }
        }
    }


}