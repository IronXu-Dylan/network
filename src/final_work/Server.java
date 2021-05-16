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
                byte[] recv = new byte[58];
                in.read(recv);
                Message msg_recv = new Message();
                msg_recv.setTotalLength(ByteBuffer.allocate(4).put(recv, 0, 4).getInt(0));
                msg_recv.setCommandID(ByteBuffer.allocate(4).put(recv, 4, 4).getInt(0));
                msg_recv.setUserName((new String(recv, 8, 20, "UTF-8")).trim());
                msg_recv.setPasswd((new String(recv, 28, 30, "UTF-8")).trim());
                System.out.println("{" + "\n" +
                        "\ttotalLength: " + msg_recv.getTotalLength() + ",\n" +
                        "\tcommandID: " + msg_recv.getCommandID() + ",\n" +
                        "\tdata: {" + "\n" +
                        "\t\tuserName: " + msg_recv.getUserName() + ",\n" +
                        "\t\tpasswd: " + msg_recv.getPasswd() + "\n" +
                        "\t}" + "\n" +
                        "}");

                Message msg_res = new Message();
                msg_res.setTotalLength(73);
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
                                    msg_res.setDescription("登录失败：密码不正确");
                                }
                                break;
                            }
                            while(!data.equals("%")) data = br.readLine();
                            data = br.readLine();
                        }
                        if(!flag) {
                            msg_res.setStatus((byte)0);
                            msg_res.setDescription("登录失败：用户名不存在");
                        }

                        br.close();
                        fos.close();
                        ps.close();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }

                OutputStream out = clientSocket.getOutputStream();
                byte[] send = new byte[73];
                byte[] arr;
                arr = ByteBuffer.allocate(4).putInt(msg_res.getTotalLength()).array();
                System.arraycopy(arr, 0, send, 0, 4);
                arr = ByteBuffer.allocate(4).putInt(msg_res.getCommandID()).array();
                System.arraycopy(arr, 0, send, 4, 4);
                send[8] = msg_res.getStatus();
                System.arraycopy(msg_res.getDescription().getBytes("UTF-8"), 0, send, 9, msg_res.getDescription().getBytes("UTF-8").length);
                out.write(send);

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
