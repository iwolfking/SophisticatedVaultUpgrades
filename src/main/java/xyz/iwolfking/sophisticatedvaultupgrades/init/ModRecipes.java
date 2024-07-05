package xyz.iwolfking.sophisticatedvaultupgrades.init;

import iskallia.vault.VaultMod;
import iskallia.vault.recipe.InitDollRecipe;
import iskallia.vault.recipe.MysteryEggRecipe;
import iskallia.vault.recipe.NonGrantedCrystalShapedRecipe;
import iskallia.vault.recipe.ShapelessCopyNbtRecipe;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionBrewing;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SimpleRecipeSerializer;
import net.minecraftforge.event.RegistryEvent;
import xyz.iwolfking.sophisticatedvaultupgrades.SophisticatedVaultUpgrades;
import xyz.iwolfking.sophisticatedvaultupgrades.recipes.InitOwnedItemRecipe;

public class ModRecipes {
    public ModRecipes() {
    }

    public static void initialize() {

    }

    public static class Serializer {

        public static SimpleRecipeSerializer INIT_OWNED_ITEM;

        public Serializer() {
        }

        public static void register(RegistryEvent.Register<RecipeSerializer<?>> event) {
            INIT_OWNED_ITEM = register(event, "init_owned_item", new SimpleRecipeSerializer(InitOwnedItemRecipe::new));
        }

        private static <S extends RecipeSerializer<T>, T extends Recipe<?>> S register(RegistryEvent.Register<RecipeSerializer<?>> event, String name, S serializer) {
            serializer.setRegistryName(SophisticatedVaultUpgrades.loc(name));
            event.getRegistry().register(serializer);
            return serializer;
        }
    }
}
