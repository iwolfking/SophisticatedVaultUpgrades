package xyz.iwolfking.sophisticatedvaultupgrades.events;

import iskallia.vault.recipe.InitDollRecipe;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.p3pp3rf1y.sophisticatedbackpacks.api.CapabilityBackpackWrapper;
import net.p3pp3rf1y.sophisticatedbackpacks.util.PlayerInventoryProvider;
import net.p3pp3rf1y.sophisticatedcore.util.InventoryHelper;
import xyz.iwolfking.sophisticatedvaultupgrades.util.SVUInventoryHelper;

import java.util.concurrent.atomic.AtomicReference;

public class InteractEvents {

    public static void activateInteractUpgrades(PlayerInteractEvent.RightClickBlock event) {
        Player player = event.getPlayer();
        Level level = event.getWorld();
        BlockPos blockPos = event.getHitVec().getBlockPos();
        PlayerInventoryProvider.get().runOnBackpacks(player, (backpack, inventoryHandlerName, identifier, slot) -> backpack.getCapability(CapabilityBackpackWrapper.getCapabilityInstance())
                .map(wrapper -> {
                    SVUInventoryHelper.runInteractOnInteractUpgrades(level, player, wrapper.getUpgradeHandler(), blockPos);
                    return true;
                }).orElse(false), false
        );
    }

}
