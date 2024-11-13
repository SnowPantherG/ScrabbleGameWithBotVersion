/**
 * Ani 1111
 * The scrabblegame GUI class manages all visual components and user interactions for the game. It initalizes the
 * game setup with player selection, have interactive game board with premimum sqaures, has drag and drop funcationailty with player
 * tile rack, traks the score and updates the display, includes menu items such as restart and end game for better and quick access of the user.
 *
 *
 * @author Shenhao Gong
 * @version 2024.11.09
 *
 * @author Anique Ali
 * @version 2024.11.10
 */


import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.*;
import java.awt.event.*;
import java.util.List;

public class ScrabbleGUI extends JFrame {
    private GameController controller;
    private JPanel startPanel;
    private JPanel gamePanel;
    private JTextArea messageArea;
    private JButton[][] boardButtons;
    private JPanel rackPanel;
    private JLabel[] rackLabels;
    private JComboBox<String> playerComboBox;

    // Declare buttons for the start panel
    private JButton playButton;
    private JButton helpButtonStart;
    private JButton quitButtonStart;

    // Declare buttons for the game panel
    private JButton checkButton;
    private JButton passButton;
    private JButton rerollButton;
    
    /**
     * Create a new Scrabble game with specific controller, initializes the UI and
     * @param controller the controller hat will be managing logic at back
     */
    public ScrabbleGUI(GameController controller) {
        this.controller = controller;
        initUI();
        createMenuBar();
    }

    /**
     * Initilaizes the user interface components.
     */
    private void initUI() {
        setupFrame();
        setupStartPanel();
        setupActionListeners();
    }

    /**
     * Sets up the basic frame properties like size, title and resizability.
     */
    private void setupFrame(){
        setTitle("Scrabble Game");
        setSize(600, 300);
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    /**
     * Sets up inital start panel with welcome message and invokes center panel.
     */
    private void setupStartPanel(){
        // Start Panel
        startPanel = new JPanel();
        startPanel.setLayout(new BorderLayout());

        JLabel welcomeLabel = new JLabel("Welcome to Scrabble Game!", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 36)); // Increased font size
        startPanel.add(welcomeLabel, BorderLayout.NORTH);

        // Center Panel for number of players and buttons
        JPanel centerPanel = createCenterPanel();
        startPanel.add(centerPanel, BorderLayout.CENTER);

        getContentPane().add(startPanel);
    }

    /**
     * Create center panel including game buttons and player selection panel
     * @return centerpanel The centerpanel that was created
     */
    private JPanel createCenterPanel() {

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));

        // Number of Players Panel
        JPanel playerSelectionPanel = new JPanel();
        JLabel playerLabel = new JLabel("Number of Players:");
        playerLabel.setFont(new Font("Arial", Font.PLAIN, 24)); // Increased font size
        playerSelectionPanel.add(playerLabel);

        String[] playerOptions = { "2", "3", "4" };
        JComboBox<String> playerComboBox = new JComboBox<>(playerOptions);
        playerComboBox.setFont(new Font("Arial", Font.PLAIN, 24)); // Increased font size
        playerSelectionPanel.add(playerComboBox);

        centerPanel.add(playerSelectionPanel);

        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.Y_AXIS));
        buttonsPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Play Button
        playButton = new JButton("Play");
        playButton.setFont(new Font("Arial", Font.BOLD, 24)); // Increased font size
        playButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        buttonsPanel.add(playButton);
        buttonsPanel.add(Box.createRigidArea(new Dimension(0, 10))); // Add space between buttons

        // Help Button (on start panel)
        helpButtonStart = new JButton("Help");
        helpButtonStart.setFont(new Font("Arial", Font.BOLD, 24)); // Increased font size
        helpButtonStart.setAlignmentX(Component.CENTER_ALIGNMENT);
        buttonsPanel.add(helpButtonStart);
        buttonsPanel.add(Box.createRigidArea(new Dimension(0, 10))); // Add space between buttons

        // Quit Button
        quitButtonStart = new JButton("Quit");
        quitButtonStart.setFont(new Font("Arial", Font.BOLD, 24)); // Increased font size
        quitButtonStart.setAlignmentX(Component.CENTER_ALIGNMENT);
        buttonsPanel.add(quitButtonStart);

        centerPanel.add(buttonsPanel);

        return centerPanel;
    }

    /**
     * Sets up action listener for the buttons.
     */
    private void setupActionListeners() {
        // Store the combo box reference for cleaner access
        JComboBox<?> playerComboBox = (JComboBox<?>)
                ((JPanel)((JPanel)startPanel.getComponent(1)).getComponent(0)).getComponent(1);

        playButton.addActionListener(e -> {
            int numPlayers = Integer.parseInt((String) playerComboBox.getSelectedItem());
            showGamePanel();
            controller.startGame(numPlayers);
        });

        helpButtonStart.addActionListener(e -> showHelpDialog());
        quitButtonStart.addActionListener(e -> System.exit(0));
    }

    /**
     * Creates menu bar including end game, restart, and help as menu items.
     */
    private void createMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        // Game menu
        JMenu gameMenu = new JMenu("Game");
        JMenuItem restartMenuItem = new JMenuItem("Restart");
        JMenuItem endGameMenuItem = new JMenuItem("End Game");
        JMenuItem quitMenuItem = new JMenuItem("Quit");

        restartMenuItem.addActionListener(e -> controller.restartGame());
        endGameMenuItem.addActionListener(e -> controller.endGame());
        quitMenuItem.addActionListener(e -> System.exit(0));

        gameMenu.add(restartMenuItem);
        gameMenu.add(endGameMenuItem);
        gameMenu.addSeparator();
        gameMenu.add(quitMenuItem);

        // Help menu
        JMenu helpMenu = new JMenu("Help");
        JMenuItem helpMenuItem = new JMenuItem("Help");

        helpMenuItem.addActionListener(e -> showHelpDialog());

        helpMenu.add(helpMenuItem);

        menuBar.add(gameMenu);
        menuBar.add(helpMenu);

        setJMenuBar(menuBar);
    }

    /**
     * Sets the visual appearance of the board squares, configures background color and text for different squares.
     * @param button The button representing square
     * @param squareType The types of square
     * @param row The row square
     * @param col The column of square
     */
    private void setSquareAppearance(JButton button, SquareType squareType, int row, int col) {
        button.setBackground(Color.WHITE);
        button.setText("");
    }

    /**
     * Creates and displays the main game panel with board and rack, sets up message area, player rack
     * and game buttons.
     */
    private void showGamePanel() {
        // Remove start panel
        getContentPane().remove(startPanel);

        // Initialize gamePanel
        gamePanel = new JPanel(new BorderLayout());

        // Board Panel
        JPanel boardPanel = new JPanel(new GridLayout(15, 15));
        boardButtons = new JButton[15][15];
        Board gameBoard = controller.getGame().getBoard(); // Assuming you have this method

        for (int row = 0; row < 15; row++) {
            for (int col = 0; col < 15; col++) {
                JButton cell = new JButton();
                cell.setPreferredSize(new Dimension(40, 40));
                cell.setFont(new Font("Arial", Font.BOLD, 14)); // Adjusted font size
                cell.setMargin(new Insets(0, 0, 0, 0)); // Remove margins
                cell.setHorizontalAlignment(SwingConstants.CENTER);
                cell.setActionCommand(row + "," + col);
                cell.setTransferHandler(new ValueImportTransferHandler(row, col)); // Keep drag-and-drop for placing tiles

                // Initialize hasTile property
                cell.putClientProperty("hasTile", false);

                // Set background color and label based on square type
                SquareType squareType = gameBoard.getSquareType(row, col);
                setSquareAppearance(cell, squareType, row, col);

                // Add MouseListener for right-click
                int finalRow = row;
                int finalCol = col;
                cell.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        // Check if right-click (button 3)
                        if (SwingUtilities.isRightMouseButton(e)) {
                            handleBoardRightClick(finalRow, finalCol);
                        }
                    }
                });

                boardButtons[row][col] = cell;
                boardPanel.add(cell);
            }
        }


        gamePanel.add(boardPanel, BorderLayout.CENTER);

        // Message Area
        messageArea = new JTextArea(20, 20);
        messageArea.setEditable(false);
        messageArea.setText("Instructions:\n- Drag tiles from your rack to the board to play.\n" +
                "- Right-click on a tile on the board to return it to your rack before confirming your move.\n");
        JScrollPane scrollPane = new JScrollPane(messageArea);
        gamePanel.add(scrollPane, BorderLayout.EAST);

        // **Player Rack Panel**
        rackPanel = new JPanel();
        rackPanel.setLayout(new FlowLayout());
        rackLabels = new JLabel[7];
        for (int i = 0; i < 7; i++) {
            JLabel tileLabel = new JLabel();
            tileLabel.setPreferredSize(new Dimension(40, 40));
            tileLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            tileLabel.setHorizontalAlignment(SwingConstants.CENTER);
            tileLabel.setFont(new Font("Arial", Font.BOLD, 24));
            tileLabel.setTransferHandler(new ValueExportTransferHandler("text")); // Enable drag
            tileLabel.addMouseListener(new DragMouseAdapter()); // Add mouse listener for drag
            rackLabels[i] = tileLabel;
            rackPanel.add(tileLabel);
        }

        // **Buttons Panel (Check, Pass, Reroll)**
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.Y_AXIS));

        checkButton = new JButton("Check");
        passButton = new JButton("Pass");
        rerollButton = new JButton("Reroll");

        Dimension buttonSize = new Dimension(100, 40);
        checkButton.setMaximumSize(buttonSize);
        passButton.setMaximumSize(buttonSize);
        rerollButton.setMaximumSize(buttonSize);

        checkButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        passButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        rerollButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        buttonsPanel.add(checkButton);
        buttonsPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        buttonsPanel.add(passButton);
        buttonsPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        buttonsPanel.add(rerollButton);

        // **Rack and Buttons Panel**
        JPanel rackAndButtonsPanel = new JPanel(new BorderLayout());
        rackAndButtonsPanel.add(rackPanel, BorderLayout.CENTER);
        rackAndButtonsPanel.add(buttonsPanel, BorderLayout.EAST);

        gamePanel.add(rackAndButtonsPanel, BorderLayout.SOUTH);

        // **Add action listeners for buttons**
        checkButton.addActionListener(e -> handleCheckWord());
        passButton.addActionListener(e -> controller.passTurn());
        rerollButton.addActionListener(e -> controller.rerollTiles());

        getContentPane().add(gamePanel);

        // Resize the frame to a larger size
        setSize(1000, 800);
        // Make the frame resizable
        setResizable(true);

        revalidate();
        repaint();
    }

    /**
     * Handles checking if word is correct through game controller method checkWord()
     */
    private void handleCheckWord() {
        controller.checkWord();
    }

    /**
     * Updates the board display with current state, such as tile postion and square
     * appearances.
     * @param board The board state of display
     */
    public void updateBoard(Board board) {
        for (int row = 0; row < 15; row++) {
            for (int col = 0; col < 15; col++) {
                Tile tile = board.getTileAt(row, col);
                JButton button = boardButtons[row][col];
                if (tile != null) {
                    button.setText(String.valueOf(tile.getLetter()));
                    button.setForeground(Color.BLACK); // Ensure the text is visible
                    button.putClientProperty("hasTile", true); // Mark that the button has a tile
                    if (board.isTileFixed(row, col)) {
                        button.setEnabled(false);
                        button.setBackground(Color.decode("#90EE90")); // Light green color
                        button.setOpaque(true);
                        button.setBorderPainted(false);
                    } else {
                        button.setEnabled(true);
                        button.setBackground(Color.LIGHT_GRAY); // Indicate occupied but not fixed
                    }
                } else {
                    // Reset the button to its original state
                    button.putClientProperty("hasTile", false);
                    SquareType squareType = board.getSquareType(row, col);

                    // Set the basic appearance
                    setSquareAppearance(button, squareType, row, col);




                    button.setEnabled(true);
                    button.setOpaque(true);
                    button.setBorderPainted(true);
                }
            }
        }
    }

    /**
     * Updates player racks with current tiles, manages tile display and drag and
     * drop functionality.
     * @param tiles List of tiles to display in the rack
     */
    public void updateRack(List<Tile> tiles) {
        for (int i = 0; i < rackLabels.length; i++) {
            JLabel tileLabel = rackLabels[i];
            if (i < tiles.size()) {
                Tile tile = tiles.get(i);
                tileLabel.setText(String.valueOf(tile.getLetter()));
                tileLabel.setFont(new Font("Arial", Font.BOLD, 24));
                tileLabel.setTransferHandler(new ValueExportTransferHandler("text")); // Ensure handler is set
                tileLabel.addMouseListener(new DragMouseAdapter()); // Ensure mouse listener is set
            } else {
                tileLabel.setText("");
                tileLabel.setTransferHandler(null); // Remove handler
                // Remove mouse listeners
                for (MouseListener ml : tileLabel.getMouseListeners()) {
                    if (ml instanceof DragMouseAdapter) {
                        tileLabel.removeMouseListener(ml);
                    }
                }
            }
        }
    }

    public void showMessage(String message) {
        messageArea.append(message + "\n");
        messageArea.setCaretPosition(messageArea.getDocument().getLength());
    }

    /**
     * Updates scoreboard display with the current players scores
     * @param players List of player with their score
     */
    public void updateScoreboard(List<Player> players) {
        // Update the scoreboard or message area with players' scores
        StringBuilder sb = new StringBuilder();
        sb.append("Scores:\n");
        for (Player player : players) {
            sb.append(player.getName()).append(": ").append(player.getCurrentScore()).append("\n");
        }
        messageArea.setText(sb.toString());
    }

    /**
     * Pop up dialog that shows game instructions and rules
     */
    private void showHelpDialog() {
        String helpMessage = "Welcome to Scrabble!\n\n"
                + "Instructions:\n"
                + "- Drag tiles from your rack to the board to play.\n"
                + "- Right-click on a tile on the board to return it to your rack before confirming your move.\n"
                + "- Use the 'Check' button to validate your word.\n"
                + "- Use 'Pass' to skip your turn.\n"
                + "- Use 'Reroll' to exchange your tiles (limited uses).\n"
                + "- The game ends when all tiles are used or all players pass.\n"
                + "- Highest score wins!\n\n"
                + "Enjoy the game!";
        JOptionPane.showMessageDialog(this, helpMessage, "Help", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Handles drag operation from players tile rack.
     */
    class ValueExportTransferHandler extends TransferHandler {
        public ValueExportTransferHandler(String property) {
            super(property);
        }

        @Override
        public int getSourceActions(JComponent c) {
            return MOVE;
        }

        @Override
        protected Transferable createTransferable(JComponent c) {
            JLabel label = (JLabel) c;
            return new StringSelection(label.getText());
        }

        @Override
        protected void exportDone(JComponent source, Transferable data, int action) {
            if (action == MOVE) {
                JLabel label = (JLabel) source;
                label.setText(""); // Clear the label text after moving
            }
        }
    }

    /**
     * Handles drop operation onto board squares, this is when we drop to board.
     */
    class ValueImportTransferHandler extends TransferHandler {
        private int row;
        private int col;

        public ValueImportTransferHandler(int row, int col) {
            this.row = row;
            this.col = col;
        }

        @Override
        public boolean canImport(TransferSupport support) {
            JButton button = (JButton) support.getComponent();
            boolean hasTile = (boolean) button.getClientProperty("hasTile");

            return support.isDataFlavorSupported(DataFlavor.stringFlavor) && button.isEnabled() && !hasTile;
        }

        @Override
        public boolean importData(TransferSupport support) {
            if (!canImport(support)) return false;

            try {
                String data = (String) support.getTransferable().getTransferData(DataFlavor.stringFlavor);
                JButton button = (JButton) support.getComponent();
                button.setText(data);
                button.setFont(new Font("Arial", Font.BOLD, 24));
                button.putClientProperty("hasTile", true); // Mark that the button now has a tile

                // Notify the controller of the tile placement
                controller.placeTileOnBoard(data.charAt(0), row, col);
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return false;
        }
    }

    // MouseAdapter to initiate drag from tile labels
    class DragMouseAdapter extends MouseAdapter {
        @Override
        public void mousePressed(MouseEvent e) {
            JComponent c = (JComponent) e.getSource();
            TransferHandler handler = c.getTransferHandler();
            handler.exportAsDrag(c, e, TransferHandler.MOVE);
        }
    }

    /**
     * Handles right click events on board game, this allows removal of tile from board
     * if the player decides to change tile in their turn .
     * @param row The row position of the square clicked
     * @param col The column position of the square clicked
     */
    private void handleBoardRightClick(int row, int col) {
        if (controller.isTileFixed(row, col)) {
            showMessage("Cannot remove a fixed tile.");
            return;
        }

        JButton button = boardButtons[row][col];
        String letter = button.getText();
        if (!letter.isEmpty()) {
            // Remove the letter from the board button
            // Reset the button to its original state
            button.putClientProperty("hasTile", false); // No tile on the button
            SquareType squareType = controller.getGame().getBoard().getSquareType(row, col);
            setSquareAppearance(button, squareType, row, col);

            Font originalFont = (Font) button.getClientProperty("originalFont");
            if (originalFont != null) {
                button.setFont(originalFont);
            } else {
                // Set to default font if originalFont is null
                button.setFont(new Font("Arial", Font.BOLD, 14));
            }
            // Notify the controller to remove the tile from the board and add it back to the rack
            controller.removeTileFromBoard(letter.charAt(0), row, col);
        }
    }

    /**
     * Shows current players name in message area
     * @param playerName Name of current player
     */
    public void showCurrentPlayer(String playerName) {
        messageArea.append("\nCurrent player: " + playerName + "\n");
    }

    /**
     * Shows a pop up dialog with final scores
     * @param players List of players with their final scores
     */
    public void displayFinalScores(List<Player> players) {
        StringBuilder sb = new StringBuilder();
        sb.append("Final Scores:\n");
        for (Player player : players) {
            sb.append(player.getName()).append(": ").append(player.getCurrentScore()).append("\n");
        }
        JOptionPane.showMessageDialog(this, sb.toString(), "Game Over", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Disables all the game interactions when the game ends, disabling buttons and drag
     * and drop of tiles.
     */
    public void disableGameActions() {
        // Disable buttons and inputs
        checkButton.setEnabled(false);
        passButton.setEnabled(false);
        rerollButton.setEnabled(false);
        // Optionally, disable drag-and-drop
        for (JLabel rackLabel : rackLabels) {
            rackLabel.setTransferHandler(null);
            MouseListener[] listeners = rackLabel.getMouseListeners();
            for (MouseListener ml : listeners) {
                if (ml instanceof DragMouseAdapter) {
                    rackLabel.removeMouseListener(ml);
                }
            }
        }
        // Disable board interactions
        for (int row = 0; row < 15; row++) {
            for (int col = 0; col < 15; col++) {
                JButton button = boardButtons[row][col];
                button.setEnabled(false);
            }
        }
    }
}
