package xyz.iwolfking.sophisticatedvaultupgrades.upgrades.diffuser;


import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.p3pp3rf1y.sophisticatedcore.client.gui.StorageScreenBase;
import net.p3pp3rf1y.sophisticatedcore.client.gui.UpgradeSettingsTab;
import net.p3pp3rf1y.sophisticatedcore.client.gui.controls.ButtonDefinition;
import net.p3pp3rf1y.sophisticatedcore.client.gui.controls.ButtonDefinitions;
import net.p3pp3rf1y.sophisticatedcore.client.gui.controls.ToggleButton;
import net.p3pp3rf1y.sophisticatedcore.client.gui.utils.*;
import net.p3pp3rf1y.sophisticatedcore.upgrades.FilterLogic;
import net.p3pp3rf1y.sophisticatedcore.upgrades.FilterLogicContainer;
import net.p3pp3rf1y.sophisticatedcore.upgrades.FilterLogicControl;

import java.util.Map;

import static net.p3pp3rf1y.sophisticatedcore.client.gui.utils.GuiHelper.getButtonStateData;

public class DiffuserUpgradeTab extends UpgradeSettingsTab<DiffuserUpgradeContainer> {
    private static final MutableComponent VOID_OVERFLOW_TOOLTIP = new TextComponent("Diffuse Overflow");
    private static final MutableComponent VOID_OVERFLOW_TOOLTIP_DETAIL = new TextComponent("Automatically diffuse items when they overflow.").withStyle(ChatFormatting.GRAY);
    private static final MutableComponent VOID_ANYTHING_DISABLED_TOOLTIP = new TextComponent("Diffuse Overflow").withStyle(ChatFormatting.RED);

    private static final MutableComponent COMPACT_SHARD_TOOLTIP = new TextComponent("Do Compact");
    private static final MutableComponent COMPACT_SHARD_DETAIL = new TextComponent("Compacts dust into shards automatically.").withStyle(ChatFormatting.GRAY);
    private static final MutableComponent COMPACT_SHARD_DISABLE = new TextComponent("Don't Compact").withStyle(ChatFormatting.RED);

    private static final MutableComponent HOLD_SHARDS = new TextComponent("Hold Shards");
    private static final MutableComponent HOLD_SHARDS_DETAIL = new TextComponent("Either holds shards in backpack or sends to Shard Pouch.").withStyle(ChatFormatting.GRAY);
    private static final MutableComponent BAG_SHARDS = new TextComponent("Send to Pouch").withStyle(ChatFormatting.LIGHT_PURPLE);
    private static final ButtonDefinition.Toggle<Boolean> VOID_OVERFLOW = ButtonDefinitions.createToggleButtonDefinition(
            Map.of(
                    true, getButtonStateData(new UV(224, 16), Dimension.SQUARE_16, new Position(1, 1), VOID_OVERFLOW_TOOLTIP, VOID_OVERFLOW_TOOLTIP_DETAIL),
                    false, getButtonStateData(new UV(208, 16), "Diffuse Any", Dimension.SQUARE_16, new Position(1, 1))
            ));
    private static final ButtonDefinition.Toggle<Boolean> VOID_OVERFLOW_ONLY = ButtonDefinitions.createToggleButtonDefinition(
            Map.of(
                    true, getButtonStateData(new UV(224, 16), Dimension.SQUARE_16, new Position(1, 1), VOID_OVERFLOW_TOOLTIP, VOID_OVERFLOW_TOOLTIP_DETAIL, VOID_ANYTHING_DISABLED_TOOLTIP),
                    false, getButtonStateData(new UV(208, 16), "Diffuse Any", Dimension.SQUARE_16, new Position(1, 1))
            ));

    private static final ButtonDefinition.Toggle<Boolean> COMPACT_SHARDS_BTN = ButtonDefinitions.createToggleButtonDefinition(
            Map.of(
                    true, getButtonStateData(new UV(224, 16), Dimension.SQUARE_16, new Position(1, 1), COMPACT_SHARD_TOOLTIP, COMPACT_SHARD_DETAIL, COMPACT_SHARD_DISABLE),
                    false, getButtonStateData(new UV(208, 16), "Compact Shards", Dimension.SQUARE_16, new Position(1, 1))
            ));

    private static final ButtonDefinition.Toggle<Boolean> HOLD_SHARDS_BTN = ButtonDefinitions.createToggleButtonDefinition(
            Map.of(
                    true, getButtonStateData(new UV(224, 16), Dimension.SQUARE_16, new Position(1, 1), HOLD_SHARDS, HOLD_SHARDS_DETAIL, BAG_SHARDS),
                    false, getButtonStateData(new UV(208, 16), "Hold Shards", Dimension.SQUARE_16, new Position(1, 1))
            ));

    protected FilterLogicControl<FilterLogic, FilterLogicContainer<FilterLogic>> filterLogicControl;

    protected DiffuserUpgradeTab(DiffuserUpgradeContainer upgradeContainer, Position position, StorageScreenBase<?> screen, Component tabLabel, Component closedTooltip) {
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

    public static class Basic extends DiffuserUpgradeTab {
        public Basic(DiffuserUpgradeContainer upgradeContainer, Position position, StorageScreenBase<?> screen, int slotsPerRow) {
            super(upgradeContainer, position, screen, new TextComponent("Soul Diffuser"), new TextComponent("Soul Diffuser"));
            filterLogicControl = addHideableChild(new FilterLogicControl.Basic(screen, new Position(x + 3, y + 44), getContainer().getFilterLogicContainer(),
                    slotsPerRow));
        }
    }

    public static class Advanced extends DiffuserUpgradeTab {
        public Advanced(DiffuserUpgradeContainer upgradeContainer, Position position, StorageScreenBase<?> screen, int slotsPerRow) {
            super(upgradeContainer, position, screen, new TextComponent("Soul Diffuser"), new TextComponent("Soul Diffuser"));
            addHideableChild(new ToggleButton<>(new Position(x + 39, y + 24), COMPACT_SHARDS_BTN, button -> getContainer().setShouldCompactShards(!getContainer().shouldCompactShards()),
                    getContainer()::shouldCompactShards));
            addHideableChild(new ToggleButton<>(new Position(x + 57, y + 24), HOLD_SHARDS_BTN, button -> getContainer().setShouldHoldShards(!getContainer().shouldHoldShards()),
                    getContainer()::shouldHoldShards));
            filterLogicControl = addHideableChild(new FilterLogicControl.Advanced(screen, new Position(x + 3, y + 44), getContainer().getFilterLogicContainer(),
                    slotsPerRow));
        }
    }
}