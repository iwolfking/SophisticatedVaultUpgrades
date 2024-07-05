package xyz.iwolfking.sophisticatedvaultupgrades.upgrades.diffuser;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.p3pp3rf1y.sophisticatedcore.common.gui.UpgradeContainerBase;
import net.p3pp3rf1y.sophisticatedcore.common.gui.UpgradeContainerType;
import net.p3pp3rf1y.sophisticatedcore.upgrades.FilterLogic;
import net.p3pp3rf1y.sophisticatedcore.upgrades.FilterLogicContainer;
import net.p3pp3rf1y.sophisticatedcore.upgrades.voiding.VoidUpgradeContainer;
import net.p3pp3rf1y.sophisticatedcore.upgrades.voiding.VoidUpgradeWrapper;
import net.p3pp3rf1y.sophisticatedcore.util.NBTHelper;

public class DiffuserUpgradeContainer extends UpgradeContainerBase<DiffuserUpgradeWrapper, DiffuserUpgradeContainer> {
    private static final String DATA_SHOULD_WORKD_IN_GUI = "shouldWorkdInGUI";
    private static final String DATA_SHOULD_VOID_OVERFLOW = "shouldVoidOverflow";
    private static final String DATA_SHOULD_HOLD_SHARDS = "shouldHoldShards";
    private static final String DATA_SHOULD_COMPACT_SHARDS = "shouldCompactShards";
    private final FilterLogicContainer<FilterLogic> filterLogicContainer;

    public DiffuserUpgradeContainer(Player player, int containerId, DiffuserUpgradeWrapper wrapper, UpgradeContainerType<DiffuserUpgradeWrapper, DiffuserUpgradeContainer> type) {
        super(player, containerId, wrapper, type);
        filterLogicContainer = new FilterLogicContainer<>(upgradeWrapper::getFilterLogic, this, slots::add);
    }

    @Override
    public void handleMessage(CompoundTag data) {
        if (data.contains(DATA_SHOULD_WORKD_IN_GUI)) {
            setShouldWorkdInGUI(data.getBoolean(DATA_SHOULD_WORKD_IN_GUI));
        } else if (data.contains(DATA_SHOULD_VOID_OVERFLOW)) {
            setShouldVoidOverflow(data.getBoolean(DATA_SHOULD_VOID_OVERFLOW));
        }
        else if(data.contains(DATA_SHOULD_COMPACT_SHARDS)) {
            setShouldCompactShards(data.getBoolean(DATA_SHOULD_COMPACT_SHARDS));
        }
        else if(data.contains(DATA_SHOULD_HOLD_SHARDS)) {
            setShouldHoldShards(data.getBoolean(DATA_SHOULD_HOLD_SHARDS));
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

    public void setShouldHoldShards(boolean shouldHoldShards) {
        upgradeWrapper.setShouldHoldShards(shouldHoldShards);
        sendDataToServer(() -> NBTHelper.putBoolean(new CompoundTag(), DATA_SHOULD_HOLD_SHARDS, shouldHoldShards));
    }

    public void setShouldCompactShards(boolean shouldCompactShards) {
        upgradeWrapper.setShouldCompactShards(shouldCompactShards);
        sendDataToServer(() -> NBTHelper.putBoolean(new CompoundTag(), DATA_SHOULD_COMPACT_SHARDS, shouldCompactShards));
    }

    public void setShouldVoidOverflow(boolean shouldVoidOverflow) {
        upgradeWrapper.setShouldVoidOverflow(shouldVoidOverflow);
        sendDataToServer(() -> NBTHelper.putBoolean(new CompoundTag(), DATA_SHOULD_VOID_OVERFLOW, shouldVoidOverflow));
    }


    public boolean shouldVoidOverflow() {
        return upgradeWrapper.shouldVoidOverflow();
    }

    public Boolean shouldCompactShards() {
        return upgradeWrapper.shouldCompactShards();
    }

    public Boolean shouldHoldShards() {
        return upgradeWrapper.shouldHoldShards();
    }

    public Boolean shouldWorkInGUI() {
        return upgradeWrapper.worksInGui();
    }
}