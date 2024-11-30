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
 *
 * @version v1.3, 22nd October 2024
 * @author Muhammad Maisam
 */

public class Player {
    private String name;
    private Rack rack;
    private int currentScore;
    private int status;
    private int rerollCount;
    private int skipTurns;

    /**
     * Constructor to create a new player object with specified name.
     * Initializes the player's rack, score, status, skip turns, and reroll count.
     *
     * @param name The player's name.
     */
    public Player(String name) {
        this.name = name;
        this.rack = new Rack();
        this.currentScore = 0;
        this.status = 1;
        this.skipTurns = 3; // Default turns to 3
        this.rerollCount = 3; // Players have 3 rerolls
    }

    /**
     * Get the player's name.
     *
     * @return The player's name.
     */
    public String getName() {
        return name;
    }

    /**
     * Get the player's details.
     *
     * @return The player's description.
     */
    public String getDescription() {
        return String.format("Name: %s\nRack: %s\nScore: %d\nStatus: %d\n",
                getName(), getRack(), currentScore, status);
    }

    /**
     * Returns the player's current list of tiles.
     *
     * @return A list of Tile objects representing the player's tiles.
     */
    public List<Tile> getTiles() {
        return rack.getTiles();
    }

    /**
     * Returns the reroll count of the player.
     *
     * @return The number of rerolls remaining.
     */
    public int getRerollCount() {
        return rerollCount;
    }

    /**
     * Decrements the player's reroll count by one.
     */
    public void decrementRerollCount() {
        if (rerollCount > 0) {
            rerollCount--;
        }
    }

    /**
     * Returns the player's current rack as a String.
     *
     * @return A String representing the player's tiles.
     */
    public String getRack() {
        return this.rack.toString();
    }

    /**
     * Returns the number of tiles currently in the player's rack.
     *
     * @return The number of tiles in the rack.
     */
    public int remainingTiles() {
        return rack.remainingTiles();
    }

    /**
     * Adds a tile to the player's rack.
     *
     * @param tile The tile to be added.
     * @throws IllegalStateException if the rack is full.
     */
    public void addTile(Tile tile) {
        rack.addTile(tile);
    }

    /**
     * Removes a specified tile from the player's rack.
     *
     * @param tile The tile to be removed.
     * @return True if the tile was successfully removed, false otherwise.
     */
    public Tile removeTile(Tile tile) {
        return rack.removeTile(tile);
    }

    /**
     * Clears all tiles from the player's rack.
     */
    public void clearTiles() {
        rack.clearTiles();
    }

    /**
     * Removes a tile at the specified index from the player's rack.
     *
     * @param index The index of the tile to be removed.
     * @return The removed Tile.
     * @throws IllegalStateException if the rack is empty.
     */
    public Tile removeTile(int index) {
        return rack.removeTile(index);
    }

    /**
     * Returns how many skip turns the player has left.
     *
     * @return The number of skip turns remaining.
     */
    public int getSkipTurns() {
        return skipTurns;
    }

    /**
     * Decrements the player's skip turns by one and updates status if necessary.
     */
    public void decrementTurns() {
        if (skipTurns > 0) {
            skipTurns--;
        }
        if (skipTurns == 0) {
            status = 0; // Disqualify player
        }
    }

    /**
     * Returns the player's current score.
     *
     * @return The current score of the player.
     */
    public int getCurrentScore() {
        return currentScore;
    }

    /**
     * Increments the player's score by the specified number of points.
     *
     * @param points The points to be added.
     */
    public void incrementScore(int points) {
        currentScore += points;
    }

    /**
     * Decrements the player's score by the specified number of points.
     * Ensures the score does not go negative.
     *
     * @param points The points to be subtracted.
     */
    public void decrementScore(int points) {
        if (currentScore - points >= 0) {
            currentScore -= points;
        }
    }

    /**
     * Resets the player's skip turns to the default value.
     */
    public void resetSkipTurns() {
        this.skipTurns = 3;
    }

    /**
     * Returns the player's current status.
     *
     * @return The current status of the player.
     */
    public int getCurrentStatus() {
        return status;
    }

    /**
     * Marks the player as having lost the game.
     */
    public void lostGame() {
        status = 0;
    }

    /**
     * Retrieves a tile from the rack by its letter.
     *
     * @param letter The letter of the tile to retrieve.
     * @return The Tile object if found, null otherwise.
     */
    public Tile getTileByLetter(char letter) {
        for (Tile tile : rack.getTiles()) {
            if (tile.getLetter() == letter) {
                return tile;
            }
        }
        return null;
    }
}
