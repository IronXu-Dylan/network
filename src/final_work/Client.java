package final_work;

import java.io.*;
import java.net.Socket;
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


                msg.setTotalLength(8 + (msg.getUserName() + msg.getPasswd()).length());

                OutputStream out = clientSocket.getOutputStream();
                ObjectOutputStream oos = new ObjectOutputStream(out);
                oos.writeObject(msg);
                oos.flush();

                InputStream in = clientSocket.getInputStream();
                ObjectInputStream ois = new ObjectInputStream(in);
                Message msg_recv = (Message) ois.readObject();
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
        } catch(IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
