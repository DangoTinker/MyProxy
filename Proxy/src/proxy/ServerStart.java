package proxy;

public class ServerStart {
	public static void main(String[] args) throws Exception{
		Proxy proxy=new Proxy(1234);
		proxy.run();
	}
}
