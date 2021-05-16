package final_work;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.Scanner;

public class Client {
    public static void main(String args[]) throws IOException {
        Client client = new Client();
        client.launch();
    }

    void launch() throws IOException {
        String hostName = "127.0.0.1";
        int portNumber = 12001;
        try {
            while(true) {
                Socket clientSocket = new Socket(hostName, portNumber);
                Message msg = new Message();

                System.out.println("======== 请输入操作数字：1：注册，2：登陆 ========");
                Scanner input = new Scanner(System.in);
                int op = input.nextInt();
                while(op != 1 && op != 2) {
                    System.out.println("======== 请输入正确的操作数字：1：注册，2：登陆 ========");
                    op = input.nextInt();
                }
                if(op == 1) {
                    msg.setCommandID(1);
                } else if(op == 2) {
                    msg.setCommandID(3);
                }

                System.out.print("用户名：");
                msg.setUserName(input.next());
                System.out.print("密码：");
                msg.setPasswd(input.next());

                while(msg.getUserName().length() > 20 || msg.getPasswd().length() > 30) {
                    System.out.println("用户名或密码过长，请重新输入！");
                    System.out.print("用户名：");
                    msg.setUserName(input.next());
                    System.out.print("密码：");
                    msg.setPasswd(input.next());
                }


                msg.setTotalLength(58);

                OutputStream out = clientSocket.getOutputStream();
                byte[] send = new byte[58];
                byte[] arr;
                arr = ByteBuffer.allocate(4).putInt(msg.getTotalLength()).array();
                System.arraycopy(arr, 0, send, 0, 4);
                arr = ByteBuffer.allocate(4).putInt(msg.getCommandID()).array();
                System.arraycopy(arr, 0, send, 4, 4);
                System.arraycopy(msg.getUserName().getBytes("UTF-8"), 0, send, 8, msg.getUserName().getBytes().length);
                System.arraycopy(msg.getPasswd().getBytes("UTF-8"), 0, send, 28, msg.getPasswd().getBytes().length);
                out.write(send);

                InputStream in = clientSocket.getInputStream();
                byte[] recv = new byte[73];
                in.read(recv);
                Message msg_recv = new Message();
                msg_recv.setTotalLength(ByteBuffer.allocate(4).put(recv, 0, 4).getInt(0));
                msg_recv.setCommandID(ByteBuffer.allocate(4).put(recv, 4, 4).getInt(0));
                msg_recv.setStatus(recv[8]);
                msg_recv.setDescription((new String(recv, 9, 64)).trim());
                System.out.println("{" + "\n" +
                        "\ttotalLength: " + msg_recv.getTotalLength() + ",\n" +
                        "\tcommandID: " + msg_recv.getCommandID() + ",\n" +
                        "\tdata: {" + "\n" +
                        "\t\tstatus: " + msg_recv.getStatus() + ",\n" +
                        "\t\tdescription: " + msg_recv.getDescription() + "\n" +
                        "\t}" + "\n" +
                        "}");

                clientSocket.close();
            }
        } catch(IOException e){
            e.printStackTrace();
        }
    }
}
