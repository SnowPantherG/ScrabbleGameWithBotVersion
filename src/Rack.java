import java.util.ArrayList;
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
 */

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
    public Tile removeTile(Tile newTile) {
        if (tiles.size() > 0) {
            tiles.remove(newTile);
            return newTile;
        }else{throw new IllegalStateException("The rack is empty");}
    }
    public Tile removeTile(int index) {
        if (tiles.size() > 0) {
            return tiles.remove(index);
        }else{throw new IllegalStateException("The rack is empty");}
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