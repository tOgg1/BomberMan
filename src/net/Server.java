package net;

import base.Engine;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Created by tormod on 13.04.14.
 */
public class Server {

    private ServerSocket serverSocket;
    private Thread eventThread;
    private Engine engine;

    private int port = 13337;

    public Server() {

        try {
            serverSocket = new ServerSocket(port);
            eventThread = new Thread(){
                @Override
                public void run() {

                }
            };
        } catch (IOException e) {
            throw new RuntimeException("Error while creating server");
        }
    }

    public void run(){

    }

    public void handleMessage(int[] content){

    }


    private class EventThread extends Thread{
        private ArrayList<Socket> sockets;

        private EventThread() {
            sockets = new ArrayList<>();
        }

        @Override
        public void run(){


            for(;;) {
                synchronized (this) {
                    try {
                        for (Socket socket : sockets) {

                            InputStream stream = socket.getInputStream();
                            ArrayList<Integer> input = new ArrayList<>();
                            int b;

                            while((b = stream.read()) != -1){
                                input.add(b);
                            }

                        }
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                }
            }
        }

        public void addSocket(Socket socket){
            synchronized (this){
                sockets.add(socket);
            }
        }
    }


}
