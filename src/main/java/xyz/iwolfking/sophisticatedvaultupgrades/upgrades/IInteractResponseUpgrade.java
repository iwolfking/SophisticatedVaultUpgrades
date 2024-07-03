package xyz.iwolfking.sophisticatedvaultupgrades.upgrades;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public interface IInteractResponseUpgrade {
    boolean interact(Level world, BlockPos blockState, Player player);
}
