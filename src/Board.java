import java.util.ArrayList;
import java.util.List;

/**
 * Board class will act as a board in the scrabble game.
 *
 * @author Muhammad Maisam
 * @version 2024.10.22
 *
 *
 * @author Shnehao Gong
 * @version 2024.11.09
 * added method getTilesAt()
 * add situation that tiles should be fixed after player get the score
 */

public class Board {
    private static final int BOARD_SIZE = 15;
    private final Tile[][] board;
    private List<Word> placedWords;

    private boolean[][] fixedTiles; // New array to track fixed tiles

    private SquareType[][] squareTypes;

    public Board() {
        board = new Tile[BOARD_SIZE][BOARD_SIZE];
        squareTypes = new SquareType[BOARD_SIZE][BOARD_SIZE];
        placedWords = new ArrayList<>();
        fixedTiles = new boolean[BOARD_SIZE][BOARD_SIZE];
        initializeSquareTypes();
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

    public Tile getTileAt(int row, int col) {
        return board[row][col];
    }

    public void placeTile(Tile tile, int row, int col) {
        board[row][col] = tile;
        fixedTiles[row][col] = false; // Initially not fixed
    }

    public boolean removeTile(int row, int col) {
        if (!fixedTiles[row][col]) {
            board[row][col] = null;
            return true;
        }
        return false;
    }


    public void fixTile(int row, int col) {
        fixedTiles[row][col] = true;

    }

    public boolean isTileFixed(int row, int col) {
        return fixedTiles[row][col];
    }

    private void initializeSquareTypes() {
        // Initialize all squares to NORMAL by default
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                squareTypes[row][col] = SquareType.NORMAL;
            }
        }

        //Set the premium squares
        setPremiumSquares();
    }

    private void setPremiumSquares() {
        // Define the positions of premium squares based on Scrabble rules

        // Triple Word (TW) - Dark Red
        int[][] tripleWordSquares = {
                {0, 0}, {0, 7}, {0, 14},
                {7, 0}, {7, 14},
                {14, 0}, {14, 7}, {14, 14}
        };
        for (int[] pos : tripleWordSquares) {
            squareTypes[pos[0]][pos[1]] = SquareType.TRIPLE_WORD;
        }

        // Double Word (DW) - Light Red
        int[][] doubleWordSquares = {
                {1, 1}, {2, 2}, {3, 3}, {4, 4},
                {13, 13}, {12, 12}, {11, 11}, {10, 10},
                {1, 13}, {2, 12}, {3, 11}, {4, 10},
                {13, 1}, {12, 2}, {11, 3}, {10, 4},
                {7, 7} // Center square
        };
        for (int[] pos : doubleWordSquares) {
            squareTypes[pos[0]][pos[1]] = SquareType.DOUBLE_WORD;
        }

        // Triple Letter (TL) - Dark Blue
        int[][] tripleLetterSquares = {
                {5, 1}, {9, 1},
                {1, 5}, {5, 5}, {9, 5}, {13, 5},
                {5, 9}, {9, 9},
                {1, 9}, {5, 13}, {9, 13}, {13, 9}
        };
        for (int[] pos : tripleLetterSquares) {
            squareTypes[pos[0]][pos[1]] = SquareType.TRIPLE_LETTER;
        }

        // Double Letter (DL) - Light Blue
        int[][] doubleLetterSquares = {
                {0, 3}, {0, 11},
                {2, 6}, {2, 8},
                {3, 0}, {3, 7}, {3, 14},
                {6, 2}, {6, 6}, {6, 8}, {6, 12},
                {7, 3}, {7, 11},
                {8, 2}, {8, 6}, {8, 8}, {8, 12},
                {11, 0}, {11, 7}, {11, 14},
                {12, 6}, {12, 8},
                {14, 3}, {14, 11}
        };
        for (int[] pos : doubleLetterSquares) {
            squareTypes[pos[0]][pos[1]] = SquareType.DOUBLE_LETTER;
        }
    }


    public SquareType getSquareType(int row, int col) {
        return squareTypes[row][col];
    }

    public static int getBoardSize() {
        return BOARD_SIZE;
    }
}


