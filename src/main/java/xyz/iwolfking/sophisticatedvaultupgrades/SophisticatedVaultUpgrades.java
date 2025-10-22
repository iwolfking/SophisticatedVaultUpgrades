package xyz.iwolfking.sophisticatedvaultupgrades;

import com.mojang.logging.LogUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.LoadingModList;
import net.p3pp3rf1y.sophisticatedcore.inventory.ItemStackKey;
import org.slf4j.Logger;
import xyz.iwolfking.sophisticatedvaultupgrades.config.Config;
import xyz.iwolfking.sophisticatedvaultupgrades.events.InteractEvents;
import xyz.iwolfking.sophisticatedvaultupgrades.events.VaultEvents;
import xyz.iwolfking.sophisticatedvaultupgrades.init.ModItemsSophisticatedBP;
import xyz.iwolfking.sophisticatedvaultupgrades.init.ModItemsSophisticatedST;

// The value here should match an entry in the META-INF/mods.toml file
@Mod("sophisticatedvaultupgrades")
public class SophisticatedVaultUpgrades {

    // Directly reference a slf4j logger
    public static final Logger LOGGER = LogUtils.getLogger();
    public static final String MOD_ID = "sophisticatedvaultupgrades";


    public SophisticatedVaultUpgrades() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, Config.SERVER_SPEC);
        if(LoadingModList.get().getModFileById("sophisticatedstorage") != null) {
            ModItemsSophisticatedST.registerHandlers(FMLJavaModLoadingContext.get().getModEventBus());
        }

        ModItemsSophisticatedBP.registerHandlers(FMLJavaModLoadingContext.get().getModEventBus());

        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
        MinecraftForge.EVENT_BUS.addListener(InteractEvents::activateInteractUpgrades);
        MinecraftForge.EVENT_BUS.addListener(VaultEvents::onDimensionChange);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);

    }

    public static ResourceLocation loc(String name) {
        return new ResourceLocation(SophisticatedVaultUpgrades.MOD_ID, name);
    }

}
