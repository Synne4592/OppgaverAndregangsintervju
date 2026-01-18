package oppgaver;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.zip.GZIPInputStream;

public class RockyouReader {

    @FunctionalInterface
    public interface LineTransformer {
        String transform(String line) throws Exception;
    }

    public static long transformResourceGzToFile(String resourceName, Path outputFile, LineTransformer transformer) throws Exception {
        long linesWritten = 0;

        try (InputStream is = RockyouReader.class.getClassLoader().getResourceAsStream(resourceName)) {
            if (is == null) throw new IllegalStateException("Fant ikke " + resourceName + " i resources");

            try (GZIPInputStream gz = new GZIPInputStream(is);
                 BufferedReader br = new BufferedReader(new InputStreamReader(gz, StandardCharsets.UTF_8));
                BufferedWriter bw = Files.newBufferedWriter(outputFile, StandardCharsets.UTF_8)){

                String line;
                while ((line = br.readLine()) != null) {
                    String out = transformer.transform(line);
                    bw.write(out);
                    bw.newLine();
                    linesWritten++;
                }
            }
        }
        return linesWritten;
    }
}
