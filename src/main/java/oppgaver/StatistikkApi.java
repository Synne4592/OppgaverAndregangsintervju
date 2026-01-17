package oppgaver;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
public class StatistikkApi {

    private final RestTemplate restTemplate = new RestTemplate();

    // cache: hentes bare én gang, gjenbrukes for alle passord
    private List<Map<String, Object>> cached;

    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> hentVegobjekterStatistikk() {
        if (cached != null) return cached;

        // NVDB API base:
        // https://nvdbapiles.atlas.vegvesen.no/
        String url = "https://nvdbapiles.atlas.vegvesen.no/vegobjekter/statistikk";

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Client", "OppgavePassordGenerator"); // viktig!

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<List> resp = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                List.class
        );

        cached = (List<Map<String, Object>>) resp.getBody();
        return cached;
    }
}
