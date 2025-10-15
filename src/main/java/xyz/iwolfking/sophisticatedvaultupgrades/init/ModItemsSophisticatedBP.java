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
import xyz.iwolfking.sophisticatedvaultupgrades.config.Config;
import xyz.iwolfking.sophisticatedvaultupgrades.upgrades.activation.ActivationUpgradeItem;
import xyz.iwolfking.sophisticatedvaultupgrades.upgrades.diffuser.DiffuserUpgradeContainer;
import xyz.iwolfking.sophisticatedvaultupgrades.upgrades.diffuser.DiffuserUpgradeItem;
import xyz.iwolfking.sophisticatedvaultupgrades.upgrades.diffuser.DiffuserUpgradeTab;
import xyz.iwolfking.sophisticatedvaultupgrades.upgrades.diffuser.DiffuserUpgradeWrapper;
import xyz.iwolfking.sophisticatedvaultupgrades.upgrades.drop.DropUpgradeContainer;
import xyz.iwolfking.sophisticatedvaultupgrades.upgrades.drop.DropUpgradeItem;
import xyz.iwolfking.sophisticatedvaultupgrades.upgrades.drop.DropUpgradeTab;
import xyz.iwolfking.sophisticatedvaultupgrades.upgrades.drop.DropUpgradeWrapper;
import xyz.iwolfking.sophisticatedvaultupgrades.upgrades.identify.*;
import xyz.iwolfking.sophisticatedvaultupgrades.upgrades.opener.OpenerUpgradeContainer;
import xyz.iwolfking.sophisticatedvaultupgrades.upgrades.opener.OpenerUpgradeItem;
import xyz.iwolfking.sophisticatedvaultupgrades.upgrades.opener.OpenerUpgradeTab;
import xyz.iwolfking.sophisticatedvaultupgrades.upgrades.opener.OpenerUpgradeWrapper;
import xyz.iwolfking.sophisticatedvaultupgrades.upgrades.recycler.RecyclerUpgradeContainer;
import xyz.iwolfking.sophisticatedvaultupgrades.upgrades.recycler.RecyclerUpgradeItem;
import xyz.iwolfking.sophisticatedvaultupgrades.upgrades.recycler.RecyclerUpgradeTab;
import xyz.iwolfking.sophisticatedvaultupgrades.upgrades.recycler.RecyclerUpgradeWrapper;

public class ModItemsSophisticatedBP {
    private ModItemsSophisticatedBP() {
    }

    public static final TagKey<Item> STORAGE_UPGRADE_TAG = TagKey.create(Registry.ITEM_REGISTRY, ModItems.BACKPACK_UPGRADE_TAG_NAME);
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, SophisticatedBackpacks.MOD_ID);
    public static final RegistryObject<IdentificationUpgradeItem> IDENTIFICATION_UPGRADE = ITEMS.register("identification_upgrade",
            () -> new IdentificationUpgradeItem(SophisticatedBackpacks.ITEM_GROUP, xyz.iwolfking.sophisticatedvaultupgrades.config.Config.SERVER.identificationUpgradeConfig));

    public static final RegistryObject<IdentificationUpgradeItem> ADVANCED_IDENTIFICATION_UPGRADE = ITEMS.register("identification_upgrade_advanced",
            () -> new IdentificationUpgradeItem(SophisticatedBackpacks.ITEM_GROUP, xyz.iwolfking.sophisticatedvaultupgrades.config.Config.SERVER.advancedIdentificationUpgradeConfig));
    public static final RegistryObject<ActivationUpgradeItem> ACTIVATION_UPGRADE = ITEMS.register("activator_upgrade",
            () -> new ActivationUpgradeItem(SophisticatedBackpacks.ITEM_GROUP));

    public static final RegistryObject<DiffuserUpgradeItem> DIFFUSER_UPGRADE = ITEMS.register("diffuser_upgrade",
            () -> new DiffuserUpgradeItem(SophisticatedBackpacks.ITEM_GROUP, xyz.iwolfking.sophisticatedvaultupgrades.config.Config.SERVER.diffuserUpgrade));

    public static final RegistryObject<DiffuserUpgradeItem> ADVANCED_DIFFUSER_UPGRADE = ITEMS.register("diffuser_upgrade_advanced",
            () -> new DiffuserUpgradeItem(SophisticatedBackpacks.ITEM_GROUP, xyz.iwolfking.sophisticatedvaultupgrades.config.Config.SERVER.advancedDiffuserUpgradeConfig));

    public static final RegistryObject<RecyclerUpgradeItem> RECYCLER_UPGRADE = ITEMS.register("recycler_upgrade",
            () -> new RecyclerUpgradeItem(SophisticatedBackpacks.ITEM_GROUP, xyz.iwolfking.sophisticatedvaultupgrades.config.Config.SERVER.recyclerUpgradeConfig));

    public static final RegistryObject<RecyclerUpgradeItem> ADVANCED_RECYCLER_UPGRADE = ITEMS.register("recycler_upgrade_advanced",
            () -> new RecyclerUpgradeItem(SophisticatedBackpacks.ITEM_GROUP, xyz.iwolfking.sophisticatedvaultupgrades.config.Config.SERVER.advancedRecyclerUpgradeConfig));

    public static final RegistryObject<DropUpgradeItem> DROP_UPGRADE = ITEMS.register("drop_upgrade",
            () -> new DropUpgradeItem(SophisticatedBackpacks.ITEM_GROUP, xyz.iwolfking.sophisticatedvaultupgrades.config.Config.SERVER.dropUpgradeConfig));

    public static final RegistryObject<DropUpgradeItem> ADVANCED_DROP_UPGRADE = ITEMS.register("drop_upgrade_advanced",
            () -> new DropUpgradeItem(SophisticatedBackpacks.ITEM_GROUP, xyz.iwolfking.sophisticatedvaultupgrades.config.Config.SERVER.advancedDropUpgradeConfig));

    public static final RegistryObject<OpenerUpgradeItem> OPENER_UPGRADE = ITEMS.register("opener_upgrade",
            () -> new OpenerUpgradeItem(SophisticatedBackpacks.ITEM_GROUP, Config.SERVER.openerUpgradeConfig));

    public static void registerHandlers(IEventBus modBus) {
        ITEMS.register(modBus);
        modBus.addGenericListener(MenuType.class, ModItemsSophisticatedBP::registerContainers);
    }

    private static final UpgradeContainerType<IdentificationUpgradeWrapper, IdentificationUpgradeContainer> IDENTIFICATION_TYPE = new UpgradeContainerType<>(IdentificationUpgradeContainer::new);
    private static final UpgradeContainerType<IdentificationUpgradeWrapper, IdentificationUpgradeContainer> ADVANCED_IDENTIFICATION_UPGRADE_TYPE = new UpgradeContainerType<>(IdentificationUpgradeContainer::new);
    private static final UpgradeContainerType<DiffuserUpgradeWrapper, DiffuserUpgradeContainer> DIFFUSER_TYPE = new UpgradeContainerType<>(DiffuserUpgradeContainer::new);
    private static final UpgradeContainerType<RecyclerUpgradeWrapper, RecyclerUpgradeContainer> RECYCLER_TYPE = new UpgradeContainerType<>(RecyclerUpgradeContainer::new);
    private static final UpgradeContainerType<DropUpgradeWrapper, DropUpgradeContainer> DROP_TYPE = new UpgradeContainerType<>(DropUpgradeContainer::new);
    private static final UpgradeContainerType<RecyclerUpgradeWrapper, RecyclerUpgradeContainer> ADVANCED_RECYCLER_TYPE = new UpgradeContainerType<>(RecyclerUpgradeContainer::new);
    private static final UpgradeContainerType<DropUpgradeWrapper, DropUpgradeContainer> ADVANCED_DROP_TYPE = new UpgradeContainerType<>(DropUpgradeContainer::new);
    private static final UpgradeContainerType<DiffuserUpgradeWrapper, DiffuserUpgradeContainer> ADVANCED_DIFFUSER_TYPE = new UpgradeContainerType<>(DiffuserUpgradeContainer::new);
    private static final UpgradeContainerType<OpenerUpgradeWrapper, OpenerUpgradeContainer> OPENER_TYPE = new UpgradeContainerType<>(OpenerUpgradeContainer::new);
    public static void registerContainers(RegistryEvent.Register<MenuType<?>> evt) {
        UpgradeContainerRegistry.register(DIFFUSER_UPGRADE.getId(), DIFFUSER_TYPE);
        UpgradeContainerRegistry.register(ADVANCED_IDENTIFICATION_UPGRADE.getId(), ADVANCED_IDENTIFICATION_UPGRADE_TYPE);
        UpgradeContainerRegistry.register(IDENTIFICATION_UPGRADE.getId(), IDENTIFICATION_TYPE);
        UpgradeContainerRegistry.register(RECYCLER_UPGRADE.getId(), RECYCLER_TYPE);
        UpgradeContainerRegistry.register(DROP_UPGRADE.getId(), DROP_TYPE);
        UpgradeContainerRegistry.register(ADVANCED_RECYCLER_UPGRADE.getId(), ADVANCED_RECYCLER_TYPE);
        UpgradeContainerRegistry.register(ADVANCED_DIFFUSER_UPGRADE.getId(), ADVANCED_DIFFUSER_TYPE);
        UpgradeContainerRegistry.register(ADVANCED_DROP_UPGRADE.getId(), ADVANCED_DROP_TYPE);
        UpgradeContainerRegistry.register(OPENER_UPGRADE.getId(), OPENER_TYPE);
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
            UpgradeGuiManager.registerTab(DIFFUSER_TYPE, (DiffuserUpgradeContainer uc, Position p, StorageScreenBase<?> s) ->
                    new DiffuserUpgradeTab.Basic(uc, p, s, xyz.iwolfking.sophisticatedvaultupgrades.config.Config.SERVER.diffuserUpgrade.slotsInRow.get()));
            UpgradeGuiManager.registerTab(ADVANCED_DIFFUSER_TYPE, (DiffuserUpgradeContainer uc, Position p, StorageScreenBase<?> s) ->
                    new DiffuserUpgradeTab.Advanced(uc, p, s, xyz.iwolfking.sophisticatedvaultupgrades.config.Config.SERVER.advancedDiffuserUpgradeConfig.slotsInRow.get()));
            UpgradeGuiManager.registerTab(RECYCLER_TYPE, (RecyclerUpgradeContainer uc, Position p, StorageScreenBase<?> s) ->
                    new RecyclerUpgradeTab.Basic(uc, p, s, xyz.iwolfking.sophisticatedvaultupgrades.config.Config.SERVER.recyclerUpgradeConfig.slotsInRow.get()));
            UpgradeGuiManager.registerTab(ADVANCED_RECYCLER_TYPE, (RecyclerUpgradeContainer uc, Position p, StorageScreenBase<?> s) ->
                    new RecyclerUpgradeTab.Advanced(uc, p, s, xyz.iwolfking.sophisticatedvaultupgrades.config.Config.SERVER.advancedRecyclerUpgradeConfig.slotsInRow.get()));
            UpgradeGuiManager.registerTab(DROP_TYPE, (DropUpgradeContainer uc, Position p, StorageScreenBase<?> s) ->
                    new DropUpgradeTab.Basic(uc, p, s, xyz.iwolfking.sophisticatedvaultupgrades.config.Config.SERVER.dropUpgradeConfig.slotsInRow.get()));
            UpgradeGuiManager.registerTab(ADVANCED_DROP_TYPE, (DropUpgradeContainer uc, Position p, StorageScreenBase<?> s) ->
                    new DropUpgradeTab.Advanced(uc, p, s, Config.SERVER.advancedDropUpgradeConfig.slotsInRow.get()));
            UpgradeGuiManager.registerTab(IDENTIFICATION_TYPE, (IdentificationUpgradeContainer uc, Position p, StorageScreenBase<?> s) ->
                    new IdentificationUpgradeTab.Basic(uc, p, s, xyz.iwolfking.sophisticatedvaultupgrades.config.Config.SERVER.identificationUpgradeConfig.slotsInRow.get()));
            UpgradeGuiManager.registerTab(ADVANCED_IDENTIFICATION_UPGRADE_TYPE, (IdentificationUpgradeContainer uc, Position p, StorageScreenBase<?> s) ->
                    new IdentificationUpgradeTab.Advanced(uc, p, s, xyz.iwolfking.sophisticatedvaultupgrades.config.Config.SERVER.advancedIdentificationUpgradeConfig.slotsInRow.get()));
            UpgradeGuiManager.registerTab(OPENER_TYPE, (OpenerUpgradeContainer uc, Position p, StorageScreenBase<?> s) ->
                    new OpenerUpgradeTab.Basic(uc, p, s, Config.SERVER.dropUpgradeConfig.slotsInRow.get()));
        });
    }
}
