import java.util.ArrayList;
import java.util.List;

public class Board {
    private static final int BOARD_SIZE = 15;
    private final Tile[][] board;
    private List<Word> placedWords;

    public Board() {
        board = new Tile[BOARD_SIZE][BOARD_SIZE];
        placedWords = new ArrayList<>();
    }
    public Tile getSquare(int row, int col) {
        return board[row][col];
    }

    public void placeWord(String word, int row, int col, String direction) {
        if (isValidPlacement(word, row, col, direction)) {
            // Place the word on the board
            for (int i = 0; i < word.length(); i++) {
                if (direction.equals("horizontal")) {
                    board[row][col + i] = word.charAt(i);
                } else {
                    board[row + i][col] = word.charAt(i);
                }
            }

            // Add the word to the placedWords list
            placedWords.add(new Word(word, row, col, direction));
        }else {
            // Handle invalid placement
            System.out.println("Invalid placement. Please try again.");
        }
    }

    private boolean isValidPlacement(String word, int row, int col, String direction) {
        // Check if the word fits within the board boundaries
        if (row < 0 || row >= BOARD_SIZE || col < 0 || col >= BOARD_SIZE) {
            return false;
        }

        // Check if the word overlaps with existing words
        for (int i = 0; i < word.length(); i++) {
            if (direction.equals("horizontal")) {
                if (board[row][col + i] != ' ') {
                    return false;
                }
            } else {
                if (board[row + i][col] != ' ') {
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
                sb.append(board[i][j]);
            }
            sb.append("\n");

        }
        return sb.toString();
    }
}