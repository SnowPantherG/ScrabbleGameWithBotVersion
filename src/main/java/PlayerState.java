import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PlayerState implements Serializable {
    private static final long serialVersionUID = 1L;

    private final String name;
    private final int score;
    private final boolean isAIPlayer;
    private final List<Tile> tiles; // Save rack tiles

    public PlayerState(Player player) {
        this.name = player.getName();
        this.score = player.getCurrentScore();
        this.isAIPlayer = player instanceof AIPlayer;
        this.tiles = new ArrayList<>(player.getTiles()); // Copy tiles from rack
    }

    public String getName() {
        return name;
    }

    public int getScore() {
        return score;
    }

    public boolean isAIPlayer() {
        return isAIPlayer;
    }

    public List<Tile> getTiles() {
        return tiles;
    }
}
