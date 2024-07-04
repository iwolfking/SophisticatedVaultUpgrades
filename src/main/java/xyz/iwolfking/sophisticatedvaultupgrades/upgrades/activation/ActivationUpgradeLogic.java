package xyz.iwolfking.sophisticatedvaultupgrades.upgrades.activation;

import iskallia.vault.block.TreasureDoorBlock;
import iskallia.vault.core.event.CommonEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.p3pp3rf1y.sophisticatedcore.api.IStorageWrapper;
import net.p3pp3rf1y.sophisticatedcore.inventory.ItemStackKey;
import net.p3pp3rf1y.sophisticatedcore.util.InventoryHelper;

import java.util.HashSet;
import java.util.Set;

public class ActivationUpgradeLogic {
    public static boolean tryOpenTreasureDoor(Player player, Level world, BlockState blockState, BlockPos blockPos, TreasureDoorBlock door, IStorageWrapper storageWrapper) {
        Set<ItemStackKey> itemsToRemove = new HashSet<>();
        boolean isOpen = door.isOpen(blockState);
        if (!isOpen) {
            storageWrapper.getInventoryForUpgradeProcessing().getTrackedStacks().forEach(itemStackKey -> {
                if (itemStackKey.getStack().getItem() == blockState.getValue(TreasureDoorBlock.TYPE).getKey()) {
                    itemsToRemove.add(itemStackKey);
                    door.setOpen(player, world, blockState, blockPos, true);
                    CommonEvents.TREASURE_ROOM_OPEN.invoke(world, player, blockPos);
                }
            });
            for (ItemStackKey key : itemsToRemove) {
                InventoryHelper.extractFromInventory(key.stack(), storageWrapper.getInventoryForUpgradeProcessing(), false);
            }
            return true;
        }
        else {
            return false;
        }
    }

}

