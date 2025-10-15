package xyz.iwolfking.sophisticatedvaultupgrades.upgrades.opener;


import iskallia.vault.item.LootableItem;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import xyz.iwolfking.sophisticatedvaultupgrades.mixin.accessors.LootableItemAccessor;


import java.util.*;
import java.util.function.Supplier;

public class OpenerUpgradeHelper {

    public static boolean isSupported(ItemStack stack) {
        if(stack.getItem() instanceof LootableItem) {
            return true;
        }

        return false;
    }

    public static List<ItemStack> getOpenerOutput(ItemStack stack) {
        Item itemType = stack.getItem();

        if(itemType instanceof LootableItem lootableItem) {
            return handleLootableItem(stack, lootableItem);
        }

        else {
            return List.of();
        }
    }

    public static List<ItemStack> handleLootableItem(ItemStack stack, LootableItem lootableItem) {
        List<ItemStack> lootItems = new ArrayList<>();
        for(int i = 0; i < stack.getCount(); i++) {
            Supplier<ItemStack> lootableSupplier = ((LootableItemAccessor)lootableItem).getSupplier();
            ItemStack randomLoot = lootableSupplier.get();

            ItemStack copy = randomLoot.copy();
            lootItems.add(copy);
        }

        return lootItems;
    }




}
