package xyz.iwolfking.sophisticatedvaultupgrades.upgrades.recycler;

import iskallia.vault.config.VaultRecyclerConfig;
import iskallia.vault.gear.VaultGearRarity;
import iskallia.vault.gear.attribute.type.VaultGearAttributeTypeMerger;
import iskallia.vault.gear.data.VaultGearData;
import iskallia.vault.gear.item.VaultGearItem;
import iskallia.vault.init.ModConfigs;
import iskallia.vault.init.ModGearAttributes;
import iskallia.vault.init.ModItems;
import iskallia.vault.item.gear.RecyclableItem;
import net.minecraft.world.item.ItemStack;
import xyz.iwolfking.sophisticatedvaultupgrades.SophisticatedVaultUpgrades;

import java.util.List;
import java.util.Random;

public class RecyclerUpgradeHelper {

    private static final Random random = new Random();

    public static List<ItemStack> getVaultRecyclerOutputs(ItemStack stack) {
        if(stack.getItem() instanceof RecyclableItem || VHAPIIntegration.hasRecyclerOutput(stack.getItem().getRegistryName())) {
            ItemStack input = stack.copy();
            VaultRecyclerConfig.RecyclerOutput output;
            float resultPercentage = 1.0F;

            if(stack.getItem() instanceof RecyclableItem recyclableItem) {
                output = recyclableItem.getOutput(stack);
                resultPercentage = recyclableItem.getResultPercentage(stack);
            }
            else {
                output = VHAPIIntegration.getRecyclerOutput(stack.getItem().getRegistryName());
            }

            if(output == null) {
                SophisticatedVaultUpgrades.LOGGER.warn("A Recycler Upgrade output was null, this ideally shouldn't happen...");
                return List.of();
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

            if(stack.getItem() instanceof VaultGearItem) {
                VaultGearData data = VaultGearData.read(input);
                VaultGearRarity rarity = data.getRarity();
                if (rarity == VaultGearRarity.UNIQUE) {
                    return List.of(new ItemStack(ModItems.UNIQUE_SHARD), ItemStack.EMPTY, ItemStack.EMPTY);
                }
                boolean isCrafted = data.hasAttribute(ModGearAttributes.CRAFTED_BY) || data.getFirstValue(ModGearAttributes.CRAFTED_BY).isPresent();
                boolean isLegendary = data.get(ModGearAttributes.IS_LEGENDARY, VaultGearAttributeTypeMerger.anyTrue());
                additionalChance = ModConfigs.VAULT_RECYCLER.getAdditionalOutputRarityChance(rarity);
                return List.of(output.generateMainOutput(additionalChance), output.generateExtraOutput1(stack, additionalChance, rarity, isCrafted, isLegendary), output.generateExtraOutput2(stack, additionalChance, rarity, isCrafted, isLegendary));
            }
            else {
                return List.of(output.generateMainOutput(additionalChance), output.generateExtraOutput1(stack, additionalChance), output.generateExtraOutput2(stack, additionalChance));
            }
        }
        return List.of();
    }



}
