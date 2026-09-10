# Damien Dupe Mod

A Minecraft Fabric mod for version 1.21.1 that adds item duplication functionality with GUI controls.

## Features

### Module 1: Item Duplication to Hotbar
- Select an item from your inventory
- Automatically places it in your hotbar (if not already there)
- Instantly places the item in the closest item frame
- Repeats as fast as possible

### Module 2: Chest Duplication
- Duplicate selected items into a target chest
- Keeps one original item in inventory
- Duplicates the remaining count into the chest
- Automatically finds the nearest chest

### Module 3: Counter Display
- Displays the total number of items duped/created
- Shows formatted count (K for thousands, M for millions)
- Counter can be reset

## Installation

1. Download the mod jar file
2. Place it in your `.minecraft/mods` folder
3. Ensure you have Fabric Loader installed for Minecraft 1.21.1
4. Launch Minecraft with Fabric profile

## Controls

- **Press `-` (Minus Key)** to open the Damien Dupe GUI
- Use the GUI buttons to control each module

## Requirements

- Minecraft 1.21.1
- Fabric Loader 0.16.9+
- Fabric API 0.102.0+

## Building

```bash
./gradlew build
```

The compiled mod will be in `build/libs/`

## Usage

1. Open GUI with `-` key
2. Use Module 1 to select and duplicate items to hotbar
3. Use Module 2 to duplicate items into a chest
4. Monitor Module 3 counter for total items duplicated

## Author

anrchaw9ds
