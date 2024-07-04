package xyz.iwolfking.sophisticatedvaultupgrades.upgrades.diffuser;

import iskallia.vault.init.ModConfigs;
import net.minecraft.world.item.ItemStack;

public class DiffuserUpgradeHelper {
    public static int getDiffuserValue(ItemStack stack) {
        if(ModConfigs.VAULT_DIFFUSER.getDiffuserOutputMap().get(stack.getItem().getRegistryName()) != null) {
            return ModConfigs.VAULT_DIFFUSER.getDiffuserOutputMap().get(stack.getItem().getRegistryName());
        }
        else {
            return 0;
        }

    }
}
