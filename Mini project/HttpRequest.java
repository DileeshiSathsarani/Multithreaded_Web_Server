import java.io.*;
import java.net.*;
import java.util.*;

public final class HttpRequest implements Runnable{
	final static String EOL = "\r\n";
	Socket socket1;
	
	public HttpRequest (Socket socket1) throws Exception {
		this.socket1 = socket1;
	}
	
	//implement the run() method according to the above runnable interface
	public void run() {
		try{
			processRequest();
		}catch (Exception e){
			System.out.println(e);
		}
	}
	
	private void processRequest() throws Exception{
		// for socket's IO stream, get a refrence
		InputStream ins = socket1.getInputStream();
		DataOutputStream outs = new DataOutputStream (socket1.getOutputStream());
		// set up input filters
		BufferedReader bfr = new BufferedReader (new InputStreamReader(ins));
		
		//get the request line in the HTTP request message
		String requestLine = bfr.readLine();
		//display
		System.out.println();
		System.out.println(requestLine);
		// display the header line after getting it
		String headLine = null;
		while ((headLine = bfr.readLine()).length() !=0){
			System.out.println(headLine);
		}
	
	//extract file name from request line
	StringTokenizer tokens = new StringTokenizer(requestLine);
	tokens.nextToken();
	String fileName = tokens.nextToken();
	fileName = "." + fileName;
	System.out.println("File name to Get" + fileName);
	//open the requested file
	FileInputStream files = null;
	boolean fileExists = true;
	
	try {
		files = new FileInputStream(fileName);
	} catch (FileNotFoundException e){
		fileExists = false;
	}
	
	//build the response message
	String statusLine = null;
	String contentTypeLine = null;
	String entityBody = null;
	
	if (fileExists){
		statusLine = "200 OK:";
		contentTypeLine = "Content-Type: text/html" + contentType(fileName) + EOL;
	}
	
	else {
		statusLine = "HTTP/1.0 404 Not Found :";
		contentTypeLine = "Content-Type : text/html" + EOL;
		entityBody = "<HTML>" + "<HEAD><TITLE> Not Found </TITLE> </HTML>" + "<BODY> Not Found:";
		}
		
		//send status line
		outs.writeBytes(statusLine);
		//send content type line
		outs.writeBytes(contentTypeLine);
		//send a blank line to specify the end of the header lines
		outs.writeBytes(EOL);
		
		//send the entity body
		if (fileExists){
			sendBytes (files,outs);
			files.close();
		}
		
		else {
			outs.writeBytes(entityBody);
		}
		
		outs.writeBytes(entityBody);
		DataOutputStream outToClient = new DataOutputStream (socket1.getOutputStream());
		
		outToClient.writeBytes ("HTTP/1.0 200 OK");
		outToClient.writeBytes ("<HTML> <BODY> <h1> GROUP NUMBER 10 MINI PROJECT </h1>  <h2>Multithreaded Web Server </h2></BODY> </HTML>");
		
		outToClient.close();
		
		//close all sockets and streams
		outs.close();
		bfr.close();
		socket1.close();
	}
	
	private static String contentType(String fileName){
		if (fileName.endsWith(".html") || fileName.endsWith (".htm")){
			return "text/html";
		}
			return "application/octet-stream";
			
	}
	
	private static void sendBytes (FileInputStream files , OutputStream outs) throws Exception {
		// build a 1024bytes buffer to hold bytes 
		byte[] buffer = new byte [1024];
		int bytes = 0;
		while ((bytes = files.read(buffer)) != -1){
			outs.write(buffer, 0, bytes);
		}
	}
}