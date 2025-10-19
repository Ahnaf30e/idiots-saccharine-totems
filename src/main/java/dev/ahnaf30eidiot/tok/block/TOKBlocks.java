package dev.ahnaf30eidiot.tok.block;

import dev.ahnaf30eidiot.tok.IdiotsSaccharineTotems;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;

public class TOKBlocks {

    public static final Block FERROUS_METAL_BLOCK = registerBlock("ferrous_metal_block", new Block(AbstractBlock.Settings.create().requiresTool().strength(52.0F, 1800.0F).sounds(BlockSoundGroup.NETHERITE).velocityMultiplier(1.0F).slipperiness(1.02041F))); // Perfect slipperiness for items

    private static Block registerBlock(String name, Block block) {
        registerBlockItem(name, block);
        return Registry.register(Registries.BLOCK, Identifier.of(IdiotsSaccharineTotems.MOD_ID, name), block);
    }

    private static void registerBlockItem(String name, Block block) {
        Registry.register(Registries.ITEM, Identifier.of(IdiotsSaccharineTotems.MOD_ID, name), new BlockItem(block, new Item.Settings()));
    }

    public static void regsiterBlocks() {
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.BUILDING_BLOCKS).register(entries -> {
            entries.add(FERROUS_METAL_BLOCK);
        });
    }

}
