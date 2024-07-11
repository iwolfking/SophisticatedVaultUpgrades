package xyz.iwolfking.sophisticatedvaultupgrades.upgrades.activation;

import iskallia.vault.block.ScavengerAltarBlock;
import iskallia.vault.block.TreasureDoorBlock;
import iskallia.vault.block.entity.ScavengerAltarTileEntity;
import iskallia.vault.core.event.CommonEvents;
import iskallia.vault.core.vault.Vault;
import iskallia.vault.core.vault.objective.Objective;
import iskallia.vault.core.vault.objective.ScavengerObjective;
import iskallia.vault.core.vault.objective.scavenger.ScavengerGoal;
import iskallia.vault.core.vault.player.Listener;
import iskallia.vault.core.vault.player.Listeners;
import iskallia.vault.world.data.ServerVaults;
import it.unimi.dsi.fastutil.Hash;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.p3pp3rf1y.sophisticatedcore.api.IStorageWrapper;
import net.p3pp3rf1y.sophisticatedcore.inventory.InventoryHandler;
import net.p3pp3rf1y.sophisticatedcore.inventory.ItemStackKey;
import net.p3pp3rf1y.sophisticatedcore.util.InventoryHelper;

import java.util.*;

public class ActivationUpgradeLogic {
    public static boolean tryOpenTreasureDoor(Player player, Level world, BlockState blockState, BlockPos blockPos, TreasureDoorBlock door, IStorageWrapper storageWrapper) {
        Set<ItemStackKey> itemsToRemove = new HashSet<>();
        boolean isOpen = door.isOpen(blockState);
        if (!isOpen) {
            storageWrapper.getInventoryForUpgradeProcessing().getTrackedStacks().forEach(itemStackKey -> {
                if (itemStackKey.getStack().getItem() == blockState.getValue(TreasureDoorBlock.TYPE).getKey()) {
                    itemsToRemove.add(itemStackKey);
                    door.setOpen(player, world, blockState, blockPos, true);
                    CommonEvents.TREASURE_ROOM_OPEN.invoke(world, player, blockPos);
                }
            });
            for (ItemStackKey key : itemsToRemove) {
                InventoryHelper.extractFromInventory(key.stack(), storageWrapper.getInventoryForUpgradeProcessing(), false);
            }
            return true;
        }
        else {
            return false;
        }
    }

    public static boolean tryAddingToScavAltar(Player player, Level world, BlockPos blockPos, ScavengerAltarBlock altar, IStorageWrapper storageWrapper) {
        Set<ItemStack> validScavItems = new HashSet<>();
        Optional<Vault> optionalVault = ServerVaults.get(world);
        if(optionalVault.isPresent()) {
            Vault vault = optionalVault.get();

            Listener listener = vault.get(Vault.LISTENERS).get(player.getUUID());
            if(listener == null) {
                return false;
            }

            Iterator<Objective> vaultObjectives = listener.getObjectives(vault);
            ScavengerObjective scavObj = null;
            while(vaultObjectives.hasNext()) {
                if(vaultObjectives.next() instanceof ScavengerObjective obj) {
                    scavObj = obj;
                }
            }

            if(scavObj != null) {
                ScavengerObjective.GoalMap goalMap = scavObj.get(ScavengerObjective.GOALS);
                List<ScavengerGoal> goals = (goalMap).get(listener.get(Listener.ID));
                for (ScavengerGoal goal : goals) {
                    if(goal.isCompleted()) {
                        continue;
                    }
                    int stackSize = goal.get(ScavengerGoal.TOTAL) - goal.get(ScavengerGoal.CURRENT);
                    Iterator<ScavengerGoal.Entry> entries = goal.getEntries();
                    while(entries.hasNext()) {
                        ScavengerGoal.Entry entry = entries.next();
                        validScavItems.add(entry.getStack(stackSize));
                    }
                }
            }


        }

        if(!validScavItems.isEmpty()) {
            for(ItemStack scavStack : validScavItems) {
                useScavAltar(world, blockPos, player, InventoryHelper.extractFromInventory(scavStack, storageWrapper.getInventoryHandler(), false).copy());
                break;
            }
        }
        return true;
    }


    public static boolean useScavAltar(Level world, BlockPos pos, Player player, ItemStack stack) {
            BlockEntity tileEntity = world.getBlockEntity(pos);
            if (tileEntity instanceof ScavengerAltarTileEntity tile) {
                if (stack.isEmpty()) {
                    return false;
                } else {
                    tile.setHeldItem(stack);
                    tile.setItemPlacedBy(player.getUUID());
                    tile.ticksToConsume = 40;
                    tile.consuming = false;
                    world.playSound(null, pos, SoundEvents.ITEM_FRAME_ADD_ITEM, SoundSource.BLOCKS, 1.0F, 1.0F);
                    tile.setChanged();
                    world.sendBlockUpdated(pos, tile.getBlockState(), tile.getBlockState(), 3);
                    return true;
                }
            } else {
                return false;
            }
        }

}

