package xyz.iwolfking.sophisticatedvaultupgrades.upgrades.diffuser;

import iskallia.vault.container.inventory.ShardPouchContainer;
import iskallia.vault.init.ModItems;
import iskallia.vault.item.ItemShardPouch;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
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
import org.jline.utils.DiffHelper;
import top.theillusivec4.curios.api.CuriosApi;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class DiffuserUpgradeWrapper extends UpgradeWrapperBase<DiffuserUpgradeWrapper, DiffuserUpgradeItem>
        implements IInsertResponseUpgrade, IFilteredUpgrade, ISlotChangeResponseUpgrade, ITickableUpgrade, IOverflowResponseUpgrade, ISlotLimitUpgrade {
    private final FilterLogic filterLogic;
    private final Set<Integer> slotsToVoid = new HashSet<>();
    private boolean shouldVoidOverflow;

    private static final int COOLDOWN = 100;

    public DiffuserUpgradeWrapper(IStorageWrapper storageWrapper, ItemStack upgrade, Consumer<ItemStack> upgradeSaveHandler) {
        super(storageWrapper, upgrade, upgradeSaveHandler);
        filterLogic = new FilterLogic(upgrade, upgradeSaveHandler, upgradeItem.getFilterSlotCount());
        filterLogic.setAllowByDefault(true);
        setShouldVoidOverflowDefaultOrLoadFromNbt(false);
    }

    @Override
    public @NotNull ItemStack onBeforeInsert(@NotNull IItemHandlerSimpleInserter inventoryHandler, int slot, @NotNull ItemStack stack, boolean simulate) {
        if(stack.getItem().equals(ModItems.SOUL_DUST) || stack.getItem().equals(ModItems.SOUL_SHARD)) {
            return stack;
        }


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
                    inventoryHandler.insertItem(new ItemStack(ModItems.SOUL_DUST, DiffuserUpgradeHelper.getDiffuserValue(stack) * count), simulate);
                    return ItemStack.EMPTY;
                }
            }
            return stack;
        }

        if(!shouldVoidOverflow && filterLogic.matchesFilter(stack) && DiffuserUpgradeHelper.getDiffuserValue(stack) != 0) {
            int count = stack.getCount();
            inventoryHandler.insertItem(new ItemStack(ModItems.SOUL_DUST, DiffuserUpgradeHelper.getDiffuserValue(stack) * count), simulate);
            return ItemStack.EMPTY;
        }
        else {
            return stack;
        }
    }

    @Override
    public void onAfterInsert(@NotNull IItemHandlerSimpleInserter inventoryHandler, int slot) {
        if(!shouldCompactShards()) {
            return;
        }
        if(inventoryHandler.getStackInSlot(slot).getItem().equals(ModItems.SOUL_DUST)) {
            compactDust(inventoryHandler, slot);
        }
    }

    @Override
    public @NotNull FilterLogic getFilterLogic() {
        return filterLogic;
    }

    public void setShouldWorkdInGUI(boolean shouldWorkdInGUI) {
        NBTHelper.setBoolean(upgrade, "shouldWorkInGUI", shouldWorkdInGUI);
        save();
    }

    public void setShouldHoldShards(boolean shouldHoldShards) {
        NBTHelper.setBoolean(upgrade, "shouldHoldShards", shouldHoldShards);
        save();
    }

    public void setShouldCompactShards(boolean shouldHoldShards) {
        NBTHelper.setBoolean(upgrade, "shouldCompactShards", shouldHoldShards);
        save();
    }


    public boolean shouldWorkInGUI() {
        return NBTHelper.getBoolean(upgrade, "shouldWorkInGUI").orElse(false);
    }

    public boolean isShouldCompactShards() {
        return NBTHelper.getBoolean(upgrade, "shouldCompactShards").orElse(false);
    }

    public boolean isShouldHoldShards() {
        return NBTHelper.getBoolean(upgrade, "shouldHoldShards").orElse(false);
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
        if (filterLogic.matchesFilter(slotStack) && DiffuserUpgradeHelper.getDiffuserValue(slotStack) != 0) {
            slotsToVoid.add(slot);
        }
    }

    @Override
    public void tick(@Nullable LivingEntity entity, Level world, BlockPos pos) {
        InventoryHandler storageInventory = storageWrapper.getInventoryHandler();
        for (int slot : slotsToVoid) {
            ItemStack stack = storageInventory.getStackInSlot(slot);
            if(stack.getItem().equals(ModItems.SOUL_DUST) || stack.getItem().equals(ModItems.SOUL_SHARD)) {
                continue;
            }
            int soulValue = DiffuserUpgradeHelper.getDiffuserValue(stack);
            int itemCount = stack.getCount();
            if(soulValue != 0) {
                storageInventory.insertItem(new ItemStack(ModItems.SOUL_DUST, itemCount * soulValue), false);
                storageInventory.extractItem(slot, itemCount, false);
            }
        }

        //We only want to move to shard pouch when the cooldown is finished
        if(isInCooldown(world) || shouldHoldShards()) {
            return;
        }

        //Attempt to add shards to pouch
        if (entity == null) {
            world.getEntities(EntityType.PLAYER, new AABB(pos).inflate(3), p -> true).forEach((p) -> DiffuserUpgradeHelper.tryAndAddShardsToPouch(storageWrapper, p));
        } else {
            DiffuserUpgradeHelper.tryAndAddShardsToPouch(storageWrapper, (Player)entity);
        }

        setCooldown(world, COOLDOWN);
        slotsToVoid.clear();
    }


    @Override
    public boolean worksInGui() {
        return shouldWorkInGUI();
    }


    public boolean shouldHoldShards() {
        return isShouldHoldShards();
    }

    public boolean shouldCompactShards() {
        return isShouldCompactShards();
    }


    @Override
    public @NotNull ItemStack onOverflow(@NotNull ItemStack stack) {
        if(filterLogic.matchesFilter(stack) && shouldVoidOverflow) {
            int count = stack.getCount();
            int soulValue = DiffuserUpgradeHelper.getDiffuserValue(stack);
            if(soulValue != 0) {
                return new ItemStack(ModItems.SOUL_DUST, count * soulValue);
            }
        }
        return stack;
    }

    @Override
    public boolean stackMatchesFilter(@NotNull ItemStack stack) {
        if(stack.getItem().equals(ModItems.SOUL_DUST) || stack.getItem().equals(ModItems.SOUL_SHARD)) {
            return filterLogic.isAllowList();
        }
        return filterLogic.matchesFilter(stack);
    }

    public boolean isVoidAnythingEnabled() {
        return upgradeItem.isVoidAnythingEnabled();
    }


    @Override
    public int getSlotLimit() {
        return Integer.MAX_VALUE;
    }

    private void compactDust(IItemHandlerSimpleInserter inventoryHandler, int slot) {
        ItemStack slotStack = inventoryHandler.getStackInSlot(slot);

        if(slotStack.isEmpty()) {
            return;
        }

        Item item = slotStack.getItem();

        tryCompacting(inventoryHandler, item);
    }


    private void tryCompacting(IItemHandlerSimpleInserter inventoryHandler, Item item) {
        int totalCount = 3 * 3;
        RecipeHelper.CompactingResult compactingResult = RecipeHelper.getCompactingResult(item, 3, 3);
        if (!compactingResult.getResult().isEmpty()) {
            ItemStack extractedStack = InventoryHelper.extractFromInventory(item, totalCount, inventoryHandler, true);
            if (extractedStack.getCount() != totalCount) {
                return;
            }

            while (extractedStack.getCount() == totalCount) {
                ItemStack resultCopy = compactingResult.getResult().copy();
                List<ItemStack> remainingItemsCopy = compactingResult.getRemainingItems().isEmpty() ? Collections.emptyList() : compactingResult.getRemainingItems().stream().map(ItemStack::copy).toList();

                if (!fitsResultAndRemainingItems(inventoryHandler, remainingItemsCopy, resultCopy)) {
                    break;
                }
                InventoryHelper.extractFromInventory(item, totalCount, inventoryHandler, false);
                inventoryHandler.insertItem(resultCopy, false);
                InventoryHelper.insertIntoInventory(remainingItemsCopy, inventoryHandler, false);
                extractedStack = InventoryHelper.extractFromInventory(item, totalCount, inventoryHandler, true);
            }
        }
    }

    private boolean fitsResultAndRemainingItems(IItemHandler inventoryHandler, List<ItemStack> remainingItems, ItemStack result) {
        if (!remainingItems.isEmpty()) {
            IItemHandler clonedHandler = InventoryHelper.cloneInventory(inventoryHandler);
            return InventoryHelper.insertIntoInventory(result, clonedHandler, false).isEmpty()
                    && InventoryHelper.insertIntoInventory(remainingItems, clonedHandler, false).isEmpty();
        }
        return InventoryHelper.insertIntoInventory(result, inventoryHandler, true).isEmpty();
    }


}
