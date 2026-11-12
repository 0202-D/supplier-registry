package ru.petrov.supplier.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import ru.petrov.supplier.model.Supplier;
import ru.petrov.supplier.provider.SupplierFileLoader;
import ru.petrov.supplier.registry.SupplierRegistry;

import java.nio.file.Path;
import java.util.List;

@Configuration
public class AppConfig {

    @Bean
    RestClient restClient(RestClient.Builder builder) {
        return builder.build();
    }

    @Bean
    SupplierFileLoader supplierFileLoader(ObjectMapper objectMapper) {
        return new SupplierFileLoader(objectMapper);
    }

    @Bean
    SupplierRegistry supplierRegistry(
            SupplierFileLoader loader,
            @Value("${suppliers.file:}") String suppliersFile) {
        List<Supplier> suppliers = suppliersFile.isBlank()
                ? List.of()
                : loader.load(Path.of(suppliersFile));
        return new SupplierRegistry(suppliers);
    }
}
