package Server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Iterator;

public class RatingGame {
    
    final static ArrayList<Player> list = new ArrayList<>();
    
    public static void show() {
        
        ParserData.pdata.println("Player Rating:");
        
        for(Player person: list)
            ParserData.pdata.println(createStatistics(person));
        
        ParserData.pdata.println("***");
    }
    
    public static void deletePlayer(String name) {
        
        try {
            boolean ok = false;
        
            for(Iterator<Player> it = list.listIterator(); it.hasNext();) {
                if(it.next().getName().equals(name)) {
                    it.remove();
                    ok = true;
                    writeFile();
                
                    ParserData.pdata.println("Player successfully removed.");
                }
            }
        
            if(!ok) 
                ParserData.pdata.println("Player not found.");
       
            Main.rating();
        } 
        catch (Exception e) {e.printStackTrace();}
    }
    
    public static void reset() {
        
        list.clear();
        writeFile();
        
        ParserData.pdata.println("Rating cleared.");
        Main.rating();
    }
    
    public static void readFile() {
        
        list.clear();
        
        try(BufferedReader fileReader = new BufferedReader(new FileReader(System.getProperty("user.dir") + "/rating.txt"))) {
            while(fileReader.ready()) {
                
                String[] line = fileReader.readLine().split(";");
                list.add(new Player(line[0].split("-")[1],
                                    Integer.parseInt(line[1].split("-")[1]),
                                    Integer.parseInt(line[2].split("-")[1]),
                                    Integer.parseInt(line[3].split("-")[1])));
        
            }
        } catch(Exception e) {
            ParserData.pdata.println("Rating file was not loaded.");
            //e.printStackTrace();
        }
    }
    
    public static void writeFile() {
        
        try(BufferedWriter fileWriter = new BufferedWriter(new FileWriter(System.getProperty("user.dir") + "/rating.txt"))) {
            for(Player person: list)
                fileWriter.write(createStatistics(person));
        
        } catch(Exception e) {
            ParserData.pdata.println("File write error");
            e.printStackTrace();}
    }
    
    private static String createStatistics(Player person) {
        return "Name-" + person.getName()
           + "; Victories-" + person.getVictories()
           + "; Defeats-" + person.getDefeats()
           + "; Draws-" + person.getDraws();
    }
}