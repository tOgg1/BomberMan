package net;

import components.BombLayer;
import components.Defeatable;
import components.Moveable;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static net.Util.*;

/**
 * Created by tormod on 13.04.14.
 */
public class Server {

    private ServerSocket serverSocket;
    private EventThread eventThread;
    private int port = 13337;

    private HashMap<Integer, Moveable> updatedMoveables = new HashMap<>();
    private HashMap<Integer, BombLayer> addedBombs = new HashMap<>();
    private HashMap<Integer, Defeatable> defeatables = new HashMap<>();

    public Server() {

        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            throw new RuntimeException("Error while creating server: " + e.getMessage());
        }
    }

    public void init(){

    }

    public void run(){
        eventThread = new EventThread();
        eventThread.start();
        while(true){
            try {
                Socket socket = serverSocket.accept();
                System.out.println("Accepted connection");
                eventThread.addSocket(socket);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void updateMoveable(int entity_id, int direction){

    }

    public void addBomb(int x, int y, int damage, int spread){

    }


    private class EventThread extends Thread{
        private HashMap<Integer, Socket> sockets = new HashMap<>();
        private ArrayList<Socket> newSockets = new ArrayList<>();
        private HashMap<Integer, BufferedReader> inputs = new HashMap<>();
        private HashMap<Integer, BufferedWriter> outputs = new HashMap<>();

        private int index = 0;

        private EventThread() {
        }

        @Override
        public void run(){
            for(;;) {
                synchronized (this) {
                    try {
                        for (Socket newSocket : newSockets) {

                            sockets.put(++index, newSocket);
                            inputs.put(index, new BufferedReader(new InputStreamReader(newSocket.getInputStream())));
                            outputs.put(index, new BufferedWriter(new OutputStreamWriter(newSocket.getOutputStream())));
                        }

                        ArrayList<Integer> tempList = new ArrayList<>();
                        for (Map.Entry<Integer, Socket> entry : sockets.entrySet()) {
                            if(entry.getValue().isClosed()){
                                tempList.add(entry.getKey());
                            }
                        }

                        for (Integer integer : tempList) {
                            sockets.remove(integer);
                            inputs.remove(integer);
                            outputs.remove(integer);
                        }

                        int content;


                        // Get info
                        try{
                            for (BufferedReader input : inputs.values()) {

                                int lastFlag = 0;
                                int contentLength = 0;
                                int[] data;
                                while((content = input.read()) != -1){

                                    if(content == START_CLIENT_TRANSFER){
                                        lastFlag = START_CLIENT_TRANSFER;
                                        System.out.println("Transfer started");
                                    }else if(lastFlag == CONTENT_LENGTH){
                                        contentLength = content;
                                        data = new int[contentLength];
                                        for (int i = 0; i < contentLength; i++) {
                                            data[i] = input.read();
                                        }
                                        handleData(data);
                                        continue;
                                    }else if(content == END_CLIENT_TRANSFER){
                                        break;
                                    }

                                    lastFlag = content;
                                }
                            }
                        }catch(RuntimeException f){

                        }


                        // Send new frame
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                }
            }
        }

        public void handleData(int[] data){
            for (int i = 0; i < data.length; i++) {

                if(data[i] == START_CLIENT_UPDATE_MOVEABLE){
                    for (int j = i; j < data.length; j++) {
                        if(data[j] == END_CLIENT_UPDATE_MOVEABLE){
                            int length = j - i;
                            if(length != 2){
                                throw new RuntimeException("Invalid formatted data. Update moveable data not correct");
                            }else{
                                updateMoveable(data[i], data[i+1]);
                            }
                        }
                    }
                    throw new RuntimeException("Invalid formatted data. Couldn't find update moveable end tag");
                }else if(data[i] == START_CLIENT_DROP_BOMB){
                    for (int j = i; j < data.length; j++) {
                        if(data[j] == END_CLIENT_DROP_BOMB){
                            int length = j - i;
                            if(length != 4){
                                throw new RuntimeException("Invalid formatted data. Update moveable data not correct");
                            }else{
                                addBomb(data[i], data[i+1], data[i+2], data[i+3]);
                            }
                        }
                    }
                    throw new RuntimeException("Invalid formatted data. Couldn't find update moveable end tag");

                }
            }
        }

        public void addSocket(Socket socket){
            synchronized (this){
                System.out.println("Hello");
                newSockets.add(socket);
            }
        }
    }

    public static void main(String[] args) {
        Server server = new Server();
        server.init();
        server.run();
    }

}
