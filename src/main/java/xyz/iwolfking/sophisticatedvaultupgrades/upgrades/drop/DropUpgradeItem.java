package xyz.iwolfking.sophisticatedvaultupgrades.upgrades.drop;

import net.minecraft.world.item.CreativeModeTab;
import net.p3pp3rf1y.sophisticatedcore.upgrades.UpgradeItemBase;
import net.p3pp3rf1y.sophisticatedcore.upgrades.UpgradeType;
import net.p3pp3rf1y.sophisticatedcore.upgrades.voiding.VoidUpgradeConfig;
import org.jetbrains.annotations.NotNull;

public class DropUpgradeItem extends UpgradeItemBase<DropUpgradeWrapper> {
    private final DropUpgradeConfig dropUpgradeConfig;
    public static final UpgradeType<DropUpgradeWrapper> TYPE = new UpgradeType<>(DropUpgradeWrapper::new);


    public DropUpgradeItem(CreativeModeTab itemGroup, DropUpgradeConfig dropUpgradeConfig) {
        super(itemGroup);
        this.dropUpgradeConfig = dropUpgradeConfig;
    }

    @Override
    public @NotNull UpgradeType<DropUpgradeWrapper> getType() {
        return TYPE;
    }


    public int getFilterSlotCount() {
        return dropUpgradeConfig.filterSlots.get();
    }

    public boolean isVoidAnythingEnabled() {
        return dropUpgradeConfig.voidAnythingEnabled.get();
    }
}
