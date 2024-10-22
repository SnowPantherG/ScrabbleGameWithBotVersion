import java.util.ArrayList;
import java.util.List;

public class Rack {
    private List<Tile> tiles;

    public Rack() {
        tiles = new ArrayList<>();
        // Initialize the rack with 7 tiles
        for (int i = 0; i < 7; i++) {
            tiles.add(new Tile()); // Replace with actual Tile creation logic
        }
    }

    public void updateTile(int index, Tile newTile) {
        tiles.set(index, newTile);
    }

    public List<Tile> getTiles() {
        return tiles;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Tile tile : tiles) {
            sb.append(tile.getLetter());
            sb.append(" ");
        }
        return sb.toString();
    }
}