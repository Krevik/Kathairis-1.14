package io.github.krevik.kathairis.block;

import io.github.krevik.kathairis.util.networking.PacketHandler;
import io.github.krevik.kathairis.util.networking.packets.PacketServerPlayerUseJadeVine;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Random;

import static io.github.krevik.kathairis.init.ModBlocks.JADE_VINES;

/**
 * @author Krevik
 */
public class BlockJadeVines extends BlockKathairisPlant {

	public static final EnumProperty<EnumType> VARIANT = EnumProperty.create("variant", EnumType.class);
	Random random = new Random();

	public BlockJadeVines() {
		super();
		setDefaultState(getDefaultState().with(VARIANT, EnumType.TOP));
	}

	@Override
	public boolean isFullCube(BlockState state) {
		return false;
	}

	@Override
	public void neighborChanged(BlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
		if (!worldIn.isRemote) {
			handleVariantsAndCanBlockBeHere(worldIn, pos, state);
			if (worldIn.isAirBlock(pos.up())) {
				worldIn.destroyBlock(pos, true);
			}
		}
	}

	@Nonnull
	@Override
	@OnlyIn(Dist.CLIENT)
	public BlockRenderLayer getRenderLayer() {
		return BlockRenderLayer.CUTOUT;
	}

	@Override
	public boolean isValidPosition(BlockState state, IWorldReader worldIn, BlockPos pos) {
		boolean can = false;
		if (!worldIn.isAirBlock(pos.up()) && worldIn.getBlockState(pos.up()).isSolid()) {
			can = true;
		}
		if (worldIn.getBlockState(pos.up()).getBlock() instanceof BlockJadeVines) {
			can = true;
		}
		return can;
	}

	private void handleVariantsAndCanBlockBeHere(World world, BlockPos pos, BlockState actualState) {
		if (world.isAirBlock(pos.up())) {
			world.setBlockState(pos, Blocks.AIR.getDefaultState());
		}
		EnumType variant = actualState.get(VARIANT);
		if (variant == EnumType.TOP) {
			if (world.getBlockState(pos.up()).getBlock() instanceof BlockJadeVines) {
				EnumType upperVariant = world.getBlockState(pos.up()).get(VARIANT);
				if (upperVariant == EnumType.TOP) {
					world.setBlockState(pos, actualState.with(VARIANT, EnumType.MID));
					if (random.nextInt(10) == 0) {
						world.setBlockState(pos, actualState.with(VARIANT, EnumType.EMPTY));
					}
					if (random.nextInt(10) == 0) {
						world.setBlockState(pos, actualState.with(VARIANT, EnumType.BOTTOM));
					}
				}
				if (upperVariant == EnumType.MID) {
					world.setBlockState(pos, actualState.with(VARIANT, EnumType.MID));
				}
				if (upperVariant == EnumType.BOTTOM) {
					world.setBlockState(pos, Blocks.AIR.getDefaultState());
				}
				if (upperVariant == EnumType.EMPTY) {
					world.setBlockState(pos, actualState.with(VARIANT, EnumType.EMPTY));
				}
			}
		}
		if (variant == EnumType.EMPTY) {
			if (world.getBlockState(pos.up()).getBlock() instanceof BlockJadeVines) {
				EnumType upperVariant = world.getBlockState(pos.up()).get(VARIANT);
				if (upperVariant == EnumType.TOP) {
				}
				if (upperVariant == EnumType.MID) {
					world.setBlockState(pos, actualState.with(VARIANT, EnumType.MID));
				}
				if (upperVariant == EnumType.BOTTOM) {
					world.setBlockState(pos, Blocks.AIR.getDefaultState());
				}
				if (upperVariant == EnumType.EMPTY) {
				}
			}
		}
		if (variant == EnumType.MID) {
			if (world.getBlockState(pos.up()).getBlock() instanceof BlockJadeVines) {
				EnumType upperVariant = world.getBlockState(pos.up()).get(VARIANT);
				if (upperVariant == EnumType.TOP) {
				}
				if (upperVariant == EnumType.MID) {
				}
				if (upperVariant == EnumType.BOTTOM) {
					world.setBlockState(pos, Blocks.AIR.getDefaultState());
				}
				if (upperVariant == EnumType.EMPTY) {
					world.setBlockState(pos, actualState.with(VARIANT, EnumType.EMPTY));
				}
			}
		}
		if (variant == EnumType.BOTTOM) {
			EnumType upperVariant = world.getBlockState(pos.up()).get(VARIANT);
			if (upperVariant == EnumType.BOTTOM) {
				world.setBlockState(pos, Blocks.AIR.getDefaultState());
			}
		}
	}

	@Override
	public void tick(BlockState state, World worldIn, BlockPos pos, Random rand) {
		if (!worldIn.isRemote) {
			handleVariantsAndCanBlockBeHere(worldIn, pos, state);
			if (rand.nextInt(20) == 0) {
				if (worldIn.isAirBlock(pos.down())) {
					grow(worldIn, pos);
				}
			}
			if (worldIn.isAirBlock(pos.up())) {
				worldIn.destroyBlock(pos, true);
			}
		}
	}

	@Override
	public void onEntityCollision(BlockState p_196262_1_, World world, BlockPos p_196262_3_, Entity entity) {
		if(world.isRemote) {
			if(Minecraft.getInstance().gameSettings.keyBindJump.isKeyDown()) {
				PacketHandler.sendToServer(new PacketServerPlayerUseJadeVine());
			}
		}
		if(entity instanceof PlayerEntity){
			entity.fallDistance=0;
		}
	}

	@Nullable
	@Override
	public BlockState getStateForPlacement(BlockItemUseContext context) {
		World world = context.getWorld();
		Random random = new Random();
		if (random.nextInt(8) == 0) {
			return JADE_VINES.getDefaultState().with(VARIANT, EnumType.BOTTOM);
		} else if (random.nextInt(5) == 0) {
			return JADE_VINES.getDefaultState().with(VARIANT, EnumType.EMPTY);
		} else {
			return JADE_VINES.getDefaultState();
		}
	}

	@Override
	public void harvestBlock(World worldIn, PlayerEntity player, BlockPos pos, BlockState state, @Nullable TileEntity te, ItemStack stack) {
		if (!worldIn.isRemote && stack.getItem() == Items.SHEARS) {
			spawnAsEntity(worldIn, pos, new ItemStack(JADE_VINES, 1));
		} else {
			super.harvestBlock(worldIn, player, pos, state, te, stack);
		}
	}

	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
		if (RANDOM.nextInt(5) == 0) {
			worldIn.setBlockState(pos, JADE_VINES.getDefaultState().with(VARIANT, EnumType.EMPTY));
		}
		if (RANDOM.nextInt(5) == 0) {
			worldIn.setBlockState(pos, JADE_VINES.getDefaultState().with(VARIANT, EnumType.BOTTOM));
		}
		handleVariantsAndCanBlockBeHere(worldIn, pos, state);
	}

	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> p_206840_1_) {
		super.fillStateContainer(p_206840_1_);
		p_206840_1_.add(VARIANT);
	}

	public void grow(World worldIn, BlockPos pos) {
		if (worldIn.isAirBlock(pos.down(2)) && worldIn.isAirBlock(pos.down())) {
			worldIn.setBlockState(pos.down(), JADE_VINES.getDefaultState());
			if (random.nextInt(5) == 0) {
				worldIn.setBlockState(pos.down(), JADE_VINES.getDefaultState().with(VARIANT, EnumType.EMPTY));
			}
			if (random.nextInt(5) == 0) {
				worldIn.setBlockState(pos.down(), JADE_VINES.getDefaultState().with(VARIANT, EnumType.BOTTOM));
			}
			handleVariantsAndCanBlockBeHere(worldIn, pos.down(), worldIn.getBlockState(pos.down()));
		}
	}

	//TODO REMOVE REMOVE REMOVE. GETTING RID OF THIS STUFF IS WHAT 1.13 WAS ALL ABOUT
	public enum EnumType implements IStringSerializable {
		TOP(0, "top"),
		MID(1, "mid"),
		BOTTOM(2, "bottom"),
		EMPTY(2, "empty");

		private static final EnumType[] META_LOOKUP = new EnumType[values().length];
		static {
			for (EnumType blockstone$enumtype : values()) {
				META_LOOKUP[blockstone$enumtype.getMetadata()] = blockstone$enumtype;
			}
		}
		private final int meta;
		private final String name;

		EnumType(int p_i46384_3_, final String name) {
			this.meta = p_i46384_3_;
			this.name = name;
		}

		public static EnumType byMetadata(int meta) {
			if (meta < 0 || meta >= META_LOOKUP.length) {
				meta = 0;
			}

			return META_LOOKUP[meta];
		}

		public int getMetadata() {
			return this.meta;
		}

		@Override
		public String getName() {
			return name;
		}
	}

}
