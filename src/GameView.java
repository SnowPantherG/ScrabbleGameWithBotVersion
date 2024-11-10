/**
 * @author Muhammad Maisam
 * @version 2024.10.22
 */
public class GameView {

    public void GameView() {}
    /**
     * Print out the opening message for the player.
     */
    public void printWelcome()
    {
        System.out.println();
        System.out.println("Welcome to the Ultimate Scrabble Game!");
        System.out.println("Ultimate Scrabble Game is a new game.");
        System.out.println("Type 'help' if you need help.");
        System.out.println();
    }
    /**
     * Print out some help information.
     * Here we print some stupid, cryptic message and a list of the
     * command words.
     */
    public void printHelp(){
        System.out.println();
        System.out.println("Type 'rules' if you need to understand the rules.");
        System.out.println("Type 'quit' if you need to quit the game.");
        System.out.println();
    }

    /**
     * Print all valid Command Words.
     */
    public void printCommandWords(String string) {
        System.out.println("Your command words are: ");
        System.out.println(string);
    }
    /**
     * Print out the ...
     */
    public void printRules(){
        System.out.println();
        System.out.println("Rules for game:");
        System.out.println("The following syntax: 'command {}' represents that the command does not take any secondword.");
        System.out.println("The following syntax: 'command {...}' represents that the command takes a secondword. '...' will be substituted with the type of input which can be provided with the command word.");
        System.out.println();
        System.out.println("How to place a word?");
        System.out.println("Follow the following order of commands to place your word successfully!");
        System.out.println("word {...} -> locationX {...} -> locationY {...} -> place {...}");
        System.out.println();
        System.out.println("'play {int 2-4 number of players//empty space defaults to 2 players}'");
        System.out.println("'word {whatever word you want to place on board}'");
        System.out.println("'locationX {int 0-15 column number}'");
        System.out.println("'locationY {int 0-15 row number}'");
        System.out.println("'place {'vertical' or 'horizontal'}'");
        System.out.println("'pass {}' to skip turn");
        System.out.println("'challenge {}' to challenge last word");
        System.out.println("'exchnage {}' to reshuffle your rack");
        System.out.println("'status {}' print current status of game");
        System.out.println("'baoard {}' print current board of game");
        System.out.println("'player {}' print current player of game");
        //System.out.println("'players {}' print current players of game // NOT IMPLEMENTED YET");
        System.out.println();
    }
    /**
     * Print out the ...
     */
    public void printBoard(String string){
        System.out.println();
        System.out.println("Print board");
        System.out.println(string);
        System.out.println();
    }
    /**
     * Print out the ...
     */
    public void printPlayers(String string){
        System.out.println();
        System.out.println("Print Players");
        System.out.println(string);
        System.out.println();
    }
    /**
     * Print out the ...
     */
    public void printPlayer(String string){
        System.out.println();
        System.out.println("Print Player");
        System.out.println(string);
        System.out.println();
    }
    /**
     * Print out the ...
     */
    public void printStatus(String string){
        System.out.println();
        System.out.println("Print Status: ");
        System.out.println(string);
        System.out.println();
        /*Print Board
        Print Players
        Current Player
        Last Player
        Last Word*/
    }
    /**
     * Print out the ...
     */
    public void printChallenge(String string){
        System.out.println();
        System.out.println("Print Challenge:");
        System.out.println(string);
        System.out.println();
    }

    /**
     * Print out the ...
     */
    public void printError(int error){
        System.out.println();
        if (error == 0){
            System.out.println("Please Enter a Valid Command."); //error method in view
        }else if (error == 1){
            System.out.println("I think your commands are off or not in order. Please move forward with the play command");
        }else if (error == 2){
            System.out.println("I think your commands are off or not in order. Please move forward with the challenge, word, exchange or pass command");
        }else if (error == 3){
            System.out.println("I think your commands are off or not in order. Please move forward with the locationX command");
        }else if (error == 4){
            System.out.println("I think your commands are off or not in order. Please move forward with the locationY command");
        }else if (error == 5){
            System.out.println("I think your commands are off or not in order. Please move forward with the place command");
        }else if (error == 6){
            System.out.println("Number of players out of bound");
        }else if (error == 7){
            System.out.println("Quit what?");
        }else if (error == 8){
            System.out.println("Huh what? Word placement please thank you!");
        }else if (error == 9){
            System.out.println("Only 'vertical' or 'horizontal' will be accepted");
        }else if (error == 10){
            System.out.println("Invalid number string or more than 15. Range allowed is positive integers upto 15.");
        }else{
            System.out.println("ERROR DETECTED");
        }
        System.out.println();
    }
    /**
     * Print out the End.
     */
    public void printEnd()
    {
        System.out.println("Thank you for playing. Good bye.");
    }

}
