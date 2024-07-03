package xyz.iwolfking.sophisticatedvaultupgrades.upgrades.diffuser;

import iskallia.vault.init.ModConfigs;
import net.minecraft.world.item.ItemStack;

public class DiffuserUpgradeHelper {
    public static int getDiffuserValue(ItemStack stack) {
        return ModConfigs.VAULT_DIFFUSER.getDiffuserOutputMap().get(stack.getItem().getRegistryName());
    }
}
