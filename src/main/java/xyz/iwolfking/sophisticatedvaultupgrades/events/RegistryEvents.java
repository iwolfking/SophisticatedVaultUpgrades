package xyz.iwolfking.sophisticatedvaultupgrades.events;

import iskallia.vault.init.ModAbilities;
import iskallia.vault.init.ModDynamicModels;
import iskallia.vault.init.ModItems;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import xyz.iwolfking.sophisticatedvaultupgrades.init.ModBlocks;
import xyz.iwolfking.sophisticatedvaultupgrades.init.ModRecipes;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class RegistryEvents {
    @SubscribeEvent
    public static void onRecipeRegister(RegistryEvent.Register<RecipeSerializer<?>> event) {
        ModRecipes.Serializer.register(event);
    }

    @SubscribeEvent
    public static void onItemRegister(RegistryEvent.Register<Item> event) {
        ModBlocks.registerBlockItems(event);
    }

    @SubscribeEvent
    public static void onTileEntityRegister(RegistryEvent.Register<BlockEntityType<?>> event) {
        ModBlocks.registerTileEntities(event);
    }

    @SubscribeEvent
    public static void onBlockRegister(RegistryEvent.Register<Block> event) {
        ModBlocks.registerBlocks(event);
    }
}
