package xyz.iwolfking.sophisticatedvaultupgrades.mixin.accessors;

import iskallia.vault.item.LootableItem;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.function.Supplier;

@Mixin(value = LootableItem.class, remap = false)
public interface LootableItemAccessor {
    @Accessor
    Supplier<ItemStack> getSupplier();
}
