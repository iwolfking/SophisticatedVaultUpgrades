package xyz.iwolfking.sophisticatedvaultupgrades.upgrades.diffuser;

import net.minecraft.world.item.CreativeModeTab;
import net.p3pp3rf1y.sophisticatedcore.upgrades.UpgradeItemBase;
import net.p3pp3rf1y.sophisticatedcore.upgrades.UpgradeType;
import net.p3pp3rf1y.sophisticatedcore.upgrades.voiding.VoidUpgradeConfig;
import org.jetbrains.annotations.NotNull;

public class DiffuserUpgradeItem extends UpgradeItemBase<DiffuserUpgradeWrapper> {
    private final VoidUpgradeConfig voidUpgradeConfig;
    public static final UpgradeType<DiffuserUpgradeWrapper> TYPE = new UpgradeType<>(DiffuserUpgradeWrapper::new);


    public DiffuserUpgradeItem(CreativeModeTab itemGroup, VoidUpgradeConfig voidUpgradeConfig) {
        super(itemGroup);
        this.voidUpgradeConfig = voidUpgradeConfig;
    }

    @Override
    public @NotNull UpgradeType<DiffuserUpgradeWrapper> getType() {
        return TYPE;
    }


    public int getFilterSlotCount() {
        return voidUpgradeConfig.filterSlots.get();
    }

    public boolean isVoidAnythingEnabled() {
        return voidUpgradeConfig.voidAnythingEnabled.get();
    }
}
