package Server;

import java.io.FileWriter;

public class ParserData {
				
				static FileWriter massage;
				static ParserData pdata;
				
				public ParserData() throws Exception {
								massage = new FileWriter(System.getProperty("user.dir") + "/massage.txt");
								pdata = this;
				}
				
				public void println(Object text) {
								try {
												massage.append(text.toString() + "\n");
								} catch(Exception e) {e.printStackTrace();}
				}
				
				public void println() {
								try {
												massage.append("\n");
								} catch(Exception e) {e.printStackTrace();}
				}
				
				public void print(Object text) {
								try {
												massage.append(text.toString());
								} catch(Exception e) {e.printStackTrace();}
				}
}
