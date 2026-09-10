package com.damien.dupemod.client.modules;

import net.minecraft.item.ItemStack;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ChestDuplicator {
    
    /**
     * Duplicates all selected items (except one) into the target chest
     */
    public void duplicateToChest(World world, ItemStack item, int targetX, int targetY, int targetZ) {
        BlockPos chestPos = new BlockPos(targetX, targetY, targetZ);
        BlockEntity blockEntity = world.getBlockEntity(chestPos);
        
        if (blockEntity instanceof ChestBlockEntity) {
            ChestBlockEntity chest = (ChestBlockEntity) blockEntity;
            SimpleInventory inventory = chest.getInventory();
            
            // Create copy with count - 1 (keep 1 item)
            ItemStack dupeStack = item.copy();
            dupeStack.setCount(Math.max(1, item.getCount() - 1));
            
            // Add to chest inventory
            if (!dupeStack.isEmpty()) {
                inventory.addStack(dupeStack);
            }
        }
    }
}
