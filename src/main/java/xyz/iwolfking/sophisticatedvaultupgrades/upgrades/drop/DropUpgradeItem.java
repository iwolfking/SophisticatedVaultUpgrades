package xyz.iwolfking.sophisticatedvaultupgrades.upgrades.drop;

import net.minecraft.world.item.CreativeModeTab;
import net.p3pp3rf1y.sophisticatedcore.upgrades.UpgradeItemBase;
import net.p3pp3rf1y.sophisticatedcore.upgrades.UpgradeType;
import net.p3pp3rf1y.sophisticatedcore.upgrades.voiding.VoidUpgradeConfig;
import org.jetbrains.annotations.NotNull;

public class DropUpgradeItem extends UpgradeItemBase<DropUpgradeWrapper> {
    private final VoidUpgradeConfig voidUpgradeConfig;
    public static final UpgradeType<DropUpgradeWrapper> TYPE = new UpgradeType<>(DropUpgradeWrapper::new);


    public DropUpgradeItem(CreativeModeTab itemGroup, VoidUpgradeConfig voidUpgradeConfig) {
        super(itemGroup);
        this.voidUpgradeConfig = voidUpgradeConfig;
    }

    @Override
    public @NotNull UpgradeType<DropUpgradeWrapper> getType() {
        return TYPE;
    }


    public int getFilterSlotCount() {
        return voidUpgradeConfig.filterSlots.get();
    }

    public boolean isVoidAnythingEnabled() {
        return voidUpgradeConfig.voidAnythingEnabled.get();
    }
}
