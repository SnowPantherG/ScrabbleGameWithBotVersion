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
    private final GameController gameController;

    private boolean firstWordPlayed = false;

    public ScrabbleGame(GameController gameController) {
        this.gameController = gameController;
        board = new Board();
        tileBag = new Bag();
        players = new ArrayList<>();
        dictionary = new WordDictionary();
        currentPlayerIndex = 0;
    }

    public void play(int numHumanPlayers, int numAIPlayers) {
        // board = new Board();

        int totalPlayers = numHumanPlayers + numAIPlayers;
        if (totalPlayers > 4) {
            throw new IllegalArgumentException("The maximum number of players is 4.");
        }

        for (int i = 0; i < numHumanPlayers; i++) {
            players.add(new Player("Player " + (i + 1)));
        }
        for (int i = 0; i < numAIPlayers; i++) {
            players.add(new AIPlayer(this.gameController, "AI Player " + (i + 1)));
        }

        // Initialize the game by dealing tiles to players
        for (Player player : players) {
            while (player.getTiles().size() < 7 && !tileBag.isEmpty()) {
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

        int playersChecked = 0;
        do {
            currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
            playersChecked++;
            if (playersChecked > players.size()) {
                // All players have status == 0, game over
                endGame();
                return;
            }
        } while (players.get(currentPlayerIndex).getCurrentStatus() == 0);

        Player currentPlayer = players.get(currentPlayerIndex);

        if (currentPlayer instanceof AIPlayer) {
            boolean aiPlayed = ((AIPlayer) currentPlayer).aiPlay(this, dictionary);  // Have the AI take its turn
            if (aiPlayed) {
                finalizeTurn();  // AI successfully placed a word
                nextTurn();

                return;
            } else {
                // AI could not place a word and passes
                currentPlayer.decrementTurns();
                if (currentPlayer.getSkipTurns() == 0) {
                    currentPlayer.lostGame();  // Disqualify the player
                }

            }
            if (gameListener != null) {
                gameListener.onTurnEnd();  // call gui to renew
            }
        }
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
        getCurrentPlayer().decrementTurns();
        nextTurn();
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
    public static boolean canFormString(String rack, String boardLetters, String word) {
        String str1 = (rack + boardLetters).toUpperCase();
        String str2 = word.toUpperCase();
        int[] charCount = new int[26];

        // Count the frequency of characters in str1 (rack + board letters)
        for (char c : str1.toCharArray()) {
            if (c >= 'A' && c <= 'Z') {
                charCount[c - 'A']++;
            }
        }

        // Decrement the frequency of characters in str2
        for (char c : str2.toCharArray()) {
            if (c >= 'A' && c <= 'Z') {
                charCount[c - 'A']--;
                if (charCount[c - 'A'] < 0) {
                    return false; // Not enough of this letter
                }
            } else {
                return false; // Invalid character in word
            }
        }
        return true;
    }

    /**
     * place the tile on the to the board
     */
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

    // In ScrabbleGame.removeTileFromBoard
    public boolean removeTileFromBoard(char letter, int row, int col) {
        Tile tile = board.getTileAt(row, col);
        if (tile != null && tile.getLetter() == letter) {
            boolean removed = board.removeTile(row, col);
            if (removed) {
                getCurrentPlayer().addTile(tile); // Add the tile back to the player's rack
                lastPlacedTiles.removeIf(pos -> pos.row == row && pos.col == col);
                System.out.println("Removed tile '" + letter + "' from board and added back to rack.");
                return true;
            } else {
                System.out.println("Error: Cannot remove fixed tile from the board.");
                return false;
            }
        } else {
            System.out.println("Error: Tile on board does not match.");
            return false;
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
     */

    public List<WordInfo> getNewWordsFormed(boolean isAiPlayer, List<Position> placedWordPositions) {
        List<WordInfo> newWords = new ArrayList<>();
        if(isAiPlayer) {
            lastPlacedTiles = placedWordPositions;
        }

        if (lastPlacedTiles.isEmpty()) {
            return newWords;
        }

        // Determine the direction of the main word
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
            // Tiles are not in a straight line; invalid placement
            System.out.println("Invalid placement: Tiles are not aligned horizontally or vertically.");
            return newWords;
        }

        // Detect the main word
        WordInfo mainWord = getWordAtPosition(firstRow, firstCol, isHorizontal);
        if (mainWord.word.length() > 1) {
            newWords.add(mainWord);
            System.out.println("Main word formed: " + mainWord.word);
        }

        // Detect cross words at each placed tile
        for (Position pos : lastPlacedTiles) {
            WordInfo crossWord = getWordAtPosition(pos.row, pos.col, !isHorizontal);
            if (crossWord.word.length() > 1) {
                newWords.add(crossWord);
                System.out.println("Cross word formed at (" + pos.row + ", " + pos.col + "): " + crossWord.word);
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

        if (gameListener != null) {
            gameListener.onTurnEnd();  // 或者创建一个新的回调，例如 onTurnEnd()
        }
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
        boolean allPlayersDisqualified = players.stream().allMatch(player -> player.getCurrentStatus() == 0);

        // Alternatively, game ends if all players have passed consecutively
        // You might need to implement logic to track consecutive passes

        return tileBagEmpty && anyPlayerHasNoTiles || allPlayersDisqualified;
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
        firstWordPlayed=false;
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
        if (lastPlacedTiles == null || lastPlacedTiles.isEmpty()) {
            throw new IllegalArgumentException("lastPlacedTiles cannot be null or empty");
        }

        if (lastPlacedTiles.size() <= 1) {
            return true; // only one tile, which means it is valid
        }

        Set<Position> visited = new HashSet<>();
        Queue<Position> queue = new LinkedList<>();
        Position start = lastPlacedTiles.get(0);

        queue.add(start);
        visited.add(start);

        while (!queue.isEmpty()) {
            Position current = queue.poll();

            // Check neighboring tiles (up, down, left, right)
            int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
            for (int[] dir : directions) {
                int newRow = current.row + dir[0];
                int newCol = current.col + dir[1];
                Position neighbor = new Position(newRow, newCol);

                // Check if neighbor is either a new tile or an existing tile on the board
                if (newRow >= 0 && newRow < 15 && newCol >= 0 && newCol < 15 &&
                        (lastPlacedTiles.contains(neighbor) || board.hasFixedTile(newRow, newCol)) &&
                        !visited.contains(neighbor)) {
                    queue.add(neighbor);
                    visited.add(neighbor);
                }
            }
        }

        // Verify all new tiles are visited
        return visited.containsAll(lastPlacedTiles);
    }



    public void rollbackLastPlacedTiles() {
        for (Position pos : lastPlacedTiles) {
            board.removeTile(pos.row, pos.col);
        }
        lastPlacedTiles.clear();
        System.out.println("Rolled back last placed tiles.");
    }

   /* public boolean isMoveValid(String mainWord, List<Position> positions) {
        // Temporarily place the tiles on the board
        Map<Position, Tile> tempTiles = new HashMap<>();
        Player currentPlayer = getCurrentPlayer();
        for (int i = 0; i < positions.size(); i++) {
            Position pos = positions.get(i);
            Tile existingTile = board.getTileAt(pos.row, pos.col);
            if (existingTile == null) {
                // Get the tile from the player's rack
                char letter = mainWord.charAt(i);
                Tile tile = currentPlayer.getTileByLetter(letter);
                if (tile == null) {
                    // Player does not have the required tile
                    return false;
                }
                tempTiles.put(pos, tile);
                board.placeTile(tile, pos.row, pos.col);
            }
        }

        // Collect all new words formed
        List<WordInfo> newWords = getNewWordsFormed();

        // Validate all new words
        for (WordInfo wordInfo : newWords) {
            if (!isValidWord(wordInfo.word)) {
                // Invalid word found
                // Remove the temporarily placed tiles
                for (Map.Entry<Position, Tile> entry : tempTiles.entrySet()) {
                    board.removeTile(entry.getKey().row, entry.getKey().col);
                }
                return false;
            }
        }

        // Remove the temporarily placed tiles
        for (Map.Entry<Position, Tile> entry : tempTiles.entrySet()) {
            board.removeTile(entry.getKey().row, entry.getKey().col);
        }

        // All words are valid
        return true;
    }*/
}
