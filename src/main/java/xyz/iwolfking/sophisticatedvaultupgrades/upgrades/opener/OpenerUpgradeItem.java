package xyz.iwolfking.sophisticatedvaultupgrades.upgrades.opener;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.p3pp3rf1y.sophisticatedcore.upgrades.UpgradeItemBase;
import net.p3pp3rf1y.sophisticatedcore.upgrades.UpgradeType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class OpenerUpgradeItem extends UpgradeItemBase<OpenerUpgradeWrapper> {
    private final OpenerUpgradeConfig openerUpgradeConfig;
    public static final UpgradeType<OpenerUpgradeWrapper> TYPE = new UpgradeType<>(OpenerUpgradeWrapper::new);


    public OpenerUpgradeItem(CreativeModeTab itemGroup, OpenerUpgradeConfig openerUpgradeConfig) {
        super(itemGroup);
        this.openerUpgradeConfig = openerUpgradeConfig;
    }

    @Override
    public @NotNull UpgradeType<OpenerUpgradeWrapper> getType() {
        return TYPE;
    }

    public int getFilterSlotCount() {
        return openerUpgradeConfig.filterSlots.get();
    }

}
