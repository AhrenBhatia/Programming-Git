import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;

public class Tree {
    private static ArrayList<String> entries;
    private HashMap<String, String> blobs;
    private HashSet<String> sha1List;
    public String treeFileName;

    public Tree() throws IOException {
        File file = new File("Tree");
        if (!file.exists()) {
            file.createNewFile();
        }
        entries = new ArrayList<String>();
    }

    public Tree(String fileName) {

    }

    public void add(String entry) throws IOException
    {
        String[] parts = entry.split(" : ");
    if (parts.length != 3) {
        throw new IllegalArgumentException("Invalid entry format.");
    }

    String type = parts[0];
    String sha1 = parts[1];
    String name = parts[2];

    if (type.equals("blob")) {
        File file = new File(name);
        if (!file.exists()) {
            throw new IllegalArgumentException("File does not exist.");
        }

        if (sha1.isEmpty()) {
            sha1 = hashFile(name);
            entry = "blob : " + sha1 + " : " + name;
        }
    } else {
        throw new IllegalArgumentException("Invalid type. Only 'tree' or 'blob' is supported.");
    }

    if (!entries.contains(entry)) {
        entries.add(entry);
    }
    
    }

    public void remove(String entryName) {
        if (entryName.startsWith("tree :")) {
            for (int i = 0; i < entries.size(); i++) {
                if (entries.get(i).contains(entryName)) {
                    entries.remove(i);
                    break;
                }
            }
        } 
        else {
            File file = new File(entryName);
            if (!file.exists()) 
            {
                return;
            } 
            
            else
            {
                String entryToRemove = null;
                for (String entry : entries) {
                    if (entry.endsWith(" : " + entryName)) {
                        entryToRemove = entry;
                        break;
                    }
                }
                if (entryToRemove != null) {
                    entries.remove(entryToRemove);
                }
            }
        }
    }

    public String addDirectory(String directoryPath) throws IOException, NoSuchAlgorithmException {
        List<String> filesInPath = new ArrayList<>();
        File dir = new File(directoryPath);
        if (!dir.exists() || !dir.isDirectory()) {
            throw new IOException("Invalid directory path: " + directoryPath);
        }

        for (File file : dir.listFiles()) {
            if (file.isFile()) 
            {
                String sha1 = hashFile(""+file);
                filesInPath.add("blob : " + sha1 + " : " + file.getName());
            } 

            else if (file.isDirectory())
            {
                Tree childTree = new Tree();
                childTree.addDirectory(file.getAbsolutePath());
                String sha1 = hashTree(childTree);
                entries.add("tree : " + sha1 + " : " + file.getName());
            }

        }


        StringBuilder combinedContent = new StringBuilder();
        for (String entry : entries) 
        {
            combinedContent.append(entry);
        }
        String treeBlob = Blob.hashStringToSHA1(combinedContent.toString());
        return treeBlob;
    }


    public static String hashFile(String inputFile) throws IOException {
        try {
            File file = new File(inputFile);
            StringBuilder contains = new StringBuilder();
            BufferedReader reader = new BufferedReader(new FileReader(file));
            while (reader.ready()) {
                contains.append(reader.read());
            }
            reader.close();
            return Blob.hashStringToSHA1(contains.toString());

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String hashTree(Tree tree) throws IOException {
        StringBuilder combinedContent = new StringBuilder();
        
        for (String entry : tree.getContents()) {
            combinedContent.append(entry);
        }
        return Blob.hashStringToSHA1(combinedContent.toString());
    }


    public String fileToString(String inputFile) throws IOException
    { 
        String hashed;
        {
            StringBuilder contains = new StringBuilder("");
            File file = new File(inputFile);
            BufferedReader reader = new BufferedReader(new FileReader(file));
            while (reader.ready()) 
            {
                contains.append(reader.read());
            }

            reader.close();
            hashed = Blob.hashStringToSHA1(contains.toString());
        }
        return hashed;
    }

    public void save() throws FileNotFoundException {
        StringBuilder sb = new StringBuilder("");
        int numStrings = entries.size();
        for (int i = 0; i < numStrings; i++) {
            sb.append(entries.get(i));
            if (i < numStrings - 1)
            {
                sb.append("\n");
            }
        }
        String fileName = Blob.hashStringToSHA1(sb.toString());
        treeFileName = Blob.shaFile(fileName, sb);
    }

    public ArrayList<String> getContents()
    {
        return entries;
    }
}
