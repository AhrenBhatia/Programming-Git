import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.Map;
import java.nio.file.Files;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;

public class index extends blob {

    public static HashMap<String, String> files = new HashMap<>();

    public static void init() throws IOException {

        java.nio.file.Path folderPath = Paths.get("./objects");

        if (!Files.exists(folderPath)) {
            try {
                Files.createDirectory(folderPath);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        File file = new File("Git");
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void add(String fileName) throws IOException {
        try {
            String hashName = blobFile(fileName);
            files.put(fileName, hashName);

            Path index = Paths.get("Git");
            try (BufferedWriter bw = Files.newBufferedWriter(index, StandardOpenOption.CREATE,
                    StandardOpenOption.WRITE)) {
                for (Map.Entry<String, String> entry : files.entrySet()) {
                    bw.write(entry.getKey() + " : " + entry.getValue());
                    bw.newLine();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void remove(String fileName) throws IOException {
        try {
            files.remove(fileName);
            Path index = Paths.get("./objects/index");
            try (BufferedWriter clear = Files.newBufferedWriter(index, StandardOpenOption.TRUNCATE_EXISTING)) {
            } catch (IOException e) {
                e.printStackTrace();
            }

            try (BufferedWriter write = Files.newBufferedWriter(index, StandardOpenOption.CREATE,
                    StandardOpenOption.WRITE)) {
                for (Map.Entry<String, String> entry : files.entrySet()) {
                    write.write(entry.getKey() + " : " + entry.getValue());
                    write.newLine();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}