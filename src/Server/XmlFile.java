package Server;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XmlFile implements ParserGame {
    
    static File file;
    static ArrayList<String> listNames;
    
    static DocumentBuilderFactory dbf;
    static DocumentBuilder db;
    static Document doc;
    
    static Element root;
    static Element player;
    static Element game;
    static Element step;
    static Element gameResult;
    
    public void setPlayers(String one, String two) {
        
        try {
            
            dbf = DocumentBuilderFactory.newInstance();
            db = dbf.newDocumentBuilder();
            doc = db.newDocument();
            
            root = doc.createElement("Gameplay");
            doc.appendChild(root);
            
        } catch(Exception e) {
            e.printStackTrace();
        }
        
        for(int i = 0; i < 2; i++) {
            
            player = doc.createElement("Player");
            root.appendChild(player);
            player.setAttribute("id", i == 0? "1": "2");
            player.setAttribute("name", i == 0? one: two);
            player.setAttribute("symbol", i == 0? "X": "O");
        }
        
        game = doc.createElement("Game");
        root.appendChild(game);
    }
    
    public void setStep(int count, int id, String text) {
        
        step = doc.createElement("Step");
        game.appendChild(step);
        step.setAttribute("num", String.valueOf(count));
        step.setAttribute("playerId", String.valueOf(id));
        step.setTextContent(text);
    }
    
    public void gameResult(Player p) {
        
        gameResult = doc.createElement("GameResult");
        root.appendChild(gameResult);

        if(p != null) {
            player = doc.createElement("Player");
            gameResult.appendChild(player);
            player.setAttribute("id", String.valueOf(p.getID()));
            player.setAttribute("name", p.getName());
            player.setAttribute("symbol", p.getSymbol().toString());
        }
        
        else 
            gameResult.setTextContent("Draw!");
    }
    
    public void write() {
        
        this.readNames();
        
        String fileName = "game_" + (listNames.size() + 1);
        
        try{
            
            Transformer transformer = TransformerFactory.newInstance()
                                                    .newTransformer();
            DOMSource src = new DOMSource(doc);
            FileOutputStream fos = new FileOutputStream(file.getPath() + "/" + fileName + ".xml");
                
            StreamResult result = new StreamResult(fos);
                
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty(OutputKeys.STANDALONE, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");                 
            transformer.transform(src, result);
                
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    
    public void replay() {
        
        this.readNames();
        ParserData.pdata.println("List of available replays:");
        
        Collections.sort(listNames);
        
        for(String name: listNames)
            ParserData.pdata.println(name);
                
        ParserData.pdata.println("Start replay? " + Main.copyGet + "replay='replay name'");
        ParserData.pdata.println("Delete replay? " + Main.copyDelete + "replay='replay name'");
        ParserData.pdata.println("Clear all replays? " + Main.copyDelete + "replay='deleteAll'");
        ParserData.pdata.println("Exit to menu? " + Main.copyGet + "main=menu");
    }
    
    public void delete(String name) {
        if(name.equals("deleteAll")) {
            for(String names: listNames) {
                File del = new File(file.getPath() + "/" + names + ".xml");
                del.delete();
            }
            replay();
            return;
        }else 
        if(listNames.contains(name)) {
            File del = new File(file.getPath() + "/" + name + ".xml");
            del.delete();
            replay();
            return;
        }
        else 
            ParserData.pdata.println("Invalid replay name, please try again." +
            Main.copyDelete + "?replay='replay name'\n");
    }
    
    public void read(String name) {
        
        if(!listNames.contains(name)) {
            ParserData.pdata.println("Invalid replay name, please try again." +
            Main.copyGet + "?replay='replay name'\n");
            return;
        } else
        
        try {
           
            dbf = DocumentBuilderFactory.newInstance();
            db = dbf.newDocumentBuilder();
            doc = null;
        
            try(FileInputStream fis = new FileInputStream(file.getPath() + "/" + name + ".xml")) {
            
                doc = db.parse(fis);
            
            } catch(Exception e) {
                
                e.printStackTrace();
                ParserData.pdata.println("File read error.  Try again.");
                return;
            }
            
            doc.getDocumentElement().normalize();
            
            NodeList players = doc.getElementsByTagName("Player");
            NodeList steps = doc.getElementsByTagName("Step");

            Node playerOne = players.item(0);
            Node playerTwo = players.item(1);
            
            NamedNodeMap attOne = playerOne.getAttributes();
            NamedNodeMap attTwo = playerTwo.getAttributes();

            String namePlayerOne = attOne.getNamedItem("name").getNodeValue();
            String namePlayerTwo = attTwo.getNamedItem("name").getNodeValue();
            String idPlayerOne = attOne.getNamedItem("id").getNodeValue();
            String idPlayerTwo = attTwo.getNamedItem("id").getNodeValue();
            String symbolPlayerOne = attOne.getNamedItem("symbol").getNodeValue();
            String symbolPlayerTwo = attTwo.getNamedItem("symbol").getNodeValue();
            
            ParserData.pdata.println("Player " + idPlayerOne + " " + namePlayerOne + " as " + symbolPlayerOne 
                               + " vs player "+ idPlayerTwo + " " + namePlayerTwo + " as " + symbolPlayerTwo);
            
            Game.map = new int[3][3];
            
            Pattern p = Pattern.compile("([0-9])");
            
            for(int i = 0; i < steps.getLength(); i++) {

                Node step = steps.item(i);
                NamedNodeMap att = step.getAttributes();
                
                String playerId = att.getNamedItem("playerId").getNodeValue();
                String text = step.getTextContent();
                
                try {
                    
                    Matcher m = p.matcher(text);
                    int count = 0, x = 0, y = 0;
                    boolean set = false;
                    
                    while(m.find()) {
                        ++count;
                        
                        if(count == 1)
                            x = Integer.parseInt(m.group());
                        else y = Integer.parseInt(m.group());
                    }
                    ParserData.pdata.println();
                    if(count == 1) {
                        for(int a = 0; a < 3 && !set; a++) {
                            for(int j = 0; j < 3 && !set; j++) {
                                if(x == count) {
                                    Game.map[a][j] = Integer.parseInt(playerId);
                                    set = true;
                                }
                                else count++;
                            }
                        }
                    }
                        
                    else Game.map[x-1][y-1] = Integer.parseInt(playerId);
                    
                    Game.showMap();
                    //Thread.sleep(1000);
                    
                } catch (Exception e) {e.printStackTrace();}
            }
            
            Node winner = players.item(2);
            
            try {
                
                NamedNodeMap attWinner = winner.getAttributes();

                String nameWinner = attWinner.getNamedItem("name").getNodeValue();
                String symbolWinner = attWinner.getNamedItem("symbol").getNodeValue();
                String idWinner = attWinner.getNamedItem("id").getNodeValue();
            
                ParserData.pdata.println("Player " + idWinner + " -> " + nameWinner + " is winner as '" + symbolWinner + "'!");
          
            } catch(Exception e) {
                ParserData.pdata.println("Draw!");
            }
            
        } catch(Exception e) {e.printStackTrace();}
        replay();
    }
    
    public void readNames() {
        
        listNames = new ArrayList<>();
        file = new File(System.getProperty("user.dir") + "/replay");
     
        if(file.exists()) {
            if(file.listFiles() != null) {
                
                File[] list = file.listFiles();
                
                for(int i = 0; i < list.length; i++) {
                    
                    String s = list[i].getName();
                    
                    if(s.endsWith(".xml"))
                        listNames.add(s.split("\\.")[0]);
                }
            }
            
        } else file.mkdir();
    }
}