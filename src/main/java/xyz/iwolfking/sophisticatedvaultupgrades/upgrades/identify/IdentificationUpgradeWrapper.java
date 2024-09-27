package xyz.iwolfking.sophisticatedvaultupgrades.upgrades.identify;

import iskallia.vault.gear.VaultGearState;
import iskallia.vault.gear.item.IdentifiableItem;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.players.PlayerList;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.items.IItemHandler;
import net.p3pp3rf1y.sophisticatedcore.api.ISlotChangeResponseUpgrade;
import net.p3pp3rf1y.sophisticatedcore.api.IStorageWrapper;
import net.p3pp3rf1y.sophisticatedcore.inventory.IItemHandlerSimpleInserter;
import net.p3pp3rf1y.sophisticatedcore.inventory.InventoryHandler;
import net.p3pp3rf1y.sophisticatedcore.upgrades.*;
import net.p3pp3rf1y.sophisticatedcore.util.InventoryHelper;
import net.p3pp3rf1y.sophisticatedcore.util.NBTHelper;
import org.jetbrains.annotations.NotNull;
import xyz.iwolfking.sophisticatedvaultupgrades.upgrades.IDimensionChangeResponseUpgrade;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

public class IdentificationUpgradeWrapper extends UpgradeWrapperBase<IdentificationUpgradeWrapper, IdentificationUpgradeItem>
        implements IPickupResponseUpgrade, ISlotChangeResponseUpgrade, ITickableUpgrade, IDimensionChangeResponseUpgrade, IInsertResponseUpgrade {

    private final Set<Integer> slotsToIdentify = new HashSet<>();

    boolean shouldRunIdentify = true;
    private final ItemStack upgradeStack;

    private final FilterLogic filterLogic;
    private final Map<Integer, Integer> SLOT_HASHMAP = new HashMap<>();

    private static final int IDENTIFICATION_COOLDOWN = 20;

    public IdentificationUpgradeWrapper(IStorageWrapper storageWrapper, ItemStack upgrade, Consumer<ItemStack> upgradeSaveHandler) {
        super(storageWrapper, upgrade, upgradeSaveHandler);
        this.upgradeStack = upgrade;
        this.filterLogic = new FilterLogic(upgrade, upgradeSaveHandler, upgradeItem.getFilterSlotCount());
    }





    //Handles identifying items when it is picked up directly (outside of gui)
    @Override
    public @NotNull ItemStack pickup(@NotNull Level level, @NotNull ItemStack itemStack, boolean simulate) {
        if(!shouldWorkInVault() && isInVault(level)) {
            return itemStack;
        }
        if(!simulate && matchesFilter(itemStack)) {
            tryIdentifyItem(itemStack, level);
        }
        return itemStack;
    }


    //For handling identifying gear that is inserted through an interface.
    @Override
    public void tick(LivingEntity entity, Level world, BlockPos pos) {
        if (!shouldRunIdentify || isInCooldown(world)) {
            return;
        }
        if(!shouldWorkInVault() && isInVault(world)) {
            return;
        }

        InventoryHandler storageInventory = storageWrapper.getInventoryHandler();
        for (int slot = 0; slot < storageWrapper.getInventoryHandler().getSlots(); slot++) {
            ItemStack stack = storageInventory.getStackInSlot(slot);
            if(matchesFilter(stack, slot)) {
                tryIdentifyItem(stack, slot, world, entity, storageInventory);
            }
        }
        setCooldown(world, IDENTIFICATION_COOLDOWN);
        shouldRunIdentify = false;
    }

    public boolean matchesFilter(ItemStack stack) {
        return matchesFilter(stack, -1);
    }

    public boolean matchesFilter(ItemStack stack, int slot) {
        if(SLOT_HASHMAP.containsKey(slot) && SLOT_HASHMAP.get(slot).equals(stack.hashCode())) {
            return false;
        }

        if(stack.getItem() instanceof IdentifiableItem gear && filterLogic.matchesFilter(stack)) {
            boolean isUnidentified = gear.getState(stack).equals(VaultGearState.UNIDENTIFIED);
            SLOT_HASHMAP.put(slot, stack.hashCode());
            return isUnidentified;
        }
        return false;
    }



    @Override
    public void onSlotChange(IItemHandler inventoryHandler, int slot) {
        shouldRunIdentify = true;
    }

    public boolean hasOwner() {
        return (upgradeStack.getItem() instanceof IdentificationUpgradeItem identificationUpgradeItem && identificationUpgradeItem.getOwnerID(upgradeStack) != null);
    }

    public static boolean hasOwner(ItemStack upgradeStack) {
        return (upgradeStack.getItem() instanceof IdentificationUpgradeItem identificationUpgradeItem && identificationUpgradeItem.getOwnerID(upgradeStack) != null);
    }

    public Player getOwner(Level world) {
        if(hasOwner()) {
            IdentificationUpgradeItem upgradeItem = (IdentificationUpgradeItem) upgradeStack.getItem();
            MinecraftServer server = world.getServer();
            if(server == null) {
                return null;
            }
            try {
                PlayerList list = server.getPlayerList();
                return list.getPlayer(upgradeItem.getOwnerID(upgradeStack));
            }
            catch(Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }


    private ItemStack tryIdentifyItem(ItemStack stack, int slot, Level world, Entity entity, InventoryHandler storageInventory) {
        if(entity instanceof Player player && getOwner(world) == null) {
            ItemStack identifiedStack = tryIdentifyItem(stack, world, player);
            storageInventory.setStackInSlot(slot, ItemStack.EMPTY);
            storageInventory.insertItem(identifiedStack, false);
        }
        else {
            if(getOwner(world) != null) {
                ItemStack identifiedStack = tryIdentifyItem(stack, world);
                storageInventory.setStackInSlot(slot, ItemStack.EMPTY);
                storageInventory.insertItem(identifiedStack, false);
            }
        }
        return stack.copy();
    }

    private ItemStack tryIdentifyItem(ItemStack stack, Level level, Player player) {
        if(player != null) {
            if(stack.getItem() instanceof IdentifiableItem identifiableItem) {
                identifiableItem.instantIdentify(player, stack);
                return stack.copy();
            }
        }
        else {
            tryIdentifyItem(stack, level);
        }
        return stack.copy();
    }

    private ItemStack tryIdentifyItem(ItemStack stack, Level level) {
        Player player = getOwner(level);
        if(player != null) {
            return tryIdentifyItem(stack, level, player);
        }
        return stack.copy();
    }

    @Override
    public void onDimensionChange(ResourceKey<Level> from, ResourceKey<Level> to, Player player) {
        if(shouldWorkInVault()) {
            return;
        }
        if(from.location().getNamespace().equals("the_vault")) {
            InventoryHelper.iterate(storageWrapper.getInventoryHandler(), (slot, stack) -> {
                if(stack.getItem() instanceof IdentifiableItem identifiableItem) {
                    if(identifiableItem.getState(stack).equals(VaultGearState.UNIDENTIFIED)) {
                        tryIdentifyItem(stack, player.getLevel(), player);
                    }
                }
            });
        }
    }

    public void setShouldWorkInVault(boolean shouldWorkInVault) {
        NBTHelper.setBoolean(upgrade, "shouldWorkInVault", shouldWorkInVault);
        save();
    }

    public boolean shouldWorkInVault() {
        return NBTHelper.getBoolean(upgrade, "shouldWorkInVault").orElse(true);
    }


    public boolean isInVault(Level level) {
        return level.dimension().location().getNamespace().equals("the_vault");
    }

    public @NotNull FilterLogic getFilterLogic() {
        return filterLogic;
    }

    @Override
    public @NotNull ItemStack onBeforeInsert(@NotNull IItemHandlerSimpleInserter iItemHandlerSimpleInserter, int i, @NotNull ItemStack itemStack, boolean b) {
        return itemStack;
    }

    @Override
    public void onAfterInsert(IItemHandlerSimpleInserter iItemHandlerSimpleInserter, int i) {
        shouldRunIdentify = true;
    }
}
