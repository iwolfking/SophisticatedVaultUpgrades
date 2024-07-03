package xyz.iwolfking.sophisticatedvaultupgrades.upgrades.diffuser;

import net.minecraftforge.common.ForgeConfigSpec;
import net.p3pp3rf1y.sophisticatedcore.upgrades.FilteredUpgradeConfigBase;

public class DiffuserUpgradeConfig extends FilteredUpgradeConfigBase {
    public final ForgeConfigSpec.BooleanValue voidAnythingEnabled;


    protected DiffuserUpgradeConfig(ForgeConfigSpec.Builder builder, String name, String path, int defaultFilterSlots, int defaultSlotsInRow) {
        super(builder, name, path, defaultFilterSlots, defaultSlotsInRow);
        voidAnythingEnabled = builder.comment("Determines whether diffuser upgrade allows diffusing anything or it only has overflow option").define("voidAnythingEnabled", true);

        builder.pop();
    }
}