package io.github.krevik.kathairis.block;

import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.IFluidState;
import net.minecraft.init.Fluids;
import net.minecraft.item.BlockItemUseContext;
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

	public BlockKathairisFence(Material material, float hardnessAndResistance, SoundType soundType) {
		super(Properties.create(material).hardnessAndResistance(hardnessAndResistance).sound(soundType));
	}

}
