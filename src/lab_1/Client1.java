package lab_1;

import java.io.IOException;
import java.net.Socket;

public class Client1 {
    public static void main(String[] args) throws IOException {
        String hostName = "127.0.0.1";
        int portNumber = 12001;
        Socket socket = null;
        try {
            socket = new Socket(hostName, portNumber);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
