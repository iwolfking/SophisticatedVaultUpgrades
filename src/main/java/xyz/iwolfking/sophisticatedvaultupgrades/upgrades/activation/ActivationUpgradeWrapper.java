package xyz.iwolfking.sophisticatedvaultupgrades.upgrades.activation;

import iskallia.vault.block.TreasureDoorBlock;
import iskallia.vault.core.event.CommonEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.items.IItemHandler;
import net.p3pp3rf1y.sophisticatedbackpacks.api.IItemHandlerInteractionUpgrade;
import net.p3pp3rf1y.sophisticatedbackpacks.upgrades.deposit.DepositFilterLogic;
import net.p3pp3rf1y.sophisticatedbackpacks.upgrades.deposit.DepositFilterType;
import net.p3pp3rf1y.sophisticatedbackpacks.upgrades.deposit.DepositUpgradeItem;
import net.p3pp3rf1y.sophisticatedbackpacks.upgrades.deposit.DepositUpgradeWrapper;
import net.p3pp3rf1y.sophisticatedcore.api.IStorageWrapper;
import net.p3pp3rf1y.sophisticatedcore.inventory.FilteredItemHandler;
import net.p3pp3rf1y.sophisticatedcore.inventory.ItemStackKey;
import net.p3pp3rf1y.sophisticatedcore.upgrades.FilterLogic;
import net.p3pp3rf1y.sophisticatedcore.upgrades.IFilteredUpgrade;
import net.p3pp3rf1y.sophisticatedcore.upgrades.UpgradeWrapperBase;
import net.p3pp3rf1y.sophisticatedcore.util.InventoryHelper;
import xyz.iwolfking.sophisticatedvaultupgrades.upgrades.IInteractResponseUpgrade;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

public class ActivationUpgradeWrapper extends UpgradeWrapperBase<ActivationUpgradeWrapper, ActivationUpgradeItem>
        implements IInteractResponseUpgrade {


    protected ActivationUpgradeWrapper(IStorageWrapper storageWrapper, ItemStack upgrade, Consumer<ItemStack> upgradeSaveHandler) {
        super(storageWrapper, upgrade, upgradeSaveHandler);
    }



    @Override
    public boolean interact(Level world, BlockPos blockPos, Player player) {
        BlockState blockState = world.getBlockState(blockPos);

        if(blockState.getBlock() instanceof TreasureDoorBlock door) {
            Set<ItemStackKey> itemsToRemove = new HashSet<>();
            boolean isOpen = door.isOpen(blockState);
            if(!isOpen) {
                storageWrapper.getInventoryForUpgradeProcessing().getTrackedStacks().forEach(itemStackKey -> {
                    if(itemStackKey.getStack().getItem() == blockState.getValue(TreasureDoorBlock.TYPE).getKey()) {
                        itemsToRemove.add(itemStackKey);
                        door.setOpen(player, world, blockState , blockPos, true);
                        CommonEvents.TREASURE_ROOM_OPEN.invoke(world, player, blockPos);
                    }
                });
                for(ItemStackKey key : itemsToRemove) {
                    InventoryHelper.extractFromInventory(key.stack(), storageWrapper.getInventoryForUpgradeProcessing(), false);
                }
                return true;
            }
        }
        return false;
    }
}