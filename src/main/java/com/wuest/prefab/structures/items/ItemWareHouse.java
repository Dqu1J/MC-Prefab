package com.wuest.prefab.structures.items;

import com.wuest.prefab.Prefab;
import com.wuest.prefab.structures.gui.GuiWareHouse;
import com.wuest.prefab.structures.predefined.StructureWarehouse;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * This class is used to build the warehouse structure.
 *
 * @author WuestMan
 */
public class ItemWareHouse extends StructureItem {
    public ItemWareHouse(String name) {
        super(name);
    }

    /**
     * Initializes common fields/properties for this structure item.
     */
    @Override
    protected void PostInit() {
        if (Prefab.proxy.isClient) {
            this.RegisterGui(GuiWareHouse.class);
        }
    }

    @Override
    public void scanningMode(EntityPlayer player, World world, BlockPos hitBlockPos, EnumHand hand) {
        StructureWarehouse.ScanStructure(
                world,
                hitBlockPos,
                player.getHorizontalFacing(),
                false);
    }
}