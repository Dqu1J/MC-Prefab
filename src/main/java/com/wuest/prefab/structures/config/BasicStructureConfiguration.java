package com.wuest.prefab.structures.config;

import com.wuest.prefab.blocks.FullDyeColor;
import com.wuest.prefab.structures.config.enums.*;
import com.wuest.prefab.structures.items.ItemBasicStructure;
import com.wuest.prefab.structures.predefined.StructureBasic;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;

/**
 * This class is used for the basic structures in the mod.
 *
 * @author WuestMan
 */
@SuppressWarnings({"WeakerAccess", "unused"})
public class BasicStructureConfiguration extends StructureConfiguration {
    private static final String structureEnumNameTag = "structureEnumName";
    private static final String structureDisplayNameTag = "structureDisplayName";
    private static final String bedColorTag = "bedColor";
    private static final String glassColorTag = "glassColor";
    private static final String chosenOptionTag = "chosenOption";

    /**
     * This field is used to contain the {@link EnumBasicStructureName} used by this instance.
     */
    public EnumBasicStructureName basicStructureName;

    /**
     * This field is used to contain the display name for the structure.
     */
    public String structureDisplayName;

    public EnumDyeColor bedColor;

    public FullDyeColor glassColor;

    public BaseOption chosenOption;

    /**
     * Initializes a new instance of the BasicStructureConfiguration class.
     */
    public BasicStructureConfiguration() {
        super();
    }

    /**
     * Gets the display name for this structure.
     *
     * @return The unlocalized display name for this structure
     */
    public String getDisplayName() {
        if (this.basicStructureName == EnumBasicStructureName.Custom) {
            return this.structureDisplayName;
        } else {
            return this.basicStructureName.getItemTranslationString();
        }
    }

    /**
     * Determines if this is a custom structure.
     *
     * @return A value indicating whether this is a custom structure.
     */
    public boolean IsCustomStructure() {
        return this.basicStructureName == EnumBasicStructureName.Custom;
    }

    @Override
    public void Initialize() {
        super.Initialize();
        this.houseFacing = EnumFacing.NORTH;
        this.basicStructureName = EnumBasicStructureName.AdvancedCoop;
        this.bedColor = EnumDyeColor.RED;
        this.glassColor = FullDyeColor.CLEAR;
        this.chosenOption = this.basicStructureName.baseOption.getSpecificOptions().get(0);
    }

    @Override
    protected void CustomReadFromNBTTag(NBTTagCompound messageTag, StructureConfiguration config) {
        BasicStructureConfiguration basicConfig = (BasicStructureConfiguration) config;

        if (messageTag.hasKey(BasicStructureConfiguration.structureEnumNameTag)) {
            basicConfig.basicStructureName = EnumBasicStructureName.valueOf(messageTag.getString(BasicStructureConfiguration.structureEnumNameTag));
        }

        if (messageTag.hasKey(BasicStructureConfiguration.structureDisplayNameTag)) {
            basicConfig.structureDisplayName = messageTag.getString(BasicStructureConfiguration.structureDisplayNameTag);
        }

        if (messageTag.hasKey(BasicStructureConfiguration.bedColorTag)) {
            basicConfig.bedColor = EnumDyeColor.valueOf(messageTag.getString(BasicStructureConfiguration.bedColorTag));
        }

        if (messageTag.hasKey(BasicStructureConfiguration.glassColorTag)) {
            basicConfig.glassColor = FullDyeColor.valueOf(messageTag.getString(BasicStructureConfiguration.glassColorTag));
        }

        if (messageTag.hasKey(BasicStructureConfiguration.chosenOptionTag)) {
            basicConfig.chosenOption = BaseOption.getOptionByTranslationString(messageTag.getString(BasicStructureConfiguration.chosenOptionTag));
        }
    }

    @Override
    protected NBTTagCompound CustomWriteToCompoundNBT(NBTTagCompound tag) {
        tag.setString(BasicStructureConfiguration.structureEnumNameTag, this.basicStructureName.name());

        if (this.structureDisplayName != null) {
            tag.setString(BasicStructureConfiguration.structureDisplayNameTag, this.structureDisplayName);
        }

        tag.setString(BasicStructureConfiguration.bedColorTag, this.bedColor.getName().toUpperCase());
        tag.setString(BasicStructureConfiguration.glassColorTag, this.glassColor.getName().toUpperCase());
        tag.setString(BasicStructureConfiguration.chosenOptionTag, this.chosenOption.getTranslationString());

        return tag;
    }

    /**
     * Reads information from an NBTTagCompound.
     *
     * @param messageTag The tag to read the data from.
     * @return An instance of {@link BasicStructureConfiguration} with vaules pulled from the NBTTagCompound.
     */
    @Override
    public BasicStructureConfiguration ReadFromCompoundNBT(NBTTagCompound messageTag) {
        BasicStructureConfiguration config = new BasicStructureConfiguration();

        return (BasicStructureConfiguration) super.ReadFromCompoundNBT(messageTag, config);
    }

    /**
     * This is used to actually build the structure as it creates the structure instance and calls build structure.
     *
     * @param player      The player which requested the build.
     * @param world       The world instance where the build will occur.
     * @param hitBlockPos This hit block position.
     */
    @Override
    protected void ConfigurationSpecificBuildStructure(EntityPlayer player, WorldServer world, BlockPos hitBlockPos) {
        String assetLocation = "";

        if (!this.IsCustomStructure()) {
            assetLocation = this.chosenOption.getAssetLocation();
        }

        StructureBasic structure = StructureBasic.CreateInstance(assetLocation, StructureBasic.class);

        if (structure.BuildStructure(this, world, hitBlockPos, EnumFacing.NORTH, player)) {
            ItemStack stack = ItemBasicStructure.getBasicStructureItemInHand(player);

            if (stack.getCount() == 1) {
                player.inventory.deleteStack(stack);
            } else {
                stack.setCount(stack.getCount() - 1);
            }

            player.inventoryContainer.detectAndSendChanges();
        }
    }

    /**
     * This enum is used to list the names of the basic structures and provide other information necessary.
     *
     * @author WuestMan
     */
    @SuppressWarnings("SpellCheckingInspection")
    public enum EnumBasicStructureName {
        Custom("custom", null, null, null),
        AdvancedCoop("advancedcoop", "item.prefab:item_advanced_chicken_coop.name", "item_advanced_chicken_coop", AdvancedCoopOptions.Default),
        AdvancedHorseStable("advanced_horse_stable", "item.prefab:item_advanced_horse_stable.name", "item_advanced_horse_stable", AdvancedHorseStableOptions.Default),
        Barn("barn", "item.prefab:item_barn.name", "item_barn", BarnOptions.Default),
        MachineryTower("machinery_tower", "item.prefab:item_machinery_tower.name", "item_machinery_tower", MachineryTowerOptions.Default),
        DefenseBunker("defense_bunker", "item.prefab:item_defense_bunker.name", "item_defense_bunker", DefenseBunkerOptions.Default),
        MineshaftEntrance("mineshaft_entrance", "item.prefab:item_mineshaft_entrance.name", "item_mineshaft_entrance", MineshaftEntranceOptions.Default),
        EnderGateway("ender_gateway", "item.prefab:item_ender_gateway.name", "item_ender_gateway", EnderGatewayOptions.Default),
        AquaBase("aqua_base", "item.prefab:item_aqua_base.name", "item_aqua_base", AquaBaseOptions.Default),
        GrassyPlain("grassy_plain", "item.prefab:item_grassy_plain.name", "item_grassy_plain", GrassyPlainOptions.Default),
        MagicTemple("magic_temple", "item.prefab:item_magic_temple.name", "item_magic_temple", MagicTempleOptions.Default),
        GreenHouse("green_house", "item.prefab:item_green_house.name", "item_green_house", GreenHouseOptions.Default),
        WatchTower("watch_tower", "item.prefab:item_watch_tower.name", "item_watch_tower", WatchTowerOptions.Default),
        WelcomeCenter("welcome_center", "item.prefab:item_welcome_center.name", "item_welcome_center", WelcomeCenterOptions.Default),
        Jail("jail", "item.prefab:item_jail.name", "item_jail", JailOptions.Default),
        Saloon("saloon", "item.prefab:item_saloon.name", "item_saloon", SaloonOptions.Default),
        NetherGate("nether_gate", "item.prefab:item_nether_gate.name", "item_nether_gate", NetherGateOptions.AncientSkull),
        FishPond("fishpond", "item.prefab:item_fish_pond.name", "item_fish_pond", FishPondOptions.Default),
        HorseStable("horse_stable", "item.prefab:item_horse_stable.name", "item_horse_stable", HorseStableOptions.Default),
        TreeFarm("tree_farm", "item.prefab:item_tree_farm.name", "item_tree_farm", TreeFarmOptions.Default),
        VillagerHouses("villager_houses", "item.prefab:item_villager_houses.name", "item_villager_houses", VillagerHouseOptions.FLAT_ROOF),
        ChickenCoop("chicken_coop", "item.prefab:item_chicken_coop.name", "item_chicken_coop", ChickenCoopOptions.Default),
        ProduceFarm("produce_farm", "item.prefab:item_produce_farm.name", "item_produce_farm", ProduceFarmOptions.Default),
        MonsterMasher("monster_masher", "item.prefab:item_monster_masher.name", "item_monster_masher", MonsterMasherOptions.Default),
        AdvancedWarehouse("advanced_warehouse", "item.prefab:item_advanced_warehouse.name", "item_advanced_warehouse", AdvancedWarehouseOptions.Default),
        Warehouse("warehouse", "item.prefab:item_warehouse.name", "item_warehouse", WarehouseOptions.Default);

        private final String name;
        private final String itemTranslationString;
        private final BaseOption baseOption;
        private ResourceLocation itemTextureLocation;

        /**
         * This is a basic structure which doesn't have any (or limited) custom processing.
         *
         * @param name                  - This is the name for this structure. This is used for comparative purposes in
         *                              item stacks.
         * @param itemTranslationString - This is the localization key to determine the displayed name to the user.
         * @param itemTextureLocation   - This is the resource location for the item's texture when it's in the players
         *                              and or in inventories/the world.
         */
        EnumBasicStructureName(
                String name,
                String itemTranslationString,
                String itemTextureLocation,
                BaseOption baseOption) {
            this.name = name;
            this.itemTranslationString = itemTranslationString;

            if (itemTextureLocation != null) {
                this.itemTextureLocation = new ResourceLocation("prefab", itemTextureLocation);
            }

            this.baseOption = baseOption;
        }

        /**
         * The enum name.
         *
         * @return The enum name.
         */
        public String getName() {
            return this.name;
        }

        /**
         * The unlocalized name.
         *
         * @return The unlocalized name for this structure.
         */
        public String getItemTranslationString() {
            return this.itemTranslationString;
        }

        /**
         * This is the resource location for the item's texture when it's in the players and or in inventories/the
         * world.
         *
         * @return The resource location for the item texture.
         */
        public ResourceLocation getItemTextureLocation() {
            return this.itemTextureLocation;
        }

        public BaseOption getBaseOption() {
            return this.baseOption;
        }
    }
}
