package io.github.krevik.kathairis.world.dimension.feature;

import com.mojang.datafixers.Dynamic;
import io.github.krevik.kathairis.init.ModBlocks;
import io.github.krevik.kathairis.world.dimension.feature.tree.AbstractKatharianTreeFeature;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.gen.IWorldGenerationReader;
import net.minecraft.world.gen.feature.NoFeatureConfig;

import java.util.Random;
import java.util.Set;
import java.util.function.Function;

public class FeatureKatharianForestBush extends AbstractKatharianTreeFeature {

    BlockState LOG = ModBlocks.SHINY_LOG.getDefaultState();
    BlockState LEAVES = ModBlocks.SHINY_LEAVES.getDefaultState();

    public FeatureKatharianForestBush(Function<Dynamic<?>, ? extends NoFeatureConfig> p_i49920_1_) {
        super(p_i49920_1_, true);
    }


    @Override
    protected boolean place(Set<BlockPos> changedBlocks, IWorldGenerationReader worldIn, Random rand, BlockPos pos, MutableBoundingBox box) {
        if(!canGrowInto(worldIn,pos.down())){
            return false;
        }
        if(rand.nextInt(2)==0){
            LOG=ModBlocks.MYSTIC_LOG.getDefaultState();
            LEAVES=ModBlocks.MYSTIC_LEAVES.getDefaultState();
        }
        int k=rand.nextInt(4);
        if(k==0){
            setLogState1(changedBlocks,worldIn,pos,LOG);
            setBlockState(worldIn,pos.east(),LEAVES);
            setBlockState(worldIn,pos.west(),LEAVES);
            setBlockState(worldIn,pos.south(),LEAVES);
            setBlockState(worldIn,pos.north(),LEAVES);
            setBlockState(worldIn,pos.up(),LEAVES);
        }
        else if(k==1){
            setLogState1(changedBlocks,worldIn,pos,LOG);
            setLogState1(changedBlocks,worldIn,pos.east(),LOG);
            setBlockState(worldIn,pos.west(),LEAVES);
            setBlockState(worldIn,pos.south(),LEAVES);
            setBlockState(worldIn,pos.north(),LEAVES);
            setBlockState(worldIn,pos.up(),LEAVES);
            setBlockState(worldIn,pos.east().east(),LEAVES);
            setBlockState(worldIn,pos.east().south(),LEAVES);
            setBlockState(worldIn,pos.east().north(),LEAVES);
            setBlockState(worldIn,pos.east().up(),LEAVES);
        }
        else if(k==2){
            setLogState1(changedBlocks,worldIn,pos,LOG);
            setLogState1(changedBlocks,worldIn,pos.west(),LOG);
            setBlockState(worldIn,pos.east(),LEAVES);
            setBlockState(worldIn,pos.south(),LEAVES);
            setBlockState(worldIn,pos.north(),LEAVES);
            setBlockState(worldIn,pos.up(),LEAVES);
            setBlockState(worldIn,pos.west().west(),LEAVES);
            setBlockState(worldIn,pos.west().south(),LEAVES);
            setBlockState(worldIn,pos.west().north(),LEAVES);
            setBlockState(worldIn,pos.west().up(),LEAVES);
        }
        else if(k==3){
            setLogState1(changedBlocks,worldIn,pos,LOG);
            setLogState1(changedBlocks,worldIn,pos.up(),LOG);
            setBlockState(worldIn,pos.up().up(),LEAVES);
            setBlockState(worldIn,pos.up().south(),LEAVES);
            setBlockState(worldIn,pos.up().north(),LEAVES);
            setBlockState(worldIn,pos.up().east(),LEAVES);
            setBlockState(worldIn,pos.up().west(),LEAVES);
        }

        return true;
    }

    private void func_208521_b(IWorldGenerationReader p_208521_1_, BlockPos p_208521_2_, BlockState p_208521_3_) {
        if (this.doBlockNotify) {
            p_208521_1_.setBlockState(p_208521_2_, p_208521_3_, 19);
        } else {
            p_208521_1_.setBlockState(p_208521_2_, p_208521_3_, 18);
        }

    }

    protected final void setLogState1(Set<BlockPos> changedBlocks, IWorldGenerationReader worldIn, BlockPos p_208520_3_, BlockState p_208520_4_) {
        this.func_208521_b(worldIn, p_208520_3_, p_208520_4_);
            changedBlocks.add(p_208520_3_.toImmutable());

    }
}
