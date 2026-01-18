package oppgaver;

import org.springframework.http.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class NvdbClient {

    private static final int MIN_TYPE_ID = 1;
    private static final int MAX_TYPE_ID = 300; // trygg øvre grense
    private static final int MAX_ATTEMPTS = 10;
    private final RestTemplate restTemplate = new RestTemplate();

    private List<VegObjekt> cachedNordOdd;

    public VegObjekt tilfeldigVegObjektNordOdd() {
        if (cachedNordOdd == null) {
            cachedNordOdd = hentOgByggCache();
        }
        if (cachedNordOdd.isEmpty()) {
            throw new IllegalStateException("Fant ingen vegobjekter nord for polarsirkelen med oddetalls-id.");
        }
        return cachedNordOdd.get(ThreadLocalRandom.current().nextInt(cachedNordOdd.size()));
    }

    @SuppressWarnings("unchecked")
    private List<VegObjekt> hentOgByggCache() {

        for (int attempt = 0; attempt < MAX_ATTEMPTS; attempt++) {

            int typeId = ThreadLocalRandom.current()
                    .nextInt(MIN_TYPE_ID, MAX_TYPE_ID + 1);

            String url =
                    "https://nvdbapiles.atlas.vegvesen.no/vegobjekter/api/v4/vegobjekter/" + typeId +
                            "?fylke=18,55,56" +
                            "&antall=1000" +
                            "&inkluder=metadata" +
                            "&inkluderAntall=false";

            HttpHeaders headers = new HttpHeaders();
            headers.set("X-Client", "OppgavePassordGenerator");

            HttpEntity<Void> entity = new HttpEntity<>(headers);

            ResponseEntity<Map> resp;
            try {
                resp = restTemplate.exchange(url, HttpMethod.GET, entity, Map.class);
            } catch (HttpClientErrorException e) {
                continue;
            }

            Map<String, Object> body = resp.getBody();
            if (body == null) continue;

            List<Map<String, Object>> objekter = (List<Map<String, Object>>) body.get("objekter");
            if (objekter == null || objekter.isEmpty()) continue;

            List<VegObjekt> out = new ArrayList<>();

            for (Map<String, Object> obj : objekter) {
                Number idNum = (Number) obj.get("id");
                if (idNum == null) continue;

                long id = idNum.longValue();
                if (id % 2 == 0) continue;

                Map<String, Object> metadata = (Map<String, Object>) obj.get("metadata");
                if (metadata == null) continue;

                Map<String, Object> type = (Map<String, Object>) metadata.get("type");
                if (type == null) continue;

                String navn = Objects.toString(type.get("navn"), null);
                if (navn == null || navn.isBlank()) continue;

                out.add(new VegObjekt(id, navn));
            }

            if (!out.isEmpty()) {
                return out;
            }
        }

        throw new IllegalStateException(
                "Fant ingen gyldige vegobjekter etter " + MAX_ATTEMPTS + " forsøk"
        );
    }
}