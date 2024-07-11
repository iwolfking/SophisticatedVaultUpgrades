package xyz.iwolfking.sophisticatedvaultupgrades.upgrades.recycler;


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

import java.util.List;
import java.util.Map;

import static net.p3pp3rf1y.sophisticatedcore.client.gui.utils.GuiHelper.getButtonStateData;

public class RecyclerUpgradeTab extends UpgradeSettingsTab<RecyclerUpgradeContainer> {

    public static final TextureBlitData SCRAP_UNIDENTIFIED = new TextureBlitData(SophisticatedVaultUpgrades.loc("textures/gui/scrap_unidentified.png"), new Position(1,1), Dimension.SQUARE_16, new UV(0,0), Dimension.SQUARE_16);
    public static final TextureBlitData SCRAP_IDENTIFIED_ONLY = new TextureBlitData(SophisticatedVaultUpgrades.loc("textures/gui/no_scrap_unidentified.png"), new Position(1,1), Dimension.SQUARE_16, new UV(0,0), Dimension.SQUARE_16);

    private static final MutableComponent SCRAP_UNIDENTIFIED_TT = new TextComponent("Scrap Unidentified");
    private static final MutableComponent SCRAP_UNIDENTIFIED_D = new TextComponent("Will scrap unidentified items.").withStyle(ChatFormatting.GRAY);
    private static final MutableComponent SCRAP_IDENTIFIED_ONLY_TT = new TextComponent("Only Scrap Identified");
    private static final MutableComponent SCRAP_IDENTIFIED_ONLY_D = new TextComponent("Only scrap identified items.").withStyle(ChatFormatting.GRAY);

    private static final ButtonDefinition.Toggle<Boolean> SCRAP_IDENTIFIED_TOGGLE = ButtonDefinitions.createToggleButtonDefinition(
            Map.of(
                    true, new ToggleButton.StateData(SCRAP_UNIDENTIFIED, List.of(SCRAP_UNIDENTIFIED_TT, SCRAP_UNIDENTIFIED_D)),
                    false, new ToggleButton.StateData(SCRAP_IDENTIFIED_ONLY, List.of(SCRAP_IDENTIFIED_ONLY_TT, SCRAP_IDENTIFIED_ONLY_D))
            ));

    protected FilterLogicControl<FilterLogic, FilterLogicContainer<FilterLogic>> filterLogicControl;

    protected RecyclerUpgradeTab(RecyclerUpgradeContainer upgradeContainer, Position position, StorageScreenBase<?> screen, Component tabLabel, Component closedTooltip) {
        super(upgradeContainer, position, screen, tabLabel, closedTooltip);
        addHideableChild(new ToggleButton<>(new Position(x + 3, y + 24), ButtonDefinitions.WORK_IN_GUI, button -> getContainer().setShouldWorkdInGUI(!getContainer().shouldWorkInGUI()),
                getContainer()::shouldWorkInGUI));
        addHideableChild(new ToggleButton<>(new Position(x + 21, y + 24), SCRAP_IDENTIFIED_TOGGLE, button -> getContainer().setShouldScrapUnidentified(!getContainer().shouldScrapUnidentified()),
                getContainer()::shouldScrapUnidentified));
    }

    @Override
    protected void moveSlotsToTab() {
        filterLogicControl.moveSlotsToView();
    }

    public static class Basic extends RecyclerUpgradeTab {
        public Basic(RecyclerUpgradeContainer upgradeContainer, Position position, StorageScreenBase<?> screen, int slotsPerRow) {
            super(upgradeContainer, position, screen, new TextComponent("Vault Recycler"), new TextComponent("Vault Recycler"));
            filterLogicControl = addHideableChild(new FilterLogicControl.Basic(screen, new Position(x + 3, y + 44), getContainer().getFilterLogicContainer(),
                    slotsPerRow));
        }
    }
    public static class Advanced extends RecyclerUpgradeTab {
        public Advanced(RecyclerUpgradeContainer upgradeContainer, Position position, StorageScreenBase<?> screen, int slotsPerRow) {
            super(upgradeContainer, position, screen, new TextComponent("Vault Recycler"), new TextComponent("Vault Recycler"));
            filterLogicControl = addHideableChild(new FilterLogicControl.Advanced(screen, new Position(x + 3, y + 44), getContainer().getFilterLogicContainer(),
                    slotsPerRow));
        }
    }
}