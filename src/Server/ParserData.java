package Server;

import java.io.FileWriter;

public class ParserData {
				
				static FileWriter massage;
				static ParserData pdata;
				
				public ParserData() throws Exception {
								massage = new FileWriter(System.getProperty("user.dir") + "/massage.html");
								pdata = this;
								
								massage.write(//"HTTP/1.1 200 OK\n\n" +
								    "<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01//EN\" \"http://www.w3.org/TR/html4/strict.dtd\">\n" +
           "<html>\n" +
           "<head>\n" +
           "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\">\n" +
           "<title>TicTacToe</title>\n" +
           "</head>\n" +
           "<body>\n" +
           "<p>");
				}
				
				public void println(Object text) {
								try {
												massage.append("<p>" + text.toString() + "</p>");
								} catch(Exception e) {e.printStackTrace();}
				}
				
				public void println() {
								try {
												massage.append("<p>-----</p>");
								} catch(Exception e) {e.printStackTrace();}
				}
}
