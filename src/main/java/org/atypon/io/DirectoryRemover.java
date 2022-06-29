package org.atypon.io;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

public class DirectoryRemover {

    private static final File MASTER_DIR = DirectoryCreator.getInstance().getMasterDir();

    private DirectoryRemover() {
    }

    public static DirectoryRemover getInstance() {
        return DirectoryRemoverHolder.INSTANCE;

    }

    public  void deleteDirectory(String directoryPath) {
        try {
            FileUtils.forceDelete(new File(MASTER_DIR + "/" + directoryPath));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private static class DirectoryRemoverHolder {
        private static final DirectoryRemover INSTANCE = new DirectoryRemover();
    }

}
