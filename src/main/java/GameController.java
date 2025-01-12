import javax.swing.*;
import java.io.*;
import java.util.Comparator;
import java.util.List;

/**
 * @author  Michael Kölling and David J. Barnes
 * @version 2016.02.29
 *
 * @author Muhammad Maisam
 * @version 2024.10.22
 *
 * @author Shenhao Gong
 * @version 2024.11.09
 * updated version for implement GUI
 *
 * @author Shenhao Gong
 * @version 2024-12-04
 * added save load game function, added situation if BoardStream passed in
 *
 * @author Shenhao Gong
 * @version 2024-12-04(2)
 * added determineWinner() to find the winner in all players
 */
public class GameController implements GameListener,Serializable{
    private ScrabbleGame game;
    private ScrabbleGUI gui;

    public GameController() {
        this.game = new ScrabbleGame(this);
        this.gui = new ScrabbleGUI(this);
        game.setGameListener(this);
        gui.setVisible(true);
    }

    public void startGame(int numHumanPlayers, int numAIPlayers, InputStream boardStream) {
        int totalPlayers = numHumanPlayers + numAIPlayers;
        // Ensure that the total number of players does not exceed 4
        if (totalPlayers > 4) {
            throw new IllegalArgumentException("The total number of players cannot exceed 4.");
        }

        // Reinitialize the game with the specified board configuration
        if (boardStream != null) {
            this.game = new ScrabbleGame(this, boardStream); // Pass custom board stream
        } else {
            this.game = new ScrabbleGame(this); // Default board
        }

        game.setGameListener(this);

        // Start the game with the specified number of human and AI players
        game.play(numHumanPlayers, numAIPlayers);

        // Update the GUI to reflect the state of the game
        gui.updateBoard(game.getBoard());
        gui.updateRack(game.getCurrentPlayer().getTiles());
        gui.updateScoreboard(game.getPlayers());
        gui.showMessage("Game started with " + numHumanPlayers + " human players and " + numAIPlayers + " AI players.");
        gui.enableSaveLoad();
    }


    public void checkWord() {
        List<WordInfo> newWords = game.getNewWordsFormed(false, null);
        if(newWords.isEmpty()){
            System.out.println("no words were found");
            return;
        }
        boolean allValid = true;
        if (!game.isNewTilesConnected()) {
            gui.showMessage("Tiles must be connected.");
            allValid = false;
        }
        // Validate each new word formed
        for (WordInfo wordInfo : newWords) {
            if (!game.isValidWord(wordInfo.word)) {
                allValid = false;
                gui.showMessage("Invalid word: " + wordInfo.word);
                // Optionally, reset the board or allow the player to adjust
                break;
            }
        }

        if (allValid) {
            int totalScore = 0;
            // Calculate score for each word and display messages
            for (WordInfo wordInfo : newWords) {
                int wordScore = game.calculateWordScore(wordInfo.positions, game.isFirstWord());
                totalScore += wordScore;
                gui.showMessage("Word '" + wordInfo.word + "' is valid. Score: " + wordScore);
            }

            // Update the player's score
            game.getCurrentPlayer().incrementScore(totalScore);

            // Finalize the human player's turn
            game.finalizeTurn();

            // Advance to the next player's turn
            game.nextTurn();

            // Update the GUI
            gui.updateScoreboard(game.getPlayers());
            gui.updateBoard(game.getBoard());
            gui.updateRack(game.getCurrentPlayer().getTiles());
            gui.showCurrentPlayer(game.getCurrentPlayer().getName(),game.getCurrentPlayer().getSkipTurns());

            // Inform the player
            gui.showMessage("The word: " + newWords.get(0).word + " is valid\n"
                    + "Total score for this turn: " + totalScore);
        } else {
            // Handle invalid word scenario
            gui.showMessage("Your move was invalid. Please adjust your tiles.");
            gui.updateBoard(game.getBoard());
            gui.updateRack(game.getCurrentPlayer().getTiles());
        }
    }






    public void passTurn() {
        if(game.getCurrentPlayer().getSkipTurns()>0) {
            game.pass();
            updateGameState();
            gui.showMessage("Player passed. Next player's turn.");
            gui.showMessage("Player has " + game.getCurrentPlayer().getSkipTurns()+ " skip left");
            gui.updateRack(game.getCurrentPlayer().getTiles());
        } else{
            gui.showMessage("Player has passed more than allowed turns.");
        }
    }

    public void rerollTiles() {
        boolean success = game.reroll();
        if (success) {
            gui.showMessage("Tiles rerolled.");
            gui.updateRack(game.getCurrentPlayer().getTiles());
        } else {
            if(game.getCurrentPlayer().getRerollCount()==0){
            gui.showMessage("No rerolls left.");
            }
            else{
                gui.showMessage("bag is empty");
            }
        }
    }

    public void showHelp() {
        // Show help dialog or message
        gui.showMessage("Help: Instructions for the game...");
    }

    public void quitGame() {
        // Handle quitting the game
        System.exit(0);
    }

    public void placeTileOnBoard(char letter, int row, int col) {
        game.placeTile(letter, row, col);
    }




    public ScrabbleGame getGame() {
        return game;
    }

    public void removeTileFromBoard(char letter, int row, int col) {
        boolean success = game.removeTileFromBoard(letter, row, col);
        if (success) {
            gui.updateRack(game.getCurrentPlayer().getTiles());
        } else {
            gui.showMessage("Cannot remove a fixed tile.");
        }
    }


    public void addTileToRack(char letter, int index) {
        game.addTileToRack(letter, index);
        // Update the GUI rack to reflect the added tile
        gui.updateRack(game.getCurrentPlayer().getTiles());
    }

    public boolean isTileFixed(int row, int col) {
        return game.getBoard().isTileFixed(row, col);
    }

    public void onGameEnd() {
        gui.showMessage("Game Over!");
        Player winner = determineWinner();
        String winnerMessage = "Congratulations " + winner.getName() + "! You Win!";
        gui.showVictoryScreen(winnerMessage);
    }

    public void onTurnEnd(){
        updateGameState();
    }
    public void endGame() {
        game.endGame();
        // The onGameEnd() method will be called via the listener
    }

    public void restartGame() {
        int confirm = JOptionPane.showConfirmDialog(gui, "Are you sure you want to restart the game?", "Restart Game", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            game.resetGame();
            gui.dispose();
            this.gui = new ScrabbleGUI(this);
            gui.setVisible(true);
        }
    }

    public void nextTurn(){
        game.nextTurn();

        updateGameState();
    }

    private void updateGameState() {
        gui.updateBoard(game.getBoard());
        gui.updateRack(game.getCurrentPlayer().getTiles());
        gui.updateScoreboard(game.getPlayers());
        gui.showCurrentPlayer(game.getCurrentPlayer().getName(), game.getCurrentPlayer().getSkipTurns());
    }

    public void saveGame() {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("game_state.dat"))) {
            GameState gameState = new GameState(
                    game.getBoard(),                // Current board
                    game.getPlayers(),              // Players
                    game.getCurrentPlayerIndex(),   // Current player index
                    game.isFirstWord(),             // Whether the first word was played
                    game.getLastPlacedTilePositions() // Last placed tiles
            );
            out.writeObject(gameState);
            gui.showMessage("Game saved successfully!");
        } catch (IOException e) {
            gui.showMessage("Error saving game: " + e.getMessage());
        }
    }




    public void loadGame() {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream("game_state.dat"))) {
            GameState loadedGameState = (GameState) in.readObject();
            game.restoreGameState(loadedGameState); // Delegate restoration to ScrabbleGame
            gui.updateBoard(game.getBoard());
            gui.updateRack(game.getCurrentPlayer().getTiles());
            gui.updateScoreboard(game.getPlayers());
            gui.showCurrentPlayer(game.getCurrentPlayer().getName(), game.getCurrentPlayer().getSkipTurns());
            gui.showMessage("Game loaded successfully!");
        } catch (IOException | ClassNotFoundException e) {
            gui.showMessage("Error loading game: " + e.getMessage());
        }
    }

    public void undoLastMove() {
        try {
            game.undoLastMove();
            gui.updateBoard(game.getBoard());
            gui.updateRack(game.getCurrentPlayer().getTiles());
            gui.updateScoreboard(game.getPlayers());
            gui.showCurrentPlayer(game.getCurrentPlayer().getName(), game.getCurrentPlayer().getSkipTurns());
            gui.showMessage("Last move undone.");
        } catch (Exception e) {
            gui.showMessage("Cannot undo: " + e.getMessage());
        }
    }

    public Player determineWinner() {
        return game.getPlayers().stream()
                .max(Comparator.comparingInt(Player::getCurrentScore))
                .orElseThrow(() -> new IllegalStateException("No players found"));
    }


}