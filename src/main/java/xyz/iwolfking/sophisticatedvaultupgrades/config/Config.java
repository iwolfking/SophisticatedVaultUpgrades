package xyz.iwolfking.sophisticatedvaultupgrades.config;

import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.tuple.Pair;
import xyz.iwolfking.sophisticatedvaultupgrades.upgrades.diffuser.DiffuserUpgradeConfig;
import xyz.iwolfking.sophisticatedvaultupgrades.upgrades.drop.DropUpgradeConfig;
import xyz.iwolfking.sophisticatedvaultupgrades.upgrades.identify.IdentificationUpgradeConfig;
import xyz.iwolfking.sophisticatedvaultupgrades.upgrades.recycler.RecyclerUpgradeConfig;

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
        public final ForgeConfigSpec.ConfigValue<Integer> debagnetizerRange;
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
            debagnetizerRange = builder.comment("Range that a Debagnetizer will effect pickup upgrades.").define("Debagnetizer Range", 32);
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
