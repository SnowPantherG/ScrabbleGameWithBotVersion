/**
 * @author  Michael Kölling and David J. Barnes
 * @version 2016.02.29
 *
 * @author Muhammad Maisam
 * @version 2024.10.22
 */

public class GameController {
    private Parser parser;
    private ScrabbleGame game;
    private GameView view;
    private Integer mode; //0 setup 1 play 2 word placement
    /**
     * Create the game.
     */
    public GameController()
    {
        parser = new Parser();
        game = new ScrabbleGame();
        view = new GameView();
    }

    /**
     *  Main play routine. Loops until end of play.
     */
    public void play()
    {
        view.printWelcome();

        // Enter the main command loop.  Here we repeatedly read commands and
        // execute them until the game is over.

        boolean finished = false;
        while (! finished) {
            Command command = parser.getCommand();
            finished = processCommand(command);
        }
        view.printEnd();

    }

    /**
     * Given a command, process (that is: execute) the command.
     * @param command The command to be processed.
     * @return true If the command ends the game, false otherwise.
     */
    private boolean processCommand(Command command) {
        boolean wantToQuit = false;

        if (command.isUnknown()) {
            System.out.println("I don't know what you mean..."); //error method in view
            return false;
        }

        String commandWord = command.getCommandWord();
        if (commandWord.equals("help")) {
            view.printHelp();
        }
        else if (commandWord.equals("play") && mode == 0 ) {
            mode = 1;
            handlePlay(command);
        }
        else if (commandWord.equals("player") && mode == 0 ) {
            handlePlayer(command);
        }
        else if (commandWord.equals("rules")) {
            view.printRules();
        }
        else if (commandWord.equals("challenge") && mode == 1) {
            handleChallenge(command);
        }
        else if (commandWord.equals("status")) {
            view.printStatus();
        }
        else if (commandWord.equals("board")) {
            view.printBoard();
        }
        else if (commandWord.equals("pass")) {
            handlePass(command);
        }
        else if (commandWord.equals("exchange")) {
            handleExchange(command);
        }
        else if (commandWord.equals("word") && mode == 1) {
            mode = 1;
            handleWord(command);
        }
        else if (commandWord.equals("place") && mode == 2) {
            mode = 1;
            handlePlace(command);
        }
        else if (commandWord.equals("location") && mode == 2) {
            handleLocation(command);
        }
        else if (commandWord.equals("quit")) {
            wantToQuit = handleQuit(command);
        }
        return wantToQuit;
    }

    private void handlePlay(Command command) {
        if(command.hasSecondWord()) {
            int players = (int) command.getSecondWord();
            System.out.println(command.getSecondWord());//delete
            System.out.println(players);//delete
            game.play(players);
        }
        else {
            game.play(2);  // signal that we want to quit
        }

    }

    private void handlePlayer(Command command) {
        game.player()
    }


    private void handleChallenge(Command command) {
        game.challenge()
    }

    private void handlePass(Command command) {
        game.pass()
    }

    private void handleExchange(Command command) {
        game.exchange()
    }

    private void handleWord(Command command) {
        game.word()
    }

    private void handlePlace(Command command) {
        game.place()
    }

    private void handleLocation(Command command) {
        game.location()
    }

    /**
     * "Quit" was entered. Check the rest of the command to see
     * whether we really quit the game.
     * @return true, if this command quits the game, false otherwise.
     */
    private boolean handleQuit(Command command)
    {
        if(command.hasSecondWord()) {
            System.out.println("Quit what?");
            return false;
        }
        else {
            return true;  // signal that we want to quit
        }
    }
}//END OF CLASS GAME CONTROLLER