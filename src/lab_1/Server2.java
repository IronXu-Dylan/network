package lab_1;

import java.io.IOException;
import java.net.ServerSocket;

public class Server2 {
    public static void main(String[] args) throws IOException {
        int portNumber = 12001;
        ServerSocket listenSocket = null;
        try {
            listenSocket = new ServerSocket(portNumber);
            while(true);
        } catch (Exception e) {
            e.printStackTrace();
        } finally{
            if (listenSocket != null) {
                listenSocket.close();
            }
        }
    }
}
