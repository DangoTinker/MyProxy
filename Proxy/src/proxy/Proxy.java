package proxy;
import java.awt.*;
import javax.swing.*;

import java.util.concurrent.locks.*;

import java.awt.event.*;
import java.net.*;
import java.io.*;
import java.util.*;
import java.net.*;

public class Proxy {
	ServerSocket serverSocket;
	int port;
	private Lock lock;
	public Proxy(int port) throws Exception{
		serverSocket=new ServerSocket(port);
		this.port=port;
	}
	class Addr{
		String host;
		String pathname;
		int port;
		Addr (String a,String b,int c){
			host=a;
			pathname=b;
			port=c;
		}
	}
	
	class Cache{
		private String text;
		private int LRU=0;
		private Cache(String a) {
			text=a;
			
		}
		private String getText() {
			return text;
		}
		private void addLRU() {
			LRU++;
		}
	}
	
	
	HashMap<String,Cache> cacheMap=new HashMap<String,Cache>();
	
	
	public void run() throws Exception{
		
		while(true) {
			System.out.println("已启动");
			Socket socket=serverSocket.accept();
			System.out.println("已接受");
			Thread thread=new Thread(new ServerThread(socket));
			thread.start();
			
		}	
			
		
		
		
	}
	
	private class ServerThread implements Runnable{
		Socket socket;
		
		private ServerThread(Socket s) {
			socket=s;
		}
		
		private void doit(Socket socket) throws Exception{
			InputStream input=socket.getInputStream();
			Scanner cin=new Scanner(input);
			PrintWriter cout=new PrintWriter(socket.getOutputStream(),true);
			String comd=cin.nextLine();
			Cache res=null;
			
			
			
				if((res=cacheMap.get(comd))!=null) {
					
					lock.lock();
					
					cout.println(res.getText());
					cout.println("\r\n");
					System.out.println("cache");
					res.addLRU();
					
					lock.unlock();
					return;
				}
			
		
			
			
			
			
			
			String[] temp=comd.split(" ");
			
			if(temp.length!=3) {
				return;
			}
			String method=temp[0];
			String url=temp[1];
			String ver=temp[2];
/*
			if(!method.equals("GET")) {
				cout.println("do not support the method");
				return;
			}
		*/
			Addr addr=new Addr("","",80);
			
			parse_url(url,addr);
			System.out.println(url);
			System.out.println(addr.host);
			System.out.println(addr.pathname);
			System.out.println(addr.port);
			Socket desSocket=new Socket(addr.host,addr.port);
			
			OutputStream output=desSocket.getOutputStream();
			PrintWriter sout=new PrintWriter(output,true);
			
			Scanner sin=new Scanner(desSocket.getInputStream());
			
		
			
			sout.println("GET "+addr.pathname+" HTTP/1.1");
			sout.println("Host: "+addr.host);
			sout.println("\r\n");
			
			res=new Cache("");
			
			while(sin.hasNext()) {
				String a=sin.nextLine();
				cout.println(a);
				cout.flush();
				System.out.println(a);
				res.text=res.text+a+"\n";
			}
			cout.println("\r\n");
			cout.println("END");
			cout.flush();
			System.out.println("www");
			lock.lock();
			/*	if(cacheMap.size()>=1024) {
					int min=0;
					for (Map.Entry<String, Cache> entry : cacheMap.entrySet()) { 
						   if(entry.getValue().LRU<=min) {
							   min=entry.getValue().LRU;
						   }
					}
					for (Map.Entry<String, Cache> entry : cacheMap.entrySet()) { 
						   if(entry.getValue().LRU==min) {
							   cacheMap.remove(entry.getKey());
						   }
					}
				}
					*/
				cacheMap.put(comd, res);
				
					
				
			lock.unlock();
			
			cin.close();
			cout.close();
			sin.close();
			sout.close();
			socket.close();
		}
		
		
		private void parse_url(String url,Addr addr) {

			addr.port=80;
		
			addr.host="";
			addr.pathname="";
			
			int end=0;
			for(int i=url.indexOf("://")+3;url.charAt(i)!='/';i++) {
				
				addr.host=addr.host+url.charAt(i);
				end=i;
			}
			int start=0;
			String protS="";
			if((start=addr.host.indexOf(":"))!=-1) {
				for(int i=start+1;i<addr.host.length();i++) {
					protS=protS+addr.host.charAt(i);
				}
				addr.host.replaceAll(":"+protS, "");
				port=Integer.valueOf(protS);
			}
			
			
			for(int i=end+1;i<url.length();i++) {
				
				addr.pathname=addr.pathname+url.charAt(i);
			}
			
		}
		
		
		public void run()  {
			try {
				doit(socket);
			}catch(Exception e) {
				System.out.println(e.getMessage());
			}
		}
		
		
	}
	
	
	
	
	
}	
