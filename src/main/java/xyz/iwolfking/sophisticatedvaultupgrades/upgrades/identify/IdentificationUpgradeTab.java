package xyz.iwolfking.sophisticatedvaultupgrades.upgrades.identify;


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
import xyz.iwolfking.sophisticatedvaultupgrades.upgrades.recycler.RecyclerUpgradeTab;

import java.util.List;
import java.util.Map;

public class IdentificationUpgradeTab extends UpgradeSettingsTab<IdentificationUpgradeContainer> {

    public static final TextureBlitData WORK_IN_VAULT = new TextureBlitData(SophisticatedVaultUpgrades.loc("textures/gui/work_in_vault.png"), new Position(1,1), Dimension.SQUARE_16, new UV(0,0), Dimension.SQUARE_16);
    public static final TextureBlitData NO_WORK_IN_VAULT = new TextureBlitData(SophisticatedVaultUpgrades.loc("textures/gui/no_work_in_vault.png"), new Position(1,1), Dimension.SQUARE_16, new UV(0,0), Dimension.SQUARE_16);

    private static final MutableComponent WORK_IN_VAULT_TOOLTIP = new TextComponent("Works in Vault");
    private static final MutableComponent WORK_IN_VAULT_DETAIL = new TextComponent("Automatically identifies items when in a vault.").withStyle(ChatFormatting.GRAY);
    private static final MutableComponent NO_WORK_IN_VAULT_TOOLTIP = new TextComponent("Disable in Vault");
    private static final MutableComponent NO_WORK_IN_VAULT_DETAIL = new TextComponent("Don't identify items in vault, will identify on exit.").withStyle(ChatFormatting.GRAY);

    protected FilterLogicControl<FilterLogic, FilterLogicContainer<FilterLogic>> filterLogicControl;

    private static final ButtonDefinition.Toggle<Boolean> WORK_IN_VAULT_TOGGLE = ButtonDefinitions.createToggleButtonDefinition(
            Map.of(
                    true, new ToggleButton.StateData(WORK_IN_VAULT, List.of(WORK_IN_VAULT_TOOLTIP, WORK_IN_VAULT_DETAIL)),
                    false, new ToggleButton.StateData(NO_WORK_IN_VAULT, List.of(NO_WORK_IN_VAULT_TOOLTIP, NO_WORK_IN_VAULT_DETAIL))
            ));

    protected IdentificationUpgradeTab(IdentificationUpgradeContainer upgradeContainer, Position position, StorageScreenBase<?> screen, Component tabLabel, Component closedTooltip) {
        super(upgradeContainer, position, screen, tabLabel, closedTooltip);
    }

    @Override
    protected void moveSlotsToTab() {
        filterLogicControl.moveSlotsToView();
    }

    public static class Basic extends IdentificationUpgradeTab {
        public Basic(IdentificationUpgradeContainer upgradeContainer, Position position, StorageScreenBase<?> screen, int slotsPerRow) {
            super(upgradeContainer, position, screen, new TextComponent("Identification"), new TextComponent("Identification"));
            filterLogicControl = addHideableChild(new FilterLogicControl.Basic(screen, new Position(x + 3, y + 44), getContainer().getFilterLogicContainer(),
                    slotsPerRow));
        }
    }

    public static class Advanced extends IdentificationUpgradeTab {

        public Advanced(IdentificationUpgradeContainer upgradeContainer, Position position, StorageScreenBase<?> screen, int slotsPerRow) {
            super(upgradeContainer, position, screen, new TextComponent("Identification"), new TextComponent("Identification"));
            addHideableChild(new ToggleButton<>(new Position(x + 39, y + 24), WORK_IN_VAULT_TOGGLE, button -> getContainer().setShouldWorkInVault(!getContainer().shouldWorkInVault()),
                    getContainer()::shouldWorkInVault));
            filterLogicControl = addHideableChild(new FilterLogicControl.Advanced(screen, new Position(x + 3, y + 44), getContainer().getFilterLogicContainer(),
                    slotsPerRow));
        }
    }


}