package proxy;

import java.io.PrintWriter;
import java.net.*;

public class TestClient {
	public static void main(String[] args) throws Exception{
		Socket socket=new Socket("10.173.0.37",1234);
		
		PrintWriter out=new PrintWriter(socket.getOutputStream());
		out.println("GET https://www.baidu.com/ HTTP/1.1");
		
	}
}
