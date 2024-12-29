package org.study.inventory.service;

import org.study.domain.Inventory;

import java.util.List;

public interface InventoryService {
    Inventory getInventory(long id);
    List<Inventory> getInventories();
    void stockInventory(long id, long quantity);
    void takeInventory(long id, long quantity);
}
