import java.util.*;

/**
 * The Player class represents a player in a Scrabble game.
 * It contains attributes such as the player's name, tiles (cards), remaining turns, current score, number of wins, reroll count, and
 * whether it is currently their turn. The class provides methods to manage tiles, update scores, track wins, manage turns, and
 * determine if a player can form a word with their tiles.
 *
 * @version v1, 20th October, 2024
 * @author Shenhao Gong
 *
 *
 * @version v1.1 21th October 2024
 * @author Shenhao Gong
 * added canFormWord() and removeTilesForWord()
 *
 */

public class Player {
    private String name;
    private List<Tile> tiles; // playing card
    private int remainingTurns; // turns left
    private int currentScore; // current score
    private int wins; // round of wins

    private int rerollCount; //how many times of reroll left
    private boolean isCurrentPlayer;


    public Player(String name) {
        this.name = name;
        this.tiles = new ArrayList<Tile>(); //
        this.remainingTurns = 3;
        this.currentScore = 0;
        this.wins = 0;
        this.isCurrentPlayer = false; // not play in current round
        this.rerollCount=10;
    }

    // get player name
    public String getName() {
        return name;
    }


    public List<Tile> getTiles() {
        return tiles;
    }


    public void addTile(Tile tile) {
        tiles.add(tile);
    }


    public void removeTile(Tile tile) {
        tiles.remove(tile);
    }

    // get how many round left
    public int getRemainingTurns() {
        return remainingTurns;
    }

    // reduce round
    public void decrementTurns() {
        if (remainingTurns > 0) {
            remainingTurns--;
        }
    }


    public int getCurrentScore() {
        return currentScore;
    }

    //add scores
    public void updateScore(int points) {
        currentScore += points;
    }

    // get rounds of wins
    public int getWins() {
        return wins;
    }

    // increase win round
    public void incrementWins() {
        wins++;
    }


    public boolean isCurrentPlayer() {
        return isCurrentPlayer;
    }


    public void setCurrentPlayer(boolean isCurrentPlayer) {
        this.isCurrentPlayer = isCurrentPlayer;
    }

    public void useReroll() {
        if (rerollCount > 0) {
            rerollCount--;
        }
    }

    public int getRerollCount(){
        return rerollCount;
    }

    public void resetRerollCount() {
        this.rerollCount = 3;
    }


    //Check if player has the tiles for the target word
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

    //after the player get the score, remove used tiles from players hand
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
