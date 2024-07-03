package xyz.iwolfking.sophisticatedvaultupgrades.util;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.p3pp3rf1y.sophisticatedcore.upgrades.IPickupResponseUpgrade;
import net.p3pp3rf1y.sophisticatedcore.upgrades.UpgradeHandler;
import xyz.iwolfking.sophisticatedvaultupgrades.upgrades.IInteractResponseUpgrade;

import javax.annotation.Nullable;
import java.util.List;

public class SVUInventoryHelper {
    public static void runInteractOnInteractUpgrades(Level world,
                                                         @Nullable Player player, UpgradeHandler upgradeHandler, BlockPos blockPos) {
        List<IInteractResponseUpgrade> interactUpgrades = upgradeHandler.getWrappersThatImplement(IInteractResponseUpgrade.class);

        for (IInteractResponseUpgrade interactUpgrade : interactUpgrades) {
            interactUpgrade.interact(world, blockPos, player);
        }
    }
}
