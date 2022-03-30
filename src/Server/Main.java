package Server;

public class Main {
    
    static ParserGame parser;
    
    static final String copyGet = "Make a GET request: localhost:8080/gameplay?";
    static final String copyDelete = "Make a DELETE request: localhost:8080/gameplay?";
    static final String copyPut = "Make a PUT request: localhost:8080/gameplay?";

    static boolean exit = false;
    
    public static void main() {
        
        //parser = new XmlFile();
        parser = new JsonFile();
        exit = false;
        
                
            ParserData.pdata.println("Welcome to the game Tic-Tac-Toe!");
            RatingGame.readFile();
            parser.readNames();
            menu();
    }

    public static void menu() {
        ParserData.pdata.println("Start a new game? " + copyGet + "game=start");
        ParserData.pdata.println("View game replays? " + copyGet + "main=replay");
        ParserData.pdata.println("View player ratings? " + copyGet + "main=rating");
        ParserData.pdata.println("Quit the game? " + copyGet + "main=exit");
    }

    public static void rating() {
        RatingGame.show();
        ParserData.pdata.println("Remove player? " + copyDelete + "rating='the name of the player to be removed'");
        ParserData.pdata.println("Reset rating? " + copyDelete + "rating=resetAll");
        ParserData.pdata.println("Exit menu? " + copyGet + "main=menu");
    }

    public static void exit() {
        ParserData.pdata.println("Exit the game.");
        Game.game = null;
        exit = true;
    }
}