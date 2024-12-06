import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
/**
 * Represents the state of a Scrabble game, including the board state,
 * player states, turn order, and other game-related data. This class
 * is used for saving and restoring the game state during gameplay.
 *
 * @version 2024-12-04
 * @author Shenhao Gong
 */
public class GameState implements Serializable {
    private static final long serialVersionUID = 1L;

    private final BoardState boardState;
    private final List<PlayerState> playerStates; // Store player states
    private final int currentPlayerIndex;
    private final boolean firstWordPlayed;
    private final List<Position> lastPlacedTiles; // Store last placed tiles

    public GameState(Board board, List<Player> players, int currentPlayerIndex, boolean firstWordPlayed, List<Position> lastPlacedTiles) {
        this.boardState = board.getState(); // Save board state
        this.playerStates = new ArrayList<>();
        for (Player player : players) {
            this.playerStates.add(new PlayerState(player)); // Convert players to PlayerState
        }
        this.currentPlayerIndex = currentPlayerIndex;
        this.firstWordPlayed = firstWordPlayed;
        this.lastPlacedTiles = new ArrayList<>(lastPlacedTiles); // Save last placed tiles
    }

    public BoardState restoreBoard() {
        return boardState;
    }

    public List<Player> restorePlayers(GameController gameController) {
        List<Player> restoredPlayers = new ArrayList<>();
        for (PlayerState state : playerStates) {
            Player player;
            if (state.isAIPlayer()) {
                player = new AIPlayer(gameController, state.getName());
            } else {
                player = new Player(state.getName());
            }
            player.setCurrentScore(state.getScore());
            player.setTiles(state.getTiles()); // Restore rack tiles
            restoredPlayers.add(player);
        }
        return restoredPlayers;
    }

    public int getCurrentPlayerIndex() {
        return currentPlayerIndex;
    }

    public boolean isFirstWordPlayed() {
        return firstWordPlayed;
    }

    public List<Position> getLastPlacedTiles() {
        return lastPlacedTiles;
    }
}
