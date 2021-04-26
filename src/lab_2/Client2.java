package lab_2;

import java.io.*;
import java.net.Socket;

public class Client2 {
	class Worker implements Runnable {
		char letter;
		OutputStream writer;
		Worker(char letter, OutputStream writer) {
			this.letter = letter;
			this.writer = writer;
		}
		@Override
		public void run() {
			try {
				for(int i = 0; i < 100; i++) {
					char letter = 'A';
					for(int j = 0; j < 26; j++) writer.write(letter++);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args) throws IOException {
		Client2 client = new Client2();
		client.launch();
	}

	void launch() throws IOException {
		String hostName = "127.0.0.1";
		int portNumber = 12001;
		Socket clientSocket = null;
		try {
			clientSocket = new Socket(hostName, portNumber);
			OutputStream writer = clientSocket.getOutputStream();
			(new Thread(new Worker('A', writer))).start();
			(new Thread(new Worker('a', writer))).start();
			while(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}



//Socket[] theclientSocket = new Socket[100];
//for(int i=0;;i++){
//	theclientSocket[i]= new Socket(hostName, portNumber);
//	System.out.println(i+" "+theclientSocket[i]);
//}
