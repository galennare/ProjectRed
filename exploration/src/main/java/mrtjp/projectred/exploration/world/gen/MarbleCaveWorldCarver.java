package mrtjp.projectred.exploration.world.gen;

import com.google.common.collect.ImmutableSet;
import com.mojang.serialization.Codec;
import mrtjp.projectred.exploration.init.ExplorationBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.CarvingMask;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.levelgen.Aquifer;
import net.minecraft.world.level.levelgen.carver.CarvingContext;
import net.minecraft.world.level.levelgen.carver.CaveCarverConfiguration;
import net.minecraft.world.level.levelgen.carver.CaveWorldCarver;
import org.apache.commons.lang3.mutable.MutableBoolean;

import java.util.function.Function;

import static mrtjp.projectred.exploration.init.ExplorationBlocks.MARBLE_BLOCK;

public class MarbleCaveWorldCarver extends CaveWorldCarver {

    public MarbleCaveWorldCarver(Codec<CaveCarverConfiguration> codec) {
        super(codec);

        // A little awkward, but this is now NetherWorldCarver overrides this list
        // TODO make this class generic, and intake the block for cave walls
//        this.replaceableBlocks =  new ImmutableSet.Builder<Block>()
//                .addAll(this.replaceableBlocks)
//                .add(ExplorationBlocks.MARBLE_BLOCK.get())
//                .build();
    }

    @Override
    protected boolean carveBlock(CarvingContext context, CaveCarverConfiguration config, ChunkAccess chunkAccess, Function<BlockPos, Holder<Biome>> biomeGetter, CarvingMask mask, BlockPos.MutableBlockPos pos1, BlockPos.MutableBlockPos pos2, Aquifer aquifer, MutableBoolean markFluidUpdate) {
        if (!super.carveBlock(context, config, chunkAccess, biomeGetter, mask, pos1, pos2, aquifer, markFluidUpdate)) {
            return false;
        }

        BlockPos.MutableBlockPos pos = pos1.mutable();
        BlockState carvedState = chunkAccess.getBlockState(pos);

        if (carvedState.is(Blocks.AIR) || carvedState.is(Blocks.CAVE_AIR)) {

            BlockState marbleState = MARBLE_BLOCK.get().defaultBlockState();

            for (int s = 0; s < 6; s++) {
                pos.set(pos1).move(Direction.values()[s]);
                BlockState adjacentState = chunkAccess.getBlockState(pos);
                if (canReplaceBlock(config, adjacentState)) {
                    chunkAccess.setBlockState(pos, marbleState, false);
                }
            }
        }

        return true;
    }
}
