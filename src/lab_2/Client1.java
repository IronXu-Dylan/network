package lab_2;

import java.io.*;
import java.net.Socket;

public class Client1 {
    class Worker implements Runnable {
        char letter;
        PrintWriter writer;
        Worker(char letter, PrintWriter writer) {
            this.letter = letter;
            this.writer = writer;
        }
        @Override
        public void run() {
            try {
                char letter = this.letter;
                String letterStr = "";
                for(int i = 0; i < 26; i++) {
                    letterStr += letter++;
                }
                for(int i = 0; i < 100; i++) {
                    writer.println(letterStr);
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
            PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true);
            (new Thread(new Worker('A', writer))).start();
            (new Thread(new Worker('a', writer))).start();
            while(true);
			/*
			//打开socket输入输出流
			PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true);
			BufferedReader reader = new BufferedReader(
					new InputStreamReader(clientSocket.getInputStream()));
			BufferedReader stdIn = new BufferedReader(new InputStreamReader(
					System.in));
			//向svr签到
			writer.println(stdIn.readLine());
			//读取svr返回的签到结果
			System.out.println("server answers: "+reader.readLine());
			*/
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
