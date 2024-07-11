package xyz.iwolfking.sophisticatedvaultupgrades.upgrades.identify;

import net.minecraftforge.common.ForgeConfigSpec;
import net.p3pp3rf1y.sophisticatedcore.upgrades.FilteredUpgradeConfigBase;

public class IdentificationUpgradeConfig extends FilteredUpgradeConfigBase {


    public IdentificationUpgradeConfig(ForgeConfigSpec.Builder builder, String name, String path, int defaultFilterSlots, int defaultSlotsInRow) {
        super(builder, name, path, defaultFilterSlots, defaultSlotsInRow);

        builder.pop();
    }
}