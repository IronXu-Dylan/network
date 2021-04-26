package lab_2;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class OptimizedMultiThreadSignInServer1 {

	class Worker implements Runnable {
		//为连入的客户端打开的套接口
		Socket socket;
		Worker(Socket socket) {
			this.socket = socket;
		}

		@Override
		public void run() {
			try {
				BufferedReader reader = new BufferedReader(new InputStreamReader(
						socket.getInputStream()));
				while(true) {
					String msg = reader.readLine();
					System.out.println(msg);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args) throws IOException {
		OptimizedMultiThreadSignInServer1 server = new OptimizedMultiThreadSignInServer1();
		server.launch();
	}
	
	void launch() throws IOException {
		int portNumber = 12001;
		ServerSocket serverSocket = null;
		try {
			serverSocket = new ServerSocket(portNumber);
			
			while(true){
				Socket clientSocket = serverSocket.accept();
				(new Thread(new Worker(clientSocket))).start();
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}finally{
			serverSocket.close();
		}		
	}
}