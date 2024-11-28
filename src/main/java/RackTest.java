import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
/**
 * This class will act as a test for each player's rack in the scrabble game, the maximum tiles will be
 * 7 for each player which will represent alphabets on tiles.
 *
 * @author Mehedi Hasan Rafid
 * @version 2024.11.11
 */
public class RackTest {
    private Rack rack;
    private Tile tile1;
    private Tile tile2;
    private Tile tile3;
    private Tile tile4;

    @Before
    public void setUp() {
        rack = new Rack();
        tile1 = new Tile('M');
        tile2 = new Tile('O');
        tile3 = new Tile('V');
        tile4 = new Tile('E');
    }

    @Test
    public void addTile() {
        rack.addTile(tile1);
        rack.addTile(tile2);
        rack.addTile(tile3);
        rack.addTile(tile4);
        assertEquals(4, rack.getTiles().size());
        assertTrue(rack.getTiles().contains(tile1));
    }

    @Test
    public void removeTile() {
        rack.addTile(tile1);
        rack.addTile(tile2);
        rack.addTile(tile3);
        rack.addTile(tile4);
        rack.removeTile(tile1);
        assertFalse(rack.getTiles().contains(tile1));
        assertEquals(3, rack.getTiles().size());
    }

    @Test
    public void testRemoveTile() {
        rack.addTile(tile1);
        rack.addTile(tile2);
        rack.addTile(tile3);
        rack.addTile(tile4);
        rack.removeTile(1);
        assertFalse(rack.getTiles().contains(tile2));
        assertEquals(3, rack.getTiles().size());
    }

    @Test
    public void getTiles() {
        rack.addTile(tile1);
        rack.addTile(tile2);
        rack.addTile(tile3);
        rack.addTile(tile4);
        assertEquals(4, rack.getTiles().size());
        System.out.println(rack.getTiles());
    }

    @Test
    public void remainingTiles() {
        rack.addTile(tile1);
        rack.addTile(tile2);
        rack.addTile(tile3);
        assertEquals(3, rack.getTiles().size());
        rack.removeTile(tile1);
        rack.removeTile(tile2);
        assertEquals(1, rack.remainingTiles());
    }

    @Test
    public void clearTiles() {
        rack.addTile(tile1);
        rack.addTile(tile2);
        rack.addTile(tile3);
        rack.clearTiles();
        assertEquals(0, rack.getTiles().size());
        assertFalse(rack.getTiles().contains(tile1));
        assertFalse(rack.getTiles().contains(tile2));
        assertFalse(rack.getTiles().contains(tile3));
    }
}