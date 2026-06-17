package pawww.projekt.session;

import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;
import java.util.HashMap;
import java.util.Map;

@Component
@SessionScope
public class KoszykGlosow {
    private Map<Long, String> glosy = new HashMap<>();

    public Map<Long, String> getGlosy() {
        return glosy;
    }

    public void dodajGlos(Long projektId, String decyzja) {
        glosy.put(projektId, decyzja);
    }

    public void usunGlos(Long projektId) {
        glosy.remove(projektId);
    }

    public void wyczysc() {
        glosy.clear();
    }
}