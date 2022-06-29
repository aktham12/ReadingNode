package org.atypon.io;

import org.apache.commons.io.FileUtils;

import java.io.*;
import java.net.Socket;

public class SocketFileManager {

    private final DataOutputStream dataOutputStream;
    private final DataInputStream dis;

    private SocketFileManager(Socket socket) throws IOException {
        this.dataOutputStream = new DataOutputStream(socket.getOutputStream());
        dis = new DataInputStream(socket.getInputStream());
    }

    public static SocketFileManager create(Socket socket) {
        try {
            return new SocketFileManager(socket);
        } catch (IOException e) {
            throw new RuntimeException("can't create a new File manager with this socket");
        }
    }


    public  void receiveFile(String fileName,String savePath) throws IOException {
        FileOutputStream fos = new FileOutputStream(fileName + ".zip");
        int bytesRead;
        long size = dis.readLong();
        byte[] buffer = new byte[4 * 1024];

        while (size > 0 && (bytesRead = dis.read(buffer, 0, (int) Math.min(buffer.length, size))) != -1) {
            fos.write(buffer, 0, bytesRead);
            size -= bytesRead;
        }
        fos.close();
        ZipManager.unZip(new File(fileName + ".zip").toPath(), new File(savePath).toPath());
        FileUtils.delete(new File(fileName + ".zip"));

    }
}
