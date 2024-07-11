package xyz.iwolfking.sophisticatedvaultupgrades.util;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.p3pp3rf1y.sophisticatedcore.upgrades.UpgradeHandler;
import xyz.iwolfking.sophisticatedvaultupgrades.upgrades.IDimensionChangeResponseUpgrade;
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

    public static void runDimensionChangeOnDimensionChangeUpgrades(ResourceKey<Level> from, ResourceKey<Level> to,
                                                                   @Nullable Player player, UpgradeHandler upgradeHandler) {
        List<IDimensionChangeResponseUpgrade> dimensionUpgrades = upgradeHandler.getWrappersThatImplement(IDimensionChangeResponseUpgrade.class);

        for (IDimensionChangeResponseUpgrade dimensionUpgrade : dimensionUpgrades) {
            dimensionUpgrade.onDimensionChange(from, to, player);
        }
    }
}
