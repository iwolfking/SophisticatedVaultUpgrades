package xyz.iwolfking.sophisticatedvaultupgrades.upgrades.diffuser;


import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.p3pp3rf1y.sophisticatedcore.client.gui.StorageScreenBase;
import net.p3pp3rf1y.sophisticatedcore.client.gui.UpgradeSettingsTab;
import net.p3pp3rf1y.sophisticatedcore.client.gui.controls.ButtonDefinition;
import net.p3pp3rf1y.sophisticatedcore.client.gui.controls.ButtonDefinitions;
import net.p3pp3rf1y.sophisticatedcore.client.gui.controls.ToggleButton;
import net.p3pp3rf1y.sophisticatedcore.client.gui.utils.Dimension;
import net.p3pp3rf1y.sophisticatedcore.client.gui.utils.Position;
import net.p3pp3rf1y.sophisticatedcore.client.gui.utils.TranslationHelper;
import net.p3pp3rf1y.sophisticatedcore.client.gui.utils.UV;
import net.p3pp3rf1y.sophisticatedcore.upgrades.FilterLogic;
import net.p3pp3rf1y.sophisticatedcore.upgrades.FilterLogicContainer;
import net.p3pp3rf1y.sophisticatedcore.upgrades.FilterLogicControl;
import net.p3pp3rf1y.sophisticatedcore.upgrades.voiding.VoidUpgradeContainer;
import net.p3pp3rf1y.sophisticatedcore.upgrades.voiding.VoidUpgradeTab;

import java.util.Map;

import static net.p3pp3rf1y.sophisticatedcore.client.gui.utils.GuiHelper.getButtonStateData;

public class DiffuserUpgradeTab extends UpgradeSettingsTab<DiffuserUpgradeContainer> {
    private static final TranslatableComponent VOID_OVERFLOW_TOOLTIP = new TranslatableComponent(TranslationHelper.INSTANCE.translUpgradeButton("diffuser_overflow"));
    private static final MutableComponent VOID_OVERFLOW_TOOLTIP_DETAIL = new TranslatableComponent(TranslationHelper.INSTANCE.translUpgradeButton("diffuser_overflow.detail")).withStyle(ChatFormatting.GRAY);

    private static final MutableComponent VOID_ANYTHING_DISABLED_TOOLTIP = new TranslatableComponent(TranslationHelper.INSTANCE.translUpgradeButton("diffuse_anything_disabled")).withStyle(ChatFormatting.RED);
    private static final ButtonDefinition.Toggle<Boolean> VOID_OVERFLOW = ButtonDefinitions.createToggleButtonDefinition(
            Map.of(
                    true, getButtonStateData(new UV(224, 16), Dimension.SQUARE_16, new Position(1, 1), VOID_OVERFLOW_TOOLTIP, VOID_OVERFLOW_TOOLTIP_DETAIL),
                    false, getButtonStateData(new UV(208, 16), TranslationHelper.INSTANCE.translUpgradeButton("diffuse_any"), Dimension.SQUARE_16, new Position(1, 1))
            ));
    private static final ButtonDefinition.Toggle<Boolean> VOID_OVERFLOW_ONLY = ButtonDefinitions.createToggleButtonDefinition(
            Map.of(
                    true, getButtonStateData(new UV(224, 16), Dimension.SQUARE_16, new Position(1, 1), VOID_OVERFLOW_TOOLTIP, VOID_OVERFLOW_TOOLTIP_DETAIL, VOID_ANYTHING_DISABLED_TOOLTIP),
                    false, getButtonStateData(new UV(208, 16), TranslationHelper.INSTANCE.translUpgradeButton("diffuse_any"), Dimension.SQUARE_16, new Position(1, 1))
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
            super(upgradeContainer, position, screen, TranslationHelper.INSTANCE.translUpgrade("diffuser"), TranslationHelper.INSTANCE.translUpgradeTooltip("diffuser"));
            filterLogicControl = addHideableChild(new FilterLogicControl.Basic(screen, new Position(x + 3, y + 44), getContainer().getFilterLogicContainer(),
                    slotsPerRow));
        }
    }

    public static class Advanced extends DiffuserUpgradeTab {
        public Advanced(DiffuserUpgradeContainer upgradeContainer, Position position, StorageScreenBase<?> screen, int slotsPerRow) {
            super(upgradeContainer, position, screen, TranslationHelper.INSTANCE.translUpgrade("advanced_diffuser"), TranslationHelper.INSTANCE.translUpgradeTooltip("advanced_diffuser"));
            filterLogicControl = addHideableChild(new FilterLogicControl.Advanced(screen, new Position(x + 3, y + 44), getContainer().getFilterLogicContainer(),
                    slotsPerRow));
        }
    }
}