import java.util.*;

public class Player {
    private String name;
    private List<Tile> tiles; // playing card
    private int remainingTurns; // turns left
    private int currentScore; // current score
    private int wins; // round of wins

    private int rerollCount; //how many times of reroll left
    private boolean isCurrentPlayer;


    public Player(String name, int remainingTurns) {
        this.name = name;
        this.tiles = new ArrayList<Tile>(); //
        this.remainingTurns = remainingTurns;
        this.currentScore = 0;
        this.wins = 0;
        this.isCurrentPlayer = false; // not play in current round
        this.rerollCount=3;
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

    // 重置重新抽牌次数
    public void resetRerollCount() {
        this.rerollCount = 3;
    }
}
