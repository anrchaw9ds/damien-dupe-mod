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
    private static final int GUI_WIDTH = 256;
    private static final int GUI_HEIGHT = 200;
    private int guiLeft;
    private int guiTop;
    
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
        this.addDrawableChild(new ButtonWidget(this.guiLeft + 10, this.guiTop + 30, 100, 20, 
            Text.literal("Select Item"), button -> openItemSelector()));
        
        this.addDrawableChild(new ButtonWidget(this.guiLeft + 120, this.guiTop + 30, 100, 20, 
            Text.literal("Start Dupe"), button -> startItemDupe()));
        
        // Module 2: Chest Duplication
        this.addDrawableChild(new ButtonWidget(this.guiLeft + 10, this.guiTop + 80, 100, 20, 
            Text.literal("Target Chest"), button -> openChestSelector()));
        
        this.addDrawableChild(new ButtonWidget(this.guiLeft + 120, this.guiTop + 80, 100, 20, 
            Text.literal("Dupe to Chest"), button -> dupeToChest()));
        
        // Module 3: Counter Display
        this.addDrawableChild(new ButtonWidget(this.guiLeft + 10, this.guiTop + 130, 100, 20, 
            Text.literal("Reset Counter"), button -> moduleManager.resetDupeCount()));
        
        this.addDrawableChild(new ButtonWidget(this.guiLeft + 120, this.guiTop + 130, 100, 20, 
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
            this.textRenderer.draw(matrices, "Selected: " + selectedItem.getName().getString(), 
                this.guiLeft + 10, this.guiTop + 60, 0x00FF00);
        } else {
            this.textRenderer.draw(matrices, "No item selected", this.guiLeft + 10, this.guiTop + 60, 0xFF0000);
        }
        
        // Module 2 - Chest Duplication
        this.textRenderer.draw(matrices, "Module 2: Chest Dupe", this.guiLeft + 10, this.guiTop + 100, 0xFFFF00);
        this.textRenderer.draw(matrices, "Target nearest chest", this.guiLeft + 10, this.guiTop + 110, 0xAAAAAA);
        
        // Module 3 - Counter
        this.textRenderer.draw(matrices, "Module 3: Counter", this.guiLeft + 10, this.guiTop + 150, 0xFFFF00);
        String countText = "Items Duped: " + moduleManager.getCounterDisplay().getFormattedCount();
        this.textRenderer.draw(matrices, countText, this.guiLeft + 10, this.guiTop + 160, 0x00FFFF);
        
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
            this.moduleManager.getCounterDisplay().addCount(selectedItem.getCount());
        }
    }
    
    private void openChestSelector() {
        this.client.setScreen(new ChestSelectorScreen(this, this.moduleManager));
    }
    
    private void dupeToChest() {
        // This will be called from ChestSelectorScreen
    }
    
    @Override
    public void close() {
        DamienDupeClientMod.setScreenOpen(false);
        super.close();
    }
    
    @Override
    public boolean shouldCloseOnEsc() {
        return true;
    }
}
