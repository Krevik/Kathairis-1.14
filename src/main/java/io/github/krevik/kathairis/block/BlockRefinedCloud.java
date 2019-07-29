package io.github.krevik.kathairis.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.Direction;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * @author Krevik
 */
public class BlockRefinedCloud extends Block {

	public BlockRefinedCloud() {
		super(Block.Properties.create(Material.GLASS).sound(SoundType.GLASS).hardnessAndResistance(0.75f).slipperiness(0.98f));
	}

	@Override
	public boolean isFullCube(BlockState state) {
		return false;
	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public boolean isSideInvisible(BlockState state, BlockState adjacentBlockState, Direction side) {
		return adjacentBlockState.getBlock() == this || super.isSideInvisible(state, adjacentBlockState, side);
	}

	public BlockFaceShape getBlockFaceShape(IBlockReader worldIn, BlockState state, BlockPos pos, Direction face) {
		return BlockFaceShape.UNDEFINED;
	}

	@Override
	public int getOpacity(BlockState state, IBlockReader worldIn, BlockPos pos) {
		return 1;
	}

	@Override
	public IItemProvider getItemDropped(BlockState state, World worldIn, BlockPos pos, int fortune) {
		return null;
	}

	@Override
	public BlockRenderLayer getRenderLayer() {
		return BlockRenderLayer.TRANSLUCENT;
	}

	@Override
	protected boolean canSilkHarvest() {
		return true;
	}

}
