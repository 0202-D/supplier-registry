package ru.petrov.supplier.client;

import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;
import ru.petrov.supplier.model.SubscriberResponse;
import ru.petrov.supplier.model.Supplier;

@Component
public class SupplierHttpClient {

    private static final String API_KEY_HEADER = "X-API-Key";

    private final RestClient restClient;

    public SupplierHttpClient(RestClient restClient) {
        this.restClient = restClient;
    }

    public boolean isAvailable(Supplier supplier, String phone) {
        try {
            SubscriberResponse response = restClient.get()
                    .uri(supplier.baseUrl() + "/subscribers/{phone}", phone)
                    .header(API_KEY_HEADER, supplier.apiKey())
                    .retrieve()
                    .body(SubscriberResponse.class);

            return response != null && "ok".equals(response.status());
        } catch (HttpClientErrorException.NotFound exception) {
            return false;
        }
    }
}
