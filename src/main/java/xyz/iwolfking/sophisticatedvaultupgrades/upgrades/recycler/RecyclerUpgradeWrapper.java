package xyz.iwolfking.sophisticatedvaultupgrades.upgrades.recycler;

import iskallia.vault.block.entity.SpiritExtractorTileEntity;
import iskallia.vault.config.VaultRecyclerConfig;
import iskallia.vault.gear.VaultGearState;
import iskallia.vault.gear.item.IdentifiableItem;
import iskallia.vault.init.ModItems;
import iskallia.vault.item.gear.RecyclableItem;
import net.minecraft.core.BlockPos;
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
import xyz.iwolfking.sophisticatedvaultupgrades.upgrades.diffuser.DiffuserUpgradeHelper;
import xyz.iwolfking.vhapi.api.data.api.CustomRecyclerOutputs;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class RecyclerUpgradeWrapper extends UpgradeWrapperBase<RecyclerUpgradeWrapper, RecyclerUpgradeItem>
        implements IInsertResponseUpgrade, IFilteredUpgrade, ISlotChangeResponseUpgrade, ITickableUpgrade, IOverflowResponseUpgrade {
    private final FilterLogic filterLogic;
    private final Set<Integer> slotsToVoid = new HashSet<>();

    public RecyclerUpgradeWrapper(IStorageWrapper storageWrapper, ItemStack upgrade, Consumer<ItemStack> upgradeSaveHandler) {
        super(storageWrapper, upgrade, upgradeSaveHandler);
        filterLogic = new FilterLogic(upgrade, upgradeSaveHandler, upgradeItem.getFilterSlotCount());
    }

    @Override
    public @NotNull ItemStack onBeforeInsert(@NotNull IItemHandlerSimpleInserter inventoryHandler, int slot, @NotNull ItemStack stack, boolean simulate) {
        if(!(stack.getItem() instanceof RecyclableItem || !CustomRecyclerOutputs.CUSTOM_OUTPUTS.containsKey(stack.getItem().getRegistryName())) || !hasSlotSpace()) {
            return stack;
        }
        if(!shouldScrapUnidentified() && stack.getItem() instanceof IdentifiableItem identifiableItem) {
            if(identifiableItem.getState(stack).equals(VaultGearState.UNIDENTIFIED)) {
                return stack;
            }
        }

        if(stack.getItem() instanceof RecyclableItem recyclableItem && !recyclableItem.isValidInput(stack)) {
            return stack;
        }

        List<ItemStack> outputs = RecyclerUpgradeHelper.getVaultRecyclerOutputs(stack);

        if(filterLogic.matchesFilter(stack) && !outputs.isEmpty()) {
            for(ItemStack recycleStack : outputs) {
                inventoryHandler.insertItem(recycleStack, simulate);
            }
            return ItemStack.EMPTY;
        }
        else {
            return stack;
        }
    }

    @Override
    public void onAfterInsert(@NotNull IItemHandlerSimpleInserter inventoryHandler, int slot) {
        //no-op
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


    @Override
    public void onSlotChange(@NotNull IItemHandler inventoryHandler, int slot) {
        if (!shouldWorkInGUI()) {
            return;
        }

        ItemStack slotStack = inventoryHandler.getStackInSlot(slot);

        if (filterLogic.matchesFilter(slotStack) && slotStack.getItem() instanceof RecyclableItem || CustomRecyclerOutputs.CUSTOM_OUTPUTS.containsKey(slotStack.getItem().getRegistryName())) {
            if(slotStack.getItem() instanceof RecyclableItem recyclableItem && !recyclableItem.isValidInput(slotStack)) {
                return;
            }

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
            ItemStack stack = storageInventory.getStackInSlot(slot);
            if(!stackMatchesFilter(stack)) {
                continue;
            }
            if(!shouldScrapUnidentified() && stack.getItem() instanceof IdentifiableItem identifiableItem) {
                if(identifiableItem.getState(stack).equals(VaultGearState.UNIDENTIFIED)) {
                    continue;
                }
            }
            List<ItemStack> outputs = RecyclerUpgradeHelper.getVaultRecyclerOutputs(stack);
            for(ItemStack recycleStack : outputs) {
                storageInventory.insertItem(recycleStack, false);
            }
            storageInventory.extractItem(slot, stack.getCount(), false);
        }

        slotsToVoid.clear();
    }


    @Override
    public boolean worksInGui() {
        return shouldWorkInGUI();
    }

    @Override
    public @NotNull ItemStack onOverflow(@NotNull ItemStack itemStack) {
        return itemStack;
    }


    @Override
    public boolean stackMatchesFilter(@NotNull ItemStack stack) {
        if(stack.getItem() instanceof RecyclableItem recyclableItem && !recyclableItem.isValidInput(stack)) {
            return false;
        }

        return filterLogic.matchesFilter(stack);
    }


    private boolean hasSlotSpace() {
        InventoryHandler handler = storageWrapper.getInventoryHandler();
        Predicate<ItemStack> hasItemPredicate = Predicate.not(Predicate.isEqual(ItemStack.EMPTY));
        if(!handler.hasEmptySlots()) {
            return false;
        }
        else {
            return handler.getSlots() - InventoryHelper.getItemSlots(handler, hasItemPredicate).size() >= 3;
        }
    }

    public void setScrapUnidentified(boolean shouldScrapUnidentified) {
        NBTHelper.setBoolean(upgrade, "shouldScrapUnidentified", shouldScrapUnidentified);
        save();
    }

    public boolean shouldScrapUnidentified() {
        return NBTHelper.getBoolean(upgrade, "shouldScrapUnidentified").orElse(false);
    }


}
