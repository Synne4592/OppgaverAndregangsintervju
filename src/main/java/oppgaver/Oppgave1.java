package oppgaver;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicLong;

public class Oppgave1 {

    private static final String ONLY_ALPHA_REGEX = "^[A-Za-z]+$";

    public static void run() throws Exception {
        FirstNameIndex index = FirstNameIndex.load();
        NvdbClient nvdb = new NvdbClient();

        //Write output to new rockyou.txt-file instead of overriding original rockyou.txt.gz
        Path outFile = Paths.get("rockyou.txt");
        AtomicLong matches = new AtomicLong(0);

        RockyouReader.transformResourceGzToFile("rockyou.txt.gz", outFile, line -> {
            if (!line.matches(ONLY_ALPHA_REGEX)) return line;

            String reversed = new StringBuilder(line).reverse().toString();

            if (!index.matchesAny(reversed)) return line;

            VegObjekt v = nvdb.tilfeldigVegObjektNordOdd();

            String navn = v.navn()
                    .replaceAll("\\s+", "")
                    .toUpperCase(Locale.ROOT);

            matches.incrementAndGet();
            return reversed + v.id() + navn;
        });

        System.out.println("Skrev output til: " + outFile.toAbsolutePath());
        System.out.println("Antall matcher: " + matches.get());

    }
}
