package lab_3;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;


public class BlackHoleServer {
	class HeartbeatWorker implements Runnable {
		Socket socket;
		HeartbeatWorker(Socket socket) {
			this.socket = socket;
		}

		@Override
		public void run() {
			try {
				PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
				while(true){
					Thread.sleep(10000); //sleep 10秒
					writer.println("Server is OK.");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	class Worker implements Runnable {
		// 为连入的客户端打开的套接口
		Socket socket;

		Worker(Socket socket) {
			this.socket = socket;
		}

		@Override
		public void run() {
			try {
				// 打开sock输入输出流
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(socket.getInputStream()));
				(new Thread(new HeartbeatWorker(socket))).start();
				while(true){
					// 读取来自客户端的字符串并输出
					String msg = reader.readLine();
					if (msg==null){
						break;
					}
					System.out.println(msg+" ["+(new Date())+"]");
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					socket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static void main(String[] args) throws IOException {
		BlackHoleServer server = new BlackHoleServer();
		server.launch();
	}

	void launch() throws IOException {
		int portNumber = 12001;
		ServerSocket serverSocket = null;
		try {
			serverSocket = new ServerSocket(portNumber);

			while (true) {
				Socket clientSocket = serverSocket.accept();
				(new Thread(new Worker(clientSocket))).start();
				//send heart-beat to every client
				//....
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally {
			serverSocket.close();
		}
	}
}