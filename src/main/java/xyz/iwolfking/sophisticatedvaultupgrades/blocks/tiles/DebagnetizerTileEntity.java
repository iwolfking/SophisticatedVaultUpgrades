package xyz.iwolfking.sophisticatedvaultupgrades.blocks.tiles;

import iskallia.vault.init.ModConfigs;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import xyz.iwolfking.sophisticatedvaultupgrades.blocks.DebagnetizerBlock;
import xyz.iwolfking.sophisticatedvaultupgrades.config.Config;
import xyz.iwolfking.sophisticatedvaultupgrades.init.ModBlocks;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DebagnetizerTileEntity extends BlockEntity {
    public DebagnetizerTileEntity(BlockPos pos, BlockState state) {
        super(ModBlocks.DEBAGNETIZER_TILE, pos, state);
    }


    public static boolean hasDebagnetizerAround(Entity entity) {
        int radius = Config.SERVER.debagnetizerRange.get();
        return getDebagnetizers(entity, (double)radius).stream().filter((debagnetizer) -> {
            return debagnetizer.getLevel() == entity.level;
        }).filter((debagnetizer) -> {
            return debagnetizer.getBlockState().getOptionalValue(DebagnetizerBlock.DEACTIVATED).isPresent();
        }).anyMatch((debagnetizer) -> {
            return !(Boolean)debagnetizer.getBlockState().getValue(DebagnetizerBlock.DEACTIVATED);
        });
    }

    private static List<DebagnetizerTileEntity> getDebagnetizers(Entity entity, double radius) {
        if(entity == null) {
            return List.of();
        }
        List<DebagnetizerTileEntity> debagnetizers = new ArrayList();
        BlockPos entityPosition = entity.blockPosition();
        double radiusSq = radius * radius;
        int iRadius = Mth.ceil(radius);
        Vec3i radVec = new Vec3i(iRadius, iRadius, iRadius);
        ChunkPos posMin = new ChunkPos(entityPosition.subtract(radVec));
        ChunkPos posMax = new ChunkPos(entityPosition.offset(radVec));

        for(int xx = posMin.x; xx <= posMax.x; ++xx) {
            for(int zz = posMin.z; zz <= posMax.z; ++zz) {
                LevelChunk ch = entity.getLevel().getChunkSource().getChunkNow(xx, zz);
                if (ch != null) {
                    Map<BlockPos, BlockEntity> blockEntities = ch.getBlockEntities();
                    blockEntities.forEach((pos, tile) -> {
                        if (tile instanceof DebagnetizerTileEntity debagnetizer) {
                            if (pos.distSqr(entityPosition) <= radiusSq) {
                                debagnetizers.add(debagnetizer);
                            }

                        }
                    });
                }
            }
        }

        return debagnetizers;
    }
}
