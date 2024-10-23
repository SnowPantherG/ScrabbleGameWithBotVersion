import java.util.List;

public class Word {
    private List<Tile> tiles;
    private String direction; // "horizontal" or "vertical"
    private int startX; //row
    private int startY; //column
    private int endX; //row
    private int endY; //column
    private int score;
    private String player;
    private boolean isValid;

    public Word(List<Tile> tiles, String direction, int startX, int startY, int endX, int endY, String player) {
        this.tiles = tiles;
        this.direction = direction;
        this.startX = startX;
        this.startY = startY;
        this.endX = endX;
        this.endY = endY;
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