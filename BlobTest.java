import static org.junit.jupiter.api.Assertions.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class BlobTest {

    private static final String testDirectory = "testDirectory/";
    private static final String fileTest = "testFile.txt";
    private static final String fileContent = "Hello, world!";

    @BeforeEach
    public void setUp() throws IOException {
        File dir = new File(testDirectory);
        dir.mkdir();
        File file = new File(testDirectory + fileTest);
        if (!file.exists()) {
            file.createNewFile();
        }
    }

    @Test
    public void testBlobCreation() throws IOException {
        Blob blob = new Blob(testDirectory + fileTest);

        File objectsDir = new File("./objects");
        assertTrue(objectsDir.exists());

        String sha1 = blob.getSha();
        assertNotNull(sha1);

        File blobFile = new File("./objects/" + sha1);
        assertTrue(blobFile.exists());

        try (BufferedReader reader = new BufferedReader(new FileReader(blobFile))) {
            String content = reader.readLine();
            assertEquals(fileContent, content);
        }
    }

    @Test
    public void testSHA1Hashing() {
        String input = "test";
        String expectedSha1 = "a94a8fe5ccb19ba61c4c0873d391e987982fbbd3";
        assertEquals(expectedSha1, Blob.hashStringToSHA1(input));
    }
}