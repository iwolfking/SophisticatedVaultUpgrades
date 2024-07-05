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
import net.p3pp3rf1y.sophisticatedbackpacks.SophisticatedBackpacks;
import net.p3pp3rf1y.sophisticatedcore.common.gui.UpgradeContainerRegistry;
import net.p3pp3rf1y.sophisticatedcore.common.gui.UpgradeContainerType;
import net.p3pp3rf1y.sophisticatedstorage.Config;
import net.p3pp3rf1y.sophisticatedstorage.SophisticatedStorage;
import xyz.iwolfking.sophisticatedvaultupgrades.upgrades.diffuser.DiffuserUpgradeContainer;
import xyz.iwolfking.sophisticatedvaultupgrades.upgrades.diffuser.DiffuserUpgradeItem;
import xyz.iwolfking.sophisticatedvaultupgrades.upgrades.diffuser.DiffuserUpgradeWrapper;
import xyz.iwolfking.sophisticatedvaultupgrades.upgrades.identify.IdentificationUpgradeItem;

import java.util.function.Supplier;

public class ModItemsSophisticatedST {
    private ModItemsSophisticatedST() {
    }

    public static final TagKey<Item> STORAGE_UPGRADE_TAG = TagKey.create(Registry.ITEM_REGISTRY, net.p3pp3rf1y.sophisticatedstorage.init.ModItems.STORAGE_UPGRADE_TAG_NAME);
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, "sophisticatedstorage");

    static Supplier<RegistryObject<DiffuserUpgradeItem>> DIFFUSER_UPGRADE;
    static Supplier<RegistryObject<DiffuserUpgradeItem>> ADVANCED_DIFFUSER_UPGRADE;


    public static void registerHandlers(IEventBus modBus) {
        RegistryObject<IdentificationUpgradeItem> IDENTIFICATION_UPGRADE = ITEMS.register("identification_upgrade",
                () -> new IdentificationUpgradeItem(SophisticatedStorage.CREATIVE_TAB));
        DIFFUSER_UPGRADE = () -> ITEMS.register("diffuser_upgrade",
                () -> new DiffuserUpgradeItem(SophisticatedStorage.CREATIVE_TAB, Config.SERVER.voidUpgrade));

        ADVANCED_DIFFUSER_UPGRADE = () -> ITEMS.register("diffuser_upgrade_advanced",
                () -> new DiffuserUpgradeItem(SophisticatedStorage.CREATIVE_TAB, net.p3pp3rf1y.sophisticatedstorage.Config.SERVER.advancedVoidUpgrade));
        ITEMS.register(modBus);
        modBus.addGenericListener(MenuType.class, ModItemsSophisticatedST::registerContainers);
    }


    public static void registerContainers(RegistryEvent.Register<MenuType<?>> evt) {
        UpgradeContainerType<DiffuserUpgradeWrapper, DiffuserUpgradeContainer> DIFFUSER_TYPE = new UpgradeContainerType<>(DiffuserUpgradeContainer::new);
        UpgradeContainerType<DiffuserUpgradeWrapper, DiffuserUpgradeContainer> ADVANCED_DIFFUSER_TYPE = new UpgradeContainerType<>(DiffuserUpgradeContainer::new);
        UpgradeContainerRegistry.register(DIFFUSER_UPGRADE.get().getId(), DIFFUSER_TYPE);
        UpgradeContainerRegistry.register(ADVANCED_DIFFUSER_UPGRADE.get().getId(), ADVANCED_DIFFUSER_TYPE);
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
        });
    }
}
