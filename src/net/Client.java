package net;

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

    private HashMap<Integer, Integer> updatedMoveables = new HashMap<>();
    private ArrayList<Integer[]> droppedBombs = new ArrayList<>();

    public Client() {
        try {
            socket = new Socket(ip, port);
            input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            output = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            output.write(1337);
            output.flush();
        } catch (IOException e) {
            throw new RuntimeException("Unable to connect to server");
        }
    }

    @Override
    public void update(float dt) {
        try {
            // Send info about next frame
            //
            ArrayList<Integer> out = new ArrayList<>();

            for (Map.Entry<Integer, Integer> entry : updatedMoveables.entrySet()) {
                out.add(START_CLIENT_UPDATE_MOVEABLE);
                out.add(entry.getKey());
                out.add(entry.getValue());
                out.add(END_CLIENT_UPDATE_MOVEABLE);
            }

            for (Integer[] droppedBomb : droppedBombs) {
                out.add(START_CLIENT_DROP_BOMB);

                //Just assume it is rightly formatted. If not, the exception deserves to be thrown.
                out.add(droppedBomb[0]);
                out.add(droppedBomb[1]);
                out.add(droppedBomb[2]);
                out.add(droppedBomb[3]);

                out.add(END_CLIENT_DROP_BOMB);
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
            return;
        }

        // Wait for information
        //

    }

    @Override
    public void removeEntity(int id) {
        return;
    }

    public static void main(String[] args) {
        Client client = new Client();

    }
}
