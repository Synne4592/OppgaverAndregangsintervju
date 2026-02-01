package oppgaver;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;
import java.util.regex.Pattern;

public class Oppgave3 {

    private static final Pattern IPV4 = Pattern.compile(
            "^(25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]\\d?|0)" +
                    "(\\.(25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]\\d?|0)){3}$"
    );

    public static void run(Path folder) throws IOException {
        Path txtFile = findSingleTxt(folder);
        Map<String, Integer> ipToTxtLine = loadTxtIps(txtFile);

        List<Path> csvFiles = listCsvFiles(folder);

        for (Path csv : csvFiles) {
            Match m = scanCsvForFirstMatch(csv, ipToTxtLine);
            if (m != null) {
                System.out.println("Match funnet:");
                System.out.println("CSV-fil: " + csv.getFileName());
                System.out.println("CSV-linje: " + m.csvLine);
                System.out.println("TXT-fil: " + txtFile.getFileName());
                System.out.println("TXT-linje: " + m.txtLine);
                return;
            }
        }

        System.out.println("Ingen match funnet.");
    }

    private static Path findSingleTxt(Path folder) throws IOException {
        try (var s = Files.list(folder)) {
            List<Path> txts = s.filter(p -> p.getFileName().toString().toLowerCase().endsWith(".txt"))
                    .toList();

            if (txts.isEmpty()) throw new IllegalStateException("Fant ingen .txt-fil i " + folder.toAbsolutePath());
            if (txts.size() > 1) throw new IllegalStateException("Fant flere .txt-filer (forventet 1): " + txts);
            return txts.get(0);
        }
    }

    private static List<Path> listCsvFiles(Path folder) throws IOException {
        try (var s = Files.list(folder)) {
            return s.filter(p -> p.getFileName().toString().toLowerCase().endsWith(".csv"))
                    .sorted(Comparator.comparing(p -> p.getFileName().toString()))
                    .toList();
        }
    }

    private static Map<String, Integer> loadTxtIps(Path txtFile) throws IOException {
        Map<String, Integer> map = new HashMap<>();
        try (BufferedReader br = Files.newBufferedReader(txtFile, StandardCharsets.UTF_8)) {
            String line;
            int lineNo = 0;
            while ((line = br.readLine()) != null) {
                lineNo++;
                String ip = line.trim();
                if (ip.isEmpty()) continue;
            }
        }
        return map;
    }

    private static Match scanCsvForFirstMatch(Path csvFile, Map<String, Integer> ipToTxtLine) throws IOException {
        try (BufferedReader br = Files.newBufferedReader(csvFile, StandardCharsets.UTF_8)) {
            String line;
            int lineNo = 0;
            while ((line = br.readLine()) != null) {
                lineNo++;

                String last = lastField(line);
                if (last == null) continue;

                if (!isValidIpv4(last)) continue;

                Integer txtLine = ipToTxtLine.get(last);
                if (txtLine != null) {
                    return new Match(lineNo, txtLine, last);
                }
            }
        }
        return null;
    }

    private static String lastField(String csvLine) {
        int i = csvLine.lastIndexOf(',');
        if (i < 0) return null;
        return csvLine.substring(i + 1).trim();
    }

    private static boolean isValidIpv4(String s) {
        if (s == null) return false;
        if (s.indexOf(':') >= 0) return false; // IPv6
        if (s.indexOf('-') >= 0) return false; // f.eks v-10.0.0.1
        return IPV4.matcher(s).matches();
    }

    private static class Match {
        final int csvLine;
        final int txtLine;
        final String ip;

        Match(int csvLine, int txtLine, String ip) {
            this.csvLine = csvLine;
            this.txtLine = txtLine;
            this.ip = ip;
        }
    }
}
