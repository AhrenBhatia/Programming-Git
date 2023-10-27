import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.NoSuchAlgorithmException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TreeTest {
    
    private Tree tree;
/* 
    private static final String fileName = "testFile.txt";
    private static final String fileContent = "I love me some cookies";

    @BeforeEach
    public void setUp() throws IOException {
        tree = new Tree();
        File file = new File(fileName);
        if (!file.exists()) {
            PrintWriter pw = new PrintWriter(file);
            pw.write(fileContent);
            pw.close();
        }
    }

    @Test
    public void testTreeCreation() throws IOException {
        File treeFile = new File("Tree");
        assertTrue(treeFile.exists());
    }
    
    @Test
    public void testAddBlob() throws IOException {
        tree.add(fileName);
        tree.save();
        assertTrue(tree.getContents().contains(fileName));
    }
    
    @Test
    public void testRemoveBlob() throws IOException {
        tree.add(fileName);
        tree.remove(fileName);
        tree.save();
        assertFalse(tree.getContents().contains(fileName));
    }
    
    @Test
    public void testSHA1Hashing() {
        String input = fileContent;
        String expectedSha1 = Blob.hashStringToSHA1(input);
        assertEquals(expectedSha1, "2ef7bde608ce5404e97d5f042f95f89f1c232871");
    }
*/
    @BeforeEach
    public void setUp() throws IOException {
        tree = new Tree();
    }

    @Test
    public void testAddDirectoryCase1() throws IOException, NoSuchAlgorithmException {
        new File("testFolder").mkdirs();
        try (PrintWriter pw1 = new PrintWriter("testFolder/text1.txt");
             PrintWriter pw2 = new PrintWriter("testFolder/text2.txt");
             PrintWriter pw3 = new PrintWriter("testFolder/text3.txt")) {

            pw1.write("hello world");
            pw2.write("goodbye moon");
            pw3.write("yo mars!");
        }

        String treeBlob = tree.addDirectory("testFolder");

        assertTrue(tree.getContents().contains("blob : " + Blob.hashStringToSHA1("hello world") + " : text1.txt"));
        assertTrue(tree.getContents().contains("blob : " + Blob.hashStringToSHA1("goodbye moon") + " : text2.txt"));
        assertTrue(tree.getContents().contains("blob : " + Blob.hashStringToSHA1("yo mars!") + " : text3.txt"));
    }

    @Test
    public void testAddDirectoryCase2() throws IOException, NoSuchAlgorithmException {
        new File("testFolder").mkdirs();
        new File("testFolder/innerFolder").mkdirs();
        try (PrintWriter pw1 = new PrintWriter("testFolder/text1.txt");
             PrintWriter pw2 = new PrintWriter("testFolder/text2.txt");
             PrintWriter pw3 = new PrintWriter("testFolder/text3.txt");
             PrintWriter pw4 = new PrintWriter("testFolder/innerFolder/text4.txt");
             PrintWriter pw5 = new PrintWriter("testFolder/innerFolder/text5.txt")) {

            pw1.write("hello world");
            pw2.write("goodbye moon");
            pw3.write("yo mars!");
            pw4.write("mercury is in retrograde");
            pw5.write("jupiter power");
        }

        String treeBlob = tree.addDirectory("testFolder");

        assertTrue(tree.getContents().contains("blob : " + Blob.hashStringToSHA1("hello world") + " : text1.txt"));
        assertTrue(tree.getContents().contains("blob : " + Blob.hashStringToSHA1("goodbye moon") + " : text2.txt"));
        assertTrue(tree.getContents().contains("blob : " + Blob.hashStringToSHA1("yo mars!") + " : text3.txt"));
        boolean hasInnerFolder = false;
        for (String entry : tree.getContents()) 
        {
            if (entry.startsWith("tree : ") && entry.endsWith(" : innerFolder")) 
            {
                hasInnerFolder = true;
                break;
            }
        }
        assertTrue(hasInnerFolder);
    }
}

