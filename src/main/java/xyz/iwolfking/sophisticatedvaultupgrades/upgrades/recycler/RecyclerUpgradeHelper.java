package xyz.iwolfking.sophisticatedvaultupgrades.upgrades.recycler;

import iskallia.vault.block.entity.VaultRecyclerTileEntity;
import iskallia.vault.config.VaultRecyclerConfig;
import iskallia.vault.container.inventory.ShardPouchContainer;
import iskallia.vault.gear.VaultGearRarity;
import iskallia.vault.gear.data.VaultGearData;
import iskallia.vault.gear.item.VaultGearItem;
import iskallia.vault.init.ModConfigs;
import iskallia.vault.init.ModItems;
import iskallia.vault.init.ModNetwork;
import iskallia.vault.item.ItemShardPouch;
import iskallia.vault.item.gear.RecyclableItem;
import iskallia.vault.network.message.RecyclerParticleMessage;
import iskallia.vault.util.MiscUtils;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.network.PacketDistributor;
import net.p3pp3rf1y.sophisticatedcore.api.IStorageWrapper;
import net.p3pp3rf1y.sophisticatedcore.util.InventoryHelper;
import top.theillusivec4.curios.api.CuriosApi;
import xyz.iwolfking.vhapi.api.data.api.CustomRecyclerOutputs;

import java.util.List;
import java.util.Random;

public class RecyclerUpgradeHelper {

    private static final Random random = new Random();

    public static List<ItemStack> getVaultRecyclerOutputs(ItemStack stack) {
        if(stack.getItem() instanceof RecyclableItem || CustomRecyclerOutputs.CUSTOM_OUTPUTS.containsKey(stack.getItem().getRegistryName())) {
            ItemStack input = stack.copy();
            VaultRecyclerConfig.RecyclerOutput output;
            float resultPercentage = 1.0F;
            if(stack.getItem() instanceof RecyclableItem recyclableItem) {
                output = recyclableItem.getOutput(stack);
                resultPercentage = recyclableItem.getResultPercentage(stack);
            }
            else {
                output = CustomRecyclerOutputs.CUSTOM_OUTPUTS.get(stack.getItem().getRegistryName());
            }

            float additionalChance = 0.0F;
            if (input.getItem() instanceof VaultGearItem) {
                VaultGearRarity rarity = VaultGearData.read(input).getRarity();
                additionalChance = ModConfigs.VAULT_RECYCLER.getAdditionalOutputRarityChance(rarity);
            }

            if(resultPercentage < 1.0F) {
                if(resultPercentage < random.nextFloat()) {
                    return List.of();
                }
            }

            return List.of(output.generateMainOutput(additionalChance), output.generateExtraOutput1(additionalChance), output.generateExtraOutput2(additionalChance));
        }
        return List.of();
    }



}
