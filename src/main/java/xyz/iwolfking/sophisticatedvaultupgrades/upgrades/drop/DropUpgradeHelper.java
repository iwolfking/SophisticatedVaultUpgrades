package xyz.iwolfking.sophisticatedvaultupgrades.upgrades.drop;

import net.minecraft.core.BlockPos;
import net.minecraft.world.Containers;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.p3pp3rf1y.sophisticatedcore.inventory.InventoryHandler;

public class DropUpgradeHelper {
    private static final int MAX_DROPPED_STACKS_PER_TICK = 16;

    public static void dropStackAtPosition(InventoryHandler storageInventory, int slot, Level world, BlockPos pos, boolean simulate) {
        ItemStack stack = storageInventory.getStackInSlot(slot);
        int cnt = Math.min(stack.getCount(), stack.getMaxStackSize());
        ItemStack extractedStack = storageInventory.extractItem(slot, cnt, simulate);

        int dropped = 0;
        while(!extractedStack.isEmpty()) {
            Containers.dropItemStack(world, pos.getX(), pos.getY(), pos.getZ(), extractedStack);
            dropped++;
            if (dropped >= MAX_DROPPED_STACKS_PER_TICK) break;
            stack = storageInventory.getStackInSlot(slot);
            cnt = Math.min(stack.getCount(), stack.getMaxStackSize());
            extractedStack = storageInventory.extractItem(slot, cnt, simulate);
        }
    }
}
