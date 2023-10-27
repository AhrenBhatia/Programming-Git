import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Git {
    static class Tree {
        String sha1;
        List<Entry> entries;

        static class Entry {
            String type;
            String sha1;
            String name;
        }
    }

    static class Blob {
        String sha1;
        byte[] content;
    }
        public static void checkout(String commitSha1) throws IOException {
        String rootTreeSha1 = readFromObjectsFolder(commitSha1);
        recursiveTree(rootTreeSha1, Paths.get("."));
    }

    private static void recursiveTree(String treeSha1, Path currentDirectory) throws IOException {
        Tree tree = getTree(treeSha1);
        for (Tree.Entry entry : tree.entries) {
            Path newPath = currentDirectory.resolve(entry.name);
            
            if (entry.type.equals("blob")) {
                Blob blob = getBlob(entry.sha1);
                Files.write(newPath, blob.content);
            } 
            else if ("tree".equals(entry.type))
             {
                if (!Files.exists(newPath)) {
                    Files.createDirectory(newPath);
                }
                recursiveTree(entry.sha1, newPath);
            }
        }
    }

    private static Tree getTree(String sha1) throws IOException {
        Path treePath = Paths.get("objects", sha1);
        List<String> lines = Files.readAllLines(treePath);

        Tree tree = new Tree();
        tree.sha1 = sha1;
        tree.entries = new ArrayList<>();

        for (String line : lines) {
            String[] parts = line.split(" : ");
            if (parts.length == 3) {
                Tree.Entry entry = new Tree.Entry();
                entry.type = parts[0];
                entry.sha1 = parts[1];
                entry.name = parts[2];
                tree.entries.add(entry);
            }
        }

    return tree;
    }

    private static Blob getBlob(String sha1) throws IOException {
        Path blobPath = Paths.get("objects", sha1);
        Blob blob = new Blob();
        blob.sha1 = sha1;
        blob.content = Files.readAllBytes(blobPath);
        return blob;
    }

    private static String readFromObjectsFolder(String sha1) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get("objects", sha1));
        return lines.get(0); 
    }
}
