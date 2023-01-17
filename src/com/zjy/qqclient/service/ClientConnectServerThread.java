package com.zjy.qqclient.service;

import com.zjy.qqcommon.Message;
import com.zjy.qqcommon.MessageType;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

public class ClientConnectServerThread extends Thread {
    //该线程需要持有Socket
    private Socket socket;

    //构造器可以接受一个Socket对象
    public ClientConnectServerThread(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        //因为Thread需要在后台和服务器通信，因此我们while循环
        while (true) {
            System.out.println("客户端线程，等待从读取从服务器端发送的消息");
            try {
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                //如果服务器没有发送Message对象，线程就会阻塞在这里
                Message message = (Message) ois.readObject();

                //判断message类型
                if(message.getMesType().equals(MessageType.MESSAGE_RET_ONLINE_FRIEND)){

                    String[] onlineUsers = message.getContent().split(" ");
                    System.out.println("\n--------当前在线用户列表--------");

                    for (int i = 0; i < onlineUsers.length; i++) {
                        System.out.println("用户："+onlineUsers[i]+" ");
                    }
                }else if(message.getMesType().equals(MessageType.MESSAGE_COMM_MES)){

                    System.out.println("\n" + message.getSender()
                            + "对" + message.getGetter() + "说"
                            + message.getContent());

                }else if(message.getMesType().equals(MessageType.MESSAGE_TO_ALL_MES)){
                    System.out.println("\n" + message.getSender() + "对大家说：" +message.getContent());
                }else if(message.getMesType().equals(MessageType.MESSAGE_FILE_MES)){

                    System.out.println("\n" +message.getSender() +
                            "给" + message.getGetter()+"发文件："
                            +message.getSrc()+"到我的电脑目录"+message.getDes());

                    FileOutputStream fileOutputStream = new FileOutputStream(message.getDes());
                    fileOutputStream.write(message.getFileBytes());
                    fileOutputStream.close();
                    System.out.println("\n" +"保存文件成功");



                }
                else{
                    System.out.println("其他类型不处理");

                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    //为了更方便的得到Socket
    public Socket getSocket() {
        return socket;
    }

}
