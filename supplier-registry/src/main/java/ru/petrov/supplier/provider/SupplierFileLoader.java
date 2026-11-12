package ru.petrov.supplier.provider;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import ru.petrov.supplier.model.Supplier;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public class SupplierFileLoader {

    private final ObjectMapper objectMapper;

    public SupplierFileLoader(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public List<Supplier> load(Path path) {
        try {
            return objectMapper.readValue(path.toFile(), new TypeReference<>() {});
        } catch (IOException exception) {
            throw new IllegalStateException(
                    "Failed to load suppliers from: " + path.toAbsolutePath(),
                    exception
            );
        }
    }
}
