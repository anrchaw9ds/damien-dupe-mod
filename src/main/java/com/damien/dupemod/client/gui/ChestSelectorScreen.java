package com.damien.dupemod.client.gui;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.ChestBlockEntity;
import com.damien.dupemod.client.modules.DupeModuleManager;
import com.damien.dupemod.client.DamienDupeClientMod;

public class ChestSelectorScreen extends Screen {
    private DamienDupeScreen previousScreen;
    private DupeModuleManager moduleManager;
    private BlockPos targetChestPos = null;
    private static final int GUI_WIDTH = 256;
    private static final int GUI_HEIGHT = 150;
    private int guiLeft;
    private int guiTop;
    
    public ChestSelectorScreen(DamienDupeScreen previousScreen, DupeModuleManager moduleManager) {
        super(Text.literal("Select Target Chest"));
        this.previousScreen = previousScreen;
        this.moduleManager = moduleManager;
    }
    
    @Override
    protected void init() {
        super.init();
        this.guiLeft = (this.width - GUI_WIDTH) / 2;
        this.guiTop = (this.height - GUI_HEIGHT) / 2;
        
        this.addDrawableChild(new ButtonWidget(this.guiLeft + 10, this.guiTop + 60, 100, 20, 
            Text.literal("Find Nearest"), button -> findNearestChest()));
        
        this.addDrawableChild(new ButtonWidget(this.guiLeft + 120, this.guiTop + 60, 100, 20, 
            Text.literal("Dupe & Back"), button -> dupeAndReturn()));
        
        this.addDrawableChild(new ButtonWidget(this.guiLeft + 85, this.guiTop + 100, 80, 20, 
            Text.literal("Back"), button -> this.close()));
    }
    
    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackground(matrices);
        
        fill(matrices, this.guiLeft, this.guiTop, this.guiLeft + GUI_WIDTH, this.guiTop + GUI_HEIGHT, 0xFF8B8B8B);
        fill(matrices, this.guiLeft + 2, this.guiTop + 2, this.guiLeft + GUI_WIDTH - 2, this.guiTop + GUI_HEIGHT - 2, 0xFF3F3F3F);
        
        this.textRenderer.draw(matrices, "Target Chest Selector", this.guiLeft + 10, this.guiTop + 10, 0xFFFFFF);
        
        if (targetChestPos != null) {
            this.textRenderer.draw(matrices, "Target: " + targetChestPos.getX() + ", " + 
                targetChestPos.getY() + ", " + targetChestPos.getZ(), 
                this.guiLeft + 10, this.guiTop + 35, 0x00FF00);
        } else {
            this.textRenderer.draw(matrices, "No chest selected", this.guiLeft + 10, this.guiTop + 35, 0xFF0000);
        }
        
        super.render(matrices, mouseX, mouseY, delta);
    }
    
    private void findNearestChest() {
        if (this.client == null || this.client.player == null || this.client.world == null) return;
        
        BlockPos playerPos = this.client.player.getBlockPos();
        double closestDistance = Double.MAX_VALUE;
        BlockPos closestChest = null;
        
        // Search within 64 block radius
        for (int x = playerPos.getX() - 64; x <= playerPos.getX() + 64; x++) {
            for (int y = playerPos.getY() - 64; y <= playerPos.getY() + 64; y++) {
                for (int z = playerPos.getZ() - 64; z <= playerPos.getZ() + 64; z++) {
                    BlockPos pos = new BlockPos(x, y, z);
                    BlockEntity blockEntity = this.client.world.getBlockEntity(pos);
                    
                    if (blockEntity instanceof ChestBlockEntity) {
                        double distance = playerPos.getSquaredDistance(pos);
                        if (distance < closestDistance) {
                            closestDistance = distance;
                            closestChest = pos;
                        }
                    }
                }
            }
        }
        
        this.targetChestPos = closestChest;
    }
    
    private void dupeAndReturn() {
        if (targetChestPos != null && this.client != null && this.client.world != null) {
            moduleManager.duplicateToChest(moduleManager.getSelectedItem(), 
                targetChestPos.getX(), targetChestPos.getY(), targetChestPos.getZ());
            moduleManager.getCounterDisplay().addCount(moduleManager.getSelectedItem().getCount() - 1);
        }
        this.close();
    }
    
    @Override
    public void close() {
        if (this.client != null) {
            this.client.setScreen(this.previousScreen);
        }
    }
    
    @Override
    public boolean shouldCloseOnEsc() {
        return true;
    }
}
