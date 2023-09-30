import java.io.IOException;

public class Tester {
    public static void main(String[] args) throws IOException {
        Index.init();
        Index.add("testfile1.txt");
        // Index.remove("testfile2.txt");
        Index.add("testfile2.txt");
        Index.remove("testfile2.txt");
    }

}
