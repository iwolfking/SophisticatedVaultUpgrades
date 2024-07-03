package xyz.iwolfking.sophisticatedvaultupgrades.upgrades.activation;

import net.minecraft.world.item.CreativeModeTab;
import net.p3pp3rf1y.sophisticatedbackpacks.SophisticatedBackpacks;
import net.p3pp3rf1y.sophisticatedbackpacks.upgrades.deposit.DepositUpgradeWrapper;
import net.p3pp3rf1y.sophisticatedcore.upgrades.UpgradeItemBase;
import net.p3pp3rf1y.sophisticatedcore.upgrades.UpgradeType;

public class ActivationUpgradeItem extends UpgradeItemBase<ActivationUpgradeWrapper> {
    private static final UpgradeType<ActivationUpgradeWrapper> TYPE = new UpgradeType<>(ActivationUpgradeWrapper::new);



    public ActivationUpgradeItem(CreativeModeTab itemGroup) {
        super(itemGroup);
    }

    @Override
    public UpgradeType<ActivationUpgradeWrapper> getType() {
        return TYPE;
    }


}
