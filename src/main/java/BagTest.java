/**
 * This class will act as a test for each bag in the scrabble game, the maximum tiles will be
 * 99 for each bag which will represent alphabets on tiles.
 *
 * @author Mehedi Hasan Rafid
 * @version 2024.11.11
 *
 * @author Anique Ali
 * @version 2024.11.11
 */

import org.junit.*;
import static org.junit.Assert.*;

public class BagTest {
    private Bag bag;

    @Before
    public void setUp() {
        bag = new Bag(); // before each test new bag is created
    }

    @Test
    public void testIsEmpty() {
        assertFalse(bag.isEmpty()); // Here we assertFalse to check to see if bag is empty meaning that it returns false if there are tiles in it
        for (int i = 0; i < 98; i++) {
            bag.removeTile(); // we will remove tiles from bag completely
        }
        assertTrue(bag.isEmpty()); // This returns true as there are no tiles left in bag
    }

    @Test
    public void testRemoveTile() {
        Tile tile = bag.removeTile(); // first tile is removed
        assertEquals(97, bag.getRemainingTiles()); // one tiles is removed so 97 tiles are left we will compare that to
        //remaining tiles left in the bag using getRemainingTiles method and check if integers are equal
    }

    @Test
    public void testRemoveTileThrowsException() {

        for (int i = 0; i < 98; i++) {
            bag.removeTile(); // remove all tiles from bag using for loop
        }

        try {
            bag.removeTile(); // now if we try to remove the next tile it will throw exception as there is no tile left
        } catch (IllegalStateException e) {
            assertEquals("The bag is empty", e.getMessage()); //check exception message and check if they are equal
        }
    }

    @Test
    public void testGetRemainingTiles() {
        assertEquals(98, bag.getRemainingTiles()); // bag starting with 98 tiles
        bag.removeTile(); // remove a tile
        assertEquals(97, bag.getRemainingTiles()); // bag should not have 97 tiles checking it with assertEquals method
    }
}