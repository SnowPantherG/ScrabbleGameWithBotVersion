import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
/**
 * Represents the state of a Scrabble board, including tiles, fixed tiles,
 * square types, and placed words. This class provides methods to retrieve
 * board-related information in a serializable and immutable manner, ensuring
 * compatibility with game saving and loading.
 *
 * @version 2024-12-04
 * @author Shenhao Gong
 */
public class BoardState implements Serializable {
    private static final long serialVersionUID = 1L;

    private final Tile[][] tiles; // Stores all tiles on the board
    private final boolean[][] fixedTiles; // Tracks fixed tiles
    private final SquareType[][] squareTypes; // Stores square types
    private final List<Word> placedWords; // Stores all placed words

    public BoardState(Tile[][] tiles, boolean[][] fixedTiles, SquareType[][] squareTypes, List<Word> placedWords) {
        this.tiles = deepCopyTiles(tiles); // Ensure immutability with deep copy
        this.fixedTiles = deepCopyFixedTiles(fixedTiles); // Ensure immutability
        this.squareTypes = deepCopySquareTypes(squareTypes); // Ensure immutability
        this.placedWords = new ArrayList<>(placedWords); // Create a deep copy of the words list
    }

    public Tile[][] getTiles() {
        return tiles;
    }

    public boolean[][] getFixedTiles() {
        return fixedTiles;
    }

    public SquareType[][] getSquareTypes() {
        return squareTypes;
    }

    public List<Word> getPlacedWords() {
        return placedWords;
    }

    private Tile[][] deepCopyTiles(Tile[][] original) {
        Tile[][] copy = new Tile[original.length][original[0].length];
        for (int row = 0; row < original.length; row++) {
            for (int col = 0; col < original[row].length; col++) {
                if (original[row][col] != null) {
                    copy[row][col] = new Tile(original[row][col].getLetter()); // Corrected method
                }
            }
        }
        return copy;
    }


    private boolean[][] deepCopyFixedTiles(boolean[][] original) {
        boolean[][] copy = new boolean[original.length][original[0].length];
        for (int row = 0; row < original.length; row++) {
            System.arraycopy(original[row], 0, copy[row], 0, original[row].length);
        }
        return copy;
    }

    private SquareType[][] deepCopySquareTypes(SquareType[][] original) {
        SquareType[][] copy = new SquareType[original.length][original[0].length];
        for (int row = 0; row < original.length; row++) {
            System.arraycopy(original[row], 0, copy[row], 0, original[row].length);
        }
        return copy;
    }
}
