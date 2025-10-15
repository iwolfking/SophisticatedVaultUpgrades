package xyz.iwolfking.sophisticatedvaultupgrades.upgrades.opener;

import com.mojang.authlib.GameProfile;
import iskallia.vault.gear.VaultGearState;
import iskallia.vault.gear.item.IdentifiableItem;
import iskallia.vault.init.ModItems;
import iskallia.vault.item.GatedLootableItem;
import iskallia.vault.item.gear.RecyclableItem;
import net.minecraft.core.BlockPos;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.items.IItemHandler;
import net.p3pp3rf1y.sophisticatedcore.api.ISlotChangeResponseUpgrade;
import net.p3pp3rf1y.sophisticatedcore.api.IStorageWrapper;
import net.p3pp3rf1y.sophisticatedcore.inventory.IItemHandlerSimpleInserter;
import net.p3pp3rf1y.sophisticatedcore.inventory.InventoryHandler;
import net.p3pp3rf1y.sophisticatedcore.upgrades.*;
import net.p3pp3rf1y.sophisticatedcore.util.InventoryHelper;
import net.p3pp3rf1y.sophisticatedcore.util.NBTHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.iwolfking.sophisticatedvaultupgrades.upgrades.identify.IdentificationUpgradeItem;
import xyz.iwolfking.sophisticatedvaultupgrades.upgrades.recycler.RecyclerUpgradeHelper;
import xyz.iwolfking.vhapi.api.data.api.CustomRecyclerOutputs;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class OpenerUpgradeWrapper extends UpgradeWrapperBase<OpenerUpgradeWrapper, OpenerUpgradeItem>
        implements IInsertResponseUpgrade, IFilteredUpgrade, ISlotChangeResponseUpgrade, ITickableUpgrade, IOverflowResponseUpgrade {
    private final FilterLogic filterLogic;
    private final Set<Integer> slotsToHandle = new HashSet<>();
    private final ItemStack upgradeStack;

    public OpenerUpgradeWrapper(IStorageWrapper storageWrapper, ItemStack upgrade, Consumer<ItemStack> upgradeSaveHandler) {
        super(storageWrapper, upgrade, upgradeSaveHandler);
        this.upgradeStack = upgrade;
        filterLogic = new FilterLogic(upgrade, upgradeSaveHandler, upgradeItem.getFilterSlotCount());
    }

    @Override
    public @NotNull ItemStack onBeforeInsert(@NotNull IItemHandlerSimpleInserter inventoryHandler, int slot, @NotNull ItemStack stack, boolean simulate) {
        if(!stackMatchesFilter(stack) || !hasSlotSpace()) {
            return stack;
        }

        if(filterLogic.matchesFilter(stack)) {
            List<ItemStack> outputs = OpenerUpgradeHelper.getOpenerOutput(stack);

            if(!hasSlotSpace(outputs.size())) {
                slotsToHandle.add(slot);
                return stack;
            }

            if(!outputs.isEmpty()) {
                for(ItemStack lootStack : outputs) {
                    inventoryHandler.insertItem(lootStack, simulate);
                }

                return ItemStack.EMPTY;
            }
        }

        return stack;
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

        if (stackMatchesFilter(slotStack)) {
            slotsToHandle.add(slot);
        }
    }

    @Override
    public void tick(@Nullable LivingEntity entity, @NotNull Level world, @NotNull BlockPos pos) {
        if(slotsToHandle.isEmpty()) {
            return;
        }

        InventoryHandler storageInventory = storageWrapper.getInventoryHandler();

        for (int slot : slotsToHandle) {
            ItemStack stack = storageInventory.getStackInSlot(slot);

            if(!stackMatchesFilter(stack)) {
                slotsToHandle.remove(slot);
                continue;
            }

            List<ItemStack> outputs = OpenerUpgradeHelper.getOpenerOutput(stack);

            if(!hasSlotSpace(outputs.size())) {
                return;
            }

            for(ItemStack lootStack : outputs) {
                if(storageInventory.insertItem(lootStack, true).isEmpty()) {
                    storageInventory.insertItem(lootStack, false);
                    storageInventory.extractItem(slot, 1, false);
                }
                else {
                    return;
                }

            }


        }

        slotsToHandle.clear();
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
        if(!OpenerUpgradeHelper.isSupported(stack)) {
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
            return handler.getSlots() - InventoryHelper.getItemSlots(handler, hasItemPredicate).size() >= 1;
        }
    }

    private boolean hasSlotSpace(int count) {
        InventoryHandler handler = storageWrapper.getInventoryHandler();
        Predicate<ItemStack> hasItemPredicate = Predicate.not(Predicate.isEqual(ItemStack.EMPTY));
        if(!handler.hasEmptySlots()) {
            return false;
        }
        else {
            return handler.getSlots() - InventoryHelper.getItemSlots(handler, hasItemPredicate).size() >= count;
        }
    }


}
