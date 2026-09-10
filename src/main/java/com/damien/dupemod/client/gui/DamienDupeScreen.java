package com.damien.dupemod.client.gui;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.item.ItemStack;
import net.minecraft.inventory.Inventory;
import com.damien.dupemod.client.modules.DupeModuleManager;
import com.damien.dupemod.client.DamienDupeClientMod;

public class DamienDupeScreen extends Screen {
    private DupeModuleManager moduleManager;
    private ItemStack selectedItem = ItemStack.EMPTY;
    private int selectedSlot = -1;
    private boolean dupeRunning = false;
    private static final int GUI_WIDTH = 280;
    private static final int GUI_HEIGHT = 240;
    private int guiLeft;
    private int guiTop;
    private String recordingKeybind = null;
    
    public DamienDupeScreen(DupeModuleManager moduleManager) {
        super(Text.literal("Damien Dupe"));
        this.moduleManager = moduleManager;
    }
    
    @Override
    protected void init() {
        super.init();
        this.guiLeft = (this.width - GUI_WIDTH) / 2;
        this.guiTop = (this.height - GUI_HEIGHT) / 2;
        
        // Module 1: Item Selection and Duplication
        this.addDrawableChild(new ButtonWidget(this.guiLeft + 10, this.guiTop + 30, 60, 20, 
            Text.literal("Select"), button -> openItemSelector()));
        
        this.addDrawableChild(new ButtonWidget(this.guiLeft + 75, this.guiTop + 30, 50, 20, 
            Text.literal("Start"), button -> startItemDupe()));
        
        this.addDrawableChild(new ButtonWidget(this.guiLeft + 130, this.guiTop + 30, 50, 20, 
            Text.literal("Stop"), button -> stopItemDupe()));
        
        this.addDrawableChild(new ButtonWidget(this.guiLeft + 185, this.guiTop + 30, 80, 20, 
            Text.literal("Keybind 1"), button -> startKeybindRecording("module1")));
        
        // Module 2: Chest Duplication
        this.addDrawableChild(new ButtonWidget(this.guiLeft + 10, this.guiTop + 75, 60, 20, 
            Text.literal("Target"), button -> openChestSelector()));
        
        this.addDrawableChild(new ButtonWidget(this.guiLeft + 75, this.guiTop + 75, 105, 20, 
            Text.literal("Dupe to Chest"), button -> dupeToChest()));
        
        this.addDrawableChild(new ButtonWidget(this.guiLeft + 185, this.guiTop + 75, 80, 20, 
            Text.literal("Keybind 2"), button -> startKeybindRecording("module2")));
        
        // Module 3: Counter Display
        this.addDrawableChild(new ButtonWidget(this.guiLeft + 10, this.guiTop + 120, 100, 20, 
            Text.literal("Reset Counter"), button -> moduleManager.resetDupeCount()));
        
        this.addDrawableChild(new ButtonWidget(this.guiLeft + 115, this.guiTop + 120, 150, 20, 
            Text.literal("Counter Keybind 3"), button -> startKeybindRecording("module3")));
        
        // Close button
        this.addDrawableChild(new ButtonWidget(this.guiLeft + 100, this.guiTop + 200, 80, 20, 
            Text.literal("Close"), button -> this.close()));
    }
    
    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackground(matrices);
        
        // Draw GUI background
        fill(matrices, this.guiLeft, this.guiTop, this.guiLeft + GUI_WIDTH, this.guiTop + GUI_HEIGHT, 0xFF8B8B8B);
        fill(matrices, this.guiLeft + 2, this.guiTop + 2, this.guiLeft + GUI_WIDTH - 2, this.guiTop + GUI_HEIGHT - 2, 0xFF3F3F3F);
        
        // Draw title
        this.textRenderer.draw(matrices, "Damien Dupe", this.guiLeft + 10, this.guiTop + 10, 0xFFFFFF);
        
        // Module 1 - Item Selection
        this.textRenderer.draw(matrices, "Module 1: Item Dupe", this.guiLeft + 10, this.guiTop + 50, 0xFFFF00);
        if (!selectedItem.isEmpty()) {
            String status = dupeRunning ? "RUNNING" : "Selected";
            int color = dupeRunning ? 0x00FF00 : 0xAAAAAA;
            this.textRenderer.draw(matrices, status + ": " + selectedItem.getName().getString(), 
                this.guiLeft + 10, this.guiTop + 60, color);
        } else {
            this.textRenderer.draw(matrices, "No item selected", this.guiLeft + 10, this.guiTop + 60, 0xFF0000);
        }
        String key1 = moduleManager.getKeybindManager().getKeyName("module1");
        this.textRenderer.draw(matrices, "Key: " + key1, this.guiLeft + 185, this.guiTop + 50, 0x00FFFF);
        
        // Module 2 - Chest Duplication
        this.textRenderer.draw(matrices, "Module 2: Chest Dupe", this.guiLeft + 10, this.guiTop + 95, 0xFFFF00);
        this.textRenderer.draw(matrices, "Find & dupe to nearest chest", this.guiLeft + 10, this.guiTop + 105, 0xAAAAAA);
        String key2 = moduleManager.getKeybindManager().getKeyName("module2");
        this.textRenderer.draw(matrices, "Key: " + key2, this.guiLeft + 185, this.guiTop + 95, 0x00FFFF);
        
        // Module 3 - Counter
        this.textRenderer.draw(matrices, "Module 3: Counter Display", this.guiLeft + 10, this.guiTop + 140, 0xFFFF00);
        String countText = "Items Duped: " + moduleManager.getCounterDisplay().getFormattedCount();
        this.textRenderer.draw(matrices, countText, this.guiLeft + 10, this.guiTop + 150, 0x00FFFF);
        String key3 = moduleManager.getKeybindManager().getKeyName("module3");
        this.textRenderer.draw(matrices, "Key: " + key3, this.guiLeft + 10, this.guiTop + 160, 0x00FFFF);
        
        // Recording indicator
        if (recordingKeybind != null) {
            this.textRenderer.draw(matrices, "Press any key to bind...", this.guiLeft + 50, this.guiTop + 180, 0xFF5555);
        }
        
        super.render(matrices, mouseX, mouseY, delta);
    }
    
    private void openItemSelector() {
        if (this.client != null && this.client.player != null) {
            Inventory inventory = this.client.player.getInventory();
            this.selectedItem = inventory.getStack(this.client.player.getInventory().selected).copy();
            if (!this.selectedItem.isEmpty()) {
                this.moduleManager.setSelectedItem(this.selectedItem);
            }
        }
    }
    
    private void startItemDupe() {
        if (this.client != null && this.client.player != null && !selectedItem.isEmpty()) {
            this.moduleManager.selectAndDuplicateToHotbar(selectedItem);
            this.dupeRunning = true;
            this.moduleManager.getCounterDisplay().addCount(selectedItem.getCount());
        }
    }
    
    private void stopItemDupe() {
        this.moduleManager.stopDuplication();
        this.dupeRunning = false;
    }
    
    private void openChestSelector() {
        this.client.setScreen(new ChestSelectorScreen(this, this.moduleManager));
    }
    
    private void dupeToChest() {
        // This will be called from ChestSelectorScreen
    }
    
    private void startKeybindRecording(String moduleName) {
        this.recordingKeybind = moduleName;
        this.moduleManager.getKeybindManager().startRecording(moduleName);
    }
    
    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (recordingKeybind != null) {
            if (keyCode != 256) { // Not ESC
                this.moduleManager.getKeybindManager().setKeybind(recordingKeybind, keyCode);
                this.recordingKeybind = null;
                return true;
            } else {
                this.moduleManager.getKeybindManager().stopRecording();
                this.recordingKeybind = null;
                return true;
            }
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }
    
    @Override
    public void close() {
        this.moduleManager.stopDuplication();
        this.moduleManager.getKeybindManager().stopRecording();
        DamienDupeClientMod.setScreenOpen(false);
        super.close();
    }
    
    @Override
    public boolean shouldCloseOnEsc() {
        if (recordingKeybind != null) {
            this.recordingKeybind = null;
            this.moduleManager.getKeybindManager().stopRecording();
            return false;
        }
        return true;
    }
}
