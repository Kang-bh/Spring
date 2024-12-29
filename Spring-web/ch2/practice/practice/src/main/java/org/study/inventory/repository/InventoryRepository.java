package org.study.inventory.repository;

import java.util.Optional;

public interface InventoryRepository {
    long findById(long id);
    void save(long id, long quantity);
}
