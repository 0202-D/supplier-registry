package ru.petrov.supplier.registry;

import ru.petrov.supplier.model.Supplier;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;
import java.util.stream.Collectors;

public class SupplierRegistry {

    private final AtomicReference<Map<String, Supplier>> suppliers =
            new AtomicReference<>(Map.of());

    public SupplierRegistry(List<Supplier> initialSuppliers) {
        replaceAll(initialSuppliers);
    }

    public Supplier getById(String id) {
        Supplier supplier = suppliers.get().get(id);
        if (supplier == null) {
            throw new IllegalArgumentException("Supplier not found: " + id);
        }
        return supplier;
    }

    public List<Supplier> getAll() {
        return List.copyOf(suppliers.get().values());
    }

    public void replaceAll(List<Supplier> newSuppliers) {
        if (newSuppliers == null) {
            throw new IllegalArgumentException("Suppliers list must not be null");
        }
        Map<String, Supplier> replacement = newSuppliers.stream()
                .collect(Collectors.toUnmodifiableMap(
                        Supplier::id,
                        Function.identity()
                ));
        suppliers.set(replacement);
    }
}
