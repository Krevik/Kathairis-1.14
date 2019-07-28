package io.github.krevik.kathairis.block;

import net.minecraft.block.*;
import net.minecraft.block.BlockFenceGate;
import net.minecraft.block.BlockWall;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.fluid.IFluidState;
import net.minecraft.init.Blocks;
import net.minecraft.init.Fluids;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.pathfinding.PathType;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.IWorldReaderBase;

/**
 * @author Krevik
 */
public class BlockKathairisWall extends WallBlock {

	public static final BooleanProperty UP = BlockStateProperties.UP;
	private final VoxelShape[] field_196422_D;
	private final VoxelShape[] field_196423_E;

	public BlockKathairisWall(Material material, float hardnessAndResistance, SoundType soundType) {
		super(Properties.create(material).sound(soundType).hardnessAndResistance(hardnessAndResistance));
		this.setDefaultState(this.stateContainer.getBaseState().with(UP, Boolean.valueOf(true)).with(NORTH, Boolean.valueOf(false)).with(EAST, Boolean.valueOf(false)).with(SOUTH, Boolean.valueOf(false)).with(WEST, Boolean.valueOf(false)).with(WATERLOGGED, Boolean.valueOf(false)));
		this.field_196422_D = this.makeShapes(4.0F, 3.0F, 16.0F, 0.0F, 14.0F);
		this.field_196423_E = this.makeShapes(4.0F, 3.0F, 24.0F, 0.0F, 24.0F);
	}

	public static boolean isExcepBlockForAttachWithPiston(Block p_194143_0_) {
		return Block.isExceptBlockForAttachWithPiston(p_194143_0_) || p_194143_0_ == Blocks.BARRIER || p_194143_0_ == Blocks.MELON || p_194143_0_ == Blocks.PUMPKIN || p_194143_0_ == Blocks.CARVED_PUMPKIN || p_194143_0_ == Blocks.JACK_O_LANTERN || p_194143_0_ == Blocks.FROSTED_ICE || p_194143_0_ == Blocks.TNT;
	}

	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos) {
		return state.get(UP) ? this.field_196422_D[this.getIndex(state)] : super.getShape(state, worldIn, pos);
	}

	public VoxelShape getCollisionShape(BlockState state, IBlockReader worldIn, BlockPos pos) {
		return state.get(UP) ? this.field_196423_E[this.getIndex(state)] : super.getCollisionShape(state, worldIn, pos);
	}

	public boolean isFullCube(BlockState state) {
		return false;
	}

	public boolean allowsMovement(BlockState state, IBlockReader worldIn, BlockPos pos, PathType type) {
		return false;
	}

	@Override
	public boolean canBeConnectedTo(BlockState state, IBlockReader world, BlockPos pos, Direction facing) {
		BlockState other = world.getBlockState(pos.offset(facing));
		return attachesTo(other, other.getBlockFaceShape(world, pos.offset(facing), facing.getOpposite()));
	}

	public BlockState getStateForPlacement(BlockItemUseContext context) {
		IWorldReader iworldreaderbase = context.getWorld();
		BlockPos blockpos = context.getPos();
		IFluidState ifluidstate = context.getWorld().getFluidState(context.getPos());
		boolean flag = canWallConnectTo(iworldreaderbase, blockpos, Direction.NORTH);
		boolean flag1 = canWallConnectTo(iworldreaderbase, blockpos, Direction.EAST);
		boolean flag2 = canWallConnectTo(iworldreaderbase, blockpos, Direction.SOUTH);
		boolean flag3 = canWallConnectTo(iworldreaderbase, blockpos, Direction.WEST);
		boolean flag4 = (!flag || flag1 || !flag2 || flag3) && (flag || !flag1 || flag2 || !flag3);
		return this.getDefaultState().with(UP, Boolean.valueOf(flag4 || !iworldreaderbase.isAirBlock(blockpos.up()))).with(NORTH, Boolean.valueOf(flag)).with(EAST, Boolean.valueOf(flag1)).with(SOUTH, Boolean.valueOf(flag2)).with(WEST, Boolean.valueOf(flag3)).with(WATERLOGGED, Boolean.valueOf(ifluidstate.getFluid() == Fluids.WATER));
	}

	public IBlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos) {
		if (stateIn.get(WATERLOGGED)) {
			worldIn.getPendingFluidTicks().scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickRate(worldIn));
		}

		if (facing == Direction.DOWN) {
			return super.updatePostPlacement(stateIn, facing, facingState, worldIn, currentPos, facingPos);
		} else {
			boolean flag = facing == Direction.NORTH ? this.attachesTo(facingState, facingState.getBlockFaceShape(worldIn, facingPos, facing.getOpposite())) : stateIn.get(NORTH);
			boolean flag1 = facing == Direction.EAST ? this.attachesTo(facingState, facingState.getBlockFaceShape(worldIn, facingPos, facing.getOpposite())) : stateIn.get(EAST);
			boolean flag2 = facing == Direction.SOUTH ? this.attachesTo(facingState, facingState.getBlockFaceShape(worldIn, facingPos, facing.getOpposite())) : stateIn.get(SOUTH);
			boolean flag3 = facing == Direction.WEST ? this.attachesTo(facingState, facingState.getBlockFaceShape(worldIn, facingPos, facing.getOpposite())) : stateIn.get(WEST);
			boolean flag4 = (!flag || flag1 || !flag2 || flag3) && (flag || !flag1 || flag2 || !flag3);
			return stateIn.with(UP, Boolean.valueOf(flag4 || !worldIn.isAirBlock(currentPos.up()))).with(NORTH, Boolean.valueOf(flag)).with(EAST, Boolean.valueOf(flag1)).with(SOUTH, Boolean.valueOf(flag2)).with(WEST, Boolean.valueOf(flag3));
		}
	}

	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
		builder.add(UP, NORTH, EAST, WEST, SOUTH, WATERLOGGED);
	}

	public BlockFaceShape getBlockFaceShape(IBlockReader worldIn, BlockState state, BlockPos pos, Direction face) {
		return face != Direction.UP && face != Direction.DOWN ? BlockFaceShape.MIDDLE_POLE_THICK : BlockFaceShape.CENTER_BIG;
	}

	private boolean attachesTo(BlockState p_196421_1_, BlockFaceShape p_196421_2_) {
		Block block = p_196421_1_.getBlock();
		boolean flag = p_196421_2_ == BlockFaceShape.MIDDLE_POLE_THICK || p_196421_2_ == BlockFaceShape.MIDDLE_POLE && (block instanceof BlockFenceGate || block instanceof BlockKathairisFenceGate);
		return !isExcepBlockForAttachWithPiston(block) && p_196421_2_ == BlockFaceShape.SOLID || flag;
	}

	private boolean canWallConnectTo(IBlockReader world, BlockPos pos, Direction facing) {
		BlockPos off = pos.offset(facing);
		BlockState other = world.getBlockState(off);
		return other.canBeConnectedTo(world, off, facing.getOpposite()) || attachesTo(other, other.getBlockFaceShape(world, off, facing.getOpposite()));
	}

}
