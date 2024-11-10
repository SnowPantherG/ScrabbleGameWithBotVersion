import java.util.ArrayList;
import java.util.List;

/**
 * Board class will act as a board in the scrabble game.
 *
 * @author Muhammad Maisam
 * @version 2024.10.22
 *
 */

public class Board {
    private static final int BOARD_SIZE = 15;
    private final Tile[][] board;
    private List<Word> placedWords;

    public Board() {
        board = new Tile[BOARD_SIZE][BOARD_SIZE];
        placedWords = new ArrayList<>();
    }
    public char getSquare(int row, int col) {
        return board[row][col].getLetter();
    }

    public String characters(String word, int row, int col, String direction) {
        StringBuilder sb = new StringBuilder();
        List<Tile> tiles = new ArrayList<>();
        if (isValidPlacement(word, row, col, direction)) {
            for (int i = 0; i < word.length(); i++) {
                char c = word.charAt(i);
                c = Character.toUpperCase(c);
                if (direction.equals("horizontal")) {
                    if (board[row][col + i] == null) { //  ->
                        sb.append(c);
                    }else if(board[row][col + i].getLetter() != c ) { //  ->
                        sb.append(c);
                    }
                } else {
                    if (board[row + i][col] == null){
                        sb.append(c);
                    } else if(board[row + i][col].getLetter() != c) { //  ->
                        sb.append(c);
                    }
                }
            }
            return sb.toString();
        }
        else {
            return "";

        }
    }


    public void placeWord(String word, int row, int col, String direction, String player) {
        List<Tile> tiles = new ArrayList<>();
        if (isValidPlacement(word, row, col, direction)) {
            // Place the word on the board
            for (int i = 0; i < word.length(); i++) {
                Tile tile = new Tile(word.charAt(i));///delete
                tiles.add(tile);
                if (direction.equals("horizontal")) {
                    board[row][col + i] = tile;
                } else {
                    board[row + i][col] = tile;
                }
            }

            // Add the word to the placedWords list
            placedWords.add(new Word(tiles, direction, row, col, player));
        }else {
            // Handle invalid placement
            System.out.println("Invalid placement. Please try again.");
        }
    }

    private boolean isValidPlacement(String word, int row, int col, String direction) {
        boolean flag;
        // Check if the word fits within the board boundaries
        if (row < 0 || row >= BOARD_SIZE || col < 0 || col >= BOARD_SIZE) {
            return false;
        }
        /*else if ((row+word.length()) >= BOARD_SIZE || (col+word.length()) >= BOARD_SIZE) {
            System.out.println(c);
            return false;
        }*/

        // Check if the word overlaps with existing words
        for (int i = 0; i < word.length(); i++) {
            char c = word.charAt(i);
            c = Character.toUpperCase(c);
            if (direction.equals("horizontal")) {
                if (board[row][col + i] == null) { //  ->
                }
                else if(board[row][col + i].getLetter() != c && board[row][col + i] != null) { //  ->
                    return false;
                }
            }
            else if (direction.equals("vertical")){
                if (board[row + i][col] == null){

                }
                else if(board[row + i][col].getLetter() != c && board[row + i][col] != null) { //  ->
                    return false;
                }
            }
        }

        // Check if the word forms new words with existing letters
        // (Implement logic to check for intersections and new word formation)

        return true;
    }

    public List<Word> getPlacedWords() {
        return placedWords;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                if(board[i][j] == null){sb.append("-");}
                else{sb.append(board[i][j]);}
                sb.append("  ");
            }
            sb.append("\n");

        }
        return sb.toString();
    }
}