import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Rack class will act as a Rack for each player in the scrabble game, the maximum tiles will be
 * 7 for each rack which will represent alphabets on tiles.
 *
 * @author Muhammad Maisam
 * @version 2024.10.22
 *
 * @author Shenhao Gong
 * @version 2024.11.09
 * added clearTile() method
 *
 * @author  Shenhao Gong
 * @version 2024.11.22
 * modify removeTile to return boolean
 *
 */

public class Rack {
    private List<Tile> tiles;

    public Rack() {
        tiles = new ArrayList<>();
    }
    public void addTile(Tile newTile) {
        if (tiles.size() < 7) {
            tiles.add(newTile); // Alternatively, insert at a specific index if needed
        } else {
            throw new IllegalStateException("The rack is full");
        }
    }

    public Tile removeTile(Tile newTile) {
        Iterator<Tile> iterator = tiles.iterator();
        while (iterator.hasNext()) {
            Tile currentTile = iterator.next();
            // Ensure it is removing the same instance, not just by letter
            if (currentTile == newTile) {
                iterator.remove();
                return currentTile; // Tile successfully removed
            }
        }
        throw new IllegalStateException("Tile not found in rack"); // Tile not found in rack
    }

    public Tile removeTile(int index) {
        if (tiles.size() > 0 && index >= 0 && index < tiles.size()) {
            return tiles.remove(index);
        } else {
            throw new IllegalStateException("Invalid index or the rack is empty");
        }
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
    public String toString_() {
        StringBuilder sb = new StringBuilder();
        for (Tile tile : tiles) {
            sb.append(tile.getLetter());
        }
        return sb.toString();
    }

    public void clearTiles(){
        tiles.clear();
    }


}