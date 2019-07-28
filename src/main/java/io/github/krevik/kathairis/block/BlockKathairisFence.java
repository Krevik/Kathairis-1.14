package io.github.krevik.kathairis.block;

import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.IFluidState;
import net.minecraft.init.Fluids;
import net.minecraft.init.Items;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemLead;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.LeadItem;
import net.minecraft.pathfinding.PathType;
import net.minecraft.state.StateContainer;
import net.minecraft.util.Direction;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

/**
 * @author Krevik
 */
public class BlockKathairisFence extends FenceBlock implements IBucketPickupHandler, ILiquidContainer {

	private final VoxelShape[] field_199609_B;

	public BlockKathairisFence(Material material, float hardnessAndResistance, SoundType soundType) {
		super(Properties.create(material).hardnessAndResistance(hardnessAndResistance).sound(soundType));
		this.setDefaultState(this.stateContainer.getBaseState().with(NORTH, Boolean.valueOf(false)).with(EAST, Boolean.valueOf(false)).with(SOUTH, Boolean.valueOf(false)).with(WEST, Boolean.valueOf(false)).with(WATERLOGGED, Boolean.valueOf(false)));
		this.field_199609_B = this.makeShapes(2.0F, 1.0F, 16.0F, 6.0F, 15.0F);
	}

	public static boolean isExcepBlockForAttachWithPiston(Block p_194142_0_) {
		return Block.isExceptBlockForAttachWithPiston(p_194142_0_) || p_194142_0_ == Blocks.BARRIER || p_194142_0_ == Blocks.MELON || p_194142_0_ == Blocks.PUMPKIN || p_194142_0_ == Blocks.CARVED_PUMPKIN || p_194142_0_ == Blocks.JACK_O_LANTERN || p_194142_0_ == Blocks.FROSTED_ICE || p_194142_0_ == Blocks.TNT;
	}

	@Override
	public VoxelShape getRenderShape(BlockState state, IBlockReader worldIn, BlockPos pos) {
		return this.field_199609_B[this.getIndex(state)];
	}

	@Override
	public boolean isFullCube(BlockState state) {
		return false;
	}

	@Override
	public boolean allowsMovement(BlockState state, IBlockReader worldIn, BlockPos pos, PathType type) {
		return false;
	}

	public boolean attachesTo(BlockState p_196416_1_, BlockFaceShape p_196416_2_) {
		Block block = p_196416_1_.getBlock();
		boolean flag = p_196416_2_ == BlockFaceShape.MIDDLE_POLE && (p_196416_1_.getMaterial() == this.material || block instanceof FenceGateBlock || block instanceof BlockKathairisFenceGate);
		return !isExcepBlockForAttachWithPiston(block) && p_196416_2_ == BlockFaceShape.SOLID || flag;
	}

	@Override
	public boolean onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, EnumHand hand, Direction side, float hitX, float hitY, float hitZ) {
		if (!worldIn.isRemote) {
			return LeadItem.attachToFence(player, worldIn, pos);
		} else {
			ItemStack itemstack = player.getHeldItem(hand);
			return itemstack.getItem() == Items.LEAD || itemstack.isEmpty();
		}
	}

	@Override
	public BlockState getStateForPlacement(BlockItemUseContext context) {
		IBlockReader iblockreader = context.getWorld();
		BlockPos blockpos = context.getPos();
		IFluidState ifluidstate = context.getWorld().getFluidState(context.getPos());
		return super.getStateForPlacement(context)
				.with(NORTH, canFenceConnectTo(iblockreader, blockpos, EnumFacing.NORTH))
				.with(EAST, canFenceConnectTo(iblockreader, blockpos, EnumFacing.EAST))
				.with(SOUTH, canFenceConnectTo(iblockreader, blockpos, EnumFacing.SOUTH))
				.with(WEST, canFenceConnectTo(iblockreader, blockpos, EnumFacing.WEST))
				.with(WATERLOGGED, Boolean.valueOf(ifluidstate.getFluid() == Fluids.WATER));
	}

	@Override
	public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos) {
		if (stateIn.get(WATERLOGGED)) {
			worldIn.getPendingFluidTicks().scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickRate(worldIn));
		}

		return facing.getAxis().getPlane() == Direction.Plane.HORIZONTAL ? stateIn.with(FACING_TO_PROPERTY_MAP.get(facing), Boolean.valueOf(this.canFenceConnectTo(worldIn, currentPos, facing))) : super.updatePostPlacement(stateIn, facing, facingState, worldIn, currentPos, facingPos);
	}

	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
		builder.add(NORTH, EAST, WEST, SOUTH, WATERLOGGED);
	}

	@Override
	public BlockFaceShape getBlockFaceShape(IBlockReader worldIn, BlockState state, BlockPos pos, Direction face) {
		return face != EnumFacing.UP && face != EnumFacing.DOWN ? BlockFaceShape.MIDDLE_POLE : BlockFaceShape.CENTER;
	}

	@Override
	public boolean canBeConnectedTo(BlockState state, IBlockReader world, BlockPos pos, Direction facing) {
		BlockState other = world.getBlockState(pos.offset(facing));
		return attachesTo(other, other.getBlockFaceShape(world, pos.offset(facing), facing.getOpposite()));
	}

	private boolean canFenceConnectTo(IBlockReader world, BlockPos pos, Direction facing) {
		BlockPos offset = pos.offset(facing);
		BlockState other = world.getBlockState(offset);
		return other.canBeConnectedTo(world, offset, facing.getOpposite()) || getDefaultState().canBeConnectedTo(world, pos, facing);
	}

}
