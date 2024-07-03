package xyz.iwolfking.sophisticatedvaultupgrades.init;

import net.minecraft.core.Registry;
import net.minecraft.tags.TagKey;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.Item;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.p3pp3rf1y.sophisticatedstorage.SophisticatedStorage;
import xyz.iwolfking.sophisticatedvaultupgrades.upgrades.identify.IdentificationUpgradeItem;

public class ModItemsSophisticatedST {
    private ModItemsSophisticatedST() {
    }

    public static final TagKey<Item> STORAGE_UPGRADE_TAG = TagKey.create(Registry.ITEM_REGISTRY, net.p3pp3rf1y.sophisticatedstorage.init.ModItems.STORAGE_UPGRADE_TAG_NAME);
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, "sophisticatedstorage");


    public static void registerHandlers(IEventBus modBus) {
        RegistryObject<IdentificationUpgradeItem> IDENTIFICATION_UPGRADE = ITEMS.register("identification_upgrade",
                () -> new IdentificationUpgradeItem(SophisticatedStorage.CREATIVE_TAB));
        ITEMS.register(modBus);
        modBus.addGenericListener(MenuType.class, ModItemsSophisticatedST::registerContainers);
    }


    public static void registerContainers(RegistryEvent.Register<MenuType<?>> evt) {

        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
        });
    }
}
