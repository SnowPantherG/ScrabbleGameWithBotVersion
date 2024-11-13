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
 *
 * @author Shenhao Gong
 * @version v2.0 09th November 2024
 * updated Game class, moved some command from Command class CommandWords class to here, because it is not a text based
 * game anymore.
 * added placeTile() command
 *
 * @author Muhammad Maisam
 * @version v2.1 12 November 2024
 */
public class ScrabbleGame {
    private Board board;
    private Bag tileBag;
    private final List<Player> players;
    private int currentPlayerIndex;
    private WordDictionary dictionary;
    private Word lastPlacedWord;
    private List<Position> lastPlacedTiles = new ArrayList<>();
    private GameListener gameListener;

    private boolean firstWordPlayed = false;

    public ScrabbleGame() {
        board = new Board();
        tileBag = new Bag();
        players = new ArrayList<>();
        dictionary = new WordDictionary();
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

    public void nextTurn() {
        if (isGameOver()) {
            // Handle game over logic
            endGame();
            return;
        }
        currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
    }

    public boolean reroll() {
        Player player = players.get(currentPlayerIndex);
        if (player.getRerollCount() > 0 && !tileBag.isEmpty()) {
            // Return player's tiles to the bag
            //List<Tile> tiles = player.getTiles();
           // for (Tile tile : tiles) {
            //    tileBag.addTile(tile);
           // }
            player.clearTiles();

            // Give new tiles to the player
            for (int i = 0; i < 7; i++) {
                if (!tileBag.isEmpty()) {
                    player.addTile(tileBag.removeTile());
                }

            }
            player.decrementRerollCount();
            return true;
        } else {
            return false;
        }
    }

    public void pass() {

        nextTurn();
        getCurrentPlayer().decrementTurns();
    }

    // Helper methods
    private void removeTilesFromPlayer(Player player, String characters) {
        for (char c : characters.toCharArray()) {
            Tile tileToRemove = new Tile(c);
            player.removeTile(tileToRemove);
        }
        // Refill player's rack
        while (player.remainingTiles() < 7 && !tileBag.isEmpty()) {
            player.addTile(tileBag.removeTile());
        }
    }

    public int calculateWordScore(List<Position> wordPositions, boolean isFirstWord) {
        int wordScore = 0;
        int wordMultiplier = 1;

        System.out.println("Calculating score for word at positions: " + wordPositions);

        for (Position pos : wordPositions) {
            int row = pos.row;
            int col = pos.col;
            Tile tile = board.getTileAt(row, col);
            int tileValue = tile.getValue();

            SquareType squareType = board.getSquareType(row, col);
            boolean isNewTile = lastPlacedTiles.contains(pos);

            int letterMultiplier = 1;

            System.out.println("Position: (" + row + ", " + col + "), Letter: " + tile.getLetter() +
                    ", Value: " + tileValue + ", SquareType: " + squareType + ", isNewTile: " + isNewTile);

            wordScore += tileValue * letterMultiplier;
        }

        wordScore *= wordMultiplier;

        // Bonus for using all 7 tiles in one turn
        if (lastPlacedTiles.size() == 7) {
            wordScore += 50;
            System.out.println("Applied 50-point bonus for using all 7 tiles");
        }

        System.out.println("Total word score: " + wordScore);

        return wordScore;
    }

    private List<Tile> convertStringToTiles(String word) {
        List<Tile> tiles = new ArrayList<>();
        for (char c : word.toCharArray()) {
            tiles.add(new Tile(c));
        }
        return tiles;
    }

    public Player getCurrentPlayer() {
        return players.get(currentPlayerIndex);
    }

    public Board getBoard() {
        return board;
    }

    public List<Player> getPlayers() {
        return players;
    }

    // Existing methods...

    /**
     * Checks if a string can be formed from the letters in another string.
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

    /**
     * place the tile on the to the board
     * */
    public void placeTile(char letter, int row, int col) {
        // Get the current player
        Player player = getCurrentPlayer();
        // Remove the tile from the player's rack
        Tile tileToPlace = null;
        for (Tile tile : player.getTiles()) {
            if (tile.getLetter() == letter) {
                tileToPlace = tile;
                break;
            }
        }
        if (tileToPlace != null) {
            player.removeTile(tileToPlace);
            // Place the tile on the board
            board.placeTile(tileToPlace, row, col);
            //track the placed tile
            lastPlacedTiles.add(new Position(row, col));
        } else {
            // Handle error: tile not found in player's rack
            System.out.println("Error: Tile not found in player's rack.");
        }
    }

    public void removeTileFromBoard(char letter, int row, int col) {
        Tile tile = board.getTileAt(row, col);
        if (tile != null && tile.getLetter() == letter) {
            board.removeTile(row, col);
            getCurrentPlayer().addTile(tile); // Add the tile back to the player's rack
            // Remove from lastPlacedTiles if needed
            lastPlacedTiles.removeIf(pos -> pos.row == row && pos.col == col);
        } else {
            System.out.println("Error: Tile on board does not match.");
        }
    }

    public void addTileToRack(char letter, int index) {
        Tile tile = new Tile(letter);
        getCurrentPlayer().addTile(tile);
    }

    public List<Position> getLastPlacedTilePositions() {
        return new ArrayList<>(lastPlacedTiles);
    }

    /**
     * This method analyzes the board based on the positions of the newly placed tiles and determines
     * the new word(s) formed
     *  */

    public List<WordInfo> getNewWordsFormed() {
        List<WordInfo> newWords = new ArrayList<>();

        if (lastPlacedTiles.isEmpty()) {
            return newWords;
        }

        // Identify if the placement is horizontal or vertical
        boolean isHorizontal = true;
        boolean isVertical = true;

        int firstRow = lastPlacedTiles.get(0).row;
        int firstCol = lastPlacedTiles.get(0).col;

        for (Position pos : lastPlacedTiles) {
            if (pos.row != firstRow) {
                isHorizontal = false;
            }
            if (pos.col != firstCol) {
                isVertical = false;
            }
        }

        if (!isHorizontal && !isVertical) {
            // Tiles are not in a straight line
            return newWords;
        }

        // Collect all words formed by the placement
        if (isHorizontal) {
            WordInfo mainWord = getWordAtPosition(firstRow, firstCol, true);
            newWords.add(mainWord);
            // Check for vertical cross words at each placed tile
            for (Position pos : lastPlacedTiles) {
                WordInfo crossWord = getWordAtPosition(pos.row, pos.col, false);
                if (crossWord.word.length() > 1) {
                    newWords.add(crossWord);
                }
            }
        } else if (isVertical) {
            WordInfo mainWord = getWordAtPosition(firstRow, firstCol, false);
            newWords.add(mainWord);
            // Check for horizontal cross words at each placed tile
            for (Position pos : lastPlacedTiles) {
                WordInfo crossWord = getWordAtPosition(pos.row, pos.col, true);
                if (crossWord.word.length() > 1) {
                    newWords.add(crossWord);
                }
            }
        }

        return newWords;
    }

    public boolean isValidWord(String word) {
        return dictionary.isEnglishWord(word.toLowerCase());
    }

    public void finalizeTurn() {
        // Mark the last placed tiles as fixed
        for (Position pos : lastPlacedTiles) {
            board.fixTile(pos.row, pos.col);
        }
        // If this was the first word, set the flag
        if (!firstWordPlayed) {
            firstWordPlayed = true;
        }
        // Clear lastPlacedTiles for the next turn
        lastPlacedTiles.clear();
        // Refill the player's rack
        refillPlayerRack(getCurrentPlayer());
        this.nextTurn();
    }

    public boolean isFirstWord() {
        return !firstWordPlayed;
    }

    public void refillPlayerRack(Player player) {
        while (player.getTiles().size() < 7 && !tileBag.isEmpty()) {
            Tile newTile = tileBag.removeTile();
            if (newTile != null) {
                player.addTile(newTile);
            }
        }
    }

    public boolean isGameOver() {
        // Game ends if tile bag is empty and any player has emptied their rack
        boolean tileBagEmpty = tileBag.isEmpty();
        boolean anyPlayerHasNoTiles = players.stream().anyMatch(player -> player.getTiles().isEmpty());

        // Alternatively, game ends if all players have passed consecutively
        // You might need to implement logic to track consecutive passes

        return tileBagEmpty && anyPlayerHasNoTiles;
    }

    public void endGame() {
        // Calculate final scores
        calculateFinalScores();
        // Notify the controller
        if (gameListener != null) {
            gameListener.onGameEnd();
        }
    }

    public void calculateFinalScores() {
        // Subtract the sum of the player's unplayed letters from their score
        for (Player player : players) {
            int unplayedTilesScore = player.getTiles().stream().mapToInt(Tile::getValue).sum();
            player.decrementScore(unplayedTilesScore);
        }

        // Optionally, add the sum of all other players' unplayed letters to the score of the player who used all their tiles
        // Find the player who used all their tiles
        for (Player player : players) {
            if (player.getTiles().isEmpty()) {
                int otherPlayersUnplayedScore = players.stream()
                        .filter(p -> p != player)
                        .flatMap(p -> p.getTiles().stream())
                        .mapToInt(Tile::getValue)
                        .sum();
                player.incrementScore(otherPlayersUnplayedScore);
                break;
            }
        }
    }

    public void setGameListener(GameListener listener) {
        this.gameListener = listener;
    }

    private WordInfo getWordAtPosition(int row, int col, boolean horizontal) {
        StringBuilder wordBuilder = new StringBuilder();
        List<Position> positions = new ArrayList<>();

        int startRow = row;
        int startCol = col;

        // Find the start of the word
        while (true) {
            int prevRow = horizontal ? startRow : startRow - 1;
            int prevCol = horizontal ? startCol - 1 : startCol;
            if (prevRow < 0 || prevCol < 0) break;
            if (board.getTileAt(prevRow, prevCol) != null) {
                startRow = prevRow;
                startCol = prevCol;
            } else {
                break;
            }
        }

        // Build the word
        int currentRow = startRow;
        int currentCol = startCol;
        while (currentRow >= 0 && currentCol >= 0 && currentRow < 15 && currentCol < 15) {
            Tile tile = board.getTileAt(currentRow, currentCol);
            if (tile != null) {
                wordBuilder.append(tile.getLetter());
                positions.add(new Position(currentRow, currentCol));
                if (horizontal) {
                    currentCol++;
                } else {
                    currentRow++;
                }
            } else {
                break;
            }
        }

        String word = wordBuilder.toString();
        return new WordInfo(word, positions);
    }

    public void resetGame() {
        board = new Board();
        players.clear();
        currentPlayerIndex = 0;
        lastPlacedTiles.clear();
        tileBag = new Bag();
        // Reset any other necessary game state variables
    }

    public boolean isNewTilesConnected() {
        if (lastPlacedTiles.isEmpty()) {
            return false;
        }

        // check new places tile are connected
        boolean newTilesConnected = areNewTilesConnected();
        if (!newTilesConnected) {
            return false;
        }

        // if it is the first tile, then no need to check
        if (!firstWordPlayed) {
            return true;
        }


        for (Position pos : lastPlacedTiles) {
            int row = pos.row;
            int col = pos.col;

            // check up,down,left,right
            if ((row > 0 && board.isTileFixed(row - 1, col) && !lastPlacedTiles.contains(new Position(row - 1, col))) ||
                    (row < 14 && board.isTileFixed(row + 1, col) && !lastPlacedTiles.contains(new Position(row + 1, col))) ||
                    (col > 0 && board.isTileFixed(row, col - 1) && !lastPlacedTiles.contains(new Position(row, col - 1))) ||
                    (col < 14 && board.isTileFixed(row, col + 1) && !lastPlacedTiles.contains(new Position(row, col + 1)))) {
                return true;
            }
        }

        // no connected tiles
        return false;
    }

    private boolean areNewTilesConnected() {
        if (lastPlacedTiles.size() <= 1) {
            return true; // only one tile, which means is vaild
        }

        // new learnt algorithm : BFS breadth-First Search
        Set<Position> visited = new HashSet<>();
        Queue<Position> queue = new LinkedList<>();
        Position start = lastPlacedTiles.get(0);
        queue.add(start);
        visited.add(start);

        while (!queue.isEmpty()) {
            Position current = queue.poll();

            // check neighbour  tiles
            int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
            for (int[] dir : directions) {
                int newRow = current.row + dir[0];
                int newCol = current.col + dir[1];
                Position neighbor = new Position(newRow, newCol);

                if (newRow >= 0 && newRow < 15 && newCol >= 0 && newCol < 15 &&
                        lastPlacedTiles.contains(neighbor) && !visited.contains(neighbor)) {
                    queue.add(neighbor);
                    visited.add(neighbor);
                }
            }
        }

        // see of all the tiles are checked
        return visited.size() == lastPlacedTiles.size();
    }

}
