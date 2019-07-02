package proxy;
import java.util.*;
import java.net.*;
import java.io.*;
public class Main {
	public static void main(String[] args) throws Exception{
		
		System.out.println(InetAddress.getByName("www.baidu.com"));
		Socket s = new Socket(InetAddress.getByName("www.baidu.com"), 80);
		if (s.isConnected()) {
		System.out.println(s.getInetAddress());
		System.out.println("客户端已经连上");
		try {
		BufferedReader br = new BufferedReader(new InputStreamReader(
		s.getInputStream()));
		PrintWriter out = new PrintWriter(s.getOutputStream(), true);
		out.println("GET / HTTP/1.1\r\nHost:www.baidu.com\r\nUser-Agent: Mozilla/5.0 (Windows NT 5.1; rv:10.0.2) Gecko/20100101 Firefox/10.0.2\r\nAccept-Language: zh-cn,zh;q=0.5\r\nAccept-Encoding: gzip, deflate\r\nConnection:Keep-Alive\r\n\r\n");
//		out.println("Host: www.baidu.com:80");
//		out.println("\r\n");
		String line = "";
		while ((line = br.readLine()) != null) {
		System.out.println(line);
		}
		br.close();
		out.close();
		} catch (IOException e) {
		e.printStackTrace();
		}
		}

			
		}
		
	}

