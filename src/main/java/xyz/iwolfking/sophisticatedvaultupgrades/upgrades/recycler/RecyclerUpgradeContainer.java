package xyz.iwolfking.sophisticatedvaultupgrades.upgrades.recycler;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.p3pp3rf1y.sophisticatedcore.common.gui.UpgradeContainerBase;
import net.p3pp3rf1y.sophisticatedcore.common.gui.UpgradeContainerType;
import net.p3pp3rf1y.sophisticatedcore.upgrades.FilterLogic;
import net.p3pp3rf1y.sophisticatedcore.upgrades.FilterLogicContainer;
import net.p3pp3rf1y.sophisticatedcore.util.NBTHelper;

public class RecyclerUpgradeContainer extends UpgradeContainerBase<RecyclerUpgradeWrapper, RecyclerUpgradeContainer> {
    private static final String DATA_SHOULD_WORKD_IN_GUI = "shouldWorkdInGUI";
    private final FilterLogicContainer<FilterLogic> filterLogicContainer;

    public RecyclerUpgradeContainer(Player player, int containerId, RecyclerUpgradeWrapper wrapper, UpgradeContainerType<RecyclerUpgradeWrapper, RecyclerUpgradeContainer> type) {
        super(player, containerId, wrapper, type);
        filterLogicContainer = new FilterLogicContainer<>(upgradeWrapper::getFilterLogic, this, slots::add);
    }

    @Override
    public void handleMessage(CompoundTag data) {
        if (data.contains(DATA_SHOULD_WORKD_IN_GUI)) {
            setShouldWorkdInGUI(data.getBoolean(DATA_SHOULD_WORKD_IN_GUI));
        }
        filterLogicContainer.handleMessage(data);
    }

    public FilterLogicContainer<FilterLogic> getFilterLogicContainer() {
        return filterLogicContainer;
    }

    public void setShouldWorkdInGUI(boolean shouldWorkdInGUI) {
        upgradeWrapper.setShouldWorkdInGUI(shouldWorkdInGUI);
        sendDataToServer(() -> NBTHelper.putBoolean(new CompoundTag(), DATA_SHOULD_WORKD_IN_GUI, shouldWorkdInGUI));
    }

    public Boolean shouldWorkInGUI() {
        return upgradeWrapper.worksInGui();
    }
}