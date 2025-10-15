package xyz.iwolfking.sophisticatedvaultupgrades.upgrades.opener;


import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TextComponent;
import net.p3pp3rf1y.sophisticatedcore.client.gui.StorageScreenBase;
import net.p3pp3rf1y.sophisticatedcore.client.gui.UpgradeSettingsTab;
import net.p3pp3rf1y.sophisticatedcore.client.gui.controls.ButtonDefinition;
import net.p3pp3rf1y.sophisticatedcore.client.gui.controls.ButtonDefinitions;
import net.p3pp3rf1y.sophisticatedcore.client.gui.controls.ToggleButton;
import net.p3pp3rf1y.sophisticatedcore.client.gui.utils.Dimension;
import net.p3pp3rf1y.sophisticatedcore.client.gui.utils.Position;
import net.p3pp3rf1y.sophisticatedcore.client.gui.utils.TextureBlitData;
import net.p3pp3rf1y.sophisticatedcore.client.gui.utils.UV;
import net.p3pp3rf1y.sophisticatedcore.upgrades.FilterLogic;
import net.p3pp3rf1y.sophisticatedcore.upgrades.FilterLogicContainer;
import net.p3pp3rf1y.sophisticatedcore.upgrades.FilterLogicControl;
import xyz.iwolfking.sophisticatedvaultupgrades.SophisticatedVaultUpgrades;
import xyz.iwolfking.sophisticatedvaultupgrades.upgrades.recycler.RecyclerUpgradeContainer;

import java.util.List;
import java.util.Map;

public class OpenerUpgradeTab extends UpgradeSettingsTab<OpenerUpgradeContainer> {

    protected FilterLogicControl<FilterLogic, FilterLogicContainer<FilterLogic>> filterLogicControl;

    protected OpenerUpgradeTab(OpenerUpgradeContainer upgradeContainer, Position position, StorageScreenBase<?> screen, Component tabLabel, Component closedTooltip) {
        super(upgradeContainer, position, screen, tabLabel, closedTooltip);
        addHideableChild(new ToggleButton<>(new Position(x + 3, y + 24), ButtonDefinitions.WORK_IN_GUI, button -> getContainer().setShouldWorkdInGUI(!getContainer().shouldWorkInGUI()),
                getContainer()::shouldWorkInGUI));
    }

    @Override
    protected void moveSlotsToTab() {
        filterLogicControl.moveSlotsToView();
    }

    public static class Basic extends OpenerUpgradeTab {
        public Basic(OpenerUpgradeContainer upgradeContainer, Position position, StorageScreenBase<?> screen, int slotsPerRow) {
            super(upgradeContainer, position, screen, new TextComponent("Opener Upgrade"), new TextComponent("Opener Upgrade"));
            filterLogicControl = addHideableChild(new FilterLogicControl.Basic(screen, new Position(x + 3, y + 44), getContainer().getFilterLogicContainer(),
                    slotsPerRow));
        }
    }
    public static class Advanced extends OpenerUpgradeTab {
        public Advanced(OpenerUpgradeContainer upgradeContainer, Position position, StorageScreenBase<?> screen, int slotsPerRow) {
            super(upgradeContainer, position, screen, new TextComponent("Advanced Opener Upgrade"), new TextComponent("Advanced Opener Upgrade"));
            filterLogicControl = addHideableChild(new FilterLogicControl.Advanced(screen, new Position(x + 3, y + 44), getContainer().getFilterLogicContainer(),
                    slotsPerRow));
        }
    }
}