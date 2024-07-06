package xyz.iwolfking.sophisticatedvaultupgrades.upgrades.diffuser;

import net.minecraft.world.item.CreativeModeTab;
import net.p3pp3rf1y.sophisticatedcore.upgrades.UpgradeItemBase;
import net.p3pp3rf1y.sophisticatedcore.upgrades.UpgradeType;
import net.p3pp3rf1y.sophisticatedcore.upgrades.voiding.VoidUpgradeConfig;
import org.jetbrains.annotations.NotNull;

public class DiffuserUpgradeItem extends UpgradeItemBase<DiffuserUpgradeWrapper> {
    private final DiffuserUpgradeConfig diffuserUpgradeConfig;
    public static final UpgradeType<DiffuserUpgradeWrapper> TYPE = new UpgradeType<>(DiffuserUpgradeWrapper::new);


    public DiffuserUpgradeItem(CreativeModeTab itemGroup, DiffuserUpgradeConfig diffuserUpgradeConfig) {
        super(itemGroup);
        this.diffuserUpgradeConfig = diffuserUpgradeConfig;
    }

    @Override
    public @NotNull UpgradeType<DiffuserUpgradeWrapper> getType() {
        return TYPE;
    }


    public int getFilterSlotCount() {
        return diffuserUpgradeConfig.filterSlots.get();
    }

    public boolean isVoidAnythingEnabled() {
        return diffuserUpgradeConfig.voidAnythingEnabled.get();
    }
}
