import java.util.*;

/**
 * The ScrabbleGame class represents a simple Scrabble game implementation.
 * It manages players, the dictionary of words, the tile bag, and the game board.
 * The class allows multiple players to take turns, form words, and earn points.
 * This is a beta version for scrabble game controller , after create GUI view class, this class need to be rewritten
 *
 * @version V1.0 21st October 2024
 * @author Shenhao Gong
 *
 *
 * @version V1.1 22nd October 2024
 * @author Anique Ali
 * added docstrings
 *
 * @author Muhammad Maisam
 * @version V1.2 22nd October 2024
 * Another Implementation of Scrabble Game
 */
public class ScrabbleGame {
    //private final WordDictionary dictionary;
    private final Board board;
    private final Bag tileBag;
    private final List<Player> players;
    private int currentPlayerIndex;
    public ScrabbleGame() {
        board = new Board();
        tileBag = new Bag();
        players = new ArrayList<>();
        //dictionary = new WordDictionary();
        currentPlayerIndex = 0;
    }

    public void play(int numPlayers){
        for (int i = 0; i < numPlayers; i++) {
            players.add(new Player("Player " + (i + 1)));
        }
        // Initialize the game by dealing tiles to players
        for (Player player : players) {
            for (int i = 0; i < 7; i++) {
                player.addTile(tileBag.removeTile());
            }
        }
    }

    private int lastPlayerIndex(){
        if(currentPlayerIndex == 0){
            return 3;
        } else {
            return currentPlayerIndex-1;
        }
    }

    public String challenge(){
        return "I am command function challenge";
        /*
        Player challenger = players.get(currentPlayerIndex);
        Player challenged = players.get(lastPlayerIndex());
        ///check if last word was true if not true then penalize


        Player player = players.get(currentPlayerIndex);
            int remainingTiles = player.remainingTiles();
            while(player.remainingTiles() != 0){
                tileBag.addTile(player.removeTile(player.remainingTiles()-1));
            }
         */

    }

    public void pass(){
        Player player = players.get(currentPlayerIndex);
        if(player.getSkipTurns() <= 0){
            player.lostGame();
            int remainingTiles = player.remainingTiles();
            while(player.remainingTiles() != 0){
                tileBag.addTile(player.removeTile(player.remainingTiles()-1));
            }
        }else if(player.getSkipTurns() <= 3 && player.getSkipTurns() > 0){
            player.decrementTurns();
        }
        changeTurn();
    }


    public void exchange(){
        if(!tileBag.isEmpty()){
            Player player = players.get(currentPlayerIndex);
            int remainingTiles = player.remainingTiles();
            while(player.remainingTiles() != 0){
                tileBag.addTile(player.removeTile(player.remainingTiles()-1));
            }
            while(player.remainingTiles() != remainingTiles){
                player.addTile(tileBag.removeTile());
            }
            pass();
        }else{throw new IllegalStateException("The bag is empty");}
    }

    private void changeTurn(){
        if(currentPlayerIndex < players.size()-1 && currentPlayerIndex >= 0){
            currentPlayerIndex++;
        }else{
            currentPlayerIndex = 0;
        }
    }
    private void removeTile(String string){
        Player player = players.get(currentPlayerIndex);
        for (int i = 0; i < string.length(); i++) {
            Tile tile = new Tile(string.charAt(i));
            player.removeTile(tile);
        }
        for (int i = 0; i < string.length(); i++) {
            player.addTile(tileBag.removeTile());
        }
    }
    public void word(String word, String direction, int row, int col){
        //Check if player has valid tiles.
        Player player = players.get(currentPlayerIndex);
        String playerRack = player.getRack_();
        String characters = board.characters(word, row, col, direction);
        if(characters!="" && canFormString(playerRack, characters)){
            board.placeWord(word, row, col, direction, player.getName());
            removeTile(characters);
            changeTurn();
        } else{
            System.out.println("Invalid placement. Please try again. In scrabble game");


            //throw new IllegalStateException("The word placement is wrong");
        }
    }




    public String getPlayers(){
        String description = "Players: \n";
        for (Player player : players) {
            description = description + player.getDescription() + "\n";
        }
        description = description + "All players have been listed \n";
        return description + "\n";
    }
    public String getPlayer(){
        Player player = players.get(currentPlayerIndex);
        return player.getDescription();

    }
    public String getBoard(){
        return board.toString();
    }
    public String getStatus(){
        return getBoard() + "\n" + getPlayers() + "Current Player: \n" + getPlayer() + "\nRemaining Tiles: \n" + tileBag.getRemainingTiles();
    }

    /**
     * Generated using AI to help check if a string can be formed using the characters in another string.
     */
    private static boolean canFormString(String rack, String word) {
        String str1 = rack.toLowerCase();
        String str2 = word.toLowerCase();
        int[] charCount = new int[26];

        // Count the frequency of characters in str1
        for (char c : str1.toCharArray()) {
            charCount[c - 'a']++;
        }

        // Decrement the frequency of characters in str2
        for (char c : str2.toCharArray()) {
            charCount[c - 'a']--;
            if (charCount[c - 'a'] < 0) {
                return false;
            }
        }
        return true;
    }
}
