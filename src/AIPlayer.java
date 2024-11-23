import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * The AIPlayer class represents a computer-controlled player in the Scrabble game.
 * It extends the Player class and adds the ability to make automated moves.
 */

public class AIPlayer extends Player {
    WordDictionary dictionary;



    /**
     * Create new AI player with a name
     * @param name The name of the AI player
     */
    public AIPlayer(String name) {
        super(name);
        dictionary=new WordDictionary();
    }

    /**
     * The aiPlay method is the main logic for how the AI takes its turn.
     * The AI will try to form a valid word using the tiles in its hand and any fixed tiles on the board.
     * The first valid word it finds will be placed on the board.
     *
     * @param game       The current state of the Scrabble game.
     * @param dictionary The dictionary to use for word validation.
     * @return boolean Returns true if word was placed successfully; false otherwise and turn is passed
     */
    public boolean aiPlay(ScrabbleGame game, WordDictionary dictionary) {
        Board board = game.getBoard();
        List<Tile> rackTiles = getTiles();

        // Convert the player's tiles to a string
        String rack = rackTiles.stream()
                .map(tile -> String.valueOf(tile.getLetter()))
                .collect(Collectors.joining())
                .toUpperCase();

        // Get available letters from the board
        String boardLetters = getAvailableBoardLetters(board).toUpperCase();

        System.out.println(getName() + "'s rack: " + rack);

        // Filter words that can be formed with rack and board letters
        List<String> possibleWords = dictionary.getAllWords().stream()
                .map(String::toUpperCase)
                .filter(word -> word.length() <= rack.length() + boardLetters.length())
                .filter(word -> ScrabbleGame.canFormString(rack, boardLetters, word))
                .collect(Collectors.toList());

        // Sort words by length or score (longer words first)
        possibleWords.sort((a, b) -> b.length() - a.length());

        // If no possible words, pass the turn
        if (possibleWords.isEmpty()) {
            System.out.println(getName() + " has no possible words to place.");
            return false;
        }

        // Try to place each word
        for (String word : possibleWords) {
            System.out.println("AI is considering word: " + word);
            boolean placed = tryPlaceWord(game, board, word, rack);
            if (placed) {
                // 成功放置主单词，接下来验证所有生成的新单词（主单词和交叉单词）
                List<WordInfo> newWords = game.getNewWordsFormed();
                boolean allValid = true;

                // 检查所有新形成的单词是否都在词典中
                for (WordInfo wordInfo : newWords) {
                    if (!game.isValidWord(wordInfo.word)) {
                        allValid = false;
                        System.out.println("Invalid word formed: " + wordInfo.word);
                        break;  // 找到一个无效单词后就停止
                    }
                }

                if (allValid) {
                    // 所有单词合法，结束回合
                    game.finalizeTurn();
                    System.out.println(getName() + " placed the word: " + word);
                    return true;
                } else {
                    // 如果有非法单词，回滚这次的放置
                    game.rollbackLastPlacedTiles();
                    System.out.println(getName() + " had to rollback the word: " + word);
                }
            }
        }

        // If no word could be placed, pass the turn
        System.out.println(getName() + " could not place a word and passes.");
        return false;
    }

    /**
     * Attempts to place a word on the board. This implementation tries to place the word
     * connecting to existing tiles on the board to form a valid word.
     *
     * @param game  The current state of the Scrabble game.
     * @param board The current state of the board.
     * @param word  The word to be placed.
     * @return Returns true if the word was successfully placed, false otherwise.
     */
    private boolean tryPlaceWord(ScrabbleGame game, Board board, String word, String rack) {
        word = word.toUpperCase(); // Ensure word is in uppercase
        int boardSize = Board.getBoardSize();
        // First, try to place the word by aligning with existing letters on the board
        for (int row = 0; row < boardSize; row++) {
            for (int col = 0; col < boardSize; col++) {
                Tile boardTile = board.getTileAt(row, col);
                if (boardTile != null) {
                    char boardLetter = boardTile.getLetter();
                    // For each index in the word where the letter matches the board letter
                    for (int i = 0; i < word.length(); i++) {
                        if (word.charAt(i) == boardLetter) {
                            // Try to place the word horizontally
                            int startCol = col - i;
                            if (startCol >= 0 && startCol + word.length() <= boardSize) {
                                List<TilePlacement> tilesToPlace = new ArrayList<>();
                                if (canPlaceWordHorizontally(board, word, row, startCol, game.isFirstWord(), rack, tilesToPlace) && dictionary.isEnglishWord(word)) {
                                    placeWordHorizontally(game, tilesToPlace);
                                    return handleScoreAndFinalize(game);
                                }
                            }
                            // Try to place the word vertically
                            int startRow = row - i;
                            if (startRow >= 0 && startRow + word.length() <= boardSize) {
                                List<TilePlacement> tilesToPlace = new ArrayList<>();
                                if (canPlaceWordVertically(board, word, startRow, col, game.isFirstWord(), rack, tilesToPlace) && dictionary.isEnglishWord(word)) {
                                    placeWordVertically(game, tilesToPlace);
                                    return handleScoreAndFinalize(game);
                                }
                            }
                        }
                    }
                }
            }
        }

        // If no placement was found, try to place the word starting from anchor points
        List<Position> anchorPoints = getAnchorPoints(board);

        if (anchorPoints.isEmpty()) {
            // First move, so use the center square as the anchor point
            int center = Board.getBoardSize() / 2;
            anchorPoints.add(new Position(center, center));
        }

        for (Position pos : anchorPoints) {
            int row = pos.row;
            int col = pos.col;

            // Try placing the word horizontally
            List<TilePlacement> tilesToPlace = new ArrayList<>();
            if (canPlaceWordHorizontally(board, word, row, col, game.isFirstWord(), rack, tilesToPlace)) {
                placeWordHorizontally(game, tilesToPlace);
                return handleScoreAndFinalize(game);
            }

            // Try placing the word vertically
            tilesToPlace.clear();
            if (canPlaceWordVertically(board, word, row, col, game.isFirstWord(), rack, tilesToPlace)) {
                placeWordVertically(game, tilesToPlace);
                return handleScoreAndFinalize(game);
            }
        }
        return false;
    }

    /**
     * Handles score calculation and finalizing the turn after placing a word.
     *
     * @param game The current state of the Scrabble game.
     * @return True if scoring and finalizing are successful, false otherwise.
     */
    private boolean handleScoreAndFinalize(ScrabbleGame game) {
        // After placing the word, calculate the score
        List<WordInfo> newWords = game.getNewWordsFormed();
        int totalScore = 0;
        for (WordInfo wordInfo : newWords) {
            if(dictionary.isEnglishWord(wordInfo.word)){
                int wordScore = game.calculateWordScore(wordInfo.positions, game.isFirstWord());


                System.out.println("Word '" + wordInfo.word + "' is valid. Score: " + wordScore);
                totalScore += wordScore;
                this.incrementScore(totalScore);
                System.out.println(getName() + "'s new score: " + getCurrentScore());
                break;
            }
            else{
                System.out.println("AI cannot find a word pass turn");
                break;
            }
        }

        // Increment AI player's score


        // Finalize the turn
        game.finalizeTurn();

        return true;
    }

    /**
     * Determines if a word can be placed on the board in the specified direction.
     *
     * @param board         The current state of the board.
     * @param word          The word to be placed.
     * @param row           The starting row.
     * @param col           The starting column.
     * @param isFirstWord   True if this is the first word being placed on the board.
     * @param rack          The letters available in the player's rack.
     * @param tilesToPlace  The list to collect TilePlacement objects for tiles to place.
     * @param direction     The direction of placement (HORIZONTAL or VERTICAL).
     * @return True if the word can be placed, false otherwise.
     */
    private boolean canPlaceWord(Board board, String word, int row, int col, boolean isFirstWord,
                                 String rack, List<TilePlacement> tilesToPlace, Direction direction) {
        int boardSize = board.getBoardSize();

        // Calculate end position based on direction
        int endRow = direction == Direction.HORIZONTAL ? row : row + word.length() - 1;
        int endCol = direction == Direction.VERTICAL ? col : col + word.length() - 1;

        // Check if the word fits within the board boundaries
        if (endRow >= boardSize || endCol >= boardSize) {

            return false;
        }

        boolean connectsToExistingWord = false;
        boolean requiresNewTile = false;
        StringBuilder rackCopy = new StringBuilder(rack); // Create a mutable copy of the rack letters

        for (int i = 0; i < word.length(); i++) {
            int currentRow = direction == Direction.HORIZONTAL ? row : row + i;
            int currentCol = direction == Direction.VERTICAL ? col : col + i;
            char letter = word.charAt(i);
            Tile boardTile = board.getTileAt(currentRow, currentCol);

            if (boardTile != null) {
                if (boardTile.getLetter() != letter) {
                    return false; // Conflicting tile found
                } else {
                    // The word overlaps with an existing tile that matches
                    connectsToExistingWord = true;

                }
            } else {
                // Check if the letter is available in the rack
                int index = rackCopy.indexOf(String.valueOf(letter));
                if (index != -1) {
                    rackCopy.deleteCharAt(index); // Use the letter from the rack
                    tilesToPlace.add(new TilePlacement(letter, currentRow, currentCol));
                    requiresNewTile = true;
                } else {
                    return false; // Letter not available in rack
                }
            }

            // Check adjacent tiles to ensure the word connects properly
            if (hasAdjacentTile(board, currentRow, currentCol)) {
                connectsToExistingWord = true;
            }
        }

        // Ensure that at least one new tile is placed
        if (!requiresNewTile) {
            System.out.println("No new tiles required for word '" + word + "'. Invalid move.");
            return false;
        }

        // For the first word, ensure it covers the center square
        if (isFirstWord) {
            int center = boardSize / 2;
            boolean coversCenter = false;
            for (int i = 0; i < word.length(); i++) {
                int checkRow = direction == Direction.HORIZONTAL ? row : row + i;
                int checkCol = direction == Direction.VERTICAL ? col : col + i;
                if (checkRow == center && checkCol == center) {
                    coversCenter = true;
                    break;
                }
            }
            if (!coversCenter) {
                System.out.println("First word '" + word + "' does not cover center square.");
                return false;
            }
        }

        // If it's not the first word, ensure the word connects to existing tiles
        if (!isFirstWord && !connectsToExistingWord) {
            System.out.println("Word '" + word + "' does not connect to existing words.");
            return false;
        }

        // All checks passed; the word can be placed
        return true;
    }

    /**
     * Checks if a word can be placed on the board horizontally.
     *
     * @param board         The current state of the board.
     * @param word          The word to be placed.
     * @param row           The starting row.
     * @param col           The starting column.
     * @param isFirstWord   True if this is the first word being placed on the board.
     * @param rack          The letters available in the player's rack.
     * @param tilesToPlace  The list to collect TilePlacement objects for tiles to place.
     * @return True if the word can be placed horizontally, false otherwise.
     */
    private boolean canPlaceWordHorizontally(Board board, String word, int row, int col, boolean isFirstWord,
                                             String rack, List<TilePlacement> tilesToPlace) {
        return canPlaceWord(board, word, row, col, isFirstWord, rack, tilesToPlace, Direction.HORIZONTAL);
    }

    /**
     * Checks if a word can be placed on the board vertically.
     *
     * @param board         The current state of the board.
     * @param word          The word to be placed.
     * @param row           The starting row.
     * @param col           The starting column.
     * @param isFirstWord   True if this is the first word being placed on the board.
     * @param rack          The letters available in the player's rack.
     * @param tilesToPlace  The list to collect TilePlacement objects for tiles to place.
     * @return True if the word can be placed vertically, false otherwise.
     */
    private boolean canPlaceWordVertically(Board board, String word, int row, int col, boolean isFirstWord,
                                           String rack, List<TilePlacement> tilesToPlace) {
        return canPlaceWord(board, word, row, col, isFirstWord, rack, tilesToPlace, Direction.VERTICAL);
    }

    private void placeWord(ScrabbleGame game, List<TilePlacement> tilesToPlace) {
        Player currentPlayer = game.getCurrentPlayer();
        System.out.println("Player's rack before placement: " + currentPlayer.getRack());

        // Backup original rack in case of failure
        List<Tile> originalRack = new ArrayList<>(currentPlayer.getTiles());
        // Record placed tiles to facilitate rollback if needed
        List<TilePlacement> placedTiles = new ArrayList<>();

        for (TilePlacement tp : tilesToPlace) {
            Tile tileToPlace = currentPlayer.getTileByLetter(tp.letter);
            if (tileToPlace != null) {
                try {
                    // Remove the tile from the player's rack is now handled by placeTile
                    game.placeTile(Character.toUpperCase(tileToPlace.getLetter()), tp.row, tp.col); // Place the tile on the board
                    placedTiles.add(tp);
                    System.out.println("Placed tile '" + tp.letter + "' at (" + tp.row + ", " + tp.col + ")");
                } catch (Exception e) { // Catch all exceptions to ensure rollback
                    System.out.println("Error: Failed to place tile '" + tp.letter + "' at (" + tp.row + ", " + tp.col + "). Reason: " + e.getMessage());
                    // Rollback placed tiles from the board
                    for (TilePlacement placed : placedTiles) {
                        game.removeTileFromBoard(placed.letter, placed.row, placed.col); // Correct method name
                    }
                    // Restore the original rack
                    restoreOriginalRack(currentPlayer, originalRack);
                    return;
                }
            } else {
                System.out.println("Error: Tile '" + tp.letter + "' not found in player's rack during placement.");
                // Rollback any tiles that have already been placed
                for (TilePlacement placed : placedTiles) {
                    game.removeTileFromBoard(placed.letter, placed.row, placed.col); // Correct method name
                }
                // Restore the original rack
                restoreOriginalRack(currentPlayer, originalRack);
                return;
            }
        }
        System.out.println("Player's rack after placement: " + currentPlayer.getRack());
    }


    /**
     * Places a word horizontally on the board.
     */
    private void placeWordHorizontally(ScrabbleGame game, List<TilePlacement> tilesToPlace) {
        placeWord(game, tilesToPlace);
    }

    /**
     * Places a word vertically on the board.
     */
    private void placeWordVertically(ScrabbleGame game, List<TilePlacement> tilesToPlace) {
        placeWord(game, tilesToPlace);
    }

    private void restoreOriginalRack(Player currentPlayer, List<Tile> originalRack) {
        currentPlayer.clearTiles();
        for (Tile tile : originalRack) {
            currentPlayer.addTile(tile);
        }
    }

    public String getAvailableBoardLetters(Board board) {
        Set<Character> boardLetters = new HashSet<>();
        int boardSize = board.getBoardSize();
        for (int row = 0; row < boardSize; row++) {
            for (int col = 0; col < boardSize; col++) {
                Tile tile = board.getTileAt(row, col);
                if (tile != null) {
                    boardLetters.add(tile.getLetter());
                }
            }
        }
        // Convert set to string
        return boardLetters.stream()
                .map(String::valueOf)
                .collect(Collectors.joining());
    }


    private boolean hasAdjacentTile(Board board, int row, int col) {
        int boardSize = board.getBoardSize();
        // Above
        if (row > 0 && board.getTileAt(row - 1, col) != null) {
            return true;
        }
        // Below
        if (row < boardSize - 1 && board.getTileAt(row + 1, col) != null) {
            return true;
        }
        // Left
        if (col > 0 && board.getTileAt(row, col - 1) != null) {
            return true;
        }
        // Right
        if (col < boardSize - 1 && board.getTileAt(row, col + 1) != null) {
            return true;
        }
        // You may include diagonal checks if your game rules allow
        return false;
    }

    /**
     * Identifies all valid anchor points on the board where new words can be placed
     * @param board The current state of the board
     * @return List lists of all valid positions where new words can be placed
     */
    private List<Position> getAnchorPoints(Board board) {
        List<Position> anchorPoints = new ArrayList<>();
        int boardSize = board.getBoardSize();

        for (int row = 0; row < boardSize; row++) {
            for (int col = 0; col < boardSize; col++) {
                if (board.getTileAt(row, col) != null) {
                    // Check adjacent squares for empty positions
                    if (row > 0 && board.getTileAt(row - 1, col) == null)
                        anchorPoints.add(new Position(row - 1, col));
                    if (row < boardSize - 1 && board.getTileAt(row + 1, col) == null)
                        anchorPoints.add(new Position(row + 1, col));
                    if (col > 0 && board.getTileAt(row, col - 1) == null)
                        anchorPoints.add(new Position(row, col - 1));
                    if (col < boardSize - 1 && board.getTileAt(row, col + 1) == null)
                        anchorPoints.add(new Position(row, col + 1));
                }
            }
        }

        return anchorPoints;
    }

    /**
     * This static class represents the placement of tile on the board with it's position and letter
     */
    private static class TilePlacement {
        char letter;
        int row;
        int col;

        /**
         * Constructs a new tile placement object.
         * @param letter The letter to be placed
         * @param row The position of the row
         * @param col The position of the column
         */
        public TilePlacement(char letter, int row, int col) {
            this.letter = letter;
            this.row = row;
            this.col = col;
        }
    }

    /**
     * Represents different direction to place the word on the board.
     */
    public enum Direction {
        HORIZONTAL(0, 1),
        VERTICAL(1, 0);

        private final int deltaRow;
        private final int deltaCol;

        /**
         * Creates a Direction enum value using deltas for movement.
         * @param deltaRow change in row position
         * @param deltaCol change in column position
         */
        Direction(int deltaRow, int deltaCol) {
            this.deltaRow = deltaRow;
            this.deltaCol = deltaCol;
        }

        /**
         * Getter to get the row movement delta for the direction
         * @return int The change in the row position
         */
        public int getDeltaRow() {
            return deltaRow;
        }

        /**
         * Getter to get the column movement delta for the direction
         * @return int The change in column position
         */
        public int getDeltaCol() {
            return deltaCol;
        }
    }
}
