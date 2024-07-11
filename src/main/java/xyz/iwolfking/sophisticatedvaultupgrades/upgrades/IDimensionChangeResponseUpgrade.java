package xyz.iwolfking.sophisticatedvaultupgrades.upgrades;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public interface IDimensionChangeResponseUpgrade {

    void onDimensionChange(ResourceKey<Level> from, ResourceKey<Level> to, Player player);
}
