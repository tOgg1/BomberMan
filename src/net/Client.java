package net;

import base.Factory;
import nodes.NetworkNode;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static net.Util.*;

/**
 * Created by tormod on 14.04.14.
 */
public class Client extends base.System{

    private Socket socket;
    private BufferedReader input;
    private BufferedWriter output;

    private static int port = 13337;
    private static String ip = "127.0.0.1";

    private HashMap<Integer, NetworkNode> nodes = new HashMap<>();
    private HashMap<Integer, NetworkNode> temps = new HashMap<>();

    private HashMap<Integer, Integer> updatedMoveables = new HashMap<>();
    private ArrayList<int[]> droppedBombs = new ArrayList<>();

    public Client() {
        try {
            socket = new Socket(ip, port);
            input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            output = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        } catch (IOException e) {
            throw new RuntimeException("Unable to connect to server");
        }
    }

    public void addUpdatedMoveable(int entity_id, int dir){
        updatedMoveables.put(entity_id, dir);
    }

    public void addDroppedBomb(int[] data){
        droppedBombs.add(data);
    }

    public void addToClient(int entity_id, NetworkNode node){
        if(temps.containsKey(entity_id) || nodes.containsKey(entity_id))
            return;
        temps.put(entity_id, node);
    }

    private void acceptDroppedBomb(int x, int y, int damage, int depth){
        Factory.getInstance().createBomb(x,y, damage, depth, 40);
    }

    private void acceptUpdatedMoveable(int entity, int dir){

        if(!nodes.containsKey(entity)){
            throw new RuntimeException("Entity did not exist");
        }

        if(!nodes.get(entity).isMoveable()){
            throw new RuntimeException("Entity was not moveable when it should've been");
        }

        nodes.get(entity).moveable.move = true;
        nodes.get(entity).moveable.curDir = dir;
    }

    @Override
    public void update(float dt) {
        for (Map.Entry<Integer, NetworkNode> nodeEntry : temps.entrySet()) {
            nodes.put(nodeEntry.getKey(), nodeEntry.getValue());
        }
        temps.clear();

        if(!updatedMoveables.isEmpty() || !droppedBombs.isEmpty()){
            try {
                // Send info about next frame
                //
                ArrayList<Integer> out = new ArrayList<>();

                for (Map.Entry<Integer, Integer> entry : updatedMoveables.entrySet()) {
                    out.add(START_UPDATE_MOVEABLE);
                    out.add(entry.getKey());
                    out.add(entry.getValue());
                    out.add(END_UPDATE_MOVEABLE);
                }

                for (int[] droppedBomb : droppedBombs) {
                    out.add(START_DROP_BOMB);

                    //Just assume it is rightly formatted. If not, the exception deserves to be thrown.
                    out.add(droppedBomb[0]);
                    out.add(droppedBomb[1]);
                    out.add(droppedBomb[2]);
                    out.add(droppedBomb[3]);

                    out.add(END_DROP_BOMB);
                }

                output.write(START_CLIENT_TRANSFER);
                output.write(CONTENT_LENGTH);
                output.write(out.size());

                for (Integer integer : out) {
                    output.write(integer);
                }

                output.write(END_CLIENT_TRANSFER);
                output.flush();
            }catch(Exception e){
                e.printStackTrace();
                return;
            }
        }

        clearAllUpdated();

        // Wait for at most 1 ms information
        //
        try {
            if(!input.ready()){
                return;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        try {
            int content;
            int lastFlag = 0;
            int contentLength = 0;
            int[] data;
            while((content = input.read()) != -1){

                if(content == START_SERVER_TRANSFER){
                    lastFlag = START_SERVER_TRANSFER;
                }else if(lastFlag == CONTENT_LENGTH){
                    contentLength = content;

                    data = new int[contentLength];
                    for (int i = 0; i < contentLength; i++) {
                        data[i] = input.read();
                    }
                    lastFlag = 0;
                    handleData(data);
                    continue;
                }else if(content == END_SERVER_TRANSFER){
                    break;
                }

                lastFlag = content;
            }
        } catch (IOException e) {
            throw new RuntimeException("Something went wrong while reading from stream");
        }
    }

    private void clearAllUpdated(){
        updatedMoveables.clear();
        droppedBombs.clear();
    }

    public void handleData(int[] data){
        if(data.length == 0){
            return;
        }
        for (int i = 0; i < data.length; i++) {
            if(data[i] == START_UPDATE_MOVEABLE){
                for (int j = i+1; j < data.length; j++) {
                    if(data[j] == END_UPDATE_MOVEABLE){
                        int length = j - i;
                        if(length != 3){
                            throw new RuntimeException("Invalid formatted data. Update moveable data not correct: " + length);
                        } else {
                            acceptUpdatedMoveable(data[j-2], data[j-1]);
                        }
                    }
                }
            }else if(data[i] == START_DROP_BOMB){
                for (int j = i+1; j < data.length; j++) {
                    if(data[j] == END_DROP_BOMB){
                        int length = j - i;
                        if(length != 5){
                            throw new RuntimeException("Invalid formatted data. Drop bomb data not correct: " + length);
                        }else{
                            acceptDroppedBomb(data[j-4], data[j-3], data[j-2], data[j-1]);
                        }
                    }
                }
            }
        }
    }

    @Override
    public void removeEntity(int id) {
        return;
    }

    public static void main(String[] args) {
        Client client = new Client();
        client.addDroppedBomb(new int[]{1,2,3,4});
        client.update(0);

    }
}
