package oppgaver;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.zip.GZIPInputStream;

public class RockyouReader {

    public static void testRead(FirstNameIndex firstNameIndex) throws Exception {
        try (InputStream is = RockyouReader.class.getClassLoader().getResourceAsStream("rockyou.txt.gz")) {
            if (is == null) throw new IllegalStateException("Fant ikke rockyou.txt.gz i resources");

            try (GZIPInputStream gz = new GZIPInputStream(is);
                 BufferedReader br = new BufferedReader(new InputStreamReader(gz, StandardCharsets.UTF_8))) {

                String line;
                long matches = 0;

                while ((line = br.readLine()) != null) {

                    if (!line.matches("^[A-Za-z]+$")) continue;

                    String reversed = new StringBuilder(line).reverse().toString();

                    if (firstNameIndex.matchesAny(reversed)) {
                        System.out.println("MATCH: " + line + " -> " + reversed);
                        matches++;
                    }

                }
            }
        }
    }
}
