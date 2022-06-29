package org.atypon;


import org.atypon.reading.ListenerNode;
import org.atypon.reading.ReadingNode;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(Integer.parseInt(args[0]));
        ExecutorService executorService = Executors.newCachedThreadPool();
        executorService.submit(new ListenerNode(Integer.parseInt(args[0])));
        while (true) {
            Socket socket = serverSocket.accept();
            executorService.submit(new ReadingNode(socket));
        }

    }
}