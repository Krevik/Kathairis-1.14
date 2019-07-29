package io.github.krevik.kathairis.block;

import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.pathfinding.PathType;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

/**
 * @author Krevik
 */
public class BlockKathairisFenceGate extends FenceGateBlock {

	public BlockKathairisFenceGate(Material material, float hardnessAndResistance, SoundType soundType) {
		super(Properties.create(material).hardnessAndResistance(hardnessAndResistance).sound(soundType));
	}

}
