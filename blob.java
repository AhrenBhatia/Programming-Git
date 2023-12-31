import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Blob {
    public static StringBuilder contains = new StringBuilder();

    String sha = "";

    public Blob(String inputFile) throws IOException {
        try {
            File file = new File(inputFile);
            BufferedReader reader = new BufferedReader(new FileReader(file));
            while (reader.ready()) {
                contains.append(reader.read());
            }
            reader.close();
            String hashed = hashStringToSHA1(contains.toString());
            shaFile(hashed, contains);

            sha = hashed;

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String fileContents() {
        return contains.toString();
    }

    public static String shaFile(String hashed, StringBuilder contains) {
        try {
            String newFile = hashed;
            PrintWriter pw = new PrintWriter("./objects/" + newFile);
            pw.write(contains.toString());
            pw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return hashed;
    }

    public static String hashStringToSHA1(String input) {
        try {
            MessageDigest sha1Digest = MessageDigest.getInstance("SHA-1");
            byte[] inputBytes = input.getBytes();
            sha1Digest.update(inputBytes);
            byte[] hashBytes = sha1Digest.digest();
            StringBuilder hexString = new StringBuilder();
            for (byte hashByte : hashBytes) {
                String hex = Integer.toHexString(0xFF & hashByte);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getSha ()
    {
        return sha;
    }
}
