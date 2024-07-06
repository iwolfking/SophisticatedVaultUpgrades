package xyz.iwolfking.sophisticatedvaultupgrades.upgrades.drop;

import net.minecraftforge.common.ForgeConfigSpec;
import net.p3pp3rf1y.sophisticatedcore.upgrades.FilteredUpgradeConfigBase;

public class DropUpgradeConfig extends FilteredUpgradeConfigBase {
    public final ForgeConfigSpec.BooleanValue voidAnythingEnabled;


    public DropUpgradeConfig(ForgeConfigSpec.Builder builder, String name, String path, int defaultFilterSlots, int defaultSlotsInRow) {
        super(builder, name, path, defaultFilterSlots, defaultSlotsInRow);
        voidAnythingEnabled = builder.comment("Determines whether drop upgrade allows dropping anything or it only has overflow option").define("dropAnythingEnabled", true);

        builder.pop();
    }
}