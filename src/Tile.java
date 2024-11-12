import java.util.HashMap;

/**
 * The tile class is a representation of a single tile that is used in scrabble game.
 * Every tile is assigned a unique letter and a corresponding point value.
 * The class includes functions for getting the letter and value of a tile, as well as for comparing two tiles by their letters.
 *
 * @author Rafid Mehedi Hasan
 * @version V1.0 21st October 2024
 *
 * @author Anique Ali
 * @version V1.1 22nd October 2024
 * added docstrings
 *
 * @author Muhammad Maisam
 * @version V1.2 22nd October 2024
 */
public class Tile {
    private char letter;  // The letter on the tile ('A', 'B', ....., 'Z')
    private int value;    // The integer value of the tile

    // Static HashMap that holds the values for each letter in Scrabble
    private static final HashMap<Character, Integer> tvalues = new HashMap<>();

    // Static block to initialize the values (modified values)
    static {
        tvalues.put('A', 2);
        tvalues.put('B', 5);
        tvalues.put('C', 4);
        tvalues.put('D', 3);
        tvalues.put('E', 1);
        tvalues.put('F', 6);
        tvalues.put('G', 3);
        tvalues.put('H', 7);
        tvalues.put('I', 2);
        tvalues.put('J', 9);
        tvalues.put('K', 8);
        tvalues.put('L', 2);
        tvalues.put('M', 5);
        tvalues.put('N', 2);
        tvalues.put('O', 2);
        tvalues.put('P', 4);
        tvalues.put('Q', 10);
        tvalues.put('R', 2);
        tvalues.put('S', 1);
        tvalues.put('T', 1);
        tvalues.put('U', 3);
        tvalues.put('V', 7);
        tvalues.put('W', 8);
        tvalues.put('X', 9);
        tvalues.put('Y', 6);
        tvalues.put('Z', 10);
    }

    /**
     * Constructor to initialize the Tile
     * @param letter The character on the tile
     */

    public Tile(char letter) {
        this.letter = Character.toUpperCase(letter);  // Convert to uppercase for consistency
        if (tvalues.containsKey(letter)) {
            this.value = tvalues.get(this.letter);  // Get the value if the letter exists in the map
        } else {
            this.value = 0;  // Assign 0 if the letter is not found
        }
    }

    /**
     * Gets the letter on the tile
     * @return The letter on the tile
     */

    public char getLetter() {
        return letter;
    }
    /**
     * Method gets the value of the tile
     * @return The integer value of the tile
     */

    public int getValue() {
        return value;
    }

    /**
     * Compare two tiles by their letter
     * @param other The other tile to compare to
     * @return True if both tiles have same alphabet, false otherwise
     */
    public boolean sameTiles(Tile other) {
        return this.letter == other.letter;
    }

    /**
     * Compare ths tile object to another object to determine if they are equal
     * comparision is based on alphabet on tile
     * @param obj The object to be compared with this tile
     * @return true if obj is same instance as this tile or if obj is a tile and have same alphabets; false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true; // check if both reference points to same object
        if (!(obj instanceof Tile)) return false; // check if obj is instance of tile, if it is not return false
        Tile other = (Tile) obj; // cast obj as tile and compare the letter field
        return this.letter == other.letter;
    }

    /**
     * String representation of the tile (letter only)
     * @return The letter on the tile as a string
     */

    public String toString() {
        return String.valueOf(letter);
    }

    /**
     * String representation of the tile (letter and value)
     * @return The letter and value on the tile as a string
     */

    public String tileDescription() {
        return "{" + letter + ", " + value + "}";
    }
}