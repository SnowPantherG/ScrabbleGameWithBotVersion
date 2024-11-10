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
    private int currentScore; // current score
    private int status; //0: quit or disqualified from playing or 1: is playing
//

    //private int wins; // round of wins
    //private int rerollCount; //how many times of reroll left
    //private boolean isCurrentPlayer;
    private int skipTurns; // turns left


    /**
     * Constructor to create a new player object with specified name
     * Initializes the player's tiles, remaining turns, score, wins, reroll count, and turn
     * @param name The player's name
     */
    public Player(String name) {
        this.name = name;
        this.rack = new Rack();
        this.currentScore = 0;
        this.status = 1;
        //this.wins = 0;
        //this.isCurrentPlayer = false; // not play in current round  //checkk
        //this.rerollCount=10;
        this.skipTurns = 3; //default turns to 3
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
     * Get the player's details.
     *
     * @return The player's description
     */
    public String getDescription() {
        String description =
                "Name: " + getName() + " \n" +
                        "Rack: " + getRack() + " \n" +
                        "Score: " + currentScore + " \n" +
                        "Status: " + status + " \n";

        return description;
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
     * Returns the player's current list of tiles as a String.
     *
     * @return A String representing the player's tiles.
     */
    public String getRack() {
        return rack.toString();
    }
    public String getRack_() {
        return rack.toString_();
    }

    /**
     * Returns the player's current amount of remaining tiles as an int.
     *
     * @return A int representing the player's remaining tiles.
     */
    public int remainingTiles() {
        return rack.remainingTiles();
    }

    /**
     * Adds a tile to the player's list of tiles.
     *
     * @param tile The tile to be added.
     */
    public void addTile(Tile tile) {
        rack.addTile(tile);
    }

    /**
     * Removes a specified tile from the player's list of tiles.
     *
     * @param tile The tile to be removed.
     */
    public void removeTile(Tile tile) {
        rack.removeTile(tile);
    }

    /**
     * Removes a specified tile from the player's list of tiles.
     *
     * @param integer The tile index to be removed.
     */
    public Tile removeTile(int index) {
        return rack.removeTile(index);
    }

    /**
     * Returns how many turns left for player.
     *
     * @return The number of remaining turns for skips.
     */
    public int getSkipTurns() {
        return skipTurns;
    }

    /**
     * Decrease the player's skip turns and ensure the turns
     * do not go below zero
     */
    public void decrementTurns() {
        if (skipTurns > 0) {
            skipTurns--;
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
     * Increment the players current score by adding specified numbers of points
     * @param points The points to be added
     */
    public void incrementScore(int points) {
        currentScore += points;
    }
    /**
     * Decrement the players current score by subtracting specified numbers of points
     * @param points The points to be added
     */
    public void decrementScore(int points) {
        if ((currentScore - points)>=0){
            currentScore -= points;
        } else{throw new IllegalStateException("Score can not be negative");}
    }

    /**
     * Resetting player's remaining turn count to 3
     */
    public void resetSkipTurns() {
        this.skipTurns = 3;
    }
    /**
     * Getting the player's current status.
     *
     * @return The current status of the player.
     */
    public int getCurrentStatus() {
        return status;
    }

    /**
     * Getting the player's current status.
     *
     * @return The current status of the player.
     */
    public void lostGame() {
        status = 0;
    }


} ///END OF CLASS


