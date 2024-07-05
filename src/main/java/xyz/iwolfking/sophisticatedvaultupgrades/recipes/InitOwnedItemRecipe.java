package xyz.iwolfking.sophisticatedvaultupgrades.recipes;

import iskallia.vault.init.ModItems;
import iskallia.vault.item.VaultDollItem;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import xyz.iwolfking.sophisticatedvaultupgrades.init.ModItemsSophisticatedBP;
import xyz.iwolfking.sophisticatedvaultupgrades.init.ModItemsSophisticatedST;
import xyz.iwolfking.sophisticatedvaultupgrades.init.ModRecipes;
import xyz.iwolfking.sophisticatedvaultupgrades.upgrades.identify.IdentificationUpgradeItem;
import xyz.iwolfking.sophisticatedvaultupgrades.upgrades.identify.IdentificationUpgradeWrapper;

public class InitOwnedItemRecipe extends CustomRecipe {
    public InitOwnedItemRecipe(ResourceLocation id) {
        super(id);
    }

    public InitOwnedItemRecipe(Object o) {
        super((ResourceLocation) o);
    }

    public boolean matches(CraftingContainer inv, Level level) {
        boolean foundUpgrade = false;

        for(int i = 0; i < inv.getContainerSize(); ++i) {
            ItemStack stack = inv.getItem(i);
            if (!stack.isEmpty()) {
                if (foundUpgrade || !(stack.getItem() instanceof IdentificationUpgradeItem) || IdentificationUpgradeWrapper.hasOwner(stack)) {
                    return false;
                }

                foundUpgrade = true;
            }
        }

        return foundUpgrade;
    }

    public @NotNull ItemStack assemble(CraftingContainer inv) {
        for(int i = 0; i < inv.getContainerSize(); ++i) {
            ItemStack stack = inv.getItem(i);
            if (!stack.isEmpty() && (stack.getItem() instanceof IdentificationUpgradeItem identificationUpgradeItem)  && !IdentificationUpgradeWrapper.hasOwner(stack)) {
                return stack;
            }
        }

        return ItemStack.EMPTY;
    }

    public boolean canCraftInDimensions(int width, int height) {
        return width * height >= 1;
    }

    public @NotNull RecipeSerializer<?> getSerializer() {
        return ModRecipes.Serializer.INIT_OWNED_ITEM;
    }


}
