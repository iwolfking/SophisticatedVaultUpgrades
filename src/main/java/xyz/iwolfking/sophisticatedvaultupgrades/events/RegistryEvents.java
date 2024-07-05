package xyz.iwolfking.sophisticatedvaultupgrades.events;

import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import xyz.iwolfking.sophisticatedvaultupgrades.init.ModRecipes;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class RegistryEvents {
    @SubscribeEvent
    public static void onRecipeRegister(RegistryEvent.Register<RecipeSerializer<?>> event) {
        ModRecipes.Serializer.register(event);
    }
}
