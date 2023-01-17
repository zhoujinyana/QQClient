package com.zjy.qqclient.service;

import com.zjy.qqcommon.Message;
import com.zjy.qqcommon.MessageType;
import com.zjy.qqcommon.User;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

public class UserClientService {
    private User u = new User();//因为可能在其他地方用到user信息,因此做成成员属性

    private Socket socket;
    public boolean checkUser(String userId,String pwd) {
        boolean b = false;
        u.setUserId(userId);
        u.setPasswd(pwd);

        try {
            //连接到服务器，发送user对象
            socket = new Socket(InetAddress.getByName("127.0.0.1"), 9999);
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject(u);//发送user对象

            //读取从服务器回复的Message对象
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            Message ms = (Message) ois.readObject();
            if (ms.getMesType().equals(MessageType.MESSAGE_LOGIN_SUCCEED)) {//登录OK

                ClientConnectServerThread clientConnectServerThread = new ClientConnectServerThread(socket);
                clientConnectServerThread.start();

                ManageClientConnectServerThread.addClientConnectServerThread(userId,clientConnectServerThread);

                b = true;

            } else {
                socket.close();

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return b;
    }
    //向服务器端请求在线用户列表
    public void onlineFriendList(){
        Message message = new Message();
        message.setMesType(MessageType.MESSAGE_GET_ONLINE_FRIEND);
        message.setSender(u.getUserId());


        //得到当前线程对应的objectoutput对象
        try {
            ClientConnectServerThread clientConnectServerThread = ManageClientConnectServerThread.getClientConnectServerThread(u.getUserId());
            Socket socket = clientConnectServerThread.getSocket();
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //退出客户端，并发送消息
    public void logout(){
        Message message = new Message();
        message.setMesType(MessageType.MESSAGE_CLIENT);
        message.setSender(u.getUserId());

        try {
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject(message);
            System.out.println(u.getUserId()+"退出进程");
            System.exit(0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
