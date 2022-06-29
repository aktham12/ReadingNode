package org.atypon.reading;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class URLGenerator {
    private URLGenerator() {}
    public static String generateURL() {
        if(isRunningInsideDocker()) {
            return "host.docker.internal";
        }
        return "localhost";

    }
    private static Boolean isRunningInsideDocker() {
        try (Stream< String > stream =
                     Files.lines(Paths.get("/proc/1/cgroup"))) {
            return stream.anyMatch(line -> line.contains("/docker"));
        } catch (IOException e) {
            return false;
        }
    }


}
