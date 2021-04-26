package lab_2;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class OptimizedMultiThreadSignInServer2 {
    class Worker implements Runnable {
        //为连入的客户端打开的套接口
        Socket socket;
        Worker(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try {
                InputStream reader = socket.getInputStream();
                BufferedInputStream bis = new BufferedInputStream(reader);
                char c = 0;
                int cnt = 0;
                while ( (int)(c = (char)bis.read()) != -1){
                    System.out.print(c);
                    if(++cnt == 26) {
                        System.out.println();
                        cnt = 0;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) throws IOException {
        OptimizedMultiThreadSignInServer2 server = new OptimizedMultiThreadSignInServer2();
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
