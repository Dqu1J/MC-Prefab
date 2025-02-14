package com.wuest.prefab.structures.config.enums;

import net.minecraft.core.Direction;

public class MachineryTowerOptions extends BaseOption {
    public static MachineryTowerOptions Default = new MachineryTowerOptions(
            "item.prefab.machinery.tower",
            "assets/prefab/structures/machinery_tower.zip",
            "textures/gui/machinery_tower_topdown.png",
            false,
            true);

    protected MachineryTowerOptions(String translationString,
                                    String assetLocation,
                                    String pictureLocation,
                                    boolean hasBedColor,
                                    boolean hasGlassColor) {
        super(
                translationString,
                assetLocation,
                pictureLocation,
                hasBedColor,
                hasGlassColor);
    }
}
