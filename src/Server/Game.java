package Server;

public class Game {
    
    Step[] steps = new Step[9];
    static int countStep = 0;
    static int[][] map;
    static Player playerOne, playerTwo;
    static Game game;
    static boolean exit = false, end = false;
    
    public Game(Player one, Player two) {
        playerOne = one;
        playerTwo = two;
        
        game = this;
        map = new int[3][3];
        exit = false; end = false;
    }
    
    public static void start() {
            System.out.println("???");
        
            if(game == null) {
                ParserData.pdata.println(Main.copyPut + "playerOne='first player name'&" +
                    "playerTwo='second player name'\n");
            } 
        
            else {
                ParserData.pdata.println("Want to change players?");
                ParserData.pdata.println("Yes?");
                ParserData.pdata.println(Main.copyPut + "playerOne='first player name'&" +
                "playerTwo='second player name'");
                ParserData.pdata.println("No?");
                ParserData.pdata.println(Main.copyGet + "game=game");
            }
    }
    
    public static void login(String nameOne, String nameTwo) {
        playerOne = null;
        playerTwo = null;

        if(nameOne.equals(nameTwo)) {
            ParserData.pdata.println("The names match!  Enter different names." +
                Main.copyPut + "playerOne='first player name'&" +
                "playerTwo='second player name'\n");
            
        }
            for(Player person: RatingGame.list) {
                if(person.getName().equals(nameOne)) 
                    playerOne = person;
                
                if(person.getName().equals(nameTwo))
                    playerTwo = person;
            }
        
            if(playerOne == null) {
                playerOne = new Player(nameOne, 0, 0, 0);
                playerOne.setSymbol(Symbol.X);
                playerOne.setID(1);
                RatingGame.list.add(playerOne);
            }
            if(playerTwo == null) {
                playerTwo = new Player(nameTwo, 0, 0, 0);
                playerTwo.setSymbol(Symbol.O);
                playerTwo.setID(2);
                RatingGame.list.add(playerTwo);
            }
        game();
    }
    
    public static void game() {
        new Game(playerOne, playerTwo);
        Main.parser.setPlayers(playerOne.getName(), playerTwo.getName());

        showMap();
                
        ParserData.pdata.println("First player - " +
            playerOne.getName() + " makes a move.");
        ParserData.pdata.println(Main.copyPut + "moveOne='free cell number'");
    }
    
    public static void move(Player player, String value) {
        
        setValue(player.getID(), value);
        showMap();
        testMap(player.getID());
        
        if(game.steps[8] != null && !end) {
            ParserData.pdata.println("Draw!");
                    
            playerOne.addDraws();
            playerTwo.addDraws();
                    
            Main.parser.gameResult(null);
                    
            end = true;
            RatingGame.writeFile();
            Main.parser.write();
            Main.menu();
            return;
        }
        
        if(end) {
            gameResult(player);
        } else {
            if(player.getID() == 1) {
                ParserData.pdata.println("Second player - " +
                    playerTwo.getName() + " makes a move.");
                ParserData.pdata.println(Main.copyPut + "moveTwo='free cell number'");
            }else {ParserData.pdata.println("First player - " +
                    playerOne.getName() + " makes a move.");
                ParserData.pdata.println(Main.copyPut + "moveOne='free cell number'");
            }
        }
    }
    
    public static void gameResult(Player player) {
        
        ParserData.pdata.println("Player " + player.getName() + ", play for '" + player.getSymbol() + "', won!");
        player.addVictories();
        
        if(player == playerOne)
            playerTwo.addDefeats();
        else
            playerOne.addDefeats();
        
        Main.parser.gameResult(player);

        RatingGame.writeFile();
        Main.parser.write();
        Main.menu();
    }
    
    public static void showMap() {
        
        int count = 1;
                
        for(int i = 0; i < 3; i++) {
            
            StringBuilder lineMap = new StringBuilder();

            for(int j = 0; j < 3; j++) {
                lineMap.append("[");
                if(map[i][j] == 0)
                    lineMap.append(count);
                if(map[i][j] == 1)
                    lineMap.append("X");
                if(map[i][j] == 2)
                    lineMap.append("O");
                lineMap.append("]");
                count++;
            }
            ParserData.pdata.println(lineMap);
            
        }
        //ParserData.pdata.println();
    }
    
    public static void setValue(int player, String value) {
        
        try {
            int set = Integer.parseInt(value);
            int count = 1;
            if(set > 9 || set < 1)
                ParserData.pdata.println("Invalid cell number!");
            
            else {
                
                for(int i = 0; i < 3; i++) {
                    for(int j = 0; j < 3; j++) {
                        if(set == count) {
                            if(map[i][j] == 0) {
                                map[i][j] = player;
                            
                                Main.parser.setStep(countStep + 1, player, String.valueOf(set));
                                return;
                            }
                            else {
                                ParserData.pdata.println("Cell is already occupied!");
                                
                            }
                        }
                        count++;
                    }
                }
            }
            
        } catch (Exception e) {
            //e.printStackTrace();
            ParserData.pdata.println("Invalid cell number!");
        }
        
        ParserData.pdata.println("Try again.");
        if(player == 2)
                ParserData.pdata.println("Second player - " +
                playerTwo.getName() + " makes a move." +
                Main.copyPut + "moveTwo='free cell number'");
            else ParserData.pdata.println("First player - " +
                playerOne.getName() + " makes a move." +
                Main.copyPut + "moveOne='free cell number'");
    }
    
    public static void testMap(int player) {
        
        if(map[0][0] == player &&
           map[1][1] == player &&
           map[2][2] == player) {
                
            end = true;
            return;
        }
        
        if(map[0][2] == player &&
           map[1][1] == player &&
           map[2][0] == player) {
            
            end = true;
            return;
        }
        
        for(int i = 0; i < 3; i++) {
            if(map[0][i] == player &&
               map[1][i] == player &&
               map[2][i] == player) {
                    
                end = true;
                return;
            }
            
            if(map[i][0] == player &&
               map[i][1] == player &&
               map[i][2] == player) {
                    
                end = true;
                return;
            }
        }
    }
}