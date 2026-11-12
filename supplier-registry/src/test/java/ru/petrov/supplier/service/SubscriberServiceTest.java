package ru.petrov.supplier.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClient;
import ru.petrov.supplier.client.SupplierHttpClient;
import ru.petrov.supplier.model.Supplier;
import ru.petrov.supplier.registry.SupplierRegistry;


import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.header;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

class SubscriberServiceTest {

    private MockRestServiceServer server;
    private SubscriberService subscriberService;

    @BeforeEach
    void setUp() {
        RestClient.Builder builder = RestClient.builder();
        server = MockRestServiceServer.bindTo(builder).build();

        Supplier supplier = new Supplier(
                "supplier-1",
                "https://api.supplier.test",
                "test-api-key"
        );
        SupplierRegistry registry = new SupplierRegistry(List.of(supplier));
        SupplierHttpClient httpClient = new SupplierHttpClient(builder.build());
        subscriberService = new SubscriberService(registry, httpClient);
    }

    @Test
    void shouldReturnTrueWhenSupplierReturnsStatusOk() {
        server.expect(requestTo("https://api.supplier.test/subscribers/79991234567"))
                .andExpect(method(HttpMethod.GET))
                .andExpect(header("X-API-Key", "test-api-key"))
                .andRespond(withSuccess("{\"status\":\"ok\"}", MediaType.APPLICATION_JSON));

        assertTrue(subscriberService.isAvailable("supplier-1", "79991234567"));
        server.verify();
    }

    @Test
    void shouldReturnFalseWhenSupplierReturnsNotFound() {
        server.expect(requestTo("https://api.supplier.test/subscribers/79991234567"))
                .andExpect(method(HttpMethod.GET))
                .andExpect(header("X-API-Key", "test-api-key"))
                .andRespond(withStatus(HttpStatus.NOT_FOUND));

        assertFalse(subscriberService.isAvailable("supplier-1", "79991234567"));
        server.verify();
    }

    @Test
    void shouldPropagateOtherHttpErrors() {
        server.expect(requestTo("https://api.supplier.test/subscribers/79991234567"))
                .andRespond(withStatus(HttpStatus.INTERNAL_SERVER_ERROR));

        assertThrows(HttpServerErrorException.class,
                () -> subscriberService.isAvailable("supplier-1", "79991234567"));
        server.verify();
    }
}
