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
 */


public class ScrabbleGame {
    private final List<Player> players;
    private final WordDictionary dictionary;
    private final Bag tileBag;
    private final Map<String, Integer> board; // A simple representation for tiles placed on the board
    private int currentPlayerIndex;

    /**
     * Initialization for new scrabble game with players, bag and dictionary
     *
     * @param players A list of Player objects participating in the game.
     */

    public ScrabbleGame(List<Player> players) {
        this.players = players;
        this.dictionary = new WordDictionary();
        this.tileBag = new Bag();
        this.board = new HashMap<>();
        this.currentPlayerIndex = 0;
    }

    /**
     * The method play is where players take turn forming words or rerolling their tiles and score accordingly
     * The game ends when either the bag is empty or no players have any tiles left.
     */
    private void play() {
        for (Player player : players) {
            for (int i = 0; i < 7; i++) {
                player.addTile(tileBag.removeTile());
            }
        }

        Scanner scanner = new Scanner(System.in);
        while (!tileBag.isEmpty() && playersHaveTiles()) {
            Player currentPlayer = players.get(currentPlayerIndex);
            System.out.println("It's " + currentPlayer.getName() + "'s turn.");

            List<Tile> currentTiles = currentPlayer.getTiles();
            System.out.println("Your tiles: " + currentTiles);

            boolean validAction = false;

            while (!validAction) {
                System.out.print("Enter a word to play or type 'reroll' to exchange tiles: ");
                String word = scanner.nextLine().trim().toUpperCase();
                if (word.equals("CHECKBAG")){
                    System.out.println(tileBag.getRemainingTiles());
                }
                else if(word.equals("ENDGAME")){
                    declareWinner();
                    return;
                }
                else if (word.equals("REROLL")) {
                    if (currentPlayer.getRerollCount() > 0) {
                        currentPlayer.useReroll();
                        rerollTiles(currentPlayer);
                        System.out.println("Tiles rerolled: " + currentPlayer.getTiles());
                        validAction = true;  // Move to the next player after rerolling
                    } else {
                        System.out.println("No rerolls remaining.");
                    }
                }
                else if (dictionary.isEnglishWord(word)) {
                    if (currentPlayer.canFormWord(word)) {
                        int score = calculateScore(word);
                        currentPlayer.updateScore(score);
                        currentPlayer.removeTilesForWord(word);  // Remove used tiles
                        replenishTiles(currentPlayer);  // Draw new tiles
                        System.out.println("Word accepted! You earned " + score + " points.");
                        addWordToBoard(word);
                        validAction = true;  // Successful play, move to the next player
                    } else {
                        System.out.println("You do not have the tiles to form this word. Please try again.");
                    }
                } else {
                    System.out.println("Invalid word. Please try again.");
                }
            }

            currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
        }

        declareWinner();
    }

    /**
     * Replenishes the player's tiles after they play a word, draws from tile bag until players has 7 tiles.
     * @param player The players whose tiles are being replenished
     */
    private void replenishTiles(Player player) {
        while (player.getTiles().size() < 7 && !tileBag.isEmpty()) {
            player.addTile(tileBag.removeTile());
        }
    }

    /**
     * Checks if any players still have any tiles left.
     * @return true if players have remaining tiles, false otherwise.
     */
    private boolean playersHaveTiles() {
        return players.stream().anyMatch(player -> !player.getTiles().isEmpty());
    }

    /**
     * Adds word to the board
     * @param word The word to add to the board.
     */
    private void addWordToBoard(String word) {
        board.put(word, word.length()); // Simplified for illustration
    }

    /**
     * Calculates score for a given word by summing up the value of its tiles.
     * @param word The word played by the players
     * @return The total calculated score of the word by each tile values.
     */
    private int calculateScore(String word) {
        int score = 0;

        for (char letter : word.toCharArray()) {
            Tile tile=new Tile(letter);
            score += tile.getValue();
        }
        return score;
    }

    /**
     * The declareWinner method declares the winner based on highest scores among players
     *
     */

    private void declareWinner() {
        int highestScore = players.stream()
                .mapToInt(Player::getCurrentScore)
                .max()
                .orElse(0); // Get the highest score among all players

        List<Player> winners = new ArrayList<>();

        for (Player player : players) {
            if (player.getCurrentScore() == highestScore) {
                winners.add(player);
            }
        }

        if (winners.size() > 1) {
            System.out.print("It's a tie between: ");
            for (int i = 0; i < winners.size(); i++) {
                System.out.print(winners.get(i).getName());
                if (i < winners.size() - 1) {
                    System.out.print(" and ");
                }
            }
            System.out.println(" with " + highestScore + " points each!");
        } else if (winners.size() == 1) {
            Player winner = winners.get(0);
            System.out.println("The winner is " + winner.getName() + " with " + winner.getCurrentScore() + " points!");
        } else {
            System.out.println("No players have scored any points.");
        }
    }

    /**
     * Rerolls the player's tiles, drawing new tiles from the bag and discarding the current ones
     * @param player The player rerolling their tiles
     */
    private void rerollTiles(Player player) {
        Bag bag = this.tileBag;

        // Remove all tiles and return them to the bag (optional, depending on your rules)
        player.getTiles().clear();

        // Draw new tiles
        for (int i = 0; i < 7; i++) {
            if (!bag.isEmpty()) {
                player.addTile(bag.removeTile());
            }
        }
    }

    /**
     * Entry point of the game. Initializes players and starts the game loop
     * @param args
     */
    public static void main(String[] args) {
        List<Player> players = Arrays.asList(new Player("Player 1"), new Player("Player 2"));
        ScrabbleGame game = new ScrabbleGame(players);
        game.play();
    }
}