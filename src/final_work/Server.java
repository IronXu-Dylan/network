package final_work;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;

public class Server {
    class Worker implements Runnable {
        // 为连入的客户端打开的套接口
        Socket clientSocket;
        Worker(Socket clientSocket) {
            this.clientSocket = clientSocket;
        }

        @Override
        public void run() {
            try {
                InputStream in = clientSocket.getInputStream();
                ObjectInputStream ois = new ObjectInputStream(in);
                Message msg_recv = (Message) ois.readObject();
                System.out.println("{" + "\n" +
                        "\ttotalLength: " + msg_recv.getTotalLength() + ",\n" +
                        "\tcommandID: " + msg_recv.getCommandID() + ",\n" +
                        "\tdata: {" + "\n" +
                        "\t\tuserName: " + msg_recv.getUserName() + ",\n" +
                        "\t\tpasswd: " + msg_recv.getPasswd() + "\n" +
                        "\t}" + "\n" +
                        "}");

                Message msg_res = new Message();
                // 处理注册请求
                if(msg_recv.getCommandID() == 1) {
                    msg_res.setCommandID(2);
                    try {
                        String filePath = "D:/code/mars/network/src/final_work/RegInfo.txt";
                        File file = new File(filePath);
                        BufferedReader br = new BufferedReader(new FileReader(file));
                        FileOutputStream fos = new FileOutputStream(filePath, true);
                        PrintStream ps = new PrintStream(fos);
                        String storeData = msg_recv.getUserName() + "\n" + MD5.getMd5(msg_recv.getPasswd()) + "\n%";
                        String data = br.readLine();
                        boolean flag = false;   // 已注册过为true，未注册过为false
                        while(data != null) {
                            if(data.equals(msg_recv.getUserName())) {
                                flag = true;
                                msg_res.setStatus((byte)0);
                                msg_res.setDescription("注册失败");
                                break;
                            }
                            while(!data.equals("%")) data = br.readLine();
                            data = br.readLine();
                        }
                        if(!flag) {
                            ps.println(storeData);
                            msg_res.setStatus((byte)1);
                            msg_res.setDescription("注册成功");
                        }

                        br.close();
                        fos.close();
                        ps.close();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                // 处理登录请求
                else if(msg_recv.getCommandID() == 3) {
                    msg_res.setCommandID(4);
                    try {
                        String filePath = "D:/code/mars/network/src/final_work/RegInfo.txt";
                        File file = new File(filePath);
                        BufferedReader br = new BufferedReader(new FileReader(file));
                        FileOutputStream fos = new FileOutputStream(filePath, true);
                        PrintStream ps = new PrintStream(fos);
                        String userName = msg_recv.getUserName();
                        String passwd = msg_recv.getPasswd();
                        String data = br.readLine();
                        boolean flag = false;   // 已注册过为true，未注册过为false
                        while(data != null) {
                            if(data.equals(msg_recv.getUserName())) {
                                flag = true;
                                data = br.readLine();
                                // 登录成功
                                if(data.equals(MD5.getMd5(msg_recv.getPasswd()))) {
                                    msg_res.setStatus((byte)1);
                                    msg_res.setDescription("登录成功");
                                } else {
                                    msg_res.setStatus((byte)0);
                                    msg_res.setDescription("登录失败");
                                }
                                break;
                            }
                            while(!data.equals("%")) data = br.readLine();
                            data = br.readLine();
                        }
                        if(!flag) {
                            msg_res.setStatus((byte)0);
                            msg_res.setDescription("登录失败");
                        }

                        br.close();
                        fos.close();
                        ps.close();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                msg_res.setTotalLength(8 + (msg_res.getStatus() + msg_res.getDescription()).length());

                OutputStream out = clientSocket.getOutputStream();
                ObjectOutputStream oos = new ObjectOutputStream(out);
                oos.writeObject(msg_res);
                oos.flush();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String args[]) throws IOException {
        Server server = new Server();
        server.launch();
    }

    public void launch() throws IOException {
        int portNumber = 12001;
        ServerSocket listenSocket = null;
        try {
            listenSocket = new ServerSocket(portNumber);
            while(true){
                Socket clientSocket = listenSocket.accept();
                (new Thread(new Worker(clientSocket))).start();
            }
        } catch(IOException e) {
            e.printStackTrace();
        } finally {
            listenSocket.close();
        }
    }
}
