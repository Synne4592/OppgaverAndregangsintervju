package oppgaver;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

public class FileStore {
    private final Path passordFil;
    private final Path midlertidigFil;
    private final Path hashVault;

    public FileStore(Path passordFil, Path midlertidigFil, Path hashVault) throws IOException {
        this.passordFil = passordFil;
        this.midlertidigFil = midlertidigFil;
        this.hashVault = hashVault;

        ensureFileExists(midlertidigFil);
        ensureFileExists(hashVault);

        if (!Files.exists(passordFil)) {
            throw new IllegalStateException("Mangler inputfil: " + passordFil.toAbsolutePath());
        }
    }

    private static void ensureFileExists(Path p) throws IOException {
        if (!Files.exists(p)) {
            Path parent = p.toAbsolutePath().getParent();
            if (parent != null) {
                Files.createDirectories(parent);
            }
            Files.createFile(p);
        }
    }

    private static List<String> readAllLinesSafe(Path p) throws IOException {
        if (!Files.exists(p)) return new ArrayList<>();
        return new ArrayList<>(Files.readAllLines(p, StandardCharsets.UTF_8));
    }

    private static void writeAllLines(Path p, List<String> lines) throws IOException {
        Files.write(p, lines, StandardCharsets.UTF_8,
                StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.CREATE);
    }

    public int size(Path file) throws IOException {
        return readAllLinesSafe(file).size();
    }

    public String peek(Path file) throws IOException {
        List<String> lines = readAllLinesSafe(file);
        if (lines.isEmpty()) return null;
        return lines.get(0);
    }

    public String pop(Path file) throws IOException {
        List<String> lines = readAllLinesSafe(file);
        if (lines.isEmpty()) return null;
        String first = lines.remove(0);
        writeAllLines(file, lines);
        return first;
    }

    public void push(Path file, String s) throws IOException {
        List<String> lines = readAllLinesSafe(file);
        if (!lines.isEmpty()) {
            String existingFirst = lines.get(0);
            if (existingFirst.compareTo(s) < 0) {
                throw new IllegalStateException("Ulovlig push: kan ikke skrive '"+ s + "' til " + file.getFileName() +
                        " fordi '" + existingFirst + "' kommer før den alfabetisk."
                );
            }
        }
        lines.add(0, s);
        writeAllLines(file, lines);
    }

    public void move(Path from, Path to) throws IOException {
        String s = pop(from);
        if (s == null) {
            throw new IllegalStateException("Kan ikke flytte fra tom fil: " + from.getFileName());
        }
        push(to, s);
        writeAllLines(file, lines);
    }

    public Path passordFil() {
        return passordFil;
    }
    public Path midlertidigFil() {
        return midlertidigFil;
    }
    public Path hashVault() {
        return hashVault;
    }
}
