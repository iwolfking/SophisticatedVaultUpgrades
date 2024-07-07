package xyz.iwolfking.sophisticatedvaultupgrades.upgrades.diffuser;

import iskallia.vault.init.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
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
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class DiffuserUpgradeWrapper extends UpgradeWrapperBase<DiffuserUpgradeWrapper, DiffuserUpgradeItem>
        implements IInsertResponseUpgrade, IFilteredUpgrade, ISlotChangeResponseUpgrade, ITickableUpgrade, IOverflowResponseUpgrade {
    private final FilterLogic filterLogic;
    private final Set<Integer> slotsToVoid = new HashSet<>();
    private final Set<Integer> remainderSlotsToVoid = new HashSet<>();
    private boolean shouldVoidOverflow;

    private static final int INSERTING_COOLDOWN = 10;
    private static final int SHARD_POUCH_COOLDOWN = 20;

    private static final RecipeHelper.CompactingResult SHARD_COMPACTING_RECIPE = RecipeHelper.getCompactingResult(ModItems.SOUL_DUST, 3, 3);

    public DiffuserUpgradeWrapper(IStorageWrapper storageWrapper, ItemStack upgrade, Consumer<ItemStack> upgradeSaveHandler) {
        super(storageWrapper, upgrade, upgradeSaveHandler);
        filterLogic = new FilterLogic(upgrade, upgradeSaveHandler, upgradeItem.getFilterSlotCount());
        setShouldVoidOverflowDefaultOrLoadFromNbt(false);
    }

    @Override
    public @NotNull ItemStack onBeforeInsert(@NotNull IItemHandlerSimpleInserter inventoryHandler, int slot, @NotNull ItemStack stack, boolean simulate) {
        if(stack.getItem().equals(ModItems.SOUL_DUST) || stack.getItem().equals(ModItems.SOUL_SHARD) || !hasSlotSpace()) {
            return stack;
        }


        if (shouldVoidOverflow && inventoryHandler.getStackInSlot(slot).isEmpty() && (!filterLogic.shouldMatchNbt() || !filterLogic.shouldMatchDurability() || filterLogic.getPrimaryMatch() != PrimaryMatch.ITEM) && filterLogic.matchesFilter(stack)) {
            for (int s = 0; s < inventoryHandler.getSlots(); s++) {
                if (s == slot) {
                    continue;
                }
                if (stackMatchesFilterStack(inventoryHandler.getStackInSlot(s), stack)) {
                    ItemStack remainderStack = insertDiffusedDust((InventoryHandler) inventoryHandler, slot, stack, simulate, false);
                    if(remainderStack.isEmpty()) {
                        return ItemStack.EMPTY;
                    }
                    else {
                        return remainderStack;
                    }

                }
            }
            return stack;
        }

        if(!shouldVoidOverflow && filterLogic.matchesFilter(stack) && DiffuserUpgradeHelper.getDiffuserValue(stack) != 0) {
            ItemStack remainderStack = insertDiffusedDust((InventoryHandler) inventoryHandler, slot, stack, simulate, false);
            if(remainderStack.isEmpty()) {
                return ItemStack.EMPTY;
            }
            else {
                return remainderStack;
            }
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
        if(isInCooldown(world)) {
            return;
        }
        InventoryHandler storageInventory = storageWrapper.getInventoryHandler();
        for (int slot : slotsToVoid) {
            ItemStack stack = storageInventory.getStackInSlot(slot);
            if(!stackMatchesFilter(stack)) {
                continue;
            }
            if(stack.getItem().equals(ModItems.SOUL_DUST) || stack.getItem().equals(ModItems.SOUL_SHARD)) {
                continue;
            }
            int soulValue = DiffuserUpgradeHelper.getDiffuserValue(stack);
            if(soulValue != 0) {
                if(insertDiffusedDust(storageInventory, slot, stack, false, true).isEmpty()) {

                }
            }
        }

        if(!remainderSlotsToVoid.isEmpty()){
            List<Integer> remainderSlotList = remainderSlotsToVoid.stream().toList();
            for(int i = 0; i < remainderSlotList.size(); i++) {
                int slot = remainderSlotList.get(i);
                if(slotsToVoid.contains(slot)) {
                    continue;
                }
                ItemStack slotStack = storageInventory.getStackInSlot(slot);
                if(!slotStack.isEmpty() && stackMatchesFilter(slotStack)) {
                    if(insertDiffusedDust(storageInventory, slot, slotStack, false, true).isEmpty()) {
                        remainderSlotsToVoid.remove(slot);
                    }
                }
                else {
                    //Stack must have changed or filter did, we can remove it from the list of slots to track
                    remainderSlotsToVoid.remove(slot);
                }
            }
        }


        setCooldown(world, INSERTING_COOLDOWN);

        if(shouldCompactShards()) {
            tryCompacting(storageInventory, false);
        }


        if(shouldHoldShards()) {
            return;
        }

        //Attempt to add shards to pouch
        if (entity == null) {
            world.getEntities(EntityType.PLAYER, new AABB(pos).inflate(3), p -> true).forEach((p) -> DiffuserUpgradeHelper.tryAndAddShardsToPouch(storageWrapper, p));
        } else {
            DiffuserUpgradeHelper.tryAndAddShardsToPouch(storageWrapper, (Player)entity);
        }
        setCooldown(world, SHARD_POUCH_COOLDOWN);
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
        return stack;
//        if(filterLogic.matchesFilter(stack) && shouldVoidOverflow) {
//            System.out.println("Got " + stack.getCount() + " " + stack.getItem().getRegistryName());
//            int soulValue = DiffuserUpgradeHelper.getDiffuserValue(stack);
//            if(soulValue != 0) {
//                return insertDiffusedDust(storageWrapper.getInventoryHandler(), stack, false);
//            }
//        }
//        return stack;
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

    private void tryCompacting(InventoryHandler inventoryHandler, boolean simulate) {
        AtomicInteger totalDust = new AtomicInteger();

        InventoryHelper.iterate(inventoryHandler, (slot, stack) -> {
            if(stack.is(ModItems.SOUL_DUST)) {
                totalDust.addAndGet(stack.getCount());
            }
        });

        if(totalDust.get() < 9) {
            return;
        }

        Pair<Integer, Integer> shardDustPair = getDustAndShardPair(totalDust.get());
        ItemStack existingDust = new ItemStack(ModItems.SOUL_DUST, totalDust.get());
        ItemStack dustStack = new ItemStack(ModItems.SOUL_DUST, shardDustPair.getRight());
        ItemStack shardStack = new ItemStack(ModItems.SOUL_SHARD, shardDustPair.getLeft());


        //Check if the items will fit
        if(canFitResultingDust(inventoryHandler, existingDust, dustStack, shardStack, true)) {
            InventoryHelper.extractFromInventory(ModItems.SOUL_DUST, totalDust.get(), inventoryHandler, simulate);
            inventoryHandler.insertItem(shardStack, simulate);
            inventoryHandler.insertItem(dustStack, simulate);
        }


    }

    private boolean canFitResultingDust(IItemHandler inventoryHandler, ItemStack diffuseStack, ItemStack dustStack, ItemStack shardStack, boolean extractDiffuseStackBeforeInsert) {
        if (!diffuseStack.isEmpty()) {
            IItemHandler clonedHandler = InventoryHelper.cloneInventory(inventoryHandler);
            if(extractDiffuseStackBeforeInsert) {
                InventoryHelper.extractFromInventory(diffuseStack, clonedHandler, true);
            }
            if(InventoryHelper.insertIntoInventory(dustStack, clonedHandler, false).isEmpty() && InventoryHelper.insertIntoInventory(shardStack, clonedHandler, false).isEmpty()) {
                return true;
            }

        }

        return InventoryHelper.insertIntoInventory(dustStack, inventoryHandler, true).isEmpty() && InventoryHelper.insertIntoInventory(shardStack, inventoryHandler, true).isEmpty();
    }


    private boolean canFitResultingDust(IItemHandler inventoryHandler, ItemStack diffuseStack, ItemStack dustStack, ItemStack shardStack) {
      return canFitResultingDust(inventoryHandler, diffuseStack, dustStack, shardStack, false);
    }

    public ItemStack insertDiffusedDust(InventoryHandler inventoryHandler, int slot, ItemStack stackToDiffuse, boolean simulate, boolean shouldExtractDiffuseStack) {

        if(stackToDiffuse.isEmpty() || stackToDiffuse.getItem().equals(ModItems.SOUL_DUST) || stackToDiffuse.getItem().equals(ModItems.SOUL_SHARD)) {
            return stackToDiffuse;
        }

        int soulValue = DiffuserUpgradeHelper.getDiffuserValue(stackToDiffuse);
        int itemCount = stackToDiffuse.getCount();
        int totalValue = soulValue * itemCount;
        ItemStack dustStack;
        ItemStack shardStack = ItemStack.EMPTY;
        boolean inserted = false;

        if(soulValue == 0) {
            return stackToDiffuse;
        }

        if(shouldCompactShards()) {
            Pair<Integer, Integer> dustShardPair;
            dustShardPair = getDustAndShardPair(totalValue);
            dustStack = new ItemStack(ModItems.SOUL_DUST, dustShardPair.getRight());
            shardStack = new ItemStack(ModItems.SOUL_SHARD, dustShardPair.getLeft());
        }
        else {
            dustStack = new ItemStack(ModItems.SOUL_DUST, totalValue);
        }

        if(canFitResultingDust(inventoryHandler, stackToDiffuse, dustStack, shardStack)) {
            if(!shardStack.isEmpty()) {
                inventoryHandler.insertItem(shardStack, simulate);
            }
            inventoryHandler.insertItem(dustStack, simulate);
            inserted = true;
        }

        if(inserted) {
            if(shouldExtractDiffuseStack) {
                inventoryHandler.extractItem(slot, itemCount, simulate);
            }
            return ItemStack.EMPTY;
        }
        else {


            if(shouldCompactShards()) {
                Pair<Integer, Integer> shardDustPair = getDustAndShardPair(soulValue);
                dustStack = new ItemStack(ModItems.SOUL_DUST, shardDustPair.getRight());
                shardStack = new ItemStack(ModItems.SOUL_SHARD, shardDustPair.getLeft());
            }
            else {
                dustStack = new ItemStack(ModItems.SOUL_DUST, soulValue);
                shardStack = ItemStack.EMPTY;
            }


            if(canFitResultingDust(inventoryHandler, stackToDiffuse, dustStack, shardStack)) {
                inventoryHandler.insertItem(shardStack, simulate);
                inventoryHandler.insertItem(dustStack, simulate);

                if(shouldExtractDiffuseStack) {
                    inventoryHandler.extractItem(slot, 1, simulate);
                    remainderSlotsToVoid.add(slot);
                    return ItemStack.EMPTY;
                }
            }
            return stackToDiffuse;
        }
    }

    public ItemStack insertDiffusedDust(InventoryHandler inventoryHandler, ItemStack stackToDiffuse, boolean simulate) {
        return insertDiffusedDust(inventoryHandler, -1, stackToDiffuse, simulate, false);
    }

    public static Pair<Integer, Integer> getDustAndShardPair(int totalValue) {
        int shardValue = totalValue / 9;
        int dustValue = totalValue % 9;
        return Pair.of(shardValue, dustValue);
    }

    private boolean hasSlotSpace() {
        InventoryHandler handler = storageWrapper.getInventoryHandler();
        Predicate<ItemStack> hasItemPredicate = Predicate.not(Predicate.isEqual(ItemStack.EMPTY));
        if(!handler.hasEmptySlots()) {
            return false;
        }
        else {
            return handler.getSlots() - InventoryHelper.getItemSlots(handler, hasItemPredicate).size() > 2;
        }
    }
}
