package com.damien.dupemod.client.modules;

import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;
import java.util.HashMap;
import java.util.Map;

public class ModuleKeybindManager {
    private Map<String, Integer> moduleKeybinds = new HashMap<>();
    private String recordingModule = null;
    
    public ModuleKeybindManager() {
        // Default keybinds
        moduleKeybinds.put("module1", GLFW.GLFW_KEY_1);
        moduleKeybinds.put("module2", GLFW.GLFW_KEY_2);
        moduleKeybinds.put("module3", GLFW.GLFW_KEY_3);
    }
    
    /**
     * Start recording keybind for a module
     */
    public void startRecording(String moduleName) {
        this.recordingModule = moduleName;
    }
    
    /**
     * Stop recording and set keybind
     */
    public void stopRecording() {
        this.recordingModule = null;
    }
    
    /**
     * Check if currently recording
     */
    public boolean isRecording() {
        return recordingModule != null;
    }
    
    /**
     * Get the module being recorded
     */
    public String getRecordingModule() {
        return recordingModule;
    }
    
    /**
     * Set keybind for a module
     */
    public void setKeybind(String moduleName, int keyCode) {
        moduleKeybinds.put(moduleName, keyCode);
        stopRecording();
    }
    
    /**
     * Get keybind for a module
     */
    public int getKeybind(String moduleName) {
        return moduleKeybinds.getOrDefault(moduleName, -1);
    }
    
    /**
     * Get key name for display
     */
    public String getKeyName(String moduleName) {
        int keyCode = getKeybind(moduleName);
        if (keyCode == -1) return "Not Set";
        return InputUtil.fromKeyCode(keyCode, 0).getLocalizedText().getString();
    }
    
    /**
     * Check if module keybind was pressed
     */
    public boolean isModuleKeypressed(String moduleName) {
        // This will be checked in the tick event
        return false;
    }
}
