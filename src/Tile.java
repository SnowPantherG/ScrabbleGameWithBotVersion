import java.util.HashMap;


/**
* @author Rafid Mehedi Hasan
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
      tvalues.put('Q', 12); 
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

    // Constructor to initialize the Tile with a letter
    public Tile(char letter) {
        this.letter = Character.toUpperCase(letter);  // Convert to uppercase for consistency
        if (tvalues.containsKey(letter)) {
            this.value = tvalues.get(this.letter);  // Get the value if the letter exists in the map
        } else {
            this.value = 0;  // Assign 0 if the letter is not found
        }
    }

    // Getter for the letter on the tile
    public char getLetter() {
        return letter;
    }

    // Getter for the value of the tile
    public int getValue() {
        return value;
    }

    // Simple method to compare tiles by their letter
    public boolean sameTiles(Tile other) {
        return this.letter == other.letter;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Tile)) return false;
        Tile other = (Tile) obj;
        return this.letter == other.letter;
    }

    // String representation of the tile (letter only)
    public String toString() {
        return String.valueOf(letter);
    }
}