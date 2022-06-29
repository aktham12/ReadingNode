package org.atypon.io;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class JsonWriter {
    private final ObjectMapper mapper = new ObjectMapper();
    private static final File MASTER_DIR = new File("databases");


    private JsonWriter() {
    }

    public static JsonWriter getInstance() {
        return JsonWriterHolder.INSTANCE;
    }

    public void write(String filePath, ArrayNode content) throws IOException {
        File file = new File(MASTER_DIR + "/" + filePath);
        mapper.writerWithDefaultPrettyPrinter().writeValue(Files.newOutputStream(file.toPath()), content);
    }


    public <T extends Serializable> void write(T object, String pathTo) throws IOException {
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(Files.newOutputStream(Paths.get(pathTo)));
        objectOutputStream.writeObject(object);
        objectOutputStream.close();
    }

    public <T extends Serializable> T read(String pathTo) throws IOException, ClassNotFoundException {
        ObjectInputStream objectInputStream = new ObjectInputStream(Files.newInputStream(Paths.get(pathTo)));
        T object = (T) objectInputStream.readObject();
        objectInputStream.close();
        return object;
    }

    private static class JsonWriterHolder {
        private static final JsonWriter INSTANCE = new JsonWriter();
    }
}


