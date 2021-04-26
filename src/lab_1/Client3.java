package lab_1;

import java.io.IOException;
import java.net.Socket;

public class Client3 {
    public static void main(String[] args) throws IOException {
        String hostName = "127.0.0.1";
        int portNumber = 12001;
        Socket[] theclientSocket = new Socket[100];
        try {
            for(int i = 0; i < 100; i++) {
                theclientSocket[i] = new Socket(hostName, portNumber);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
