package net;

import components.Defeatable;
import components.Destroyable;

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

    private HashMap<Integer, Integer> updatedMoveables = new HashMap<>();
    private ArrayList<int[]> addedBombs = new ArrayList<>();
    private ArrayList<Destroyable> destroyables = new ArrayList<>();
    private ArrayList<Defeatable> defeatables = new ArrayList<>();

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
                eventThread.addSocket(socket);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void updateMoveable(int entity_id, int direction){
        updatedMoveables.put(entity_id, direction);
    }

    public void addBomb(int x, int y, int damage, int spread){
        int[] bomb = new int[]{x,y,damage,spread};
        addedBombs.add(bomb);
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
                            System.out.println("Added socket");
                        }
                        newSockets.clear();

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

                        // Get input
                        try{
                            for (BufferedReader input : inputs.values()) {
                                if(!input.ready()){
                                    continue;
                                }

                                int content;
                                int lastFlag = 0;
                                int contentLength = 0;
                                int[] data;
                                while((content = input.read()) != -1){
                                    System.out.println("Got content");
                                    if(content == START_CLIENT_TRANSFER){
                                        lastFlag = START_CLIENT_TRANSFER;
                                        System.out.println("Transfer started");
                                    }else if(lastFlag == CONTENT_LENGTH){
                                        System.out.println("Got content length");
                                        contentLength = content;

                                        data = new int[contentLength];
                                        for (int i = 0; i < contentLength; i++) {
                                            data[i] = input.read();
                                        }

                                        handleData(data);
                                        lastFlag = 0;
                                        continue;
                                    }else if(content == END_CLIENT_TRANSFER){
                                        break;
                                    }

                                    lastFlag = content;
                                }
                            }
                        }catch(RuntimeException f){
                            f.printStackTrace();
                        }


                        if(updatedMoveables.isEmpty() && addedBombs.isEmpty())
                            continue;

                        //Prepare message to write
                        ArrayList<Integer> out = new ArrayList<>();

                        for (Map.Entry<Integer, Integer> entry : updatedMoveables.entrySet()) {
                            out.add(START_UPDATE_MOVEABLE);
                            out.add(entry.getKey());
                            out.add(entry.getValue());
                            out.add(END_UPDATE_MOVEABLE);
                        }

                        for (int[] addedBomb : addedBombs) {
                            out.add(START_DROP_BOMB);
                            out.add(addedBomb[0]);
                            out.add(addedBomb[1]);
                            out.add(addedBomb[2]);
                            out.add(addedBomb[3]);
                            out.add(END_DROP_BOMB);
                        }

                        if(out.isEmpty())
                            return;
                        //Send outpu
                        for (BufferedWriter output : outputs.values()) {
                            output.write(START_SERVER_TRANSFER);
                            output.write(CONTENT_LENGTH);
                            output.write(out.size());

                            for (Integer integer : out) {
                                output.write(integer);
                            }
                            output.write(END_SERVER_TRANSFER);
                            output.flush();
                        }

                        updatedMoveables.clear();
                        addedBombs.clear();

                    }catch(Exception e){
                        inputs.clear();
                        outputs.clear();
                        sockets.clear();
                    }
                }
            }
        }

        public void handleData(int[] data){
            if(data.length == 0){
                return;
            }
            for (int i = 0; i < data.length; i++) {
                if(data[i] == START_UPDATE_MOVEABLE){
                    for (int j = i; j < data.length; j++) {
                        if(data[j] == END_UPDATE_MOVEABLE){
                            int length = j - i;
                            if(length != 3){
                                throw new RuntimeException("Invalid formatted data. Update moveable data not correct");
                            }else{
                                updateMoveable(data[i+1], data[i+2]);
                            }
                            i = j;
                            break;
                        }
                    }
                }else if(data[i] == START_DROP_BOMB){
                    for (int j = i; j < data.length; j++) {
                        if(data[j] == END_DROP_BOMB){
                            int length = j - i;
                            if(length != 5){
                                throw new RuntimeException("Invalid formatted data. Drop bomb data not correct: " + length);
                            }else{
                                addBomb(data[i+1], data[i+2], data[i+3], data[i+4]);
                            }
                            i = j;
                            break;
                        }
                    }
                }
            }
        }

        public void addSocket(Socket socket){
            synchronized (this){
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
