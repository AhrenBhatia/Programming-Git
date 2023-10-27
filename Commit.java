import java.nio.file.*;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.io.*;

public class Commit {
    private String tree;
    private String previous;
    private String next;
    private String author;
    private String date;
    private String message;
    private String commitName;

    public Commit(String tree, String author, String message, String previousCommitFile) throws IOException, NoSuchAlgorithmException {
        this.tree = tree;
        this.previous = previousCommitFile;
        this.next = "";
        this.author = author;
        this.date = getCurrentDate();
        this.message = message;
        this.commitName = "";
        createCommitFile();
        updatePreviousCommitNextLine();
    }

    public String getCommitSHA()
    {
        return commitName;
    }

    private String getCurrentDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("MMMM d, yyyy");
        return sdf.format(new Date());
    }

    public String createCommitFile() throws IOException, NoSuchAlgorithmException {
        StringBuilder sb = new StringBuilder("");
        sb.append(tree + "\n" + previous + "\n" + next + "\n" + author + "\n" + date + "\n" + message);
        String commitFile = Blob.hashStringToSHA1(sb.toString());
        String commitName = Blob.shaFile(commitFile, sb);
        return commitName;
    }


    public void updateHeadFile(String commitHash) throws IOException {
        if (Files.exists(Paths.get("head"))) {
            Files.write(Paths.get("head"), commitHash.getBytes(StandardCharsets.UTF_8));
        }
    }

    public void updatePreviousCommitNextLine() throws IOException, NoSuchAlgorithmException {
        if (previous == null || previous.isEmpty()) {
            return;
        }
        List<String> lines = Files.readAllLines(Paths.get("objects", previous));
        lines.set(2, commitName);
        Files.write(Paths.get("objects", previous), lines);
        Path headPath = Paths.get("objects", "HEAD.txt");
        File file = new File(headPath.toString());
        if (file.createNewFile())
        {
            FileWriter writer = new FileWriter(headPath.toString());
            writer.write(commitName);
        }
        else
        {
            FileWriter writer = new FileWriter(headPath.toString(), false);
            writer.write(commitName);
        }
    }
}
