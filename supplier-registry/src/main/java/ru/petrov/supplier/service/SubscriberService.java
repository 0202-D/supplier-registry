package ru.petrov.supplier.service;

import org.springframework.stereotype.Service;
import ru.petrov.supplier.client.SupplierHttpClient;
import ru.petrov.supplier.registry.SupplierRegistry;

@Service
public class SubscriberService {

    private final SupplierRegistry supplierRegistry;
    private final SupplierHttpClient supplierHttpClient;

    public SubscriberService(SupplierRegistry supplierRegistry,
                             SupplierHttpClient supplierHttpClient) {
        this.supplierRegistry = supplierRegistry;
        this.supplierHttpClient = supplierHttpClient;
    }

    public boolean isAvailable(String supplierId, String phone) {
        return supplierHttpClient.isAvailable(
                supplierRegistry.getById(supplierId),
                phone
        );
    }
}
