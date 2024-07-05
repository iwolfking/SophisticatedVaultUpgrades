package xyz.iwolfking.sophisticatedvaultupgrades.upgrades.drop;

import iskallia.vault.container.inventory.ShardPouchContainer;
import iskallia.vault.init.ModConfigs;
import iskallia.vault.init.ModItems;
import iskallia.vault.item.ItemShardPouch;
import net.minecraft.core.BlockPos;
import net.minecraft.world.Containers;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.p3pp3rf1y.sophisticatedcore.api.IStorageWrapper;
import net.p3pp3rf1y.sophisticatedcore.inventory.InventoryHandler;
import net.p3pp3rf1y.sophisticatedcore.util.InventoryHelper;
import top.theillusivec4.curios.api.CuriosApi;

public class DropUpgradeHelper {
    public static void dropStackAtPosition(InventoryHandler storageInventory, int slot, Level world, BlockPos pos, boolean simulate) {
        ItemStack stack = storageInventory.getStackInSlot(slot);
        ItemStack extractedStack = storageInventory.extractItem(slot, stack.getCount(), simulate);

        while(!extractedStack.isEmpty()) {
            Containers.dropItemStack(world, pos.getX(), pos.getY(), pos.getZ(), extractedStack.split(Math.min(extractedStack.getCount(), extractedStack.getMaxStackSize())));
            storageInventory.setStackInSlot(slot, ItemStack.EMPTY);
        }
    }
}
