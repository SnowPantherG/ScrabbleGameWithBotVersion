/**
 * @author  Michael Kölling and David J. Barnes
 * @version 2016.02.29
 *
 * @author Muhammad Maisam
 * @version 2024.10.22
 */

public class GameController {
    private Parser parser;
    private ScrabbleGame game; ///change to game/////
    //private ScrabbleGameA gameA;
    private GameView view;
    private int mode; //0 setup 1 play 2 word placement
    ///Following 5 attributes correspond to word placement
    private String word_ = "";
    private String direction_ = "";
    private int x_ = 0;
    private int y_ = 0;
    private boolean wordFlag = false;
    /**
     * Create the game.
     */
    public GameController()
    {
        parser = new Parser();
        game = new ScrabbleGame();
        view = new GameView();
        play();
    }

    /**
     *  Main play routine. Loops until end of play.
     */
    public void play()
    {
        view.printWelcome();
        mode = 0;
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
     *
     */
    private void printCommandWords() {
        view.printCommandWords(parser.getCommandWords());
    }
    private void printPlayer(){view.printPlayer(game.getPlayer());}
    private void printPlayers(){view.printPlayers(game.getPlayers());}
    private void printBoard(){view.printBoard(game.getBoard());}
    private void printStatus(){view.printStatus(game.getStatus());}
    /**
     * Given a command, process (that is: execute) the command.
     * @param command The command to be processed.
     * @return true If the command ends the game, false otherwise.
     */
    private boolean processCommand(Command command) {
        boolean wantToQuit = false;

        if (command.isUnknown()) {
            view.printError(0);
            return false;
        }
        String commandWord = command.getCommandWord();
        if (commandWord.equals("help")) {//complete
            view.printHelp();
            printCommandWords();
        }
        else if (commandWord.equals("quit")) {
            wantToQuit = handleQuit(command);
        }
        else if (commandWord.equals("rules")) {//complete
            printCommandWords();
            view.printRules();
        }
        else if (commandWord.equals("player") && mode > 0) {
            printPlayer();
        }
        else if (commandWord.equals("players") && mode > 0) {
            printPlayers();
        }
        else if (commandWord.equals("board") && mode > 0) {
            printBoard();
        }
        else if (commandWord.equals("status") && mode > 0) {
            printStatus();
        }
        else if (commandWord.equals("play") && mode == 0 ) {
            mode = 1;
            handlePlay(command);
        }
        else if(mode == 0){view.printError(1);}
        else if (commandWord.equals("challenge") && mode == 1) {
            handleChallenge(command);
        }
        else if (commandWord.equals("pass") && mode == 1) {
            handlePass(command);
        }
        else if (commandWord.equals("exchange") && mode == 1) {
            handleExchange(command);
        }
        else if (commandWord.equals("word") && mode == 1) {
            handleWord(command);
        }
        else if(mode == 1){view.printError(2);}
        else if (commandWord.equals("locationX") && mode == 2) {
            handleWord(command);
        }
        else if(mode == 2){view.printError(3);}
        else if (commandWord.equals("locationY") && mode == 3) {

            handleWord(command);
        }
        else if(mode == 3){view.printError(4);}
        else if (commandWord.equals("place") && mode == 4) {
            handleWord(command);
        }
        else if(mode == 4){view.printError(5);}
        return wantToQuit;
    }

    private void handlePlay(Command command) {
        if(command.hasSecondWord()) {
            int players = convertToInteger(command.getSecondWord());
            if(players > 0 && players <= 4){
                game.play(players);
            }else{
                view.printError(6);
                //    throw new IllegalStateException("Number of players out of bound");
            }
        }
        else {
            game.play(2);
        }
        printStatus();
    }

    private void handleChallenge(Command command) {
        view.printChallenge(game.challenge());
        printStatus();
    }

    private void handlePass(Command command) {
        game.pass();
        printPlayer();
    }

    private void handleExchange(Command command) {
        game.exchange();
        printPlayer();
    }

    private void handleWord(Command command) {

        if(command.hasSecondWord()) {
            String commandWord = command.getCommandWord();
            if (commandWord.equals("word")) {
                wordFlag = false;
                word_ = command.getSecondWord();
                mode++;
            } else if (commandWord.equals("place")) {
                if((command.getSecondWord().equals("horizontal")) || (command.getSecondWord().equals("vertical"))) {
                    direction_ = command.getSecondWord();
                    wordFlag = true;
                    mode = 1;
                }else{
                    view.printError(9);
                }
            } else if (commandWord.equals("locationX")) {
                x_ = convertToInteger(command.getSecondWord());
                mode++;
            }else if (commandWord.equals("locationY")) {
                y_ = convertToInteger(command.getSecondWord());
                mode++;
            }
            if(wordFlag){
                game.word(word_, direction_, y_, x_);//swapped x and y
                printBoard();
                printPlayer();
            }
        }
        else{view.printError(8);}
    }


    /**
     * "Quit" was entered. Check the rest of the command to see
     * whether we really quit the game.
     * @return true, if this command quits the game, false otherwise.
     */
    private boolean handleQuit(Command command)
    {
        if(command.hasSecondWord()) {
            view.printError(7);
            return false;
        }
        else {
            return true;  // signal that we want to quit
        }
    }

    /**
     *Generated using AI to convert string representation of number to int
     * @param numberStr
     * @return
     */
    private static int convertToInteger(String numberStr) {
        switch (numberStr.toLowerCase()) {
            case "zero":
            case "0":
                return 0;
            case "one":
            case "1":
                return 1;
            case "two":
            case "2":
                return 2;
            case "three":
            case "3":
                return 3;
            case "four":
            case "4":
                return 4;
            case "five":
            case "5":
                return 5;
            case "six":
            case "6":
                return 6;
            case "seven":
            case "7":
                return 7;
            case "eight":
            case "8":
                return 8;
            case "nine":
            case "9":
                return 9;
            case "ten":
            case "10":
                return 10;
            case "eleven":
            case "11":
                return 11;
            case "twelve":
            case "12":
                return 12;
            case "thirteen":
            case "13":
                return 13;
            case "fourteen":
            case "14":
                return 14;
            case "fifteen":
            case "15":
                return 15;
            default:
                throw new IllegalArgumentException("Invalid number string: " + numberStr + ".\n Valid Range: 0-15");
        }
    }
}//END OF CLASS GAME CONTROLLER