import org.junit.Test;
import static org.junit.Assert.*;

public class TileTest {

    // Test data for the Tile class
    private Tile tile1 = new Tile('A');
    private Tile tile2 = new Tile('B');
    private Tile tile3 = new Tile('a'); // lowercase letter
    private Tile tile4 = new Tile('&'); // not an alphabet
    private Tile newtile = new Tile('A');

    public static void main(String[] args) {
        return;
    }

    @Test
    public void getLetter() {
        // Test if getLetter() correctly returns the letter
        assertEquals('A', tile1.getLetter());
        assertEquals('B', tile2.getLetter());
        assertEquals('A', tile3.getLetter()); // ensures case insensitivity
        assertEquals('&', tile4.getLetter()); // not a character case
    }

    @Test
    public void getValue() {
        // Test if getValue() returns the correct tile value
        assertEquals(2, tile1.getValue());  // Value for 'A'
        assertEquals(5, tile2.getValue());  // Value for 'B'
        assertEquals(2, tile3.getValue()); // Value for 'a' should be same as 'A'
        assertEquals(0, tile4.getValue()); // Value should always be 0
    }

    @Test
    public void sameTiles() {
        assertTrue(tile1.sameTiles(newtile)); // Tiles with the same letter
        assertFalse(tile1.sameTiles(tile4)); // Tiles with different letters
    }

    @Test
    public void testEquals() {
        assertTrue(tile1.equals(newtile)); // Should return true for same letters
        assertFalse(tile1.equals(tile2)); // Should return false for different letters
    }

    @Test
    public void testToString() {
        assertEquals("A", tile1.toString());
        assertEquals("B", tile2.toString());
        assertEquals("A", tile3.toString());
        assertEquals("&", tile4.toString());
    }
}

