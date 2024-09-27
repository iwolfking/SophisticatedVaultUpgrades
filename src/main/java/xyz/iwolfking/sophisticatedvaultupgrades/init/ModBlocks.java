package xyz.iwolfking.sophisticatedvaultupgrades.init;

import com.mojang.datafixers.types.Type;
import iskallia.vault.block.DemagnetizerBlock;
import iskallia.vault.block.entity.DemagnetizerTileEntity;
import iskallia.vault.init.ModItems;
import iskallia.vault.item.crystal.CrystalData;
import iskallia.vault.item.crystal.VaultCrystalItem;
import iskallia.vault.item.crystal.properties.CapacityCrystalProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.DoubleHighBlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.event.RegistryEvent;
import xyz.iwolfking.sophisticatedvaultupgrades.SophisticatedVaultUpgrades;

import java.util.function.Consumer;

public class ModBlocks {
    //public static final DebagnetizerBlock DEBAGNETIZER_BLOCK;

    //public static final BlockEntityType<DemagnetizerTileEntity> DEBAGNETIZER_TILE;

    public static void registerBlocks(RegistryEvent.Register<Block> event) {
        //registerBlock(event, DEBAGNETIZER_BLOCK, SophisticatedVaultUpgrades.loc("debagnetizer"));
    }

    public static void registerTileEntities(RegistryEvent.Register<BlockEntityType<?>> event) {
        //registerTileEntity(event, DEBAGNETIZER_TILE, SophisticatedVaultUpgrades.loc("debagnetizer_tile"));

    }

    public static void registerBlockItems(RegistryEvent.Register<Item> event) {
        //registerBlockItem(event, DEBAGNETIZER_BLOCK);
    }

    private static void registerBlock(RegistryEvent.Register<Block> event, Block block, ResourceLocation id) {
        block.setRegistryName(id);
        event.getRegistry().register(block);
    }

    private static <T extends BlockEntity> void registerTileEntity(RegistryEvent.Register<BlockEntityType<?>> event, BlockEntityType<?> type, ResourceLocation id) {
        type.setRegistryName(id);
        event.getRegistry().register(type);
    }

    private static void registerBlockItemWithEffect(RegistryEvent.Register<Item> event, Block block, int maxStackSize, Consumer<Item.Properties> adjustProperties) {
        Item.Properties properties = (new Item.Properties()).tab(ModItems.VAULT_MOD_GROUP).stacksTo(maxStackSize);
        adjustProperties.accept(properties);
        BlockItem blockItem = new BlockItem(block, properties) {
            public boolean isFoil(ItemStack stack) {
                return true;
            }
        };
        registerBlockItem(event, block, blockItem);
    }

    private static void registerBlockItem(RegistryEvent.Register<Item> event, Block block) {
        registerBlockItem(event, block, 64);
    }

    private static void registerBlockItem(RegistryEvent.Register<Item> event, Block block, int maxStackSize) {
        registerBlockItem(event, block, maxStackSize, (properties) -> {
        });
    }

    private static void registerBlockItem(RegistryEvent.Register<Item> event, Block block, int maxStackSize, Consumer<Item.Properties> adjustProperties) {
        Item.Properties properties = (new Item.Properties()).tab(ModItems.VAULT_MOD_GROUP).stacksTo(maxStackSize);
        adjustProperties.accept(properties);
        registerBlockItem(event, block, new BlockItem(block, properties));
    }

    private static void registerBlockItem(RegistryEvent.Register<Item> event, Block block, BlockItem blockItem) {
        blockItem.setRegistryName(block.getRegistryName());
        event.getRegistry().register(blockItem);
    }

    private static void registerTallBlockItem(RegistryEvent.Register<Item> event, Block block) {
        DoubleHighBlockItem tallBlockItem = new DoubleHighBlockItem(block, (new Item.Properties()).tab(ModItems.VAULT_MOD_GROUP).stacksTo(64));
        tallBlockItem.setRegistryName(block.getRegistryName());
        event.getRegistry().register(tallBlockItem);
    }

    static {
        //DEBAGNETIZER_BLOCK = new DebagnetizerBlock();
        //DEBAGNETIZER_TILE = BlockEntityType.Builder.of(DemagnetizerTileEntity::new, new Block[]{DEBAGNETIZER_BLOCK}).build((Type)null);
    }
}
