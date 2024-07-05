package xyz.iwolfking.sophisticatedvaultupgrades.upgrades.diffuser;

import iskallia.vault.container.inventory.ShardPouchContainer;
import iskallia.vault.init.ModConfigs;
import iskallia.vault.init.ModItems;
import iskallia.vault.item.ItemShardPouch;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.p3pp3rf1y.sophisticatedcore.api.IStorageWrapper;
import net.p3pp3rf1y.sophisticatedcore.util.InventoryHelper;
import top.theillusivec4.curios.api.CuriosApi;

public class DiffuserUpgradeHelper {
    public static int getDiffuserValue(ItemStack stack) {
        if(ModConfigs.VAULT_DIFFUSER.getDiffuserOutputMap().get(stack.getItem().getRegistryName()) != null) {
            return ModConfigs.VAULT_DIFFUSER.getDiffuserOutputMap().get(stack.getItem().getRegistryName());
        }
        else {
            return 0;
        }
    }

    public static boolean tryAndAddShardsToPouch(IStorageWrapper storageWrapper, Player player) {
        IItemHandlerModifiable inventory = storageWrapper.getInventoryForUpgradeProcessing();
        if(InventoryHelper.isEmpty(inventory)) {
            return false;
        }

        InventoryHelper.iterate(inventory, (slot, stack) -> {
            if(stack.getItem().equals(ModItems.SOUL_SHARD)) {
                if(!(player.containerMenu instanceof ShardPouchContainer)) {
                    ItemStack pouchStack = ItemStack.EMPTY;
                    if(CuriosApi.getCuriosHelper().findFirstCurio(player, ModItems.SHARD_POUCH).isPresent()) {
                        pouchStack = CuriosApi.getCuriosHelper().findFirstCurio(player, ModItems.SHARD_POUCH).get().stack();
                    }

                    if (!pouchStack.isEmpty()) {
                        pouchStack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent((handler) -> {
                            ItemStack remainder = handler.insertItem(0,InventoryHelper.extractFromInventory(stack, inventory, false), false);
                            stack.setCount(remainder.getCount());
                            if (stack.isEmpty()) {
                            }
                        });
                    }
                    if(pouchStack.isEmpty()) {
                        Inventory thisInventory = player.getInventory();
                        for(int playerSlot = 0; playerSlot < thisInventory.getContainerSize(); ++playerSlot) {
                            ItemStack invStack = thisInventory.getItem(playerSlot);
                            if (invStack.getItem() instanceof ItemShardPouch) {
                                pouchStack = invStack;
                                break;
                            }
                        }

                        if (!pouchStack.isEmpty()) {
                            pouchStack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent((handler) -> {
                                ItemStack remainder = handler.insertItem(0, InventoryHelper.extractFromInventory(stack, inventory, false), false);
                                stack.setCount(remainder.getCount());
                                if (stack.isEmpty()) {
                                }
                            });
                        }
                    }

                }
            }
        });
        return true;
    }
}
