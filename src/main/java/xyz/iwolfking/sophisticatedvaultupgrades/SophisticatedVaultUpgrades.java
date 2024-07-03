package xyz.iwolfking.sophisticatedvaultupgrades;

import com.mojang.logging.LogUtils;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.LoadingModList;
import net.p3pp3rf1y.sophisticatedcore.inventory.ItemStackKey;
import org.slf4j.Logger;
import xyz.iwolfking.sophisticatedvaultupgrades.events.InteractEvents;
import xyz.iwolfking.sophisticatedvaultupgrades.init.ModItemsSophisticatedBP;
import xyz.iwolfking.sophisticatedvaultupgrades.init.ModItemsSophisticatedST;

// The value here should match an entry in the META-INF/mods.toml file
@Mod("sophisticatedvaultupgrades")
public class SophisticatedVaultUpgrades {

    // Directly reference a slf4j logger
    private static final Logger LOGGER = LogUtils.getLogger();

    public SophisticatedVaultUpgrades() {
        if(LoadingModList.get().getModFileById("sophisticatedstorage") != null) {
            ModItemsSophisticatedST.registerHandlers(FMLJavaModLoadingContext.get().getModEventBus());
        }

        ModItemsSophisticatedBP.registerHandlers(FMLJavaModLoadingContext.get().getModEventBus());

        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
        MinecraftForge.EVENT_BUS.addListener(InteractEvents::activateInteractUpgrades);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);

    }


}
