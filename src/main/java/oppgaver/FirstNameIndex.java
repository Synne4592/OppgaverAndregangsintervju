package oppgaver;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.InputStream;
import java.util.List;
import java.util.Locale;

public class FirstNameIndex {

    private final List<String> namesLower;

    private FirstNameIndex(List<String> namesLower) {
        this.namesLower = namesLower;
    }

    public static FirstNameIndex load() throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        try (InputStream is = FirstNameIndex.class
                .getClassLoader()
                .getResourceAsStream("firstnames_f.json")) {

            if (is == null) {
                throw new IllegalStateException("Fant ikke firstnames_f.json");
            }

            List<String> names = mapper.readValue(
                    is,
                    new TypeReference<List<String>>() {}
            );

            List<String> lower = names.stream()
                    .map(n -> n.toLowerCase(Locale.ROOT))
                    .toList();

            return new FirstNameIndex(lower);
        }
    }

    public boolean matchesAny(String reversed) {
        String reversedLower = reversed.toLowerCase(Locale.ROOT);

        for (String name : namesLower) {
            if (name.contains(reversedLower)) {
                return true;
            }
        }
        return false;
    }
}
