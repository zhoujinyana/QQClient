package com.zjy.qqclient.view;


import com.zjy.qqclient.service.FileClientService;
import com.zjy.qqclient.service.MessageClientService;
import com.zjy.qqclient.service.UserClientService;
import com.zjy.qqcommon.Message;

import java.util.Scanner;

public class QQView {
    private boolean loop = true;
    private String key = "";
    //用于登录和注册使用
    private UserClientService userClientService = new UserClientService();

    private MessageClientService messageClientService = new MessageClientService();//用于消息发送

    private FileClientService fileClientService = new FileClientService();

    public static void main(String[] args) {
        new QQView().mainMenu();
        System.out.println("客户端退出系统");
    }

    private void mainMenu() {
        while (loop) {
            System.out.println("--------😊欢迎登录QQ系统😊--------");
            System.out.println("\t\t1 登录系统😊");
            System.out.println("\t\t2 退出系统😢");
            System.out.println("😊请输入你的选择😊：");
            Scanner sc = new Scanner(System.in);
            key = sc.next();

            switch (key) {
                case "1":
                    System.out.println("😊请输入用户名😊：");
                    String userId = sc.next();
                    System.out.println("😊请输入密  码😊：");
                    String pwd = sc.next();

                    if (userClientService.checkUser(userId, pwd)) {
                        System.out.println("欢迎用户" + userId);
                        while (loop) {
                            System.out.println("网络通信系统二级菜单用户" + userId);
                            System.out.println("\t\t 1 显示在线用户列表");
                            System.out.println("\t\t 2 群发消息");
                            System.out.println("\t\t 3 私聊消息");
                            System.out.println("\t\t 4 发送文件");
                            System.out.println("\t\t 9 退出系统");
                            System.out.println("😊请输入你的选择😊：");
                            Scanner sc1 = new Scanner(System.in);
                            key = sc1.next();
                            switch (key) {
                                case "1":
                                    userClientService.onlineFriendList();
                                    break;
                                case "2":
                                    System.out.println("请输入你想对大家说的话：");
                                    Scanner sc4 = new Scanner(System.in);
                                    String s = sc4.next();
                                    messageClientService.sendMessageToAll(s,userId);
                                    break;
                                case "3":
                                    System.out.print("请输入想聊天的在线用户号：");
                                    Scanner sc2 = new Scanner(System.in);
                                    String getterId = sc2.next();
                                    System.out.println("请输入想说的话：");
                                    Scanner sc3 = new Scanner(System.in);
                                    String content = sc3.next();
                                    messageClientService.sendMessageToOne(content,userId,getterId);
                                    break;
                                case "4":
                                    System.out.println("请输入你想发送文件的用户：");
                                    Scanner sc5 = new Scanner(System.in);
                                    String getterid = sc5.next();
                                    System.out.println("请输入你想发送文件的路径：");
                                    Scanner sc6 = new Scanner(System.in);
                                    String src = sc6.next();
                                    System.out.println("请输入你想发送文件的对方的路径：");
                                    Scanner sc7 = new Scanner(System.in);
                                    String dest = sc7.next();
                                    fileClientService.sendFileToOne(src,dest,userId,getterid);



                                    break;
                                case "9":
                                    userClientService.logout();
                                    loop = false;
                                    break;


                            }


                        }

                    } else {
                        System.out.println("--------登陆失败😢--------");

                    }
                    break;
                case "9":
                    loop = false;
                    break;


            }
        }
    }
}
