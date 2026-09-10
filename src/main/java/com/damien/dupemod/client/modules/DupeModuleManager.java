package com.damien.dupemod.client.modules;

import net.minecraft.item.ItemStack;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerInventory;

public class DupeModuleManager {
    private ItemStack selectedItem = ItemStack.EMPTY;
    private int dupeCount = 0;
    private ItemDuplicator itemDuplicator;
    private ChestDuplicator chestDuplicator;
    private CounterDisplay counterDisplay;
    
    public DupeModuleManager() {
        this.itemDuplicator = new ItemDuplicator();
        this.chestDuplicator = new ChestDuplicator();
        this.counterDisplay = new CounterDisplay();
    }
    
    /**
     * Module 1: Select item and bring to hotbar with rapid place/break
     */
    public void selectAndDuplicateToHotbar(ItemStack item) {
        if (item.isEmpty()) return;
        
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null) return;
        
        this.selectedItem = item.copy();
        itemDuplicator.duplicateToHotbar(client.player, item);
    }
    
    /**
     * Module 2: Duplicate selected items into chest
     */
    public void duplicateToChest(ItemStack item, int targetX, int targetY, int targetZ) {
        if (item.isEmpty()) return;
        
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null || client.world == null) return;
        
        chestDuplicator.duplicateToChest(client.world, item, targetX, targetY, targetZ);
    }
    
    /**
     * Stop the rapid duplication
     */
    public void stopDuplication() {
        this.itemDuplicator.stopDuplication();
    }
    
    /**
     * Module 3: Get current dupe count
     */
    public int getDupeCount() {
        return dupeCount;
    }
    
    public void incrementDupeCount(int amount) {
        this.dupeCount += amount;
    }
    
    public void resetDupeCount() {
        this.dupeCount = 0;
    }
    
    public ItemStack getSelectedItem() {
        return this.selectedItem;
    }
    
    public void setSelectedItem(ItemStack item) {
        this.selectedItem = item.copy();
    }
    
    public CounterDisplay getCounterDisplay() {
        return counterDisplay;
    }
    
    public ItemDuplicator getItemDuplicator() {
        return itemDuplicator;
    }
}
