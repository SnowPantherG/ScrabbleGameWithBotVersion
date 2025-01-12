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
 *
 * @author Shenhao Gong
 * @version 2024-12-04
 * added save/load in memubar, added undo button, added choose board on startPanel
 *
 * @author Shenhao Gong
 * @version 2024-12-05
 * added method to show victory screen
 */


import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.*;
import java.awt.event.*;
import java.io.InputStream;
import java.io.Serializable;
import java.util.List;

public class ScrabbleGUI extends JFrame implements Serializable {
    private GameController controller;
    private JPanel startPanel;
    private JPanel gamePanel;
    private JTextArea messageArea;
    private JButton[][] boardButtons;
    private JPanel rackPanel;
    private JLabel[] rackLabels;
    private JComboBox<String> playerComboBox;
    private JComboBox<String> aiPlayerComboBox;
    JComboBox<String> boardComboBox;

    // Declare buttons for the start panel
    private JButton playButton;
    private JButton helpButtonStart;
    private JButton quitButtonStart;

    // Declare buttons for the game panel
    private JButton checkButton;
    private JButton passButton;
    private JButton rerollButton;
    private JButton undoButton;

    private JMenuItem endGameMenuItem;
    JMenuItem saveMenuItem;
    JMenuItem loadMenuItem;

    private final Font tileFont=new Font("Arial Unicode MS",Font.BOLD,20);
    InputStream inputStream;

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
        setSize(600, 400);
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

        // Number of Human Players Panel
        JPanel playerSelectionPanel = new JPanel();
        JLabel playerLabel = new JLabel("Number of Human Players:");
        playerLabel.setFont(new Font("Arial", Font.PLAIN, 24)); // Increased font size
        playerSelectionPanel.add(playerLabel);

        String[] playerOptions = { "1", "2", "3", "4" };
        playerComboBox = new JComboBox<>(playerOptions);
        playerComboBox.setFont(new Font("Arial", Font.PLAIN, 24)); // Increased font size
        playerSelectionPanel.add(playerComboBox);

        centerPanel.add(playerSelectionPanel);

        // Number of AI Players Panel
        JPanel aiPlayerSelectionPanel = new JPanel();
        JLabel aiPlayerLabel = new JLabel("Number of AI Players:");
        aiPlayerLabel.setFont(new Font("Arial", Font.PLAIN, 24)); // Increased font size
        aiPlayerSelectionPanel.add(aiPlayerLabel);

        String[] aiPlayerOptions = { "0", "1", "2", "3" };
        aiPlayerComboBox = new JComboBox<>(aiPlayerOptions);
        aiPlayerComboBox.setFont(new Font("Arial", Font.PLAIN, 24)); // Increased font size
        aiPlayerSelectionPanel.add(aiPlayerComboBox);

        centerPanel.add(aiPlayerSelectionPanel);

        // Choose Board Panel
        JPanel boardSelectionPanel = new JPanel();
        JLabel boardLabel = new JLabel("Choose Board:");
        boardLabel.setFont(new Font("Arial", Font.PLAIN, 24)); // Increased font size
        boardSelectionPanel.add(boardLabel);

        String[] boardOptions = { "Default", "Board 1", "Board 2", "Board 3", "Board 4", "Board 5" };
        boardComboBox = new JComboBox<>(boardOptions);
        boardComboBox.setFont(new Font("Arial", Font.PLAIN, 24)); // Increased font size
        boardSelectionPanel.add(boardComboBox);

        centerPanel.add(boardSelectionPanel);
        
        
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

        JComboBox<?> aiPlayerComboBox = (JComboBox<?>)
                ((JPanel)((JPanel)startPanel.getComponent(1)).getComponent(1)).getComponent(1);

        playButton.addActionListener(e -> {
            int numHumanPlayers = Integer.parseInt((String) playerComboBox.getSelectedItem());
            int numAIPlayers = Integer.parseInt((String) aiPlayerComboBox.getSelectedItem());
            String boardChoice = (String) boardComboBox.getSelectedItem();

            // Ensure the total number of players is not greater than 4
            if (numHumanPlayers + numAIPlayers > 4) {
                JOptionPane.showMessageDialog(this, "The total number of players cannot exceed 4.", "Invalid Input", JOptionPane.ERROR_MESSAGE);
                return;
            }
            switch (boardChoice) {
                case "Board 1":
                    inputStream = getClass().getClassLoader().getResourceAsStream("board1.xml");

                    break;
                case "Board 2":
                    inputStream = getClass().getClassLoader().getResourceAsStream("board2.xml");
                    break;
                case "Board 3":
                    inputStream = getClass().getClassLoader().getResourceAsStream("board3.xml");
                    break;
                case "Board 4":
                    inputStream = getClass().getClassLoader().getResourceAsStream("board4.xml");
                    break;
                case "Board 5":
                    inputStream = getClass().getClassLoader().getResourceAsStream("board5.xml");
                    break;
                default:
                    inputStream = null; // Use default board
            }

            showGamePanel();
            controller.startGame(Integer.parseInt((String) playerComboBox.getSelectedItem()),
                    Integer.parseInt((String) aiPlayerComboBox.getSelectedItem()),
                    inputStream);
            endGameMenuItem.setEnabled(true);
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
        endGameMenuItem = new JMenuItem("End Game");
        JMenuItem quitMenuItem = new JMenuItem("Quit");
        saveMenuItem = new JMenuItem("Save");
        loadMenuItem = new JMenuItem("Load");

        restartMenuItem.addActionListener(e -> controller.restartGame());
        endGameMenuItem.addActionListener(e -> controller.endGame());
        endGameMenuItem.setEnabled(false);   //while no game is playing, initially set disabled
        quitMenuItem.addActionListener(e -> System.exit(0));
        saveMenuItem.addActionListener(e -> controller.saveGame());
        loadMenuItem.addActionListener(e -> controller.loadGame());

        gameMenu.add(restartMenuItem);
        gameMenu.add(endGameMenuItem);
        gameMenu.addSeparator();
        gameMenu.add(quitMenuItem);
        gameMenu.add(saveMenuItem);
        gameMenu.add(loadMenuItem);

        saveMenuItem.setEnabled(false); //intially disabled
        loadMenuItem.setEnabled(false);

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
        switch (squareType) {
            case TRIPLE_WORD:
                button.setBackground(Color.RED.darker());
                button.setText("TW");
                break;
            case DOUBLE_WORD:
                button.setBackground(Color.PINK);
                if (row == 7 && col == 7) {
                    button.setFont(new Font("Arial Unicode MS", Font.BOLD, 28));
                    button.setText("★"); // Center square
                } else {
                    button.setText("DW");
                }
                break;
            case TRIPLE_LETTER:
                button.setBackground(Color.orange);
                button.setText("TL");
                break;
            case DOUBLE_LETTER:
                button.setBackground(Color.CYAN);
                button.setText("DL");
                break;
            default:
                button.setBackground(Color.WHITE);
                button.setText("");
                break;
        }
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
        undoButton = new JButton("Undo");

        Dimension buttonSize = new Dimension(100, 40);
        checkButton.setMaximumSize(buttonSize);
        passButton.setMaximumSize(buttonSize);
        rerollButton.setMaximumSize(buttonSize);
        undoButton.setMaximumSize(buttonSize);

        checkButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        passButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        rerollButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        undoButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        buttonsPanel.add(checkButton);
        buttonsPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        buttonsPanel.add(passButton);
        buttonsPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        buttonsPanel.add(rerollButton);
        buttonsPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        buttonsPanel.add(undoButton);

        // **Rack and Buttons Panel**
        JPanel rackAndButtonsPanel = new JPanel(new BorderLayout());
        rackAndButtonsPanel.add(rackPanel, BorderLayout.CENTER);
        rackAndButtonsPanel.add(buttonsPanel, BorderLayout.EAST);

        gamePanel.add(rackAndButtonsPanel, BorderLayout.SOUTH);

        // **Add action listeners for buttons**
        checkButton.addActionListener(e -> handleCheckWord());
        passButton.addActionListener(e -> controller.passTurn());
        rerollButton.addActionListener(e -> controller.rerollTiles());
        undoButton.addActionListener(e -> controller.undoLastMove());


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
                    button.setFont(tileFont);
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

                    // Ensure center square appears correctly
                    if (row == 7 && col == 7 && squareType == SquareType.DOUBLE_WORD) {
                        button.setFont(new Font("Arial Unicode MS", Font.BOLD, 28));
                        button.setText("★");
                    }

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
                if(row==7&&col==7){
                    button.setFont(new Font("Arial Unicode MS", Font.BOLD, 28));
                }else{
                     // Set to default font if originalFont is null
                      button.setFont(new Font("Arial", Font.BOLD, 14));
                }
            }
            // Notify the controller to remove the tile from the board and add it back to the rack
            controller.removeTileFromBoard(letter.charAt(0), row, col);
        }
    }

    /**
     * Shows current players name in message area
     * @param playerName Name of current player
     */
    public void showCurrentPlayer(String playerName, int reroll) {
        messageArea.append("\nCurrent player: " + playerName + "\n");
        messageArea.append("\nPass time left:"+ reroll +"\n");

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
        undoButton.setEnabled(false);
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

    public JMenuItem getEndGameMenuItem() {
        return endGameMenuItem;
    }


    public void enableSaveLoad(){
        saveMenuItem.setEnabled(true); //intially disabled
        loadMenuItem.setEnabled(true);
    }

    public void showVictoryScreen(String winnerMessage) {
        getContentPane().removeAll(); // Remove all existing components
        VictoryPanel victoryPanel = new VictoryPanel(winnerMessage);
        add(victoryPanel);
        revalidate();
        repaint();
    }

}
