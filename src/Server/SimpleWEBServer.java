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
            System.out.println(request);
            if(!request.contains("gameplay?")) {
                os.write(request.getBytes());
                os.close();
                is.close();
                return;
            }
            
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
                        default:
                            Main.main();
                            break;
                    }
                } else if(key.equals("replay"))
                            Main.parser.read(value);
                  else 
                        Main.main();
                            
            } else 
            if(request.startsWith("DELETE")) {
                if(key.equals("rating")) {
                    if(value.equals("resetAll")) {
                        RatingGame.reset();
                    }else {
                        RatingGame.deletePlayer(value);
                    } 
                }
                else if(key.equals("replay"))
                            Main.parser.delete(value);
                else Main.main();
                     
            } else 
            if(request.startsWith("PUT")) {
                switch (key) {
                    case "playerOne":
                        String nameOne = value.split("&")[0];
                        String nameTwo = line.split("=")[2];
                        Game.login(nameOne, nameTwo);
                        break;
                    case "moveOne":
                        Game.move(Game.playerOne, value);
                        break;
                    case "moveTwo":
                        Game.move(Game.playerTwo, value);
                        break;
                    default:
                        Main.main();
                        break;
                }
            }
            ParserData.massage.write("</p>\n</body>\n</html>");
            
            ParserData.massage.flush();
            
            File f = new File(System.getProperty("user.dir") + "/massage.html");
            
            String response = "HTTP/1.1 200 OK\n";

            
            DateFormat df = DateFormat.getTimeInstance();
            df.setTimeZone(TimeZone.getTimeZone("GMT"));

            response = response + "Last-Modified: " + df.format(new Date(f.lastModified())) + "\n";
            response = response + "Content-Length: " + f.length() + "\n";
            response = response + "Content-Type: text/html\n";
            response = response
            + "Connection: close\n"
            + "Accept-Charset: UTF-8\n"
            + "Accept-Language: ru-RU,ru;q=0.9,en-US;q=0.8,en;q=0.7\n"
            + "Server: SimpleWEBServer\n\n";

            os.write(response.getBytes());

            BufferedWriter bos = new BufferedWriter(new OutputStreamWriter(os));
            BufferedReader fileReader = new BufferedReader(new FileReader(System.getProperty("user.dir") + "/massage.html"));
            while(fileReader.ready()) {
                bos.write(fileReader.read());
            }

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