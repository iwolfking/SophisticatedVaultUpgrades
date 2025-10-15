package xyz.iwolfking.sophisticatedvaultupgrades.upgrades.identify;

import com.mojang.authlib.GameProfile;
import iskallia.vault.item.VaultDollItem;
import net.minecraft.ChatFormatting;
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
import net.p3pp3rf1y.sophisticatedcore.upgrades.IUpgradeCountLimitConfig;
import net.p3pp3rf1y.sophisticatedcore.upgrades.UpgradeItemBase;
import net.p3pp3rf1y.sophisticatedcore.upgrades.UpgradeType;
import org.jetbrains.annotations.Nullable;
import xyz.iwolfking.sophisticatedvaultupgrades.upgrades.recycler.RecyclerUpgradeConfig;

import java.util.*;


public class IdentificationUpgradeItem extends UpgradeItemBase<IdentificationUpgradeWrapper> {

    private final IdentificationUpgradeConfig identificationUpgradeConfig;
    public static final UpgradeType<IdentificationUpgradeWrapper> TYPE = new UpgradeType<>(IdentificationUpgradeWrapper::new);
    public IdentificationUpgradeItem(CreativeModeTab itemGroup, IdentificationUpgradeConfig identificationUpgradeConfig, IUpgradeCountLimitConfig upgradeCountLimitConfig) {
        super(itemGroup, upgradeCountLimitConfig);
        this.identificationUpgradeConfig = identificationUpgradeConfig;
    }

    @Override
    public UpgradeType<IdentificationUpgradeWrapper> getType() {
        return TYPE;
    }

    String NBT_OWNER = "Owner";

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (!player.getCommandSenderWorld().isClientSide && player.isSteppingCarefully()) {
            setOwner(stack, player);
            Component displayName = Objects.requireNonNullElse(player.getDisplayName(), new TextComponent("?"));
            player.displayClientMessage(new TranslatableComponent("modularrouters.itemText.security.owner", displayName.getString()), false);
            return InteractionResultHolder.success(stack);
        }
        return InteractionResultHolder.pass(stack);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        tooltip.add(new TextComponent("Owner: " + getOwnerName(stack)));
        super.appendHoverText(stack, worldIn, tooltip, flagIn);
    }


    public void onCraftedBy(ItemStack stack, Level level, Player player) {
        setOwner(stack, player);
    }

    String getOwnerName(ItemStack stack) {
        if (!stack.hasTag() || !Objects.requireNonNull(stack.getTag()).contains(NBT_OWNER)) return null;

        ListTag l = stack.getTag().getList(NBT_OWNER, Tag.TAG_STRING);
        return l.getString(0);
    }

    public UUID getOwnerID(ItemStack stack) {
        if (!stack.hasTag() || !Objects.requireNonNull(stack.getTag()).contains(NBT_OWNER)) return null;

        ListTag l = stack.getTag().getList(NBT_OWNER, Tag.TAG_STRING);
        return UUID.fromString(l.getString(1));
    }

    public void setOwner(ItemStack stack, Player player) {
        CompoundTag compound = stack.getOrCreateTag();
        ListTag owner = new ListTag();
        owner.add(StringTag.valueOf(player.getDisplayName().getString()));
        owner.add(StringTag.valueOf(player.getUUID().toString()));
        compound.put(NBT_OWNER, owner);
        stack.setTag(compound);
    }

    public int getFilterSlotCount() {
        return identificationUpgradeConfig.filterSlots.get();
    }

}