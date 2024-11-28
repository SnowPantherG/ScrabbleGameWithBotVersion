/**
 * This class will act as a test for each player in the scrabble game, the maximum tiles will be
 * 7 for each player which will represent alphabets on tiles.
 *
 * @author Mehedi Hasan Rafid
 * @version 2024.11.11
 */
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class PlayerTest {
    private List<Player> players;
    private Board board;
    private Bag bag;
    private Rack rack;
    private Tile tile1;
    private Tile tile2;

    @Before
    public void setUp() {
        this.players = new ArrayList<>();
        this.bag = new Bag();
        this.rack = new Rack();
        this.tile1 = new Tile('A');
        this.tile2 = new Tile('B');
        for (int i = 0; i < 2; i++) {
            players.add(new Player("Player " + (i + 1)));
        }
        // Initialize the game by dealing tiles to players
        for (Player player : players) {
            for (int i = 0; i < 6; i++) {
                player.addTile(bag.removeTile());
            }
        }
    }

    @Test
    public void tileSize() {
        assertEquals(6, players.get(0).getTiles().size());
        assertEquals(6, players.get(1).getTiles().size());
        for(Player player : players) {
            for(Tile tile : player.getTiles()) {
                assertNotNull(tile);
            }
        }
    }

    @Test
    public void getName() {
        assertEquals("Player 1", players.get(0).getName());
        assertEquals("Player 2", players.get(1).getName());
    }

    @Test
    public void getTiles() {
        System.out.println(players.get(0).getTiles());
        System.out.println(players.get(1).getTiles());
    }

    @Test
    public void getRerollCount() {
        assertEquals(3, players.get(0).getRerollCount());
        assertEquals(3, players.get(1).getRerollCount());
        players.get(0).decrementRerollCount();
        players.get(0).decrementRerollCount();
        assertEquals(1, players.get(0).getRerollCount());
        players.get(0).decrementRerollCount();
        players.get(0).decrementRerollCount();
        players.get(0).decrementRerollCount();
    }

    @Test
    public void getRack() {
        System.out.println(players.get(0).getTiles());
        System.out.println(players.get(1).getTiles());
    }

    @Test
    public void remainingTiles() {
    }

    @Test
    public void addTile() {
        players.get(0).addTile(tile1);
        assertTrue(players.get(0).getTiles().contains(tile1));
        assertEquals(7, players.get(0).getTiles().size());
        players.get(0).removeTile(tile1);
    }

    @Test
    public void removeTile() {
        players.get(0).addTile(tile1);
        assertTrue(players.get(0).getTiles().contains(tile1));
        players.get(0).removeTile(tile1);
        players.get(0).addTile(tile2);
        //assertFalse(players.get(0).getTiles().contains(tile1));
        assertTrue(players.get(0).getTiles().contains(tile2));
        assertEquals(7, players.get(0).getTiles().size());
        players.get(0).removeTile(tile2);
    }

    @Test
    public void clearTiles() {
        players.get(0).addTile(tile1);
        players.get(1).addTile(tile2);
        players.get(0).clearTiles();
        players.get(1).clearTiles();
        assertEquals(0, players.get(0).getTiles().size());
        assertEquals(0, players.get(1).getTiles().size());
    }
}