package Server;

public class Main {
    
    static ParserGame parser;
    
    static final String copyGet = "Make a GET request: <localhost:8080/gameplay?";
    static final String copyDelete = "Make a DELETE request: <localhost:8080/gameplay?";
    static final String copyPut = "Make a PUT request: <localhost:8080/gameplay?";

    static boolean exit = false;
    
    public static void main() {
        
        //parser = new XmlFile();
        parser = new JsonFile();
        exit = false;
        
                
            ParserData.pdata.println("Welcome to the game Tic-Tac-Toe!");
            RatingGame.readFile();
            menu();
    }

    public static void menu() {
        ParserData.pdata.println(
                "Start a new game? " + copyGet + "game=start>\n"+
                "View game replays? " + copyGet + "main=replay>\n"+
                "View player ratings? " + copyGet + "main=rating>\n"+
                "Quit the game? " + copyGet + "main=exit>");
    }

    public static void rating() {
        RatingGame.show();
        ParserData.pdata.println(
            "Remove player? " + copyDelete + "rating='the name of the player to be removed'>\n"+
            "Reset rating? " + copyDelete + "rating=resetAll>\n"+
            "Exit menu? " + copyGet + "main=menu>");
    }

    public static void exit() {
        ParserData.pdata.println("Exit the game.");
        Game.game = null;
        exit = true;
    }
}