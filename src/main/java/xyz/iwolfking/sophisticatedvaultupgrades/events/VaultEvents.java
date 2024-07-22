package xyz.iwolfking.sophisticatedvaultupgrades.events;

import iskallia.vault.core.event.common.VaultEndEvent;
import iskallia.vault.core.event.common.VaultPortalCollideEvent;
import iskallia.vault.core.event.common.VaultStartEvent;
import iskallia.vault.integration.IntegrationSB;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.p3pp3rf1y.sophisticatedbackpacks.SophisticatedBackpacks;
import net.p3pp3rf1y.sophisticatedbackpacks.api.CapabilityBackpackWrapper;
import net.p3pp3rf1y.sophisticatedbackpacks.backpack.BackpackItem;
import net.p3pp3rf1y.sophisticatedbackpacks.client.init.ModItemColors;
import net.p3pp3rf1y.sophisticatedbackpacks.init.ModItems;
import net.p3pp3rf1y.sophisticatedbackpacks.util.PlayerInventoryHandler;
import net.p3pp3rf1y.sophisticatedbackpacks.util.PlayerInventoryProvider;
import xyz.iwolfking.sophisticatedvaultupgrades.util.SVUInventoryHelper;

public class VaultEvents {
    public static void onDimensionChange(PlayerEvent.PlayerChangedDimensionEvent event) {
        Player player = event.getPlayer();
        PlayerInventoryProvider.get().runOnBackpacks(player, (backpack, inventoryHandlerName, identifier, slot) -> backpack.getCapability(CapabilityBackpackWrapper.getCapabilityInstance())
                .map(wrapper -> {
                    SVUInventoryHelper.runDimensionChangeOnDimensionChangeUpgrades(event.getFrom(), event.getTo(), player, wrapper.getUpgradeHandler());
                    return true;
                }).orElse(false), false
        );
    }
}
