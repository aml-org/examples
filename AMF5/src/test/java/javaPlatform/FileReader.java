package javaPlatform;

import java.io.*;
import java.nio.file.Files;

public interface FileReader {

    default String readResource(String path) throws IOException {
        File file = new File(this.getClass().getResource(path).getFile());
        return new String(Files.readAllBytes(file.toPath()));
    }
}
