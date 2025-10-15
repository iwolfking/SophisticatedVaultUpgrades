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
import net.p3pp3rf1y.sophisticatedcore.client.gui.StorageScreenBase;
import net.p3pp3rf1y.sophisticatedcore.client.gui.UpgradeGuiManager;
import net.p3pp3rf1y.sophisticatedcore.client.gui.utils.Position;
import net.p3pp3rf1y.sophisticatedcore.common.gui.UpgradeContainerRegistry;
import net.p3pp3rf1y.sophisticatedcore.common.gui.UpgradeContainerType;
import net.p3pp3rf1y.sophisticatedcore.upgrades.tank.TankUpgradeContainer;
import net.p3pp3rf1y.sophisticatedcore.upgrades.tank.TankUpgradeItem;
import net.p3pp3rf1y.sophisticatedcore.upgrades.tank.TankUpgradeTab;
import net.p3pp3rf1y.sophisticatedcore.upgrades.tank.TankUpgradeWrapper;
import net.p3pp3rf1y.sophisticatedstorage.SophisticatedStorage;
import xyz.iwolfking.sophisticatedvaultupgrades.config.Config;
import xyz.iwolfking.sophisticatedvaultupgrades.upgrades.diffuser.DiffuserUpgradeContainer;
import xyz.iwolfking.sophisticatedvaultupgrades.upgrades.diffuser.DiffuserUpgradeItem;
import xyz.iwolfking.sophisticatedvaultupgrades.upgrades.diffuser.DiffuserUpgradeTab;
import xyz.iwolfking.sophisticatedvaultupgrades.upgrades.diffuser.DiffuserUpgradeWrapper;
import xyz.iwolfking.sophisticatedvaultupgrades.upgrades.drop.DropUpgradeContainer;
import xyz.iwolfking.sophisticatedvaultupgrades.upgrades.drop.DropUpgradeItem;
import xyz.iwolfking.sophisticatedvaultupgrades.upgrades.drop.DropUpgradeTab;
import xyz.iwolfking.sophisticatedvaultupgrades.upgrades.drop.DropUpgradeWrapper;
import xyz.iwolfking.sophisticatedvaultupgrades.upgrades.identify.IdentificationUpgradeContainer;
import xyz.iwolfking.sophisticatedvaultupgrades.upgrades.identify.IdentificationUpgradeItem;
import xyz.iwolfking.sophisticatedvaultupgrades.upgrades.identify.IdentificationUpgradeTab;
import xyz.iwolfking.sophisticatedvaultupgrades.upgrades.identify.IdentificationUpgradeWrapper;
import xyz.iwolfking.sophisticatedvaultupgrades.upgrades.opener.OpenerUpgradeContainer;
import xyz.iwolfking.sophisticatedvaultupgrades.upgrades.opener.OpenerUpgradeItem;
import xyz.iwolfking.sophisticatedvaultupgrades.upgrades.opener.OpenerUpgradeTab;
import xyz.iwolfking.sophisticatedvaultupgrades.upgrades.opener.OpenerUpgradeWrapper;
import xyz.iwolfking.sophisticatedvaultupgrades.upgrades.recycler.RecyclerUpgradeContainer;
import xyz.iwolfking.sophisticatedvaultupgrades.upgrades.recycler.RecyclerUpgradeItem;
import xyz.iwolfking.sophisticatedvaultupgrades.upgrades.recycler.RecyclerUpgradeTab;
import xyz.iwolfking.sophisticatedvaultupgrades.upgrades.recycler.RecyclerUpgradeWrapper;

import java.util.function.Supplier;

public class ModItemsSophisticatedST {
    private ModItemsSophisticatedST() {
    }

    public static final TagKey<Item> STORAGE_UPGRADE_TAG = TagKey.create(Registry.ITEM_REGISTRY, net.p3pp3rf1y.sophisticatedstorage.init.ModItems.STORAGE_UPGRADE_TAG_NAME);
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, "sophisticatedstorage");

    static RegistryObject<DiffuserUpgradeItem> DIFFUSER_UPGRADE;
    static RegistryObject<IdentificationUpgradeItem> IDENTIFICATION_UPGRADE;
    static RegistryObject<DiffuserUpgradeItem> ADVANCED_DIFFUSER_UPGRADE;
    static RegistryObject<RecyclerUpgradeItem> RECYCLER_UPGRADE;
    static RegistryObject<RecyclerUpgradeItem> ADVANCED_RECYCLER_UPGRADE;
    static RegistryObject<DropUpgradeItem> DROP_UPGRADE;
    static RegistryObject<DropUpgradeItem> ADVANCED_DROP_UPGRADE;
    static RegistryObject<OpenerUpgradeItem> OPENER_UPGRADE;


    public static void registerHandlers(IEventBus modBus) {
        IDENTIFICATION_UPGRADE = ITEMS.register("identification_upgrade",
                () -> new IdentificationUpgradeItem(SophisticatedStorage.CREATIVE_TAB, xyz.iwolfking.sophisticatedvaultupgrades.config.Config.SERVER.identificationUpgradeConfig,net.p3pp3rf1y.sophisticatedstorage.Config.SERVER.maxUpgradesPerStorage));
        DIFFUSER_UPGRADE = ITEMS.register("diffuser_upgrade",
                () -> new DiffuserUpgradeItem(SophisticatedStorage.CREATIVE_TAB, xyz.iwolfking.sophisticatedvaultupgrades.config.Config.SERVER.diffuserUpgrade,net.p3pp3rf1y.sophisticatedstorage.Config.SERVER.maxUpgradesPerStorage));

        ADVANCED_DIFFUSER_UPGRADE = ITEMS.register("diffuser_upgrade_advanced",
                () -> new DiffuserUpgradeItem(SophisticatedStorage.CREATIVE_TAB, xyz.iwolfking.sophisticatedvaultupgrades.config.Config.SERVER.advancedDiffuserUpgradeConfig,net.p3pp3rf1y.sophisticatedstorage.Config.SERVER.maxUpgradesPerStorage));
        RECYCLER_UPGRADE = ITEMS.register("recycler_upgrade",
                () -> new RecyclerUpgradeItem(SophisticatedStorage.CREATIVE_TAB, xyz.iwolfking.sophisticatedvaultupgrades.config.Config.SERVER.recyclerUpgradeConfig,net.p3pp3rf1y.sophisticatedstorage.Config.SERVER.maxUpgradesPerStorage));
        ADVANCED_RECYCLER_UPGRADE = ITEMS.register("recycler_upgrade_advanced",
                () -> new RecyclerUpgradeItem(SophisticatedStorage.CREATIVE_TAB, xyz.iwolfking.sophisticatedvaultupgrades.config.Config.SERVER.advancedRecyclerUpgradeConfig,net.p3pp3rf1y.sophisticatedstorage.Config.SERVER.maxUpgradesPerStorage));
        DROP_UPGRADE = ITEMS.register("drop_upgrade",
                () -> new DropUpgradeItem(SophisticatedStorage.CREATIVE_TAB, xyz.iwolfking.sophisticatedvaultupgrades.config.Config.SERVER.dropUpgradeConfig,net.p3pp3rf1y.sophisticatedstorage.Config.SERVER.maxUpgradesPerStorage));
        ADVANCED_DROP_UPGRADE = ITEMS.register("drop_upgrade_advanced",
                () -> new DropUpgradeItem(SophisticatedStorage.CREATIVE_TAB, xyz.iwolfking.sophisticatedvaultupgrades.config.Config.SERVER.advancedDropUpgradeConfig,net.p3pp3rf1y.sophisticatedstorage.Config.SERVER.maxUpgradesPerStorage));
        OPENER_UPGRADE = ITEMS.register("opener_upgrade",
                () -> new OpenerUpgradeItem(SophisticatedStorage.CREATIVE_TAB, Config.SERVER.openerUpgradeConfig,net.p3pp3rf1y.sophisticatedstorage.Config.SERVER.maxUpgradesPerStorage));
        ITEMS.register(modBus);
        modBus.addGenericListener(MenuType.class, ModItemsSophisticatedST::registerContainers);
    }


    public static void registerContainers(RegistryEvent.Register<MenuType<?>> evt) {
        UpgradeContainerType<DiffuserUpgradeWrapper, DiffuserUpgradeContainer> DIFFUSER_TYPE = new UpgradeContainerType<>(DiffuserUpgradeContainer::new);
        UpgradeContainerType<DiffuserUpgradeWrapper, DiffuserUpgradeContainer> ADVANCED_DIFFUSER_TYPE = new UpgradeContainerType<>(DiffuserUpgradeContainer::new);
        UpgradeContainerType<RecyclerUpgradeWrapper, RecyclerUpgradeContainer> RECYCLER_TYPE = new UpgradeContainerType<>(RecyclerUpgradeContainer::new);
        UpgradeContainerType<RecyclerUpgradeWrapper, RecyclerUpgradeContainer> ADVANCED_RECYCLER_TYPE = new UpgradeContainerType<>(RecyclerUpgradeContainer::new);
        UpgradeContainerType<DropUpgradeWrapper, DropUpgradeContainer> DROP_TYPE = new UpgradeContainerType<>(DropUpgradeContainer::new);
        UpgradeContainerType<DropUpgradeWrapper, DropUpgradeContainer> ADVANCED_DROP_TYPE = new UpgradeContainerType<>(DropUpgradeContainer::new);
        UpgradeContainerType<IdentificationUpgradeWrapper, IdentificationUpgradeContainer> IDENTIFICATION_TYPE = new UpgradeContainerType<>(IdentificationUpgradeContainer::new);
        UpgradeContainerType<OpenerUpgradeWrapper, OpenerUpgradeContainer> OPENER_TYPE = new UpgradeContainerType<>(OpenerUpgradeContainer::new);
        UpgradeContainerRegistry.register(DIFFUSER_UPGRADE.getId(), DIFFUSER_TYPE);
        UpgradeContainerRegistry.register(ADVANCED_DIFFUSER_UPGRADE.getId(), ADVANCED_DIFFUSER_TYPE);
        UpgradeContainerRegistry.register(RECYCLER_UPGRADE.getId(), RECYCLER_TYPE);
        UpgradeContainerRegistry.register(ADVANCED_RECYCLER_UPGRADE.getId(), ADVANCED_RECYCLER_TYPE);
        UpgradeContainerRegistry.register(DROP_UPGRADE.getId(), DROP_TYPE);
        UpgradeContainerRegistry.register(ADVANCED_DROP_UPGRADE.getId(), ADVANCED_DROP_TYPE);
        UpgradeContainerRegistry.register(IDENTIFICATION_UPGRADE.getId(), IDENTIFICATION_TYPE);
        UpgradeContainerRegistry.register(OPENER_UPGRADE.getId(), OPENER_TYPE);
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
            UpgradeGuiManager.registerTab(DIFFUSER_TYPE, (DiffuserUpgradeContainer uc, Position p, StorageScreenBase<?> s) ->
                    new DiffuserUpgradeTab.Basic(uc, p, s, Config.SERVER.diffuserUpgrade.slotsInRow.get()));
            UpgradeGuiManager.registerTab(ADVANCED_DIFFUSER_TYPE, (DiffuserUpgradeContainer uc, Position p, StorageScreenBase<?> s) ->
                    new DiffuserUpgradeTab.Advanced(uc, p, s, xyz.iwolfking.sophisticatedvaultupgrades.config.Config.SERVER.advancedDiffuserUpgradeConfig.slotsInRow.get()));
            UpgradeGuiManager.registerTab(RECYCLER_TYPE, (RecyclerUpgradeContainer uc, Position p, StorageScreenBase<?> s) ->
                    new RecyclerUpgradeTab.Basic(uc, p, s, xyz.iwolfking.sophisticatedvaultupgrades.config.Config.SERVER.recyclerUpgradeConfig.slotsInRow.get()));
            UpgradeGuiManager.registerTab(ADVANCED_RECYCLER_TYPE, (RecyclerUpgradeContainer uc, Position p, StorageScreenBase<?> s) ->
                    new RecyclerUpgradeTab.Advanced(uc, p, s, xyz.iwolfking.sophisticatedvaultupgrades.config.Config.SERVER.advancedRecyclerUpgradeConfig.slotsInRow.get()));
            UpgradeGuiManager.registerTab(DROP_TYPE, (DropUpgradeContainer uc, Position p, StorageScreenBase<?> s) ->
                    new DropUpgradeTab.Basic(uc, p, s, xyz.iwolfking.sophisticatedvaultupgrades.config.Config.SERVER.dropUpgradeConfig.slotsInRow.get()));
            UpgradeGuiManager.registerTab(ADVANCED_DROP_TYPE, (DropUpgradeContainer uc, Position p, StorageScreenBase<?> s) ->
                    new DropUpgradeTab.Advanced(uc, p, s, xyz.iwolfking.sophisticatedvaultupgrades.config.Config.SERVER.advancedDropUpgradeConfig.slotsInRow.get()));
            UpgradeGuiManager.registerTab(IDENTIFICATION_TYPE, (IdentificationUpgradeContainer uc, Position p, StorageScreenBase<?> s) ->
                    new IdentificationUpgradeTab.Advanced(uc, p, s, xyz.iwolfking.sophisticatedvaultupgrades.config.Config.SERVER.identificationUpgradeConfig.slotsInRow.get()));
            UpgradeGuiManager.registerTab(OPENER_TYPE, (OpenerUpgradeContainer uc, Position p, StorageScreenBase<?> s) ->
                    new OpenerUpgradeTab.Basic(uc, p, s, Config.SERVER.openerUpgradeConfig.slotsInRow.get()));
        });
    }
}
