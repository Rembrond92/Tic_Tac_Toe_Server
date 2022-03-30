package Server;

import java.io.*;
import java.net.*;
import java.text.DateFormat;
import java.util.Date;
import java.util.TimeZone;

class SimpleWEBServer extends Thread {

    Socket s;
    InputStream is; OutputStream os;
    
    String path, request;
    int r;
    byte[] buf;
    
    public static void main(String args[]) {
        
        try {
            
            ServerSocket server = new ServerSocket(8080, 1, InetAddress.getByName("localhost"));
            System.out.println("server is started");

            
            while(!Main.exit) {
                new SimpleWEBServer(server.accept());
            }
            
            server.close();
        }
        catch(Exception e)
        {System.out.println("init error: "+e);} 
    }

    public SimpleWEBServer(Socket s) {
        
        this.s = s;
        setDaemon(true);
        setPriority(NORM_PRIORITY);
        start();
    }

    public void run() {
        
        try {
            is = s.getInputStream();
            os = s.getOutputStream();

            buf = new byte[64*1024];
            r = is.read(buf);

            new ParserData();
            
            request = new String(buf, 0, r);
            
            /*ParserData.massage.write("HTTP/1.1 200 OK\n" +
            "Connection: open\n"
                + "Server: SimpleWEBServer\n"
                + "Pragma: no-cache\n"
                + "Accept-Charset: UTF-8\n"
                + "Content-Type: text/txt; charset=utf-8"
                + "Accept-Language: ru-RU,ru;q=0.9,en-US;q=0.8,en;q=0.7\n\n"
                );
            */
            String line = request.split("gameplay\\?")[1].split("[ \n]")[0];
            
            //System.out.println("??line" + line);
            
            String key = line.split("=")[0];
            String value = line.split("=")[1];
            
            //System.out.println("??line " + line + ", key " + key + ", value " + value);
            
            if(request.startsWith("GET")) {
                if(key.equals("main")) {
                    switch (value) {
                        case "main":
                            Main.main();
                            break;
                        case "menu":
                            Main.menu();
                            break;
                        case "replay":
                            Main.parser.replay();
                            break;
                        case "rating":
                            Main.rating();
                            break;
                        case "exit":
                            Main.exit();
                            break;
                        default:
                            Main.main();
                            break;
                    }
                } else if(key.equals("game")) {
                    switch (value) {
                        case "game":
                            Game.game();
                            break;
                        case "start":
                            Game.start();
                            break;
                    }
                } else if(key.equals("replay"))
                            Main.parser.read(value);
                
            } else 
            if(request.startsWith("DELETE")) {
                if(key.equals("rating")) {
                    if(value.equals("resetAll"))
                        RatingGame.reset();
                    else
                        RatingGame.deletePlayer(value);
                } 
                else if(key.equals("replay"))
                            Main.parser.delete(value);
           
            } else 
            if(request.startsWith("PUT")) {
                switch (key) {
                    case "playerOne":
                        String nameOne = value.split("&")[0];
                        String nameTwo = line.split("=")[3];
                        Game.login(nameOne, nameTwo);
                        break;
                    case "moveOne":
                        Game.move(Game.playerOne, value);
                        break;
                    case "moveTwo":
                        Game.move(Game.playerTwo, value);
                        break;
                }
            }
            ParserData.massage.flush();
            
            File f = new File(System.getProperty("user.dir") + "/massage.txt");
            
            String response = "HTTP/1.1 200 OK\n";

            
            DateFormat df = DateFormat.getTimeInstance();
            df.setTimeZone(TimeZone.getTimeZone("GMT"));

            response = response + "Last-Modified: " + df.format(new Date(f.lastModified())) + "\n";
            response = response + "Content-Length: " + f.length() + "\n";
            response = response + "Content-Type: text//txt\n";
            response = response
            + "Connection: close\n"
            + "Accept-Charset: UTF-8\n"
            + "Accept-Language: ru-RU,ru;q=0.9,en-US;q=0.8,en;q=0.7\n"
            + "Server: SimpleWEBServer\n\n";

            os.write(response.getBytes());

            BufferedWriter bos = new BufferedWriter(new OutputStreamWriter(os));
            BufferedReader fileReader = new BufferedReader(new FileReader(System.getProperty("user.dir") + "/massage.txt"));
            while(fileReader.ready()) {
                bos.write(fileReader.read());
            }

           /* FileInputStream fis = new FileInputStream(System.getProperty("user.dir") + "/massage.txt");
            r = 1;
            while(r > 0)
            {
                r = fis.read(buf);
                if(r > 0) os.write(buf, 0, r);
            }*/
            fileReader.close();
            bos.flush();
            os.close();
            is.close();
            s.close();
        }
        catch(Exception e)
        {e.printStackTrace();} 
    }
}