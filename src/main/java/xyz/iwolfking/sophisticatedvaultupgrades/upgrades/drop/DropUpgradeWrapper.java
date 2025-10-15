package xyz.iwolfking.sophisticatedvaultupgrades.upgrades.drop;

import iskallia.vault.init.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.world.Containers;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.items.IItemHandler;
import net.p3pp3rf1y.sophisticatedcore.api.ISlotChangeResponseUpgrade;
import net.p3pp3rf1y.sophisticatedcore.api.IStorageWrapper;
import net.p3pp3rf1y.sophisticatedcore.inventory.IItemHandlerSimpleInserter;
import net.p3pp3rf1y.sophisticatedcore.inventory.InventoryHandler;
import net.p3pp3rf1y.sophisticatedcore.upgrades.*;
import net.p3pp3rf1y.sophisticatedcore.util.InventoryHelper;
import net.p3pp3rf1y.sophisticatedcore.util.NBTHelper;
import net.p3pp3rf1y.sophisticatedcore.util.RecipeHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

public class DropUpgradeWrapper extends UpgradeWrapperBase<DropUpgradeWrapper, DropUpgradeItem>
        implements IInsertResponseUpgrade, IFilteredUpgrade, ISlotChangeResponseUpgrade, ITickableUpgrade, IOverflowResponseUpgrade {
    private final FilterLogic filterLogic;
    private final Set<Integer> slotsToVoid = new HashSet<>();
    private boolean shouldVoidOverflow;


    public DropUpgradeWrapper(IStorageWrapper storageWrapper, ItemStack upgrade, Consumer<ItemStack> upgradeSaveHandler) {
        super(storageWrapper, upgrade, upgradeSaveHandler);
        filterLogic = new FilterLogic(upgrade, upgradeSaveHandler, upgradeItem.getFilterSlotCount());
        //filterLogic.setAllowByDefault();
        setShouldVoidOverflowDefaultOrLoadFromNbt(false);
    }

    @Override
    public @NotNull ItemStack onBeforeInsert(@NotNull IItemHandlerSimpleInserter inventoryHandler, int slot, @NotNull ItemStack stack, boolean simulate) {
       return stack;
    }

    @Override
    public void onAfterInsert(@NotNull IItemHandlerSimpleInserter inventoryHandler, int slot) {
        ItemStack slotStack = inventoryHandler.getStackInSlot(slot);
        if (filterLogic.matchesFilter(slotStack)) {
            slotsToVoid.add(slot);
        }
    }

    @Override
    public @NotNull FilterLogic getFilterLogic() {
        return filterLogic;
    }

    public void setShouldWorkdInGUI(boolean shouldWorkdInGUI) {
        NBTHelper.setBoolean(upgrade, "shouldWorkdInGUI", shouldWorkdInGUI);
        save();
    }


    public boolean shouldWorkInGUI() {
        return NBTHelper.getBoolean(upgrade, "shouldWorkdInGUI").orElse(false);
    }


    public void setShouldVoidOverflow(boolean shouldVoidOverflow) {
        if (!shouldVoidOverflow && !upgradeItem.isVoidAnythingEnabled()) {
            return;
        }

        this.shouldVoidOverflow = shouldVoidOverflow;
        NBTHelper.setBoolean(upgrade, "shouldVoidOverflow", shouldVoidOverflow);
        save();
    }

    public void setShouldVoidOverflowDefaultOrLoadFromNbt(boolean shouldVoidOverflowDefault) {
        shouldVoidOverflow = !upgradeItem.isVoidAnythingEnabled() || NBTHelper.getBoolean(upgrade, "shouldVoidOverflow").orElse(shouldVoidOverflowDefault);
    }

    public boolean shouldVoidOverflow() {
        return !upgradeItem.isVoidAnythingEnabled() || shouldVoidOverflow;
    }

    @Override
    public void onSlotChange(@NotNull IItemHandler inventoryHandler, int slot) {
        if (!shouldWorkInGUI() || shouldVoidOverflow()) {
            return;
        }

        ItemStack slotStack = inventoryHandler.getStackInSlot(slot);
        if (filterLogic.matchesFilter(slotStack)) {
            slotsToVoid.add(slot);
        }
    }

    @Override
    public void tick(@Nullable LivingEntity entity, @NotNull Level world, @NotNull BlockPos pos) {
        if(slotsToVoid.isEmpty()) {
            return;
        }

        InventoryHandler storageInventory = storageWrapper.getInventoryHandler();
        for (int slot : slotsToVoid) {
            if(!stackMatchesFilter(storageInventory.getStackInSlot(slot))) {
                continue;
            }
            DropUpgradeHelper.dropStackAtPosition(storageInventory, slot, world, pos, false);

        }

        slotsToVoid.clear();
    }


    @Override
    public boolean worksInGui() {
        return shouldWorkInGUI();
    }



    @Override
    public @NotNull ItemStack onOverflow(@NotNull ItemStack stack) {
        return stack;
    }

    @Override
    public boolean stackMatchesFilter(@NotNull ItemStack stack) {
        return filterLogic.matchesFilter(stack);
    }

    public boolean isVoidAnythingEnabled() {
        return upgradeItem.isVoidAnythingEnabled();
    }


}
