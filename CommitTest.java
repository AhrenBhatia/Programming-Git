import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CommitTest {
    private String parentSHA;
    private String originalCommit;

    @BeforeEach
    public void setUp() throws IOException, NoSuchAlgorithmException {
        parentSHA = Blob.hashStringToSHA1("testfile2.txt");
    }

    @Test
    public void testCommitCreation() throws IOException, NoSuchAlgorithmException {
        String nextSHA = createDummyCommitFile();
        Commit commit = new Commit("treeSHA1", "Ahren", "Finished the tester", "");

        originalCommit = commit.getCommitSHA();
        
        Path commitFile = Paths.get("objects", originalCommit);
        assertTrue(Files.exists(commitFile));

        List<String> lines = Files.readAllLines(commitFile);
        assertEquals(6, lines.size());
        assertEquals("treeSHA1", lines.get(0));
        assertEquals(parentSHA, lines.get(1));
        assertEquals(nextSHA, lines.get(2));
        assertEquals("Ahren", lines.get(3));
        assertEquals("October 19, 2023", lines.get(4));
        assertEquals("Finished the tester", lines.get(5));
    }

    private String createDummyCommitFile() throws IOException, NoSuchAlgorithmException {
        Commit commit = new Commit ("tree SHA2", "Leonard", "LLALALALLALA", originalCommit);
        return commit.getCommitSHA();
    }
}