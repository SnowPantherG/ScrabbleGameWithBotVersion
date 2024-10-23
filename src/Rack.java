import java.util.ArrayList;
import java.util.List;

public class Rack {
    private List<Tile> tiles;

    public Rack() {
        tiles = new ArrayList<>();
    }
    public void addTile(Tile newTile) {
        if (tiles.size() < 7) {
            tiles.add(newTile);
        }else{throw new IllegalStateException("The rack is full");}
    }
    public void removeTile(Tile newTile) {
        if (tiles.size() > 0) {
            tiles.remove(newTile);
        }else{throw new IllegalStateException("The rack is empty");}
    }
    public void removeTile(int index) {
        if (tiles.size() > 0) {
            tiles.remove(index);
        }else{throw new IllegalStateException("The rack is empty");}
    }
    public void updateTile(int index, Tile newTile) {
        tiles.set(index, newTile);
    }

    public List<Tile> getTiles() {
        return tiles;
    }
    public int remainingTiles() {
        return tiles.size();
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
