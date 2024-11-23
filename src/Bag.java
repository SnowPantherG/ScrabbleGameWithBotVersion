import java.util.*;

/**
 * Bag class will act as bag in the scrabble game, the maximum tiles will be 98 which will represent alphabets on tiles.
 * The bag class will check if bag is empty, when tiles is drawed or used from bag it will reduce count.
 *
 * @version v1, 20th October 2024
 * @author Anique Ali
 *
 * @version v1.1, 21st October 2024
 * updated the Bag deque, change of type from character to tile object
 * @author Shenhao Gong
 *
 * @author Muhammad Maisam
 * @version 2024.10.22
 *
 */

public class Bag {
    // Constant array representing the tile distribution, distribution represents the number of alphabets/tiles that are present in real game.
    private static final char[] SCRABBLE_TILE_DISTRIBUTION = {
            'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A',  // 9 A's
            'B', 'B',  // 2 B's
            'C', 'C',  // 2 C's
            'D', 'D', 'D', 'D',  // 4 D's
            'E', 'E', 'E', 'E', 'E', 'E', 'E', 'E', 'E', 'E', 'E', 'E',  // 12 E's
            'F', 'F',  // 2 F's
            'G', 'G', 'G',  // 3 G's
            'H', 'H',  // 2 H's
            'I', 'I', 'I', 'I', 'I', 'I', 'I', 'I', 'I',  // 9 I's
            'J',  // 1 J
            'K',  // 1 K
            'L', 'L', 'L', 'L',  // 4 L's
            'M', 'M',  // 2 M's
            'N', 'N', 'N', 'N', 'N', 'N',  // 6 N's
            'O', 'O', 'O', 'O', 'O', 'O', 'O', 'O',  // 8 O's
            'P', 'P',  // 2 P's
            'Q',  // 1 Q
            'R', 'R', 'R', 'R', 'R', 'R',  // 6 R's
            'S', 'S', 'S', 'S',  // 4 S's
            'T', 'T', 'T', 'T', 'T', 'T',  // 6 T's
            'U', 'U', 'U', 'U',  // 4 U's
            'V', 'V',  // 2 V's
            'W', 'W',  // 2 W's
            'X',  // 1 X
            'Y', 'Y',  // 2 Y's
            'Z',  // 1 Z
    };

    private Deque<Tile> bag;  // double ended queue initalized to store randomly generated tiles

    /**
     * Constructor to initalize Bag with 98 random tiles
     */
    public Bag(){
        bag = new ArrayDeque<Tile>(); // initalize bag with empyt deque
        generateTilesRandomly(); //fill hte bag with random tiles after initalizing
    }

    /**
     * The method generateTilesRandomly will fill the bag with 98 random tiles based on the TILE_DISTRIBUTION
     * Randomly selects tiles and removed them from the selection tileList to ensure not tile is picked more than once
     */
    private void generateTilesRandomly() {

        //first create a list of available tiles
        List<Tile> tileList = new ArrayList<>();
        for (char tile : SCRABBLE_TILE_DISTRIBUTION ) {
            tileList.add(new Tile(tile));
        }
        // then create random object named rand
        Random rand = new Random();

        // randomly select and remove 98 tiles from tileList which is from initally coming from SCRABBLE_TILE_DISTRIBUTION
        for (int i = 0; i < 98; i++){

            //randomly select index
            int randIndex = rand.nextInt(tileList.size());

            // get tile at random index and add it to the bag

            bag.add(tileList.get(randIndex));

            // to avoid picking up tiles again we will remove the selected tiles from the list
            tileList.remove(randIndex);
        }
    }

    /**
     * This method will check to see if bag is empty
     * @return true if bag is empty and false otherwise
     */
    public boolean isEmpty() {
        return bag.isEmpty(); // This will return true if bag has no tiles left
    }

    /**
     * The method removeTile is responsible for removing and returning a tile from the beginning of the bag.
     * @return the first tile from the bag
     * @throws IllegalStateException if the bag is empty
     */
    public Tile removeTile(){
        if (this.isEmpty()){
            throw new IllegalStateException("The bag is empty"); // stateException is to handle empty bag.
        }
        return bag.removeFirst(); // we will remove the tile from the beginning
    }

    /**
     * The method addTile is responsible for adding a tile to the bag used for reshuffling rack.
     *
     * @throws IllegalStateException if the bag is full
     */
    public void addTile(Tile tile){
        if (this.getRemainingTiles() >= 98){
            throw new IllegalStateException("The bag is full"); // stateException is to handle full bag.
        }

        bag.addLast(tile); // we will add the tile
    }

    /**
     * This method will return remaining
     *
     * @return the total tiles left in the bag
     */
    public int getRemainingTiles(){
        return bag.size(); // This will return the total tiles left in bag
    }

    /**
     * This method will clear the bag and will allow to restart, should be called when resetting the game
     */
    public void clearBag() {
        bag.clear(); // clear all tiles from the bag
    }
}
