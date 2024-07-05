package xyz.iwolfking.sophisticatedvaultupgrades.upgrades.drop;


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

public class DropUpgradeTab extends UpgradeSettingsTab<DropUpgradeContainer> {

    private static final MutableComponent VOID_OVERFLOW_TOOLTIP = new TextComponent("Drop Overflow");
    private static final MutableComponent VOID_OVERFLOW_TOOLTIP_DETAIL = new TextComponent("Automatically drops items when they overflow.").withStyle(ChatFormatting.GRAY);
    private static final MutableComponent VOID_ANYTHING_DISABLED_TOOLTIP = new TextComponent("Drop Overflow").withStyle(ChatFormatting.RED);

    private static final ButtonDefinition.Toggle<Boolean> VOID_OVERFLOW = ButtonDefinitions.createToggleButtonDefinition(
            Map.of(
                    true, getButtonStateData(new UV(224, 16), Dimension.SQUARE_16, new Position(1, 1), VOID_OVERFLOW_TOOLTIP, VOID_OVERFLOW_TOOLTIP_DETAIL),
                    false, getButtonStateData(new UV(208, 16), "Drop Any", Dimension.SQUARE_16, new Position(1, 1))
            ));
    private static final ButtonDefinition.Toggle<Boolean> VOID_OVERFLOW_ONLY = ButtonDefinitions.createToggleButtonDefinition(
            Map.of(

                    true, getButtonStateData(new UV(224, 16), Dimension.SQUARE_16, new Position(1, 1), VOID_OVERFLOW_TOOLTIP, VOID_OVERFLOW_TOOLTIP_DETAIL, VOID_ANYTHING_DISABLED_TOOLTIP),
                    false, getButtonStateData(new UV(208, 16), "Drop Any", Dimension.SQUARE_16, new Position(1, 1))
            ));

    protected FilterLogicControl<FilterLogic, FilterLogicContainer<FilterLogic>> filterLogicControl;

    protected DropUpgradeTab(DropUpgradeContainer upgradeContainer, Position position, StorageScreenBase<?> screen, Component tabLabel, Component closedTooltip) {
        super(upgradeContainer, position, screen, tabLabel, closedTooltip);
        addHideableChild(new ToggleButton<>(new Position(x + 3, y + 24), ButtonDefinitions.WORK_IN_GUI, button -> getContainer().setShouldWorkdInGUI(!getContainer().shouldWorkInGUI()),
                getContainer()::shouldWorkInGUI));
        addHideableChild(new ToggleButton<>(new Position(x + 21, y + 24), getContainer().getUpgradeWrapper().isVoidAnythingEnabled() ? VOID_OVERFLOW : VOID_OVERFLOW_ONLY, button -> getContainer().setShouldVoidOverflow(!getContainer().shouldVoidOverflow()),
                getContainer()::shouldVoidOverflow));
    }

    @Override
    protected void moveSlotsToTab() {
        filterLogicControl.moveSlotsToView();
    }

    public static class Basic extends DropUpgradeTab {
        public Basic(DropUpgradeContainer upgradeContainer, Position position, StorageScreenBase<?> screen, int slotsPerRow) {
            super(upgradeContainer, position, screen, new TextComponent("Dropper"), new TextComponent("Dropper"));
            filterLogicControl = addHideableChild(new FilterLogicControl.Basic(screen, new Position(x + 3, y + 44), getContainer().getFilterLogicContainer(),
                    slotsPerRow));
        }
    }

    public static class Advanced extends DropUpgradeTab {
        public Advanced(DropUpgradeContainer upgradeContainer, Position position, StorageScreenBase<?> screen, int slotsPerRow) {
            super(upgradeContainer, position, screen, new TextComponent("Dropper"), new TextComponent("Dropper"));
            filterLogicControl = addHideableChild(new FilterLogicControl.Advanced(screen, new Position(x + 3, y + 44), getContainer().getFilterLogicContainer(),
                    slotsPerRow));
        }
    }
}