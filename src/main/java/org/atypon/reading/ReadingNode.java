package org.atypon.reading;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.atypon.cache.Cache;
import org.atypon.cache.LRUCache;
import org.atypon.corr.LocksManager;
import org.atypon.services.CRUDService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

public class ReadingNode implements Runnable {

    private  CRUDService crudService;

    private final Socket socket;
    private final BufferedReader bufferedReader;
    private final Cache<String,ArrayList<JsonNode>> lruCache;

    public ReadingNode(Socket socket) throws IOException {
        this.socket = socket;
        InputStreamReader inputStream = new InputStreamReader(socket.getInputStream());
        bufferedReader = new BufferedReader(inputStream);
        lruCache = new LRUCache<>(100);
    }


    @Override
    public void run() {
        System.out.println("New connection");
        try {

            String input = bufferedReader.readLine();
            String[] curdServiceInfo = input.split(" ");
            crudService =  new CRUDService.CRUDServiceBuilder()
                    .currentDatabase(curdServiceInfo[0])
                    .currentCollection(curdServiceInfo[1])
                    .currentDocument(curdServiceInfo[2])
                    .build();
            String input2 = bufferedReader.readLine();

            if(input2.contains("find")) {
                String[] findInfo = input2.split(" ");
                find(curdServiceInfo, findInfo);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private void find(String[] curdServiceInfo, String[] findInfo) throws IOException {
        LocksManager.getLock("dataLock").readLock().lock();
        try {
            if (lruCache.containsKey(curdServiceInfo[2])) {
                send(socket, String.valueOf(lruCache.get(curdServiceInfo[2])));
            }
            ArrayList<JsonNode> foundList = crudService.find(findInfo[1], findInfo[2]);
            lruCache.put(curdServiceInfo[2], foundList);
            send(socket, String.valueOf(foundList));
        } finally {
            LocksManager.getLock("dataLock").readLock().unlock();
        }
    }


    private void send(Socket socket,String message) throws IOException {
        PrintWriter printWriter = new PrintWriter(socket.getOutputStream());
        printWriter.println(message);
        printWriter.flush();
    }


}
