package xyz.iwolfking.sophisticatedvaultupgrades.upgrades.recycler;

import net.minecraft.world.item.CreativeModeTab;
import net.p3pp3rf1y.sophisticatedcore.upgrades.IUpgradeCountLimitConfig;
import net.p3pp3rf1y.sophisticatedcore.upgrades.UpgradeItemBase;
import net.p3pp3rf1y.sophisticatedcore.upgrades.UpgradeType;
import net.p3pp3rf1y.sophisticatedcore.upgrades.voiding.VoidUpgradeConfig;
import org.jetbrains.annotations.NotNull;

public class RecyclerUpgradeItem extends UpgradeItemBase<RecyclerUpgradeWrapper> {
    private final RecyclerUpgradeConfig recyclerUpgradeConfig;
    public static final UpgradeType<RecyclerUpgradeWrapper> TYPE = new UpgradeType<>(RecyclerUpgradeWrapper::new);


    public RecyclerUpgradeItem(CreativeModeTab itemGroup, RecyclerUpgradeConfig recyclerUpgradeConfig, IUpgradeCountLimitConfig upgradeCountLimitConfig) {
        super(itemGroup, upgradeCountLimitConfig);
        this.recyclerUpgradeConfig = recyclerUpgradeConfig;
    }

    @Override
    public @NotNull UpgradeType<RecyclerUpgradeWrapper> getType() {
        return TYPE;
    }


    public int getFilterSlotCount() {
        return recyclerUpgradeConfig.filterSlots.get();
    }

}
