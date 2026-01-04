package cl.claveles.terreno.eip.pipeline;

import java.util.HashMap;
import java.util.Map;

public class Mensaje {
    private String payload;
    private Map<String, Object> headers = new HashMap<>();

    public Mensaje(String payload, Map<String, Object> headers) {
        this.payload = payload;
        this.headers = headers;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    public Map<String, Object> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, Object> headers) {
        this.headers = headers;
    }
}
