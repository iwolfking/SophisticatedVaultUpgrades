package xyz.iwolfking.sophisticatedvaultupgrades.init;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
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
import net.p3pp3rf1y.sophisticatedbackpacks.Config;
import net.p3pp3rf1y.sophisticatedbackpacks.SophisticatedBackpacks;
import net.p3pp3rf1y.sophisticatedbackpacks.init.ModItems;
import net.p3pp3rf1y.sophisticatedcore.client.gui.StorageScreenBase;
import net.p3pp3rf1y.sophisticatedcore.client.gui.UpgradeGuiManager;
import net.p3pp3rf1y.sophisticatedcore.client.gui.utils.Position;
import net.p3pp3rf1y.sophisticatedcore.common.gui.UpgradeContainerRegistry;
import net.p3pp3rf1y.sophisticatedcore.common.gui.UpgradeContainerType;
import net.p3pp3rf1y.sophisticatedcore.upgrades.voiding.VoidUpgradeContainer;
import net.p3pp3rf1y.sophisticatedcore.upgrades.voiding.VoidUpgradeTab;
import net.p3pp3rf1y.sophisticatedcore.upgrades.voiding.VoidUpgradeWrapper;
import net.p3pp3rf1y.sophisticatedstorage.SophisticatedStorage;
import xyz.iwolfking.sophisticatedvaultupgrades.upgrades.activation.ActivationUpgradeItem;
import xyz.iwolfking.sophisticatedvaultupgrades.upgrades.diffuser.DiffuserUpgradeContainer;
import xyz.iwolfking.sophisticatedvaultupgrades.upgrades.diffuser.DiffuserUpgradeItem;
import xyz.iwolfking.sophisticatedvaultupgrades.upgrades.diffuser.DiffuserUpgradeTab;
import xyz.iwolfking.sophisticatedvaultupgrades.upgrades.diffuser.DiffuserUpgradeWrapper;
import xyz.iwolfking.sophisticatedvaultupgrades.upgrades.identify.IdentificationUpgradeItem;

public class ModItemsSophisticatedBP {
    private ModItemsSophisticatedBP() {
    }

    public static final TagKey<Item> STORAGE_UPGRADE_TAG = TagKey.create(Registry.ITEM_REGISTRY, ModItems.BACKPACK_UPGRADE_TAG_NAME);
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, SophisticatedBackpacks.MOD_ID);
    public static final RegistryObject<IdentificationUpgradeItem> IDENTIFICATION_UPGRADE = ITEMS.register("identification_upgrade",
            () -> new IdentificationUpgradeItem(SophisticatedBackpacks.ITEM_GROUP));
    public static final RegistryObject<ActivationUpgradeItem> ACTIVATION_UPGRADE = ITEMS.register("activator_upgrade",
            () -> new ActivationUpgradeItem(SophisticatedBackpacks.ITEM_GROUP));

    public static final RegistryObject<DiffuserUpgradeItem> DIFFUSER_UPGRADE = ITEMS.register("diffuser_upgrade",
            () -> new DiffuserUpgradeItem(SophisticatedBackpacks.ITEM_GROUP, Config.SERVER.voidUpgrade));

    public static final RegistryObject<DiffuserUpgradeItem> ADVANCED_DIFFUSER_UPGRADE = ITEMS.register("diffuser_upgrade_advanced",
            () -> new DiffuserUpgradeItem(SophisticatedBackpacks.ITEM_GROUP, net.p3pp3rf1y.sophisticatedbackpacks.Config.SERVER.advancedVoidUpgrade));
    public static void registerHandlers(IEventBus modBus) {
        ITEMS.register(modBus);
        modBus.addGenericListener(MenuType.class, ModItemsSophisticatedBP::registerContainers);
    }
    private static final UpgradeContainerType<DiffuserUpgradeWrapper, DiffuserUpgradeContainer> DIFFUSER_TYPE = new UpgradeContainerType<>(DiffuserUpgradeContainer::new);
    private static final UpgradeContainerType<DiffuserUpgradeWrapper, DiffuserUpgradeContainer> ADVANCED_DIFFUSER_TYPE = new UpgradeContainerType<>(DiffuserUpgradeContainer::new);
    public static void registerContainers(RegistryEvent.Register<MenuType<?>> evt) {
        UpgradeContainerRegistry.register(DIFFUSER_UPGRADE.getId(), DIFFUSER_TYPE);
        UpgradeContainerRegistry.register(ADVANCED_DIFFUSER_UPGRADE.getId(), ADVANCED_DIFFUSER_TYPE);
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
            UpgradeGuiManager.registerTab(DIFFUSER_TYPE, (DiffuserUpgradeContainer uc, Position p, StorageScreenBase<?> s) ->
                    new DiffuserUpgradeTab.Basic(uc, p, s, net.p3pp3rf1y.sophisticatedbackpacks.Config.SERVER.voidUpgrade.slotsInRow.get()));
            UpgradeGuiManager.registerTab(ADVANCED_DIFFUSER_TYPE, (DiffuserUpgradeContainer uc, Position p, StorageScreenBase<?> s) ->
                    new DiffuserUpgradeTab.Advanced(uc, p, s, net.p3pp3rf1y.sophisticatedbackpacks.Config.SERVER.advancedVoidUpgrade.slotsInRow.get()));
        });
    }
}
