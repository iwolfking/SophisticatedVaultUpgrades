package xyz.iwolfking.sophisticatedvaultupgrades.upgrades.diffuser;


import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.*;
import net.p3pp3rf1y.sophisticatedcore.client.gui.StorageScreenBase;
import net.p3pp3rf1y.sophisticatedcore.client.gui.UpgradeSettingsTab;
import net.p3pp3rf1y.sophisticatedcore.client.gui.controls.ButtonDefinition;
import net.p3pp3rf1y.sophisticatedcore.client.gui.controls.ButtonDefinitions;
import net.p3pp3rf1y.sophisticatedcore.client.gui.controls.ToggleButton;
import net.p3pp3rf1y.sophisticatedcore.client.gui.utils.*;
import net.p3pp3rf1y.sophisticatedcore.upgrades.FilterLogic;
import net.p3pp3rf1y.sophisticatedcore.upgrades.FilterLogicContainer;
import net.p3pp3rf1y.sophisticatedcore.upgrades.FilterLogicControl;
import xyz.iwolfking.sophisticatedvaultupgrades.SophisticatedVaultUpgrades;

import java.util.List;
import java.util.Map;

import static net.p3pp3rf1y.sophisticatedcore.client.gui.utils.GuiHelper.getButtonStateData;

public class DiffuserUpgradeTab extends UpgradeSettingsTab<DiffuserUpgradeContainer> {

    public static final TextureBlitData COMPACT_SHARD = new TextureBlitData(SophisticatedVaultUpgrades.loc("textures/gui/compact_shard.png"), new Position(1,1), Dimension.SQUARE_16, new UV(0,0), Dimension.SQUARE_16);
    public static final TextureBlitData NO_SHARD = new TextureBlitData(SophisticatedVaultUpgrades.loc("textures/gui/no_shard.png"), new Position(1,1), Dimension.SQUARE_16, new UV(0,0), Dimension.SQUARE_16);
    public static final TextureBlitData KEEP_INVENTORY = new TextureBlitData(SophisticatedVaultUpgrades.loc("textures/gui/keep_inventory.png"), new Position(1,1), Dimension.SQUARE_16, new UV(0,0), Dimension.SQUARE_16);
    public static final TextureBlitData SEND_POUCH = new TextureBlitData(SophisticatedVaultUpgrades.loc("textures/gui/send_pouch.png"), new Position(1,1), Dimension.SQUARE_16, new UV(0,0), Dimension.SQUARE_16);
    private static final MutableComponent VOID_OVERFLOW_TOOLTIP = new TextComponent("Diffuse Overflow");
    private static final MutableComponent VOID_OVERFLOW_TOOLTIP_DETAIL = new TextComponent("Automatically diffuse items when they overflow.").withStyle(ChatFormatting.GRAY);
    private static final MutableComponent VOID_ANYTHING_DISABLED_TOOLTIP = new TextComponent("Diffuse Overflow").withStyle(ChatFormatting.RED);

    private static final MutableComponent COMPACT_SHARD_TOOLTIP = new TextComponent("Compact to Shards");
    private static final MutableComponent COMPACT_SHARD_DETAIL = new TextComponent("Compacts Soul Dust into Soul Shards").withStyle(ChatFormatting.GRAY);
    private static final MutableComponent NO_SHARD_TOOLTIP = new TextComponent("Keep as Dust");
    private static final MutableComponent NO_SHARD_DETAIL = new TextComponent("Don't Compact into Soul Shards.").withStyle(ChatFormatting.GRAY);

    private static final MutableComponent HOLD_SHARDS = new TextComponent("Hold Shards");
    private static final MutableComponent SEND_TO_POUCH = new TextComponent("Send to Shard Pouch");
    private static final MutableComponent HOLD_SHARDS_TOOLTIP = new TextComponent("Holds Soul Shards inside this inventory.").withStyle(ChatFormatting.GRAY);
    private static final MutableComponent SEND_POUCH_TOOLTIP = new TextComponent("Sends shards into your Shard Pouch.").withStyle(ChatFormatting.GRAY);
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
                    true, new ToggleButton.StateData(COMPACT_SHARD, List.of(COMPACT_SHARD_TOOLTIP, COMPACT_SHARD_DETAIL)),
                    false, new ToggleButton.StateData(NO_SHARD, List.of(NO_SHARD_TOOLTIP, NO_SHARD_DETAIL))
            ));

    private static final ButtonDefinition.Toggle<Boolean> HOLD_SHARDS_BTN = ButtonDefinitions.createToggleButtonDefinition(
            Map.of(
                    true, new ToggleButton.StateData(KEEP_INVENTORY, List.of(HOLD_SHARDS, HOLD_SHARDS_TOOLTIP)),
                    false, new ToggleButton.StateData(SEND_POUCH, List.of(SEND_TO_POUCH, SEND_POUCH_TOOLTIP))
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