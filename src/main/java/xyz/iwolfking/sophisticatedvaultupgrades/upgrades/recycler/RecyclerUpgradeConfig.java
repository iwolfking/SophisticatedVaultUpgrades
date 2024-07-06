package xyz.iwolfking.sophisticatedvaultupgrades.upgrades.recycler;

import net.minecraftforge.common.ForgeConfigSpec;
import net.p3pp3rf1y.sophisticatedcore.upgrades.FilteredUpgradeConfigBase;

public class RecyclerUpgradeConfig extends FilteredUpgradeConfigBase {
    public final ForgeConfigSpec.BooleanValue voidAnythingEnabled;


    public RecyclerUpgradeConfig(ForgeConfigSpec.Builder builder, String name, String path, int defaultFilterSlots, int defaultSlotsInRow) {
        super(builder, name, path, defaultFilterSlots, defaultSlotsInRow);
        voidAnythingEnabled = builder.comment("Determines whether recycler upgrade allows recycling anything or it only has overflow option").define("recycleAnythingEnabled", true);

        builder.pop();
    }
}