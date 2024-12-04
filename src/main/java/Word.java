import java.io.Serializable;
import java.util.List;
/**
 * The word class represents a word.
 * It is made of multiple tiles and stores the direction and location of
 * the word on the board. Also stores if a word is valid or unvalid.
 * It also stores which player came up with the word.021
 02 *
 * @author Muhammad Maisam
 * @version 2024.10.22
 */

public class Word implements Serializable {
    private List<Tile> tiles;
    private String direction; // "horizontal" or "vertical"
    private int startX; //row
    private int startY; //column
    private int endX; //row
    private int endY; //column
    private int score;
    private String player;
    private boolean isValid;

    public Word(List<Tile> tiles, String direction, int startX, int startY, String player) {
        this.tiles = tiles;
        this.direction = direction;
        this.startX = startX;
        this.startY = startY;
        int offset = tiles.size() - 1;
        if(direction == "vertical"){
            endX = startX;
            endY = startY + offset;
        } else if(direction == "horizontal"){
            endX = startX + offset;
            endY = startY;
        }
        //this.endX = endX;
        //this.endY = endY;
        this.player = player;
        this.isValid = true; // Default to valid

        score = 0;
        // Calculate the score
        for (Tile tile : tiles) {
            score += tile.getValue();
        }
    }

    public String getWordString() {
        StringBuilder sb = new StringBuilder();
        for (Tile tile : tiles) {
            sb.append(tile.getLetter());
        }
        return sb.toString();
    }

    public String getDescription() {
        return String.format("%s From:(%d, %d) - To:(%d, %d) - Score: %d", getWordString(), startX, startY, endX, endY, score);
    }

    public int getScore() {
        return score;
    }

    public String getPlayer() {
        return player;
    }

    public boolean isValid() {
        return isValid;
    }

    public void setInvalid() {
        isValid = false;
    }
}
