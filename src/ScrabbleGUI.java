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

    // Declare buttons for the start panel
    private JButton playButton;
    private JButton helpButtonStart;
    private JButton quitButtonStart;

    // Declare buttons for the game panel
    private JButton checkButton;
    private JButton passButton;
    private JButton rerollButton;
    private JButton endGameButton;

    public ScrabbleGUI(GameController controller) {
        this.controller = controller;
        initUI();
    }

    private void initUI() {
        setTitle("Scrabble Game");
        setSize(600, 300);
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Create the menu bar
        createMenuBar();

        // Start Panel
        startPanel = new JPanel();
        startPanel.setLayout(new BorderLayout());

        JLabel welcomeLabel = new JLabel("Welcome to Scrabble Game!", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 36)); // Increased font size
        startPanel.add(welcomeLabel, BorderLayout.NORTH);

        // Center Panel for number of players and buttons
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

        startPanel.add(centerPanel, BorderLayout.CENTER);

        getContentPane().add(startPanel);

        // Add action listener for Play button
        playButton.addActionListener(e -> {
            int numPlayers = Integer.parseInt((String) playerComboBox.getSelectedItem());
            showGamePanel(); // Initialize GUI components first
            controller.startGame(numPlayers); // Then start the game
        });

        // Add action listener for Help button on the start panel
        helpButtonStart.addActionListener(e -> showHelpDialog());

        // Add action listener for Quit button on the start panel
        quitButtonStart.addActionListener(e -> System.exit(0));
    }

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
                switch (squareType) {
                    case TRIPLE_WORD:
                        cell.setBackground(Color.RED.darker());
                        cell.setText("TW");
                        break;
                    case DOUBLE_WORD:
                        cell.setBackground(Color.PINK);
                        if (row == 7 && col == 7) {
                            cell.setText("★"); // Center square
                        } else {
                            cell.setText("DW");
                        }
                        break;
                    case TRIPLE_LETTER:
                        cell.setBackground(Color.BLUE.darker());
                        cell.setText("TL");
                        break;
                    case DOUBLE_LETTER:
                        cell.setBackground(Color.CYAN);
                        cell.setText("DL");
                        break;
                    default:
                        cell.setBackground(Color.WHITE);
                        break;
                }

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



    // Method to handle checking the word
    private void handleCheckWord() {
        controller.checkWord();
    }

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
                    button.putClientProperty("hasTile", false); // No tile on the button
                    SquareType squareType = board.getSquareType(row, col);
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
                            button.setBackground(Color.ORANGE);
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
                    button.setEnabled(true);
                }
            }
        }
    }



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

    public void updateScoreboard(List<Player> players) {
        // Update the scoreboard or message area with players' scores
        StringBuilder sb = new StringBuilder();
        sb.append("Scores:\n");
        for (Player player : players) {
            sb.append(player.getName()).append(": ").append(player.getCurrentScore()).append("\n");
        }
        messageArea.setText(sb.toString());
    }

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

    // Inner class for exporting data from tile labels
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

    // Inner class for importing data into board buttons
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
            switch (squareType) {
                case TRIPLE_WORD:
                    button.setBackground(Color.RED.darker());
                    button.setText("TW");
                    break;
                case DOUBLE_WORD:
                    button.setBackground(Color.PINK);
                    if (row == 7 && col == 7) {
                        button.setText("★"); // Center square
                    } else {
                        button.setText("DW");
                    }
                    break;
                case TRIPLE_LETTER:
                    button.setBackground(Color.BLUE.darker());
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


    public void showCurrentPlayer(String playerName) {
        messageArea.append("\nCurrent player: " + playerName + "\n");
    }

    public void updateFixedTiles(Board board) {
        for (int row = 0; row < 15; row++) {
            for (int col = 0; col < 15; col++) {
                if (board.isTileFixed(row, col)) {
                    JButton button = boardButtons[row][col];
                    button.setEnabled(false);
                    button.setBackground(Color.decode("#90EE90")); // Light green color
                    button.setOpaque(true);
                    button.setBorderPainted(false);
                }
            }
        }
    }

    public void displayFinalScores(List<Player> players) {
        StringBuilder sb = new StringBuilder();
        sb.append("Final Scores:\n");
        for (Player player : players) {
            sb.append(player.getName()).append(": ").append(player.getCurrentScore()).append("\n");
        }
        JOptionPane.showMessageDialog(this, sb.toString(), "Game Over", JOptionPane.INFORMATION_MESSAGE);
    }

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
