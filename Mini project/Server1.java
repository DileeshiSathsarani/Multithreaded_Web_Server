import java.io.*;
import java.net.*;
import java.util.*;

public final class Server1{
	public static void main (String [] argv) throws Exception {
		int port = 6789;
		
		//setup the listen socket for the server
		ServerSocket wS = new ServerSocket (6789);
		while (true){
			// listen for TCP connection request
			Socket cS = wS.accept();
			// to process the HTTP message creating an object
			HttpRequest request = new HttpRequest (cS);
			// to process the above request creating a thread 
			Thread thread = new Thread (request);
			thread.start();
		}
	}
}