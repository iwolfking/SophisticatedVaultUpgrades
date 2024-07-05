package xyz.iwolfking.sophisticatedvaultupgrades.upgrades.identify;

import iskallia.vault.gear.VaultGearState;
import iskallia.vault.gear.item.IdentifiableItem;
import net.minecraft.core.BlockPos;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.players.PlayerList;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.items.IItemHandler;
import net.p3pp3rf1y.sophisticatedcore.api.ISlotChangeResponseUpgrade;
import net.p3pp3rf1y.sophisticatedcore.api.IStorageWrapper;
import net.p3pp3rf1y.sophisticatedcore.inventory.InventoryHandler;
import net.p3pp3rf1y.sophisticatedcore.upgrades.*;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

public class IdentificationUpgradeWrapper extends UpgradeWrapperBase<IdentificationUpgradeWrapper, IdentificationUpgradeItem>
        implements IPickupResponseUpgrade, ISlotChangeResponseUpgrade, ITickableUpgrade, ISlotLimitUpgrade {

    private final Set<Integer> slotsToIdentify = new HashSet<>();
    private final ItemStack upgradeStack;

    public IdentificationUpgradeWrapper(IStorageWrapper storageWrapper, ItemStack upgrade, Consumer<ItemStack> upgradeSaveHandler) {
        super(storageWrapper, upgrade, upgradeSaveHandler);
        this.upgradeStack = upgrade;
    }



    //Handles identifying items when it is picked up directly (outside of gui)
    @Override
    public @NotNull ItemStack pickup(@NotNull Level level, @NotNull ItemStack itemStack, boolean simulate) {
        if(!simulate) {
            tryIdentifyItem(itemStack, level);
        }
        return itemStack;
    }


    //For handling identifying gear that is inserted through an interface.
    @Override
    public void tick(LivingEntity entity, Level world, BlockPos pos) {
        if (slotsToIdentify.isEmpty()) {
            return;
        }

        InventoryHandler storageInventory = storageWrapper.getInventoryHandler();
        for (int slot : slotsToIdentify) {
            ItemStack stack = storageInventory.getStackInSlot(slot);
            if(stack.getItem() instanceof IdentifiableItem identifiableItem) {
                if(!identifiableItem.getState(stack).equals(VaultGearState.UNIDENTIFIED)) {
                    continue;
                }
            }
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
                else {
                    //There is no player to use for identifying gear, so we do nothing
                }
            }
        }

        slotsToIdentify.clear();
    }

    public boolean matchesFilter(ItemStack stack) {
        if(stack.getItem() instanceof IdentifiableItem gear) {
            return gear.getState(stack).equals(VaultGearState.UNIDENTIFIED);
        }
        return false;
    }


    @Override
    public int getSlotLimit() {
        return Integer.MAX_VALUE;
    }

    @Override
    public void onSlotChange(IItemHandler inventoryHandler, int slot) {

        ItemStack slotStack = inventoryHandler.getStackInSlot(slot);
        if (this.matchesFilter(slotStack)) {
            slotsToIdentify.add(slot);
        }
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
            if(stack.getItem() instanceof IdentifiableItem identifiableItem) {
                identifiableItem.instantIdentify(player, stack);
                return stack.copy();
            }
        }
        return stack.copy();
    }

}
