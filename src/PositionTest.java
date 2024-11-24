/**
 * This class will act as a test for each position in the scrabble game tile, the maximum tiles will be
 * 7 for each player which will represent alphabets on tiles.
 *
 * @author Mehedi Hasan Rafid
 * @version 2024.11.23
 */
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class PositionTest {

    Position p1 = new Position(1, 5);
    Position p2 = new Position(2, 5);
    Position p3 = new Position(3, 5);
    Position p4 = new Position(1, 5);

    @Test
    public void testEquals() {
        assertEquals(1,p1.row);
        assertEquals(5,p1.col);
        assertEquals(2,p2.row);
        assertEquals(5,p2.col);

        assertFalse(p1.equals(p2));
        assertFalse(p1.equals(p3));

        assertNotEquals(p1.row,5);
        assertNotEquals(p1.col,1);

        assertNotNull(p3);
        assertNotNull(p2);
    }

    @Test
    public void testHashCode() {
        assertEquals(p1.hashCode(),p4.hashCode());
        assertNotEquals(p1.hashCode(),p2.hashCode());
    }
}
