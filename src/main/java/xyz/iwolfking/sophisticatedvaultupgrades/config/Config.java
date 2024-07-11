package xyz.iwolfking.sophisticatedvaultupgrades.config;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.registries.ForgeRegistries;
import net.p3pp3rf1y.sophisticatedbackpacks.backpack.BackpackItem;
import net.p3pp3rf1y.sophisticatedcore.upgrades.FilteredUpgradeConfig;
import net.p3pp3rf1y.sophisticatedcore.upgrades.battery.BatteryUpgradeConfig;
import net.p3pp3rf1y.sophisticatedcore.upgrades.cooking.AutoCookingUpgradeConfig;
import net.p3pp3rf1y.sophisticatedcore.upgrades.cooking.CookingUpgradeConfig;
import net.p3pp3rf1y.sophisticatedcore.upgrades.magnet.MagnetUpgradeConfig;
import net.p3pp3rf1y.sophisticatedcore.upgrades.pump.PumpUpgradeConfig;
import net.p3pp3rf1y.sophisticatedcore.upgrades.stack.StackUpgradeConfig;
import net.p3pp3rf1y.sophisticatedcore.upgrades.tank.TankUpgradeConfig;
import net.p3pp3rf1y.sophisticatedcore.upgrades.voiding.VoidUpgradeConfig;
import net.p3pp3rf1y.sophisticatedcore.upgrades.xppump.XpPumpUpgradeConfig;
import org.apache.commons.lang3.tuple.Pair;
import xyz.iwolfking.sophisticatedvaultupgrades.upgrades.diffuser.DiffuserUpgradeConfig;
import xyz.iwolfking.sophisticatedvaultupgrades.upgrades.drop.DropUpgradeConfig;
import xyz.iwolfking.sophisticatedvaultupgrades.upgrades.identify.IdentificationUpgradeConfig;
import xyz.iwolfking.sophisticatedvaultupgrades.upgrades.recycler.RecyclerUpgradeConfig;

import java.util.*;
import java.util.stream.Collectors;

public class Config {


    private Config() {
    }

    public static final Server SERVER;
    public static final ForgeConfigSpec SERVER_SPEC;


    static {
        final Pair<Server, ForgeConfigSpec> serverSpec = new ForgeConfigSpec.Builder().configure(Server::new);
        SERVER_SPEC = serverSpec.getRight();
        SERVER = serverSpec.getLeft();

    }

    public static class Server {
        public final DiffuserUpgradeConfig diffuserUpgrade;
        public final DiffuserUpgradeConfig advancedDiffuserUpgradeConfig;
        public final RecyclerUpgradeConfig recyclerUpgradeConfig;
        public final RecyclerUpgradeConfig advancedRecyclerUpgradeConfig;
        public final DropUpgradeConfig dropUpgradeConfig;
        public final DropUpgradeConfig advancedDropUpgradeConfig;
        public final IdentificationUpgradeConfig advancedIdentificationUpgradeConfig;
        public final IdentificationUpgradeConfig identificationUpgradeConfig;


        Server(ForgeConfigSpec.Builder builder) {
            builder.comment("Server Settings").push("server");
            diffuserUpgrade = new DiffuserUpgradeConfig(builder, "Diffuser Upgrade", "diffuserUpgrade", 9, 3);
            advancedDiffuserUpgradeConfig = new DiffuserUpgradeConfig(builder, "Advanced Diffuser Upgrade", "advancedDiffuserUpgrade", 16, 4);
            recyclerUpgradeConfig = new RecyclerUpgradeConfig(builder, "Recycler Upgrade", "recyclerUpgrade", 9, 3);
            advancedRecyclerUpgradeConfig = new RecyclerUpgradeConfig(builder, "Advanced Recycler Upgrade", "advancedRecyclerUpgrade", 16, 4);
            dropUpgradeConfig = new DropUpgradeConfig(builder, "Drop Upgrade", "dropUpgrade", 9, 3);
            advancedDropUpgradeConfig = new DropUpgradeConfig(builder, "Advanced Drop Upgrade", "advancedDropUpgrade", 16, 4);
            identificationUpgradeConfig = new IdentificationUpgradeConfig(builder, "Identification Upgrade", "IdentificationUpgrade", 9, 4);
            advancedIdentificationUpgradeConfig = new IdentificationUpgradeConfig(builder, "Advanced Identification Upgrade", "advancedIdentificationUpgrade", 16, 4);
            builder.pop();
        }
    }
}
