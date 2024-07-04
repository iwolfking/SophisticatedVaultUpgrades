package xyz.iwolfking.sophisticatedvaultupgrades.upgrades.diffuser;

import iskallia.vault.init.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.items.IItemHandler;
import net.p3pp3rf1y.sophisticatedbackpacks.upgrades.deposit.DepositUpgradeItem;
import net.p3pp3rf1y.sophisticatedbackpacks.upgrades.deposit.DepositUpgradeWrapper;
import net.p3pp3rf1y.sophisticatedcore.api.ISlotChangeResponseUpgrade;
import net.p3pp3rf1y.sophisticatedcore.api.IStorageWrapper;
import net.p3pp3rf1y.sophisticatedcore.inventory.IItemHandlerSimpleInserter;
import net.p3pp3rf1y.sophisticatedcore.inventory.InventoryHandler;
import net.p3pp3rf1y.sophisticatedcore.upgrades.*;
import net.p3pp3rf1y.sophisticatedcore.util.NBTHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.iwolfking.sophisticatedvaultupgrades.upgrades.IInteractResponseUpgrade;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

public class DiffuserUpgradeWrapper extends UpgradeWrapperBase<DiffuserUpgradeWrapper, DiffuserUpgradeItem>
        implements IInsertResponseUpgrade, IFilteredUpgrade, ISlotChangeResponseUpgrade, ITickableUpgrade, IOverflowResponseUpgrade, ISlotLimitUpgrade {
    private final FilterLogic filterLogic;
    private final Set<Integer> slotsToVoid = new HashSet<>();
    private boolean shouldVoidOverflow;

    public DiffuserUpgradeWrapper(IStorageWrapper storageWrapper, ItemStack upgrade, Consumer<ItemStack> upgradeSaveHandler) {
        super(storageWrapper, upgrade, upgradeSaveHandler);
        filterLogic = new FilterLogic(upgrade, upgradeSaveHandler, upgradeItem.getFilterSlotCount());
        filterLogic.setAllowByDefault(true);
        setShouldVoidOverflowDefaultOrLoadFromNbt(false);
    }

    @Override
    public @NotNull ItemStack onBeforeInsert(@NotNull IItemHandlerSimpleInserter inventoryHandler, int slot, @NotNull ItemStack stack, boolean simulate) {
        if (shouldVoidOverflow && inventoryHandler.getStackInSlot(slot).isEmpty() && (!filterLogic.shouldMatchNbt() || !filterLogic.shouldMatchDurability() || filterLogic.getPrimaryMatch() != PrimaryMatch.ITEM) && filterLogic.matchesFilter(stack)) {
            for (int s = 0; s < inventoryHandler.getSlots(); s++) {
                if (s == slot) {
                    continue;
                }
                if (stackMatchesFilterStack(inventoryHandler.getStackInSlot(s), stack)) {
                    if(DiffuserUpgradeHelper.getDiffuserValue(stack) == 0) {
                        continue;
                    }
                    int count = stack.getCount();
                    return new ItemStack(ModItems.SOUL_DUST, DiffuserUpgradeHelper.getDiffuserValue(stack) * count);
                }
            }
            return stack;
        }

        return !shouldVoidOverflow && filterLogic.matchesFilter(stack) && DiffuserUpgradeHelper.getDiffuserValue(stack) != 0 ? new ItemStack(ModItems.SOUL_DUST, DiffuserUpgradeHelper.getDiffuserValue(stack)) : stack;
    }

    @Override
    public void onAfterInsert(@NotNull IItemHandlerSimpleInserter inventoryHandler, int slot) {
        //noop
    }

    @Override
    public @NotNull FilterLogic getFilterLogic() {
        return filterLogic;
    }

    public void setShouldWorkdInGUI(boolean shouldWorkdInGUI) {
        NBTHelper.setBoolean(upgrade, "shouldWorkInGUI", shouldWorkdInGUI);
        save();
    }

    public boolean shouldWorkInGUI() {
        return NBTHelper.getBoolean(upgrade, "shouldWorkInGUI").orElse(false);
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
    public void onSlotChange(IItemHandler inventoryHandler, int slot) {
        if (!shouldWorkInGUI() || shouldVoidOverflow()) {
            return;
        }

        ItemStack slotStack = inventoryHandler.getStackInSlot(slot);
        if (filterLogic.matchesFilter(slotStack)) {
            slotsToVoid.add(slot);
        }
    }

    @Override
    public void tick(@Nullable LivingEntity entity, Level world, BlockPos pos) {
        if (slotsToVoid.isEmpty()) {
            return;
        }

        InventoryHandler storageInventory = storageWrapper.getInventoryHandler();
        for (int slot : slotsToVoid) {
            ItemStack stack = storageInventory.getStackInSlot(slot);
            int soulValue = DiffuserUpgradeHelper.getDiffuserValue(stack);
            int itemCount = stack.getCount();
            if(soulValue != 0) {
                storageInventory.extractItem(slot, itemCount, false);
                storageInventory.insertItem(new ItemStack(ModItems.SOUL_DUST, itemCount), false);
            }
        }

        slotsToVoid.clear();
    }

    @Override
    public boolean worksInGui() {
        return shouldWorkInGUI();
    }

    @Override
    public @NotNull ItemStack onOverflow(@NotNull ItemStack stack) {
        if(filterLogic.matchesFilter(stack)) {
            int count = stack.getCount();
            int soulValue = DiffuserUpgradeHelper.getDiffuserValue(stack);
            return new ItemStack(ModItems.SOUL_DUST, count);
        }
        return stack;
    }

    @Override
    public boolean stackMatchesFilter(@NotNull ItemStack stack) {
        return filterLogic.matchesFilter(stack);
    }

    public boolean isVoidAnythingEnabled() {
        return upgradeItem.isVoidAnythingEnabled();
    }

    @Override
    public int getSlotLimit() {
        return Integer.MAX_VALUE;
    }



}
