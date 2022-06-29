package org.atypon.reading;

import org.atypon.corr.LocksManager;
import org.atypon.io.DirectoryCreator;
import org.atypon.io.SocketFileManager;
import org.atypon.services.DatabaseService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class ListenerNode implements Runnable{
    private final BufferedReader reader;

    private final SocketFileManager socketFileManager;
    private  String url;
    public ListenerNode(int port) {
        try {
            System.out.println("trying to connect");
            Socket socket = new Socket(URLGenerator.generateURL(), 9097);
            System.out.println("connected!");
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            socketFileManager = SocketFileManager.create(socket);
            DirectoryCreator.getInstance().setUp(port);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String read() throws IOException {
        return reader.readLine();

    }

    public void receiveAllDatabases() {
        LocksManager.getLock("dataLock").writeLock().lock();

        try {
            socketFileManager.receiveFile("databases",DirectoryCreator.getInstance().getFileName());
        } catch (IOException e) {
            throw new RuntimeException("failed to receive the databases");
        } finally {
            LocksManager.getLock("dataLock").writeLock().unlock();

        }

    }

    public void receiveDatabase(String databaseName){
        LocksManager.getLock("dataLock").writeLock().lock();

        try {
            socketFileManager.receiveFile(databaseName, DirectoryCreator.getInstance().getMasterDir().toPath()+"\\");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }finally {
            LocksManager.getLock("dataLock").writeLock().unlock();

        }

    }
    @Override
    public void run() {
        while (true) {
            String input;
            try {
                input = read();
                System.out.println(input);
                if(input.equals("sending databases")) {
                    this.receiveAllDatabases();
                }
                else if(input.contains("deleteDatabase")) {
                    DatabaseService.deleteDatabase(input.substring(0,input.indexOf("deleteDatabase")));
                }
                else
                    this.receiveDatabase(input);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }


        }

    }




}
