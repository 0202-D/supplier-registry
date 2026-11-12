package ru.petrov.supplier.registry;

import org.junit.jupiter.api.Test;
import ru.petrov.supplier.model.Supplier;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SupplierRegistryTest {

    @Test
    void shouldReplaceSuppliersAsAWhole() {
        Supplier first = new Supplier("first", "https://first.test", "key-1");
        Supplier second = new Supplier("second", "https://second.test", "key-2");
        SupplierRegistry registry = new SupplierRegistry(List.of(first));

        registry.replaceAll(List.of(second));

        assertEquals(List.of(second), registry.getAll());
        assertThrows(IllegalArgumentException.class, () -> registry.getById("first"));
        assertSame(second, registry.getById("second"));
    }

    @Test
    void shouldKeepPreviousStateWhenReplacementHasDuplicateIds() {
        Supplier original = new Supplier("original", "https://original.test", "key");
        Supplier duplicateOne = new Supplier("duplicate", "https://one.test", "key-1");
        Supplier duplicateTwo = new Supplier("duplicate", "https://two.test", "key-2");
        SupplierRegistry registry = new SupplierRegistry(List.of(original));

        assertThrows(IllegalStateException.class,
                () -> registry.replaceAll(List.of(duplicateOne, duplicateTwo)));

        assertEquals(List.of(original), registry.getAll());
    }
}
