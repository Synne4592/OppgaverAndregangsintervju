package oppgaver;

import java.io.*;
        import java.nio.file.*;
        import java.util.zip.GZIPInputStream;

public class Main {
    public static void main(String[] args) throws IOException {

        File inputFile = new File("src/main/resources/rockyou.txt.gz");
        File outputFile = new File("src/main/resources/rockyou.txt");


        try (GZIPInputStream gzipInputStream = new GZIPInputStream(new FileInputStream(inputFile));
             FileOutputStream fileOutputStream = new FileOutputStream(outputFile)) {

            byte[] buffer = new byte[1024];
            int length;
            while ((length = gzipInputStream.read(buffer)) > 0) {
                fileOutputStream.write(buffer, 0, length);
            }
        }

        System.out.println("Filen er dekomprimert og lagret som rockyou.txt.");
    }
}
