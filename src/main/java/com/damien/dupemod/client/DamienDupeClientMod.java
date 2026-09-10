package com.damien.dupemod.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.damien.dupemod.client.gui.DamienDupeScreen;
import com.damien.dupemod.client.modules.DupeModuleManager;

public class DamienDupeClientMod implements ClientModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("damien-dupe-mod-client");
    
    private static KeyBinding openGuiKey;
    private static DupeModuleManager moduleManager;
    private static boolean screenOpen = false;

    @Override
    public void onInitializeClient() {
        LOGGER.info("Damien Dupe Client Mod initialized!");
        
        moduleManager = new DupeModuleManager();
        
        // Register keybinding for - key
        openGuiKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.damien-dupe-mod.open_gui",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_MINUS,
            "category.damien-dupe-mod"
        ));
        
        // Register tick event to check for key press and handle rapid duplication
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (openGuiKey.wasPressed()) {
                if (!screenOpen && client.player != null) {
                    client.setScreen(new DamienDupeScreen(moduleManager));
                    screenOpen = true;
                }
            }
            
            // Update duplication ticker - runs every tick for rapid cycling
            if (client.player != null && moduleManager.getItemDuplicator().isRunning()) {
                moduleManager.getItemDuplicator().tick(client.player, moduleManager.getSelectedItem());
            }
            
            // Check module keybinds
            if (client.player != null) {
                int module1Key = moduleManager.getKeybindManager().getKeybind("module1");
                int module2Key = moduleManager.getKeybindManager().getKeybind("module2");
                int module3Key = moduleManager.getKeybindManager().getKeybind("module3");
                
                // Module 1: Start/Stop item duplication
                if (module1Key != -1 && InputUtil.isKeyPressed(client.getWindow().getHandle(), module1Key)) {
                    if (!moduleManager.getItemDuplicator().isRunning()) {
                        moduleManager.selectAndDuplicateToHotbar(moduleManager.getSelectedItem());
                    }
                }
                
                // Module 3: Reset counter (using middle mouse or configurable key)
                if (module3Key != -1 && InputUtil.isKeyPressed(client.getWindow().getHandle(), module3Key)) {
                    moduleManager.resetDupeCount();
                }
            }
        });
    }
    
    public static DupeModuleManager getModuleManager() {
        return moduleManager;
    }
    
    public static void setScreenOpen(boolean open) {
        screenOpen = open;
    }
}
