package xyz.iwolfking.sophisticatedvaultupgrades.upgrades.recycler;

import iskallia.vault.config.VaultRecyclerConfig;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.loading.LoadingModList;
import xyz.iwolfking.vhapi.api.data.api.CustomRecyclerOutputs;

public class VHAPIIntegration {
    private static final boolean isVHAPILoaded = ModList.get().isLoaded("vhapi") || LoadingModList.get().getModFileById("vhapi") != null;

    public static boolean hasRecyclerOutput(ResourceLocation registryName){
        if (!isVHAPILoaded) return false;
        return VHAPIClass.hasRecyclerOutput(registryName);
    }

    public static VaultRecyclerConfig.RecyclerOutput getRecyclerOutput(ResourceLocation registryName) {
        if (!isVHAPILoaded) return null;
        return VHAPIClass.getRecyclerOutput(registryName);
    }


    // don't load without vhapi
    private static class VHAPIClass {
        private static boolean hasRecyclerOutput(ResourceLocation registryName){
            return CustomRecyclerOutputs.CUSTOM_OUTPUTS.containsKey(registryName);
        }

        private static VaultRecyclerConfig.RecyclerOutput getRecyclerOutput(ResourceLocation registryName){
            return CustomRecyclerOutputs.CUSTOM_OUTPUTS.get(registryName);
        }
    }

}
