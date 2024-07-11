package xyz.iwolfking.sophisticatedvaultupgrades.upgrades.identify;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.p3pp3rf1y.sophisticatedcore.common.gui.UpgradeContainerBase;
import net.p3pp3rf1y.sophisticatedcore.common.gui.UpgradeContainerType;
import net.p3pp3rf1y.sophisticatedcore.upgrades.FilterLogic;
import net.p3pp3rf1y.sophisticatedcore.upgrades.FilterLogicContainer;
import net.p3pp3rf1y.sophisticatedcore.util.NBTHelper;
import org.jetbrains.annotations.NotNull;
import xyz.iwolfking.sophisticatedvaultupgrades.upgrades.recycler.RecyclerUpgradeWrapper;

public class IdentificationUpgradeContainer extends UpgradeContainerBase<IdentificationUpgradeWrapper, IdentificationUpgradeContainer> {
    private static final String DATA_SHOULD_IDENTIFY_IN_VAULT = "shouldIdentifyInVault";
    private final FilterLogicContainer<FilterLogic> filterLogicContainer;
    public IdentificationUpgradeContainer(Player player, int containerId, IdentificationUpgradeWrapper wrapper, UpgradeContainerType<IdentificationUpgradeWrapper, IdentificationUpgradeContainer> type) {
        super(player, containerId, wrapper, type);
        filterLogicContainer = new FilterLogicContainer<>(upgradeWrapper::getFilterLogic, this, slots::add);
    }

    @Override
    public void handleMessage(CompoundTag data) {
        if (data.contains(DATA_SHOULD_IDENTIFY_IN_VAULT)) {
            setShouldWorkInVault(data.getBoolean(DATA_SHOULD_IDENTIFY_IN_VAULT));
        }
    }

    public void setShouldWorkInVault(boolean shouldWorkInVault) {
        upgradeWrapper.setShouldWorkInVault(shouldWorkInVault);
        sendDataToServer(() -> NBTHelper.putBoolean(new CompoundTag(), DATA_SHOULD_IDENTIFY_IN_VAULT, shouldWorkInVault));
    }

    public Boolean shouldWorkInVault() {
        return upgradeWrapper.shouldWorkInVault();
    }

    public FilterLogicContainer<FilterLogic> getFilterLogicContainer() {
        return filterLogicContainer;
    }

}